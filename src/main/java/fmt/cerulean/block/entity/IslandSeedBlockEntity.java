package fmt.cerulean.block.entity;

import com.mojang.datafixers.util.Pair;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.Vec2d;
import fmt.cerulean.world.gen.CeruleanBiomes;
import fmt.cerulean.world.gen.SkiesChunkGenerator;
import fmt.cerulean.world.gen.feature.BiomeDecorator;
import fmt.cerulean.world.gen.feature.ConfiguredDecoration;
import fmt.cerulean.world.gen.synth.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;

import java.util.*;

import static fmt.cerulean.world.gen.SkiesChunkGenerator.index;

public class IslandSeedBlockEntity extends BlockEntity {
	private final List<Pair<BlockPos, BlockState>> states = new ArrayList<>();
	private int countdown = -1;

	public IslandSeedBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.ISLAND_SEED, pos, state);
	}

	public static void serverTick(World world, BlockPos pos, BlockState state, IslandSeedBlockEntity isbe) {
		if (!isbe.states.isEmpty()) {
            Iterator<Pair<BlockPos, BlockState>> itr = isbe.states.iterator();
            int i = 0;
			int rate = (int) MathHelper.clampedMap(isbe.states.size(), 20000, 1, 100, 10);
			while (itr.hasNext()) {
				Pair<BlockPos, BlockState> next = itr.next();
				if (world.getBlockState(next.getFirst()).isReplaceable()) {
					world.setBlockState(next.getFirst(), next.getSecond());
				}

				itr.remove();
				i++;

				if (i > rate) {
					break;
				}
			}
		}

		if (isbe.countdown > 0) {
			isbe.countdown--;
			if (isbe.countdown == 0) {
				isbe.countdown = -1;
				isbe.doWork();
			}
		}
	}

	public void activate(ServerPlayerEntity player) {
		player.sendMessage(Text.of("Creating island in 5 seconds!"));
		countdown = 100;
	}

	private void doWork() {
		for (PlayerEntity player : world.getPlayers()) {
			player.sendMessage(Text.of("Started sampling!"));
		}

		long time = System.currentTimeMillis();

		Random random = new Random();
		long seed = random.nextLong();

		long color = random.nextLong();
		Vec2d center = new Vec2d(pos.getX() >> 2, pos.getZ() >> 2);
		VoronoiSampler vn = new Singlenoi(color, center);
		PerlinNoiseSampler s1 = new PerlinNoiseSampler(new CheckedRandom(seed));
		PerlinNoiseSampler s2 = new PerlinNoiseSampler(new CheckedRandom(seed + 1));
		IslandGenerationData data = new IslandGenerationData(vn, s1, s2);

		IslandParameters p = IslandParameters.get(center.floor(), color);

		int origCenterY = (13 + p.startOff()) * 8;
		int diff = pos.getY() - origCenterY;

		Map<BlockPos, BlockState> blocks = new HashMap<>();

		for (int cx = -2; cx <= 2; cx++) {
			for (int cz = -2; cz <= 2; cz++) {
				int chunkX = (pos.getX() >> 4) + cx;
				int chunkZ = (pos.getZ() >> 4) + cz;
				generateOneChunk(chunkX, chunkZ, data, diff, blocks);
			}
		}

		LiveAugmentedWorld lworld = new LiveAugmentedWorld((StructureWorldAccess) world, blocks, seed);

		for (int cx = -2; cx <= 2; cx++) {
			for (int cz = -2; cz <= 2; cz++) {
				int chunkX = (pos.getX() >> 4) + cx;
				int chunkZ = (pos.getZ() >> 4) + cz;
				decorateOneChunk(chunkX, chunkZ, lworld);
			}
		}

		for (Map.Entry<BlockPos, BlockState> e : blocks.entrySet()) {
			states.add(Pair.of(e.getKey(), e.getValue()));
		}

		states.sort(Comparator.comparing(k -> {
			BlockPos pk = k.getFirst();
			return Math.sqrt(pk.getSquaredDistance(pos)) + s1.sample(pk.getX() / 12., pk.getY() / 3., pk.getZ() / 12.) * 5;
		}));

		for (PlayerEntity player : world.getPlayers()) {
			player.sendMessage(Text.of("Done! Took " + (System.currentTimeMillis() - time) + " ms, created " + states.size() + " states"));
		}
	}

	private static void generateOneChunk(int chunkX, int chunkZ, IslandGenerationData data, int diff, Map<BlockPos, BlockState> blocks) {
		int chunkStartX = chunkX << 4;
		int chunkStartZ = chunkZ << 4;

		double[] noiseData = new double[5 * 5 * 49];

		for (int noiseX = 0; noiseX < 5; ++noiseX) {
			for (int noiseZ = 0; noiseZ < 5; ++noiseZ) {
				SkiesChunkGenerator.sampleNoiseColumn(data,  noiseData, chunkX * 4 + noiseX, chunkZ * 4 + noiseZ, noiseX, noiseZ);
			}
		}

		BlockPos.Mutable mutable = new BlockPos.Mutable();

		// [0, 4) -> x noise chunks
		for(int noiseX = 0; noiseX < 4; ++noiseX) {
			// Initialize noise data on the x1 column

			// [0, 4) -> z noise chunks
			for(int noiseZ = 0; noiseZ < 4; ++noiseZ) {
				// [0, 48) -> y noise chunks
				for(int noiseY = 48 - 1; noiseY >= 0; --noiseY) {
					// Lower samples
					double x0z0y0 = noiseData[index(noiseX, noiseZ, noiseY)];
					double x0z1y0 = noiseData[index(noiseX, noiseZ + 1, noiseY)];
					double x1z0y0 = noiseData[index(noiseX + 1, noiseZ, noiseY)];
					double x1z1y0 = noiseData[index(noiseX + 1, noiseZ + 1, noiseY)];
					// Upper samples
					double x0z0y1 = noiseData[index(noiseX, noiseZ, noiseY + 1)];
					double x0z1y1 = noiseData[index(noiseX, noiseZ + 1, noiseY + 1)];
					double x1z0y1 = noiseData[index(noiseX + 1, noiseZ, noiseY + 1)];
					double x1z1y1 = noiseData[index(noiseX + 1, noiseZ + 1, noiseY + 1)];

					// [0, 8) -> y noise pieces
					for(int pieceY = 8 - 1; pieceY >= 0; --pieceY) {
						int realY = noiseY * 8 + pieceY;

						// progress within loop
						double yLerp = (double) pieceY / 8.0;

						// Interpolate noise data based on y progress
						double x0z0 = MathHelper.lerp(yLerp, x0z0y0, x0z0y1);
						double x1z0 = MathHelper.lerp(yLerp, x1z0y0, x1z0y1);
						double x0z1 = MathHelper.lerp(yLerp, x0z1y0, x0z1y1);
						double x1z1 = MathHelper.lerp(yLerp, x1z1y0, x1z1y1);

						// [0, 4) -> x noise pieces
						for(int pieceX = 0; pieceX < 4; ++pieceX) {
							int realX = chunkStartX + noiseX * 4 + pieceX;
							double xLerp = (double) pieceX / (double)4;
							// Interpolate noise based on x progress
							double z0 = MathHelper.lerp(xLerp, x0z0, x1z0);
							double z1 = MathHelper.lerp(xLerp, x0z1, x1z1);

							// [0, 4) -> z noise pieces
							for(int pieceZ = 0; pieceZ < 4; ++pieceZ) {
								int realZ = chunkStartZ + noiseZ * 4 + pieceZ;
								double zLerp = (double) pieceZ / (double)4;
								// Get the real noise here by interpolating the last 2 noises together
								double rawNoise = MathHelper.lerp(zLerp, z0, z1);
								double density = MathHelper.clamp(rawNoise, -1.0D, 1.0D);

								// Iterate through structures to add density
								density = density / 2.0D - density * density * density / 24.0D;

								mutable.set(realX, realY + diff, realZ);

								// Get the blockstate based on the y and density
								BlockState state = density > 0 ? CeruleanBlocks.SPACEROCK.getDefaultState() : Blocks.AIR.getDefaultState();

								if (!state.isAir()) {
									blocks.put(mutable.toImmutable(), state);
								}
							}
						}
					}
				}
			}
		}
	}

	private static void decorateOneChunk(int chunkX, int chunkZ, DecoratingWorld world) {
		int chunkStartX = chunkX << 4;
		int chunkStartZ = chunkZ << 4;

		BlockPos pos = new BlockPos(chunkStartX, 0, chunkStartZ);

		ImprovedChunkRandom random = new ImprovedChunkRandom(world.getSeed());
		long pSeed = random.setPopulationSeed(world.getSeed(), chunkStartX, chunkStartZ);

		// TODO: decorations need to be changed to work at any island height

		int i = 0;
		for (ConfiguredDecoration deco : BiomeDecorator.LIVE_DECORATIONS.get(CeruleanBiomes.SKIES)) {
			random.setDecoratorSeed(pSeed, i, 75138);
			deco.generate(world, random, pos);
			i++;
		}
	}
}
