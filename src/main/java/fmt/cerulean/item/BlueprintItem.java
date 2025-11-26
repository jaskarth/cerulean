package fmt.cerulean.item;

import fmt.cerulean.item.component.PosPair;
import fmt.cerulean.registry.CeruleanItemComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.Optional;

public class BlueprintItem extends Item {
	public BlueprintItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		Hand hand = context.getHand();
		if (hand == Hand.OFF_HAND) {
			return ActionResult.PASS;
		}

		PlayerEntity player = context.getPlayer();
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		Direction side = context.getSide();
		ItemStack stack = context.getStack();

		PosPair pair = stack.getOrDefault(CeruleanItemComponents.POS_PAIR, PosPair.empty());
		if (pair.isEmpty()) {
			pos = pos.offset(side);

			if (world.getBlockState(pos).isReplaceable()) {
				pair = pair.insert(pos);

				stack.set(CeruleanItemComponents.POS_PAIR, pair);

				return ActionResult.SUCCESS;
			}
		} else if (pair.first().isPresent() && pair.second().isEmpty()) {
			pos = pos.offset(side);

			if (world.getBlockState(pos).isReplaceable() && isOk(pair.first().get(), pos)) {
				pair = pair.insert(pos);
				stack.set(CeruleanItemComponents.POS_PAIR, pair);

				return ActionResult.SUCCESS;
			}
		} else if (pair.first().isPresent() && pair.second().isPresent() && player != null) {
			BlockPos first = pair.first().get();
			BlockPos second = pair.second().get();

			int dx = Math.abs(first.getX() - second.getX());
			int dy = Math.abs(first.getY() - second.getY());
			int dz = Math.abs(first.getZ() - second.getZ());

			if (side.getOffsetX() != 0 && dx != 0) {
				return ActionResult.SUCCESS;
			}

			if (side.getOffsetY() != 0 && dy != 0) {
				return ActionResult.SUCCESS;
			}

			if (side.getOffsetZ() != 0 && dz != 0) {
				return ActionResult.SUCCESS;
			}

			BlockBox box = BlockBox.create(first, second);

			if (!box.contains(pos.offset(side))) {
				return ActionResult.SUCCESS;
			}

			if (!world.isClient()) {
				// TODO: get from inventory first
				ItemStack offhand = player.getOffHandStack();

				if (offhand.getItem() instanceof BlockItem ib) {
					Block block = ib.getBlock();

					for (int x = box.getMinX(); x <= box.getMaxX(); x++) {
						for (int y = box.getMinY(); y <= box.getMaxY(); y++) {
							for (int z = box.getMinZ(); z <= box.getMaxZ(); z++) {
								BlockPos local = new BlockPos(x, y, z);

								if (world.getBlockState(local).isReplaceable()) {
									BlockHitResult hit = new BlockHitResult(local.toCenterPos(), side, local, false);
									BlockState state = block.getPlacementState(new ItemPlacementContext(player, Hand.OFF_HAND, offhand, hit));

									if (state != null && offhand.getCount() > 0 && stack.getCount() > 0) {
										// break old block
										world.breakBlock(local, true, player);

										world.playSound(null, local, state.getSoundGroup().getPlaceSound(), SoundCategory.PLAYERS, 1, world.random.nextFloat() * 0.2f + 0.7f);

										world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, local, Block.getRawIdFromState(state));

										world.setBlockState(local, state, 3);

										offhand.decrementUnlessCreative(1, player);
										stack.damage(1, player, EquipmentSlot.MAINHAND);
									}
								}
							}
						}
					}

					if (stack.getCount() > 0) {
						pair = new PosPair(Optional.of(first.offset(side)), Optional.of(second.offset(side)));
						stack.set(CeruleanItemComponents.POS_PAIR, pair);
					}

					return ActionResult.SUCCESS;
				}
			}

			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (user.isSneaking()) {
			ItemStack stack = user.getStackInHand(hand);
			stack.set(CeruleanItemComponents.POS_PAIR, PosPair.empty());
			return TypedActionResult.success(stack);
		}

		return super.use(world, user, hand);
	}

	public static boolean isOk(BlockPos first, BlockPos second) {
		int dx = Math.abs(first.getX() - second.getX());
		int dy = Math.abs(first.getY() - second.getY());
		int dz = Math.abs(first.getZ() - second.getZ());

		if (dx > 8 || dy > 8 || dz > 8) {
			return false;
		}

		return dx == 0 || dy == 0 || dz == 0;
	}
}
