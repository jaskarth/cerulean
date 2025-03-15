package fmt.cerulean.world.gen.feature.decoration;

import fmt.cerulean.block.base.Plasticloggable;
import fmt.cerulean.block.entity.MirageBlockEntity;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.Vec2d;
import fmt.cerulean.util.Vec2i;
import fmt.cerulean.util.Voronoi;
import fmt.cerulean.world.gen.IslandParameters;
import fmt.cerulean.world.gen.feature.Decoration;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.world.StructureWorldAccess;

import java.util.Random;
import java.util.stream.IntStream;

public class IslandAugmentDecoration extends Decoration {
	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos pos) {
		long seed = world.getSeed();
		Voronoi vn = new Voronoi(seed);

		OctavePerlinNoiseSampler detailNoise = OctavePerlinNoiseSampler.create(new CheckedRandom(seed + 100), IntStream.of(0, 1));
		OctavePerlinNoiseSampler xzWarp = OctavePerlinNoiseSampler.create(new CheckedRandom(seed + 101), IntStream.of(0, 1));

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double scale = 48.0 * 4;
				int wx = pos.getX() + x;
				int wz = pos.getZ() + z;
				Pair<Long, Long> uniq = vn.get2(wx / scale, wz / scale);
				Pair<Vec2d, Vec2d> cells = vn.getCellPos2(wx / scale, wz / scale, scale);
				Vec2i center = cells.getLeft().floor();

				IslandParameters p = IslandParameters.get(center, uniq.getLeft());

				double sample = xzWarp.sample(wx / 15., 0, wz / 15.) * 15;

				// Plasticlogging
				if (p.plasticlogged()) {
					BlockPos islCenter = new BlockPos(center.x(), (14 + p.startOff()) * 8, center.z());
					for (int y = -60; y <= 60; y++) {
						double dx = (wx - islCenter.getX()) / (40.0 + sample);
						int wy = islCenter.getY() + y;
						double dy = (wy - islCenter.getY()) / 30.0;
						double dz = (wz - islCenter.getZ()) / (40.0 + sample);

						double dst = dx * dx + dy * dy + dz * dz;

						BlockPos local = new BlockPos(wx, wy, wz);

						if (dst + (detailNoise.sample(wx / 20., wy / 10., wz / 20.) * 0.5) < 1) {
							BlockState state = world.getBlockState(local);
							if (state.isAir() || state.isOf(CeruleanBlocks.REEDS) || state.isOf(CeruleanBlocks.REEDS_PLANT)) {
								world.setBlockState(local, CeruleanBlocks.POLYETHYLENE.getDefaultState(), 3);
							} else if (state.contains(Plasticloggable.PLASTICLOGGED)) {
								world.setBlockState(local, state.with(Plasticloggable.PLASTICLOGGED, true), 3);
							}
						}
					}
				}

				Vec2d left = cells.getLeft();
				Vec2d right = cells.getRight();

				double x0 = wx;
				double x1 = left.x();
				double x2 = right.x();

				double z0 = wz;
				double z1 = left.z();
				double z2 = right.z();

				double num = Math.abs(((z2 - z1) * x0) - ((x2 - x1) * z0) + (x2 * z1) - (z2 * x1));
				double den = Math.sqrt(((z2 - z1) * (z2 - z1)) + ((x2 - x1) * (x2 - x1)));

				double v = num / den;

				if (v < 1.5) {
					IslandParameters p2 = IslandParameters.get(center, uniq.getRight());

					Vec2d posVec = new Vec2d(x0, z0);

					int start1 = (15 + p.startOff()) * 8 - 1;
					int start2 = (15 + p2.startOff()) * 8 - 1;
					int nstart = (start1 + start2) / 2;
					int y = (int) MathHelper.clampedMap(posVec.distSqr(left) / posVec.distSqr(right),
							0, 1, start1, nstart);

					BlockPos worldPos = new BlockPos(wx, y, wz);

					if (world.getBlockState(worldPos).isAir()) {
						world.setBlockState(worldPos, CeruleanBlocks.MIRAGE.getDefaultState(), 3);
						MirageBlockEntity.set(world.getBlockEntity(worldPos), CeruleanBlocks.SPACEROCK.getDefaultState());
					}
				}
			}
		}
	}
}
