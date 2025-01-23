package fmt.cerulean.item;

import fmt.cerulean.block.base.Plasticloggable;
import fmt.cerulean.fluid.CanisterFluidType;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanFluids;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DepressurizerItem extends FluidHandlerItem {
	public DepressurizerItem(Settings settings) {
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
			// TODO: check hand first
			for (int i = 0; i < user.getInventory().size(); i++) {
				ItemStack stack = user.getInventory().getStack(i);
				if (stack.isOf(CeruleanItems.POLYETHYLENE_DRUM)) {
					stack.decrement(1);

					itemStack.set(CeruleanItemComponents.FLUID_TYPE, CanisterFluidType.POLYETHYLENE);
					itemStack.set(CeruleanItemComponents.FLUID_AMOUNT, 10_000);

					return TypedActionResult.success(itemStack);
				}
			}
		}

		if (type == CanisterFluidType.EMPTY) {
			if (user.getInventory().insertStack(new ItemStack(CeruleanItems.EMPTY_DRUM))) {
				itemStack.set(CeruleanItemComponents.FLUID_TYPE, CanisterFluidType.NONE);
				itemStack.set(CeruleanItemComponents.FLUID_AMOUNT, 0);
				return TypedActionResult.success(itemStack);
			} else {
				return TypedActionResult.fail(itemStack);
			}
		}

		BlockHitResult res = raycast(world, user, RaycastContext.FluidHandling.NONE);
		if (res.getType() == HitResult.Type.MISS) {
			return TypedActionResult.pass(itemStack);
		} else if (res.getType() != HitResult.Type.BLOCK) {
			return TypedActionResult.pass(itemStack);
		}

		BlockPos targetPos = res.getBlockPos();
		Direction dir = res.getSide();
		BlockPos offsetPos = targetPos.offset(dir);
		if (!world.canPlayerModifyAt(user, targetPos) || !user.canPlaceOn(offsetPos, dir, itemStack)) {
			return TypedActionResult.fail(itemStack);
		}

		BlockState targetState = world.getBlockState(targetPos);
		if (type == CanisterFluidType.POLYETHYLENE) {
			if (world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.SKIES) && fluidAmt >= 250) {
				return placeDreamPolyethylene(world, targetState, targetPos, fluidAmt, itemStack, offsetPos);
			} else if (fluidAmt >= 1000) {
				return placeRealizedPolyethylene(user, world, targetState, targetPos, fluidAmt, itemStack, offsetPos);
			}
		}

		return TypedActionResult.pass(itemStack);
	}

	private static TypedActionResult<ItemStack> placeDreamPolyethylene(World world, BlockState targetState, BlockPos targetPos, int fluidAmt, ItemStack itemStack, BlockPos offsetPos) {
		if (targetState.getBlock() instanceof Plasticloggable plastic) {
			if (plastic.tryFillWithPlastic(world, targetPos, targetState, CeruleanFluids.POLYETHYLENE.getDefaultState())) {
				decrementAmount(fluidAmt, itemStack, 250);
				return TypedActionResult.success(itemStack);
			}
		}

		BlockState offsetState = world.getBlockState(offsetPos);
		if (offsetState.isAir() || (offsetState.isReplaceable() && !offsetState.isOf(CeruleanBlocks.POLYETHYLENE))) {
			boolean canBucketPlace = targetState.canBucketPlace(CeruleanFluids.POLYETHYLENE);

			if (offsetState.getBlock() instanceof Plasticloggable plastic) {
				if (plastic.tryFillWithPlastic(world, offsetPos, offsetState, CeruleanFluids.POLYETHYLENE.getDefaultState())) {
					decrementAmount(fluidAmt, itemStack, 250);
					return TypedActionResult.success(itemStack);
				}
			} else {
				if (!world.isClient && canBucketPlace && !targetState.isLiquid()) {
					world.breakBlock(offsetPos, true);
				}

				world.setBlockState(offsetPos, CeruleanFluids.POLYETHYLENE.getDefaultState().getBlockState(), Block.NOTIFY_ALL_AND_REDRAW);

				decrementAmount(fluidAmt, itemStack, 250);
				return TypedActionResult.success(itemStack);
			}
		}

		return TypedActionResult.pass(itemStack);
	}

	private static TypedActionResult<ItemStack> placeRealizedPolyethylene(PlayerEntity user, World world, BlockState targetState, BlockPos targetPos, int fluidAmt, ItemStack itemStack, BlockPos offsetPos) {
		BlockState offsetState = world.getBlockState(offsetPos);

		if (offsetState.isAir() || (offsetState.isReplaceable() && offsetState.getFluidState().getFluid() != CeruleanFluids.REALIZED_POLYETHYLENE)) {
			boolean canBucketPlace = targetState.canBucketPlace(CeruleanFluids.REALIZED_POLYETHYLENE);
			if (!world.isClient && canBucketPlace && !targetState.isLiquid()) {
				world.breakBlock(offsetPos, true);
			}

			world.setBlockState(offsetPos, CeruleanFluids.REALIZED_POLYETHYLENE.getDefaultState().getBlockState(), Block.NOTIFY_ALL_AND_REDRAW);

			if (user instanceof ServerPlayerEntity spe) {
				Criteria.PLACED_BLOCK.trigger(spe, offsetPos, itemStack);
			}

			decrementAmount(fluidAmt, itemStack, 1000);
			return TypedActionResult.success(itemStack);
		}

		return TypedActionResult.pass(itemStack);
	}

	private static void decrementAmount(int fluidAmt, ItemStack itemStack, int amt) {
		int newAmt = fluidAmt - amt;
		itemStack.set(CeruleanItemComponents.FLUID_AMOUNT, newAmt);
		if (newAmt <= 0) {
			itemStack.set(CeruleanItemComponents.FLUID_TYPE, CanisterFluidType.EMPTY);
		}
	}

	protected static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling) {
		Vec3d vec3d = player.getEyePos();
		Vec3d vec3d2 = vec3d.add(player.getRotationVector(player.getPitch(), player.getYaw()).multiply(player.getBlockInteractionRange()));

		ShapeContext ctx = ShapeContext.of(player);

		return world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, fluidHandling, ctx) {
			@Override
			public VoxelShape getBlockShape(BlockState state, BlockView world, BlockPos pos) {
				if (state.isOf(CeruleanBlocks.POLYETHYLENE)) {
					return VoxelShapes.fullCube();
				}
				return RaycastContext.ShapeType.OUTLINE.get(state, world, pos, ctx);
			}
		});
	}
}
