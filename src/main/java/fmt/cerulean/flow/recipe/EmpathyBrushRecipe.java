package fmt.cerulean.flow.recipe;

import java.util.List;

import com.google.common.collect.Lists;

import fmt.cerulean.block.entity.AddressPlaqueBlockEntity;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.world.MailWorldState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class EmpathyBrushRecipe implements BrushRecipe {
	public CanvasRequirements canvas;
	public boolean strict;
	
	public EmpathyBrushRecipe(CanvasRequirements canvas, boolean strict) {
		this.canvas = canvas;
		this.strict = strict;
	}

	@Override
	public int getCraftTime() {
		return 80;
	}

	@Override
	public int getRequiredFlowInputs() {
		return 2;
	}

	@Override
	public boolean canCraft(PigmentInventory inventory) {
		return canvas.canCraft(inventory.world, inventory.pos, inventory.flow)
			&& inventory.containsAny(s -> s.getItem() == CeruleanItems.STAMP && (strict || s.contains(DataComponentTypes.CUSTOM_NAME)))
			&& inventory.getHeldStacks().size() > 1;
	}

	@Override
	public void craft(PigmentInventory inventory) {
		List<ItemStack> stacks = Lists.newArrayList(inventory.getHeldStacks());
		String address = null;
		for (int i = 0; i < stacks.size(); i++) {
			stacks.set(i, stacks.get(i).copy());
		}
		if (!strict) {
			for (int i = 0; i < stacks.size(); i++) {
				ItemStack stack = stacks.get(i);
				if (stack.getItem() == CeruleanItems.STAMP && stack.contains(DataComponentTypes.CUSTOM_NAME)) {
					Text name = stack.get(DataComponentTypes.CUSTOM_NAME);
					address = name.getString();
					stacks.remove(i);
					break;
				}
			}
		} else {
			if (inventory.world.getBlockEntity(inventory.pos) instanceof AddressPlaqueBlockEntity apbe) {
				address = apbe.address;
			}
			for (int i = 0; i < stacks.size(); i++) {
				ItemStack stack = stacks.get(i);
				if (stack.getItem() == CeruleanItems.STAMP) {
					stacks.remove(i);
					break;
				}
			}
		}
		if (address != null && inventory.world instanceof ServerWorld serverWorld) {
			MailWorldState.get(serverWorld).send(address, stacks);
			inventory.killItems(s -> true, Integer.MAX_VALUE);
		}
	}
}
