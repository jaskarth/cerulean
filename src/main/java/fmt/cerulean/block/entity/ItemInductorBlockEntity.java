package fmt.cerulean.block.entity;

import fmt.cerulean.block.ItemActuatorBlock;
import fmt.cerulean.block.ItemInductorBlock;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class ItemInductorBlockEntity extends ItemActuatorBlockEntity {
	public ItemInductorBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.ITEM_INDUCTOR, pos, state);
	}

	// TODO: disable hopper on top

	public static void serverTick(World world, BlockPos pos, BlockState state, ItemInductorBlockEntity iibe) {
		if (iibe.cooldown > 0) {
			iibe.cooldown--;
			iibe.markDirty();
			return;
		}

		if (iibe.isPowered) {
			return;
		}

		Direction dir = state.get(ItemActuatorBlock.FACING).getOpposite();

		Inventory inv = HopperBlockEntity.getInventoryAt(world, pos.offset(dir));
		if (inv == null) {
			return;
		}

		List<ItemEntity> list = world.getEntitiesByClass(ItemEntity.class, new Box(pos), e -> true);

		for (ItemEntity entity : list) {
			ItemStack stack = entity.getStack();

			if (!iibe.filter.isEmpty() && !stack.isOf(iibe.filter.getItem())) {
				continue;
			}

			SimpleInventory sinv = new SimpleInventory(stack);

			int amt = stack.getCount();

			ItemStack transfer = HopperBlockEntity.transfer(sinv, inv, stack, dir.getOpposite());

			entity.setStack(transfer);

			if (transfer.getCount() != amt) {
				iibe.cooldown = 4;
				iibe.markDirty();
				inv.markDirty();
				break;
			}

			// TODO: try to transfer 1 if fails (needed for self collapsing cube)
		}
	}
}
