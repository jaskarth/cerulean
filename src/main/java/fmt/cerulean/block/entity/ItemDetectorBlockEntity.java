package fmt.cerulean.block.entity;

import fmt.cerulean.block.ItemDetectorBlock;
import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class ItemDetectorBlockEntity extends BlockEntity {
	public ItemDetectorBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.ITEM_DETECTOR, pos, state);
	}

	public static void serverTick(World world, BlockPos pos, BlockState state, ItemDetectorBlockEntity ibe) {
		BlockPos checkPos = pos.offset(state.get(ItemDetectorBlock.FACING));
		// TODO: optimize for solid blocks?
		if ((world.getTime() & 1) == 0) {
			if (world.getEntitiesByClass(ItemEntity.class, new Box(checkPos), e -> true).isEmpty()) {
				world.setBlockState(pos, state.with(ItemDetectorBlock.POWERED, false));
				world.updateNeighbor(pos.offset(state.get(ItemDetectorBlock.FACING).getOpposite()), state.getBlock(), pos);
			} else {
				world.setBlockState(pos, state.with(ItemDetectorBlock.POWERED, true));
				world.updateNeighbor(pos.offset(state.get(ItemDetectorBlock.FACING).getOpposite()), state.getBlock(), pos);
			}
		}
	}
}
