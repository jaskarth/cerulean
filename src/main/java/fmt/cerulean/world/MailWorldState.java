package fmt.cerulean.world;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

public class MailWorldState extends PersistentState {
	public Map<String, Mailbox> mailboxes = Maps.newHashMap();

	public void send(String address, List<ItemStack> stacks) {
		mailboxes.computeIfAbsent(address, k -> new Mailbox(k)).stacks.addAll(stacks);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		NbtCompound data = new NbtCompound();
		NbtList list = new NbtList();
		for (Mailbox mailbox : mailboxes.values()) {
			NbtCompound el = new NbtCompound();
			mailbox.writeNbt(el, registryLookup);
			list.add(el);
		}
		data.put("mailboxes", list);
		nbt.put("Data", data);
		return nbt;
	}

	public static MailWorldState readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		MailWorldState state = new MailWorldState();
		NbtCompound data = nbt.getCompound("Data");
		NbtList mailboxes = data.getList("mailboxes", NbtElement.COMPOUND_TYPE);
		for (NbtElement el : mailboxes) {
			if (el instanceof NbtCompound comp) {
				Mailbox mailbox = new Mailbox();
				mailbox.readNbt(comp, registryLookup);
			}
		}
		return state;
	}

	public static MailWorldState get(ServerWorld world) {
		world = world.getServer().getOverworld();

		return world.getPersistentStateManager().getOrCreate(
				new PersistentState.Type<>(
						() -> new MailWorldState(),
						(nbt, wrapper) -> readNbt(nbt, wrapper),
						// Fabric API handles null datafix types
						null
				),
				"cerulean_mail_state");
	}

	public static class Mailbox {
		public String address;
		public List<ItemStack> stacks = Lists.newArrayList();

		public Mailbox() {
		}

		public Mailbox(String address) {
			this.address = address;
		}

		protected void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
			nbt.putString("address", address);
			NbtList list = new NbtList();
			for (ItemStack stack : stacks) {
				list.add(stack.encodeAllowEmpty(registryLookup));
			}
			nbt.put("stacks", list);
		}

		protected void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
			address = nbt.getString("address");
			stacks.clear();
			for (NbtElement el : nbt.getList("stacks", NbtElement.COMPOUND_TYPE)) {
				ItemStack stack = ItemStack.fromNbtOrEmpty(registryLookup, (NbtCompound) el);
				stacks.add(stack);
			}
		}
	}
}
