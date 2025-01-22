package fmt.cerulean.world.gen.feature.decoration;

import fmt.cerulean.block.base.Plasticloggable;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.Vec2d;
import fmt.cerulean.util.Vec2i;
import fmt.cerulean.util.Voronoi;
import fmt.cerulean.world.gen.IslandParameters;
import fmt.cerulean.world.gen.feature.Decoration;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.world.StructureWorldAccess;

import java.util.Random;
import java.util.stream.IntStream;

public class PlasticloggingDecoration extends Decoration {
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
				long uniq = vn.get(wx / scale, wz / scale);
				Vec2d cp = vn.getCellPos(wx / scale, wz / scale, scale);
				Vec2i center = cp.floor();

				IslandParameters p = IslandParameters.get(center, uniq);

				double sample = xzWarp.sample(wx / 15., 0, wz / 15.) * 15;

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
							if (state.isAir()) {
								world.setBlockState(local, CeruleanBlocks.POLYETHYLENE.getDefaultState(), 3);
							} else if (state.contains(Plasticloggable.PLASTICLOGGED)) {
								world.setBlockState(local, state.with(Plasticloggable.PLASTICLOGGED, true), 3);
							}
						}
					}
				}
			}
		}
	}
}
