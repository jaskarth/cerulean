package fmt.cerulean.screen;

import fmt.cerulean.net.packet.UpdatePropPacket;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.registry.CeruleanScreenHandlers;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;

import java.util.BitSet;

public class ProjectorScreenHandler extends ScreenHandler implements ScreenHandlerListener {
	private final PropertyDelegate props;
	public final BitSet changed = new BitSet(6);

	public ProjectorScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(1), new ArrayPropertyDelegate(7));
	}

	public ProjectorScreenHandler(int syncId, PlayerInventory playerInventory, Inventory projector, PropertyDelegate props) {
		super(CeruleanScreenHandlers.PROJECTOR, syncId);
		checkDataCount(props, 7);
		this.props = props;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}

		this.addSlot(new PhotoSlot(projector, 0, 144, 47));

		this.addProperties(props);
		this.addListener(this);
	}

	@Override
	public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {

	}

	@Override
	public void setProperty(int id, int value) {
		super.setProperty(id, value);
		changed.set(id);
	}

	public int get(int v) {
		return this.props.get(v);
	}

	@Override
	public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
		ClientPlayNetworking.send(new UpdatePropPacket(property, value));
	}

	private static class PhotoSlot extends Slot {
		public PhotoSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return stack.isOf(CeruleanItems.PHOTOGRAPH);
		}
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	public boolean active() {
		return props.get(0) == 1;
	}

	public void setRotX(int v) {
		props.set(1, v);
		this.sendContentUpdates();
	}

	public void setRotY(int v) {
		props.set(2, v);
		this.sendContentUpdates();
	}

	public void setRotZ(int v) {
		props.set(3, v);
		this.sendContentUpdates();
	}

	public void setDist(int v) {
		props.set(4, v);
		this.sendContentUpdates();
	}

	public void setSize(int v) {
		props.set(5, v);
		this.sendContentUpdates();
	}

	public void setAlpha(int v) {
		props.set(6, v);
		this.sendContentUpdates();
	}
}
