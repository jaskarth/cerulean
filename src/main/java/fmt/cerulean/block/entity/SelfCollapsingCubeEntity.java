package fmt.cerulean.block.entity;

import java.util.List;

import fmt.cerulean.block.entity.base.SyncedBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SelfCollapsingCubeEntity extends SyncedBlockEntity implements SidedInventory {
	private static final int[] SLOTS = new int[] { 0 };
	private DefaultedList<ItemStack> filterSlots = DefaultedList.ofSize(8, ItemStack.EMPTY);
	private DefaultedList<ItemStack> bufferSlots = DefaultedList.ofSize(8, ItemStack.EMPTY);
	private int charge = 0;

	public SelfCollapsingCubeEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.SELF_COLLAPSING_CUBE, pos, state);
	}

	public void setFilterSlot(int slot, ItemStack stack) {
		if (!bufferSlots.get(slot).isEmpty()) {
			return;
		}
		filterSlots.set(slot, stack.copy());
		markDirty();
	}

	public List<ItemStack> getFilterStacks() {
		return filterSlots;
	}

	public List<ItemStack> getBufferStacks() {
		return bufferSlots;
	}

	private void ding() {
		int filled = -1;
		for (int i = 0; i < 8; i++) {
			if (!bufferSlots.get(i).isEmpty()) {
				filled++;
			}
		}
		Vec3d pos = this.getPos().toCenterPos();
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.BLOCKS, 0.3f, NoteBlock.getNotePitch(filled));
	}

	private void smash() {
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.BLOCKS, 0.3f, 2.2f);
	}

	public static void tick(World world, BlockPos pos, BlockState state, SelfCollapsingCubeEntity scce) {
		int found = 0;
		for (int i = 0; i < 8; i++) {
			if (!scce.bufferSlots.get(i).isEmpty()) {
				found++;
			}
			if (!scce.filterSlots.get(i).isEmpty() && scce.bufferSlots.get(i).isEmpty()) {
				scce.charge = 0;
				return;
			}
		}
		if (found == 0) {
			return;
		}
		scce.charge++;
		if (scce.charge < 8) {
			return;
		}
		scce.smash();
		for (int i = 0; i < 8; i++) {
			ItemStack stack = scce.bufferSlots.get(i);
			if (stack.isEmpty()) {
				continue;
			}
			Vec3d center = pos.toBottomCenterPos();
			double ox = (world.getRandom().nextDouble() * 0.6) - 0.3;
			double oz = (world.getRandom().nextDouble() * 0.6) - 0.3;
			ItemEntity entity = new ItemEntity(world, center.getX() + ox, center.getY() - 0.5d, center.getZ() + oz, stack, 0, -0.3, 0);
			world.spawnEntity(entity);
		}
		scce.bufferSlots.clear();
		scce.markDirty();
		// Drop
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		return this.createNbt(registryLookup);
	}

	@Override
	public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		if (nbt.contains("filter_slots")) {
			filterSlots.clear();
			int i = 0;
			for (NbtElement el : nbt.getList("filter_slots", NbtElement.COMPOUND_TYPE)) {
				ItemStack stack = ItemStack.fromNbtOrEmpty(registryLookup, (NbtCompound) el);
				if (i < 8) {
					filterSlots.set(i, stack);
				}
				i++;
			}
		}
		if (nbt.contains("buffer_slots")) {
			bufferSlots.clear();
			int i = 0;
			for (NbtElement el : nbt.getList("buffer_slots", NbtElement.COMPOUND_TYPE)) {
				ItemStack stack = ItemStack.fromNbtOrEmpty(registryLookup, (NbtCompound) el);
				if (i < 8) {
					bufferSlots.set(i, stack);
				}
				i++;
			}
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);

		NbtList list = new NbtList();
		for (ItemStack stack : filterSlots) {
			list.add(stack.encodeAllowEmpty(registryLookup));
		}
		nbt.put("filter_slots", list);

		list = new NbtList();
		for (ItemStack stack : bufferSlots) {
			list.add(stack.encodeAllowEmpty(registryLookup));
		}
		nbt.put("buffer_slots", list);
	}

	private int getPutIndex(ItemStack stack) {
		for (int i = 0; i < 8; i++) {
			if (!filterSlots.get(i).isEmpty() && bufferSlots.get(i).isEmpty() && ItemStack.areItemsAndComponentsEqual(filterSlots.get(i), stack)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}

	@Override
	public ItemStack getStack(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public ItemStack removeStack(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		int i = getPutIndex(stack);
		if (stack.getCount() == 1 && i != -1) {
			bufferSlots.set(i, stack.copy());
		}
		ding();
		markDirty();
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		if (stack.getCount() == 1 && getPutIndex(stack) != -1) {
			return true;
		}
		return false;
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return SLOTS;
	}
}
