package fmt.cerulean.fluid;

import java.util.Optional;

import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public abstract class RealizedPolyethyleneFluid extends FlowableFluid {
	@Override
	public Fluid getFlowing() {
		return CeruleanFluids.REALIZED_POLYETHYLENE_FLOWING;
	}

	@Override
	public Fluid getStill() {
		return CeruleanFluids.REALIZED_POLYETHYLENE;
	}


	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropStacks(state, world, pos, blockEntity);
	}

	@Override
	public int getMaxFlowDistance(WorldView world) {
		return 4;
	}

	@Override
	public BlockState toBlockState(FluidState state) {
		return CeruleanBlocks.REALIZED_POLYETHYLENE.getDefaultState().with(FluidBlock.LEVEL, Integer.valueOf(getBlockStateLevel(state)));
	}

	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == CeruleanFluids.REALIZED_POLYETHYLENE || fluid == CeruleanFluids.REALIZED_POLYETHYLENE_FLOWING;
	}

	@Override
	public int getLevelDecreasePerBlock(WorldView world) {
		return 1;
	}

	@Override
	public int getTickRate(WorldView world) {
		return 10;
	}

	@Override
	public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
		return direction == Direction.DOWN && !fluid.isIn(FluidTags.WATER);
	}

	@Override
	protected float getBlastResistance() {
		return 100.0F;
	}

	@Override
	public Optional<SoundEvent> getBucketFillSound() {
		return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
	}

	@Override
	protected boolean isInfinite(World world) {
		return false;
	}

	@Override
	public Item getBucketItem() {
		return Items.AIR;
	}

	public static class Flowing extends RealizedPolyethyleneFluid {
		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getLevel(FluidState state) {
			return (Integer)state.get(LEVEL);
		}

		@Override
		public boolean isStill(FluidState state) {
			return false;
		}
	}

	public static class Still extends RealizedPolyethyleneFluid {
		@Override
		public int getLevel(FluidState state) {
			return 8;
		}

		@Override
		public boolean isStill(FluidState state) {
			return true;
		}
	}
}
