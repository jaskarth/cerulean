package fmt.cerulean.world.gen.feature.decoration;

import fmt.cerulean.block.CeruleanPlantBlock;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.Util;
import fmt.cerulean.world.gen.feature.Decoration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 6-way plant decoration
public class CeruleanPlantDecoration extends Decoration {
	private final int hSpread;
	private final int vSpread;
	private final int count;
	private final Block block;

	public CeruleanPlantDecoration(int hSpread, int vSpread, int count, Block block) {
		this.hSpread = hSpread;
		this.vSpread = vSpread;
		this.count = count;
		this.block = block;
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos pos) {
		for (int i = 0; i < this.count; i++) {
			int dx = random.nextInt(this.hSpread) - random.nextInt(this.hSpread);
			int dy = random.nextInt(this.vSpread) - random.nextInt(this.vSpread);
			int dz = random.nextInt(this.hSpread) - random.nextInt(this.hSpread);

			BlockPos local = pos.add(dx, dy, dz);

			BlockState state = world.getBlockState(local);
			if (!state.isAir() && !state.isOf(CeruleanBlocks.POLYETHYLENE)) {
				continue;
			}

			List<Direction> ok = new ArrayList<>();

			for (Direction d : Util.DIRECTIONS) {
				if (world.getBlockState(local.offset(d)).isOf(CeruleanBlocks.SPACEROCK)) {
					ok.add(d);
				}
			}

			if (ok.isEmpty()) {
				continue;
			}

			Direction place = Util.pick(ok, random);
			world.setBlockState(local, this.block.getDefaultState()
					.with(CeruleanPlantBlock.FACING, place.getOpposite())
					.with(CeruleanPlantBlock.PLASTICLOGGED, state.isOf(CeruleanBlocks.POLYETHYLENE)), 3);
		}
	}
}
