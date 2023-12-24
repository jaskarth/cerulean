package fmt.cerulean.world.gen.feature.decoration;

import fmt.cerulean.block.ReedsBlock;
import fmt.cerulean.block.ReedsPlantBlock;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.world.gen.feature.Decoration;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;

import java.util.Random;

public class ReedsDecoration extends Decoration {
	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos pos) {
		int amt = random.nextInt(3) + 1;

		for (int i = 0; i < amt; i++) {
			int dx = random.nextInt(5) - random.nextInt(5);
			int dz = random.nextInt(5) - random.nextInt(5);

			int y = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + dx, pos.getZ() + dz);

			BlockPos local = pos.add(dx, 0, dz).withY(y);

			if (!world.getBlockState(local.down()).isOf(CeruleanBlocks.SPACEROCK)) {
				continue;
			}

			int height = 4 + random.nextInt(5);
			boolean ok = true;
			for (int j = 0; j <= height; j++) {
				if (!world.getBlockState(local.up(j)).isAir()) {
					ok = false;
					break;
				}
			}

			if (!ok) {
				continue;
			}

			for (int j = 0; j < height; j++) {
				world.setBlockState(local.up(j), CeruleanBlocks.REEDS_PLANT.getDefaultState().with(ReedsPlantBlock.BERRIES, random.nextInt(6) == 0), 3);
			}

			world.setBlockState(local.up(height), CeruleanBlocks.REEDS.getDefaultState().with(ReedsBlock.AGE, random.nextInt(25)), 3);
		}
	}
}
