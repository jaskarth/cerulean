package fmt.cerulean.screen;

import fmt.cerulean.registry.CeruleanScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class RealityAnchorScreenHandler extends ScreenHandler {
	private final PropertyDelegate props;

	public RealityAnchorScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new ArrayPropertyDelegate(2));
	}

	public RealityAnchorScreenHandler(int syncId, PlayerInventory playerInventory, PropertyDelegate props) {
		super(CeruleanScreenHandlers.REALITY_ANCHOR, syncId);
		checkDataCount(props, 2);
		this.props = props;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}

		this.addProperties(props);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	public int getFullness() {
		return props.get(0);
	}

	public int getHash() {
		return props.get(1);
	}
}
