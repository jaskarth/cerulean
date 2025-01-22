package fmt.cerulean.block;

import com.mojang.serialization.MapCodec;
import fmt.cerulean.block.base.Plasticloggable;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanFluids;
import net.minecraft.block.AbstractPlantBlock;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.KelpPlantBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class KalePlantBlock extends AbstractPlantBlock implements Plasticloggable {
	public static final MapCodec<KalePlantBlock> CODEC = createCodec(KalePlantBlock::new);

	public KalePlantBlock(Settings settings) {
		super(settings, Direction.UP, VoxelShapes.fullCube(), true);
	}

	@Override
	protected MapCodec<? extends AbstractPlantBlock> getCodec() {
		return CODEC;
	}

	@Override
	protected AbstractPlantStemBlock getStem() {
		return (AbstractPlantStemBlock) CeruleanBlocks.KALE;
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return CeruleanFluids.POLYETHYLENE.getDefaultState();
	}

	@Override
	protected BlockState getStateForNeighborUpdate(
			BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
	) {
		if (direction == this.growthDirection.getOpposite() && !state.canPlaceAt(world, pos)) {
			world.scheduleBlockTick(pos, this, 1);
		}

		AbstractPlantStemBlock abstractPlantStemBlock = this.getStem();
		if (direction == this.growthDirection && !neighborState.isOf(this) && !neighborState.isOf(abstractPlantStemBlock)) {
			return this.copyState(state, abstractPlantStemBlock.getRandomGrowthState(world));
		} else {
			if (this.tickWater) {
				world.scheduleFluidTick(pos, CeruleanFluids.POLYETHYLENE, 1);
			}

			return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		}
	}

	@Override
	public boolean tryFillWithPlastic(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}

	@Override
	public boolean tryDrainPlastic(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, BlockState state) {
		world.breakBlock(pos, true);

		return true;
	}
}
