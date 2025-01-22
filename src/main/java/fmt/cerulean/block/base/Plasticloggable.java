package fmt.cerulean.block.base;

import fmt.cerulean.registry.CeruleanFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Plasticloggable {
	BooleanProperty PLASTICLOGGED = BooleanProperty.of("plasticlogged");

	default boolean tryFillWithPlastic(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		if (!state.get(PLASTICLOGGED) && fluidState.getFluid() == CeruleanFluids.POLYETHYLENE) {
			if (!world.isClient()) {
				world.setBlockState(pos, state.with(PLASTICLOGGED, true), Block.NOTIFY_ALL);
				world.scheduleFluidTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
			}

			return true;
		} else {
			return false;
		}
	}

	default boolean tryDrainPlastic(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, BlockState state) {
		if (state.get(PLASTICLOGGED)) {
			world.setBlockState(pos, state.with(PLASTICLOGGED, false), Block.NOTIFY_ALL);
			if (!state.canPlaceAt(world, pos)) {
				world.breakBlock(pos, true);
			}

			return true;
		} else {
			return false;
		}
	}
}
