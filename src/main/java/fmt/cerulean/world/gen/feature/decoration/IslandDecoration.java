package fmt.cerulean.world.gen.feature.decoration;

import fmt.cerulean.block.entity.WellBlockEntity;
import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.flow.FlowResources;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.world.gen.feature.Decoration;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.StructureWorldAccess;

import java.util.Random;

public class IslandDecoration extends Decoration {
	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos pos) {
		double radius = 3 + random.nextInt(4);
		int radMax = (int) radius;

		OctavePerlinNoiseSampler noise = OctavePerlinNoiseSampler.create(new ChunkRandom(new CheckedRandom(world.getSeed())), -3, 1.0);

		double radiusdiv = 3.0;
		double radiusdivsub = 0.06 + random.nextDouble(0.03);

		int oy = pos.getY();

		// TODO: check for space first!

		for (int y = oy; y > 0; y--) {
			if (radius <= 0 || radiusdiv <= 0) {
				break;
			}

			for (int x = -radMax; x <= radMax; x++) {
				for (int z = -radMax; z <= radMax; z++) {
					double dx = x / radius;
					double dz = z / radius;

					BlockPos local = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
					if (dx * dx + dz * dz < 1 + (noise.sample(local.getX(), local.getY(), local.getZ()) * 0.25)) {
						world.setBlockState(local, CeruleanBlocks.SPACEROCK.getDefaultState(), 3);
					}
				}
			}

			radius -= (1 / radiusdiv);
			radiusdiv -= radiusdivsub;
		}

		if (random.nextInt(2) == 0) {
			BlockPos bp = new BlockPos(pos.getX(), oy, pos.getZ());
			world.setBlockState(bp, CeruleanBlocks.STAR_WELL.getDefaultState(), 3);
			BlockEntity be = world.getBlockEntity(bp);
			if (be instanceof WellBlockEntity wbe) {
				FlowResource.Brightness brightness = switch (random.nextInt(20)) {
					case 0, 1, 2 -> FlowResource.Brightness.DIM;
					case 3, 4, 5 -> FlowResource.Brightness.INNOCUOUS;
					default -> FlowResource.Brightness.WANING;
				};
				FlowState flow = new FlowState(FlowResources.star(FlowResource.Color.values()[random.nextInt(FlowResource.Color.amount)], brightness),
						random.nextInt(4_000) + 4_000);
				wbe.setFlow(flow);
			}
		}
	}
}
