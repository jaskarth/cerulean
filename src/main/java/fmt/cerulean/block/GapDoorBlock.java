package fmt.cerulean.block;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class GapDoorBlock extends TrapdoorBlock {
	public GapDoorBlock(BlockSetType type, Settings settings) {
		super(type, settings);
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (context instanceof EntityShapeContext ectx) {
			if (ectx.getEntity() instanceof PlayerEntity) {
				return VoxelShapes.empty();
			}
		}

		return super.getCollisionShape(state, world, pos, context);
	}
}
