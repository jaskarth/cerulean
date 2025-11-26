package fmt.cerulean.block.entity;

import fmt.cerulean.block.ItemActuatorBlock;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ItemActuatorBlockEntity extends BlockEntity {
	protected int cooldown = 0;
	protected ItemStack filter = ItemStack.EMPTY;
	protected boolean isPowered = false;

	public ItemActuatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public boolean setFilter(World world, BlockPos pos, BlockState state, ItemStack stack) {
		if (stack.isOf(CeruleanItems.CORAL_STRAINER)) {
			if (stack.contains(CeruleanItemComponents.ITEM_STACK)) {
				world.setBlockState(pos, state.with(ItemActuatorBlock.FILTERED, true));
				this.filter = stack.get(CeruleanItemComponents.ITEM_STACK);
				markDirty();

				return true;
			}

		}

		return false;
	}

	public void setPowered(boolean powered) {
		this.isPowered = powered;
		markDirty();
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		if (nbt.contains("Filter")) {
			filter = ItemStack.fromNbtOrEmpty(registryLookup, nbt.getCompound("Filter"));
		}

		cooldown = nbt.getInt("Cooldown");
		isPowered = nbt.getBoolean("Powered");
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		if (!filter.isEmpty()) {
			nbt.put("Filter", filter.encode(registryLookup));
		}

		nbt.putInt("Cooldown", cooldown);
		nbt.putBoolean("Powered", isPowered);
	}
}
