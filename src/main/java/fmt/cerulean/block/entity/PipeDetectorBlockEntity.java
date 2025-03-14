package fmt.cerulean.block.entity;

import fmt.cerulean.block.ItemDetectorBlock;
import fmt.cerulean.block.PipeBlock;
import fmt.cerulean.block.PipeDetectorBlock;
import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class PipeDetectorBlockEntity extends BlockEntity {
	public PipeDetectorBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.PIPE_DETECTOR, pos, state);
	}

	public static void serverTick(World world, BlockPos pos, BlockState state, PipeDetectorBlockEntity ibe) {
		BlockPos checkPos = pos.offset(state.get(PipeDetectorBlock.FACING));
		// TODO: optimize for solid blocks?
		if ((world.getTime() & 1) == 0) {
			boolean set = false;
			if (world.getBlockEntity(checkPos) instanceof PipeBlockEntity pbe) {
				if (!pbe.getFlow().empty()) {
					world.setBlockState(pos, state.with(PipeDetectorBlock.POWERED, true));
					world.updateNeighbor(pos.offset(state.get(ItemDetectorBlock.FACING).getOpposite()), state.getBlock(), pos);
					set = true;
				}
			}

			if (!set) {
				world.setBlockState(pos, state.with(PipeDetectorBlock.POWERED, false));
				world.updateNeighbor(pos.offset(state.get(PipeDetectorBlock.FACING).getOpposite()), state.getBlock(), pos);
			}
		}
	}
}
