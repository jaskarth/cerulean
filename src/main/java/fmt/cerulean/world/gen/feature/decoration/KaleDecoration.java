package fmt.cerulean.world.gen.feature.decoration;

import fmt.cerulean.block.ReedsBlock;
import fmt.cerulean.block.ReedsPlantBlock;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.world.gen.feature.Decoration;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;

import java.util.Random;

public class KaleDecoration extends Decoration {
	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos pos) {
		int amt = random.nextInt(20) + 50;

		for (int i = 0; i < amt; i++) {
			int dx = random.nextInt(16);
			int dz = random.nextInt(16);

			int y = world.getTopY(Heightmap.Type.OCEAN_FLOOR, pos.getX() + dx, pos.getZ() + dz);

			BlockPos local = pos.add(dx, 0, dz).withY(y);

			if (!world.getBlockState(local.down()).isOf(CeruleanBlocks.SPACEROCK)) {
				continue;
			}

			int height = 7 + random.nextInt(6);
			for (int j = 0; j <= height; j++) {
				if (!world.getBlockState(local.up(j)).isOf(CeruleanBlocks.POLYETHYLENE)) {
					height = j;
					break;
				}
			}

			if (height <= 3) {
				continue;
			}

			for (int j = 0; j < height; j++) {
				world.setBlockState(local.up(j), CeruleanBlocks.KALE_PLANT.getDefaultState(), 3);
			}

			world.setBlockState(local.up(height - 1), CeruleanBlocks.KALE.getDefaultState().with(ReedsBlock.AGE, 15 + random.nextInt(11)), 3);
		}
	}
}
