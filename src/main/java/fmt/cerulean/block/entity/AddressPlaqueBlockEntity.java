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
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AddressPlaqueBlockEntity extends SyncedBlockEntity implements Addressable, Obedient {
	public String address = "1 foo bar";

	public AddressPlaqueBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.ADDRESS_PLAQUE, pos, state);
	}

	@Override
	public String getAddress() {
		return address;
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, AddressPlaqueBlockEntity apbe) {
		if (world instanceof ServerWorld serverWorld) {
			MailWorldState mail = MailWorldState.get(serverWorld);
			if ("fill".equals(apbe.address) && !mail.mailboxes.containsKey("foo")) {
				mail.send("foo", List.of(new ItemStack(Items.STICK, 4), new ItemStack(Items.DIAMOND, 3)));
			} else {
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
								return;
							}
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
