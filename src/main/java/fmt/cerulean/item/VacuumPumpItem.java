package fmt.cerulean.item;

import fmt.cerulean.block.base.Plasticloggable;
import fmt.cerulean.fluid.CanisterFluidType;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanFluids;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class VacuumPumpItem extends FluidHandlerItem {
	public VacuumPumpItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		CanisterFluidType type = itemStack.get(CeruleanItemComponents.FLUID_TYPE);
		int fluidAmt = itemStack.getOrDefault(CeruleanItemComponents.FLUID_AMOUNT, 0);
		if (type == null) {
			type = CanisterFluidType.NONE;
			itemStack.set(CeruleanItemComponents.FLUID_TYPE, type);
		}

		if (type == CanisterFluidType.NONE) {
			int idx = user.getInventory().indexOf(new ItemStack(CeruleanItems.EMPTY_DRUM));
			if (idx != -1) {
				user.getInventory().getStack(idx).decrement(1);
				itemStack.set(CeruleanItemComponents.FLUID_TYPE, CanisterFluidType.EMPTY);
				return TypedActionResult.success(itemStack);
			}
		}

		if (fluidAmt >= 10000) {
			if (user.getInventory().insertStack(new ItemStack(type.item))) {
				itemStack.set(CeruleanItemComponents.FLUID_TYPE, CanisterFluidType.NONE);
				itemStack.set(CeruleanItemComponents.FLUID_AMOUNT, 0);
				return TypedActionResult.success(itemStack);
			} else {
				return TypedActionResult.fail(itemStack);
			}
		}

		BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
		if (blockHitResult.getType() == HitResult.Type.MISS) {
			return TypedActionResult.pass(itemStack);
		} else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
			return TypedActionResult.pass(itemStack);
		}
		BlockPos blockPos = blockHitResult.getBlockPos();
		Direction direction = blockHitResult.getSide();
		BlockPos blockPos2 = blockPos.offset(direction);
		if (!world.canPlayerModifyAt(user, blockPos) || !user.canPlaceOn(blockPos2, direction, itemStack)) {
			return TypedActionResult.fail(itemStack);
		}

		BlockState blockState = world.getBlockState(blockPos);
		if (type == CanisterFluidType.EMPTY || type == CanisterFluidType.POLYETHYLENE) {
			if (blockState.getBlock() instanceof Plasticloggable plastic && fluidAmt <= (10_000 - 250)) {
				if (plastic.tryDrainPlastic(user, world, blockPos, blockState)) {
					itemStack.set(CeruleanItemComponents.FLUID_TYPE, CanisterFluidType.POLYETHYLENE);
					itemStack.set(CeruleanItemComponents.FLUID_AMOUNT, fluidAmt + 250);
					return TypedActionResult.success(itemStack);
				}
			}

			if (blockState.isOf(CeruleanBlocks.REALIZED_POLYETHYLENE) && blockState.getFluidState().isStill() && fluidAmt <= (10_000 - 1_000)) {
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW);

				itemStack.set(CeruleanItemComponents.FLUID_TYPE, CanisterFluidType.POLYETHYLENE);
				itemStack.set(CeruleanItemComponents.FLUID_AMOUNT, fluidAmt + 1_000);
				return TypedActionResult.success(itemStack);
			}
		}

		return TypedActionResult.pass(itemStack);
	}
}
