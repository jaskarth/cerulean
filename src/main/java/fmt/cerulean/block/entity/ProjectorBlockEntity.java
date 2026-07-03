package fmt.cerulean.block.entity;

import fmt.cerulean.block.ProjectorBlock;
import fmt.cerulean.block.RealityAnchorBlock;
import fmt.cerulean.block.base.Obedient;
import fmt.cerulean.block.entity.base.SyncedBlockEntity;
import fmt.cerulean.flow.FlowOutreach;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.flow.FlowUser;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.screen.ProjectorScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ProjectorBlockEntity extends SyncedBlockEntity implements NamedScreenHandlerFactory, Obedient, SidedInventory, FlowUser {
	public DefaultedList<ItemStack> slots = DefaultedList.ofSize(1, ItemStack.EMPTY);
	public int rotX = 0;
	public int rotY = 0;
	public int rotZ = 0;
	public int dist = 3;
	public int size = 1;
	public int alpha = 255;
	public boolean enabled;
	private FlowState flow = FlowState.NONE;

	protected final PropertyDelegate propertyDelegate = new PropertyDelegate() {

		@Override
		public int get(int index) {
			return switch (index) {
				case 0 -> flow.empty() ? 0 : 1; // is enabled?
				case 1 -> rotX;
				case 2 -> rotY;
				case 3 -> rotZ;
				case 4 -> dist;
				case 5 -> size;
				case 6 -> alpha;
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value) {
			if (index == 0) {
				// shouldn't be changed
			} else if (index == 1) {
				rotX = value;
			} else if (index == 2) {
				rotY = value;
			} else if (index == 3) {
				rotZ = value;
			} else if (index == 4) {
				dist = value;
			} else if (index == 5) {
				size = value;
			} else if (index == 6) {
				alpha = value;
			}
			ProjectorBlockEntity.this.markDirty();
		}

		@Override
		public int size() {
			return 7;
		}
	};

	public ProjectorBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.PROJECTOR, pos, state);
	}

	public static void serverTick(World world, BlockPos pos, BlockState state, ProjectorBlockEntity pbe) {
		Direction direction = state.get(ProjectorBlock.FACING);
		if (world.getBlockEntity(pos.offset(direction.getOpposite())) instanceof FlowOutreach flow) {
			FlowState exported = flow.getExportedState(direction);
			if (pbe.flow != exported) {
				pbe.enabled = !exported.empty();
				pbe.flow = exported;
				pbe.markDirty();
			}
		}
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		rotX = nbt.getInt("RotX");
		rotY = nbt.getInt("RotY");
		rotZ = nbt.getInt("RotZ");
		dist = nbt.getInt("Dist");
		size = nbt.getInt("Size");
		alpha = nbt.getInt("Alpha");
		enabled = nbt.getBoolean("Enabled");
		if (nbt.contains("Slots")) {
			slots.clear();
			int i = 0;
			for (NbtElement el : nbt.getList("Slots", NbtElement.COMPOUND_TYPE)) {
				ItemStack stack = ItemStack.fromNbtOrEmpty(registryLookup, (NbtCompound) el);
				if (i < 1) {
					slots.set(i, stack);
				}
				i++;
			}
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		nbt.putInt("RotX", rotX);
		nbt.putInt("RotY", rotY);
		nbt.putInt("RotZ", rotZ);
		nbt.putInt("Dist", dist);
		nbt.putInt("Size", size);
		nbt.putInt("Alpha", alpha);
		nbt.putBoolean("Enabled", enabled);
		NbtList list = new NbtList();
		for (ItemStack stack : slots) {
			list.add(stack.encodeAllowEmpty(registryLookup));
		}
		nbt.put("Slots", list);
	}

	@Override
	public Map<String, Consumer<String>> cede() {
		Map<String, Consumer<String>> map = new HashMap<>();

//		map.put("name", intuition -> name = intuition);
//		map.put("transX", intuition -> transX = Obedient.guess(intuition));
//		map.put("transY", intuition -> transY = Obedient.guess(intuition));
//		map.put("transZ", intuition -> transZ = Obedient.guess(intuition));
//		map.put("xp", intuition -> xp = Obedient.guess(intuition));
//		map.put("yp", intuition -> yp = Obedient.guess(intuition));
//		map.put("zp", intuition -> zp = Obedient.guess(intuition));
//		map.put("scaleX", intuition -> scaleX = Obedient.guess(intuition));
//		map.put("scaleY", intuition -> scaleY = Obedient.guess(intuition));
//		map.put("scaleZ", intuition -> scaleZ = Obedient.guess(intuition));
//		map.put("alpha", intuition -> alpha = Obedient.count(intuition));

		return map;
	}

	@Override
	public Text getDisplayName() {
		return Text.empty();
	}

	@Override
	public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return new ProjectorScreenHandler(syncId, playerInventory, this, propertyDelegate);
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return new int[] { 0 };
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return stack.isOf(CeruleanItems.PHOTOGRAPH);
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return slot == 0;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return slots.isEmpty();
	}

	@Override
	public ItemStack getStack(int slot) {
		return this.slots.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack stack = Inventories.splitStack(this.slots, slot, amount);
		if (!stack.isEmpty()) {
			this.markDirty();
		}
		return stack;
	}

	@Override
	public ItemStack removeStack(int slot) {
		ItemStack stack = this.slots.get(slot);
		this.markDirty();
		return stack;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.slots.set(slot, stack);
		this.markDirty();
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		this.slots.clear();
		this.markDirty();
	}

	@Override
	public FlowState getAnyFlow() {
		return flow;
	}
}
