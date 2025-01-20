package fmt.cerulean.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.*;
import fmt.cerulean.world.gen.carver.SkyCarver;
import fmt.cerulean.world.gen.feature.BiomeDecorator;
import fmt.cerulean.world.gen.feature.ConfiguredDecoration;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SkiesChunkGenerator extends ChunkGenerator {
	public static final MapCodec<SkiesChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter(b -> b.biomeSource)
	).apply(instance, SkiesChunkGenerator::new));

	private static final BlockState AIR = Blocks.AIR.getDefaultState();

	public SkiesChunkGenerator(BiomeSource biomeSource) {
		super(biomeSource);
	}

	@Override
	protected MapCodec<? extends ChunkGenerator> getCodec() {
		return CODEC;
	}

	@Override
	public void carve(ChunkRegion world, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep) {
		ChunkPos chunkPos = chunk.getPos();

		ImprovedChunkRandom random = new ImprovedChunkRandom(seed);

		HeightContext ctx = new HeightContext(this, world);

		for(int j = -8; j <= 8; ++j) {
			for (int k = -8; k <= 8; ++k) {
				ChunkPos pos = new ChunkPos(chunkPos.x + j, chunkPos.z + k);

				random.setCarverSeed(seed, pos.x, pos.z);
				for (SkyCarver carver : BiomeDecorator.CARVERS) {
					if (carver.shouldCarve(random, pos.x, pos.z)) {
						carver.carve(ctx, chunk, random, pos);
					}
				}
			}
		}
	}

	@Override
	public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {

	}

	@Override
	public void populateEntities(ChunkRegion region) {

	}

	@Override
	public int getWorldHeight() {
		return 384;
	}

	private static int index(int nX, int nZ, int nY) {
		return (nY * 5 * 5) + (nZ * 5) + nX;
	}

	private void sampleNoiseColumn(long seed, Chunk chunk, double[] noiseData, int x, int z, int nX, int nZ) {
		// TODO: cache these per seed
		Voronoi vn = new Voronoi(seed);

		PerlinNoiseSampler s1 = new PerlinNoiseSampler(new CheckedRandom(seed));
		PerlinNoiseSampler s2 = new PerlinNoiseSampler(new CheckedRandom(seed + 1));

		double scale = 48.0;
		long uniq = vn.get(x / scale, z / scale);
		Vec2d cp = vn.getCellPos(x / scale, z / scale, scale);
		Vec2i center = cp.floor();
		int dx = center.x() - x;
		int dz = center.z() - z;

		Random random = new Random(uniq);

		int startOff = random.nextInt(7) - 3;
		double distOffset = random.nextDouble() * 2.5;

		double distScale = (14.0 + distOffset) + (s1.sample(x / 2.21, 0, z / 2.21) * 4) + (s2.sample(x / 4.21, 0, z / 4.21) * 8);
		double sq = Math.sqrt(dx * dx + dz * dz);

		int amt = 16 + startOff;
		int lAmt = 4 + startOff;

		for (int nY = lAmt; nY < amt; nY++) {
			double n = -1;

			if (sq < distScale) {
				double h = MathHelper.map(sq, 0, distScale, 1.3, -0.1);

				h = h * h * h * h;

				n = MathHelper.map(nY, lAmt, amt + 1, -1.3, h);

				n += s1.sample(x / 4.84, nY / 6.56, z / 4.84) * 0.1;
			}

			noiseData[index(nX, nZ, nY)] = n;
		}

		noiseData[index(nX, nZ, amt)] = -4;
	}

	@Override
	public CompletableFuture<Chunk> populateNoise(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
		ChunkPos pos = chunk.getPos();
		int chunkX = pos.x;
		int chunkZ = pos.z;
		int chunkStartX = chunkX << 4;
		int chunkStartZ = chunkZ << 4;

		double[] noiseData = new double[5 * 5 * 49];

		for(int noiseX = 0; noiseX < 5; ++noiseX) {
			for(int noiseZ = 0; noiseZ < 5; ++noiseZ) {
				this.sampleNoiseColumn(((Seedy)noiseConfig).getSeed(), chunk, noiseData, chunkX * 4 + noiseX, chunkZ * 4 + noiseZ, noiseX, noiseZ);
			}
		}

		ProtoChunk protoChunk = (ProtoChunk)chunk;
		Heightmap oceanFloor = protoChunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
		Heightmap worldSurface = protoChunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		// [0, 4) -> x noise chunks
		for(int noiseX = 0; noiseX < 4; ++noiseX) {
			// Initialize noise data on the x1 column

			// [0, 4) -> z noise chunks
			for(int noiseZ = 0; noiseZ < 4; ++noiseZ) {
				int lastSecY = chunk.getSectionIndex(384 - 1);
				ChunkSection section = protoChunk.getSection(lastSecY);
				section.lock();

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

						int localY = realY & 15;
						int sectionY = chunk.getSectionIndex(realY);
						// Get the chunk section
						if (lastSecY != sectionY) {
							section.unlock();
							section = protoChunk.getSection(sectionY);
							lastSecY = sectionY;
							section.lock();
						}

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
							int localX = realX & 15;
							double xLerp = (double) pieceX / (double)4;
							// Interpolate noise based on x progress
							double z0 = MathHelper.lerp(xLerp, x0z0, x1z0);
							double z1 = MathHelper.lerp(xLerp, x0z1, x1z1);

							// [0, 4) -> z noise pieces
							for(int pieceZ = 0; pieceZ < 4; ++pieceZ) {
								int realZ = chunkStartZ + noiseZ * 4 + pieceZ;
								int localZ = realZ & 15;
								double zLerp = (double) pieceZ / (double)4;
								// Get the real noise here by interpolating the last 2 noises together
								double rawNoise = MathHelper.lerp(zLerp, z0, z1);
								double density = MathHelper.clamp(rawNoise, -1.0D, 1.0D);

								// Iterate through structures to add density
								density = density / 2.0D - density * density * density / 24.0D;

								// Get the blockstate based on the y and density
								BlockState state = this.getBlockState(density, realY);

								if (state != AIR) {
									// Add light source if the state has light
									if (state.getLuminance() != 0) {
										mutable.set(realX, realY, realZ);
									}

									// Place the state at the position
									section.setBlockState(localX, localY, localZ, state, false);
									// Track heightmap data
									oceanFloor.trackUpdate(localX, realY, localZ, state);
									worldSurface.trackUpdate(localX, realY, localZ, state);
								}
							}
						}
					}
				}

				section.unlock();
			}
		}
		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor) {
		int startX = chunk.getPos().getStartX();
		int startZ = chunk.getPos().getStartZ();

		BlockPos center = new BlockPos(startX, 0, startZ);

		RegistryKey<Biome> key = world.getBiome(center).getKey().orElseThrow();

		List<ConfiguredDecoration> decos = BiomeDecorator.DECORATIONS.get(key);

		ImprovedChunkRandom random = new ImprovedChunkRandom(world.getSeed());
		long pSeed = random.setPopulationSeed(world.getSeed(), startX, startZ);
		int i = 0;
		BlockPos start = new BlockPos(startX, 0, startZ);
		for (ConfiguredDecoration deco : decos) {
			random.setDecoratorSeed(pSeed, i, 30481);
			deco.generate(world, random, start);
			i++;
		}
	}

	private BlockState getBlockState(double density, int realY) {
		return density > 0 ? CeruleanBlocks.SPACEROCK.getDefaultState() : AIR;
	}

	@Override
	public int getSeaLevel() {
		return 0;
	}

	@Override
	public int getMinimumY() {
		return 0;
	}

	@Override
	public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
		return 0;
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
		BlockState[] states = new BlockState[384];
		Arrays.fill(states, Blocks.AIR.getDefaultState());
		return new VerticalBlockSample(0, states);
	}

	@Override
	public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {

	}
}
