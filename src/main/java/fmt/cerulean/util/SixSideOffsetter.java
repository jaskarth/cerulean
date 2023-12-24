package fmt.cerulean.util;

import fmt.cerulean.block.CeruleanPlantBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

public class SixSideOffsetter implements AbstractBlock.Offsetter {
	@Override
	public Vec3d evaluate(BlockState state, BlockView world, BlockPos pos) {
		Block block = state.getBlock();
		long seed = MathHelper.hashCode(pos.getX(), 0, pos.getZ());
		float offset = block.getMaxHorizontalModelOffset();
		double a = MathHelper.clamp(((double)((float)(seed & 15L) / 15.0F) - 0.5) * 0.5, (double)(-offset), (double) offset);
		double b = MathHelper.clamp(((double)((float)(seed >> 8 & 15L) / 15.0F) - 0.5) * 0.5, (double)(-offset), (double) offset);

		if (state.contains(CeruleanPlantBlock.FACING)) {
			Direction.Axis axis = state.get(CeruleanPlantBlock.FACING).getAxis();
			if (axis == Direction.Axis.X) {
				return new Vec3d(0, a, b);
			} else if (axis == Direction.Axis.Z) {
				return new Vec3d(a, b, 0);
			} else {
				return new Vec3d(a, 0, b);
			}
		} else {
			return new Vec3d(0, 0, 0);
		}
	}
}
