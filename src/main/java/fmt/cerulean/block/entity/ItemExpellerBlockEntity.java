package fmt.cerulean.block.entity;

import fmt.cerulean.block.ItemActuatorBlock;
import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class ItemExpellerBlockEntity extends ItemActuatorBlockEntity {
	public ItemExpellerBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.ITEM_EXPELLER, pos, state);
	}

	public static void serverTick(World world, BlockPos pos, BlockState state, ItemExpellerBlockEntity iebe) {
		if (iebe.cooldown > 0) {
			iebe.cooldown--;
			iebe.markDirty();
			return;
		}

		if (iebe.isPowered) {
			return;
		}

		Direction dir = state.get(ItemActuatorBlock.FACING).getOpposite();

		Inventory inv = HopperBlockEntity.getInventoryAt(world, pos.offset(dir));
		if (inv == null || inv.isEmpty()) {
			return;
		}

		int slot = findSlot(inv, new Random(), iebe.filter);
		if (slot == -1) {
			return;
		}

		ItemStack stack = inv.getStack(slot);
		ItemStack copy = stack.copy();
		copy.setCount(1);
		stack.decrement(1);

		Vec3d vec3d = Vec3d.ofCenter(pos);
		Vec3d vec3d2 = vec3d.offset(dir, 0);

		spawnItem(world, copy, 2, dir.getOpposite(), vec3d2);

		iebe.cooldown = 4;
	}

	private static int findSlot(Inventory inventory, Random random, ItemStack filter) {
		int i = -1;
		int j = 1;

		for (int k = 0; k < inventory.size(); k++) {
			if (!inventory.getStack(k).isEmpty()
				&& random.nextInt(j++) == 0
				&& (filter.isEmpty() || inventory.getStack(k).isOf(filter.getItem()))) {
				i = k;
			}
		}

		return i;
	}

	public static void spawnItem(World world, ItemStack stack, int speed, Direction side, Position pos) {
		double d = pos.getX();
		double e = pos.getY();
		double f = pos.getZ();
		boolean isY = side.getAxis() == Direction.Axis.Y;
		if (isY) {
			e -= 0.125;
		} else {
			e -= 0.15625;
		}

		ItemEntity itemEntity = new ItemEntity(world, d, e, f, stack);
		double deviation = world.random.nextDouble() * 0.1 + 0.2;

		double deviationY = deviation;
		if (side == Direction.UP) {
			deviationY += 0.4;
		}

		itemEntity.setVelocity(
				world.random.nextTriangular((double)side.getOffsetX() * deviation, 0.0172275 * (double)speed),
				world.random.nextTriangular(isY ? (double)side.getOffsetY() * deviationY : 0.2, 0.0172275 * (double)speed),
				world.random.nextTriangular((double)side.getOffsetZ() * deviation, 0.0172275 * (double)speed)
		);
		world.spawnEntity(itemEntity);
	}
}
