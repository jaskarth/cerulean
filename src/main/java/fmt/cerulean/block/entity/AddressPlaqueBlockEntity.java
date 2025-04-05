package fmt.cerulean.block.entity;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import fmt.cerulean.block.AddressPlaqueBlock;
import fmt.cerulean.block.base.Addressable;
import fmt.cerulean.block.base.Obedient;
import fmt.cerulean.block.entity.base.SyncedBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.world.data.MailWorldState;
import fmt.cerulean.world.data.MailWorldState.Mailbox;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AddressPlaqueBlockEntity extends SyncedBlockEntity implements Addressable, Obedient {
	private static List<String> FIRST_WORDS = List.of(
		"Stellar", "Empty", "Charming", "Clouded", "Dreary", "Stable", "Shining", "Obscured", "Clear",
		"Imposing", "Brilliant", "Candescent", "Innocuous", "Waning", "Dim", "Steady", "Determined",
		"Planned", "Isolated", "Fleeting", "Dancing", "Bleak", "Ambiguous", "Searing"
	);
	private static List<String> SECOND_WORDS = List.of(
		"Viridian", "Cerulean", "Chartreuse", "Rose", "Ash", "Lilac", "Turquoise", "Contrasting",
		"Shattered", "Sheltered", "Rigid", "Placid"
	);
	private static List<String> THIRD_WORDS = List.of(
		"Groove", "Flow", "Fall", "Impulse", "Drag", "Night", "Dream", "Disarray", "Void", "Place",
		"Obsession", "Regret", "Foundation", "Stanza", "Score", "Measure", "Chapter", "Addendum",
		"Eternity", "Struggle", "Tirade", "Justification", "Antonym"
	);
	public String address = "null";

	public AddressPlaqueBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.ADDRESS_PLAQUE, pos, state);
	}

	public static String randomAddress(Random random) {
		String address = "" + random.nextInt(10_000);
		address += " " + FIRST_WORDS.get(random.nextInt(FIRST_WORDS.size()));
		address += " " + SECOND_WORDS.get(random.nextInt(SECOND_WORDS.size()));
		address += " " + THIRD_WORDS.get(random.nextInt(THIRD_WORDS.size()));
		return address;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public void setAddress(String address) {
		this.address = address;
		markDirty();
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, AddressPlaqueBlockEntity apbe) {
		if (world instanceof ServerWorld serverWorld) {
			MailWorldState mail = MailWorldState.get(serverWorld);
			if (mail.mailboxes.containsKey(apbe.address)) {
				Mailbox mailbox = mail.mailboxes.get(apbe.address);
				Direction dir = state.get(AddressPlaqueBlock.FACING);
				BlockEntity target = world.getBlockEntity(pos.offset(dir));
				if (target instanceof Inventory targetInventory) {
					Inventory facade = new SimpleInventory(1);
					for (int i = 0; i < mailbox.stacks.size(); i++) {
						ItemStack stack = mailbox.stacks.get(i);
						ItemStack falsified = stack.copyWithCount(1);
						ItemStack left = HopperBlockEntity.transfer(facade, targetInventory, falsified, dir);
						if (left.isEmpty()) {
							stack.decrement(1);
							if (stack.isEmpty()) {
								mailbox.stacks.remove(i);
								if (mailbox.stacks.isEmpty()) {
									mail.mailboxes.remove(apbe.address);
								}
							}
							mail.markDirty();
							return;
						}
					}
				}
			}
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt.putString("address", address);
	}

	@Override
	protected void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		address = nbt.getString("address");
	}

	@Override
	protected void readComponents(ComponentsAccess components) {
		super.readComponents(components);
		Text text = components.get(DataComponentTypes.CUSTOM_NAME);
		if (text != null) {
			this.address = text.getString();
		} else {
			if (this.world.isClient) {
				this.address = "";
			} else {
				this.address = AddressPlaqueBlockEntity.randomAddress(this.world.random);
			}
		}
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(WrapperLookup registryLookup) {
		return this.createNbt(registryLookup);
	}

	@Override
	public Map<String, Consumer<String>> cede() {
		return Map.of(
			"address", s -> { this.address = s; markDirty(); }
		);
	}
}
