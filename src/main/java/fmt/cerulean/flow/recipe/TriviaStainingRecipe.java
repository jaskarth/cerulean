package fmt.cerulean.flow.recipe;

import fmt.cerulean.item.component.ColorTriplex;
import fmt.cerulean.registry.CeruleanItemComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Predicate;

public class TriviaStainingRecipe implements BrushRecipe {
	public final CanvasRequirements canvas;
	public final Item input;
	public final boolean misremembered;

	public TriviaStainingRecipe(CanvasRequirements canvas, Item input, boolean misremembered) {
		this.canvas = canvas;
		this.input = input;
		this.misremembered = misremembered;
	}

	@Override
	public int getRequiredFlowInputs() {
		return 1;
	}

	@Override
	public int getCraftTime() {
		return 15;
	}

	@Override
	public boolean canCraft(PigmentInventory inventory) {
		if (!canvas.canCraft(inventory.world, inventory.pos, inventory.flow)) {
			return false;
		}

		return inventory.containsAny(getItemStackPredicate(inventory));
	}

	private @NotNull Predicate<ItemStack> getItemStackPredicate(PigmentInventory inventory) {
		return item -> {
			if (item.isOf(this.input)) {
				ColorTriplex color = item.get(CeruleanItemComponents.COLOR_TRIPLEX);
				if (color == null) {
					return false;
				}

				if (misremembered && color.contains(inventory.flow.resource().getColor())) {
					return false;
				}

				return color.isPartial();
			}

			return false;
		};
	}

	@Override
	public void craft(PigmentInventory inventory) {
		Set<ItemStack> stacks = inventory.killItems(getItemStackPredicate(inventory), 1);
		if (stacks.isEmpty()) {
			throw new IllegalStateException("My memories are fading...");
		}

		ItemStack stack = stacks.iterator().next();

		ColorTriplex triplex = stack.get(CeruleanItemComponents.COLOR_TRIPLEX);

		ItemStack copy = stack.copy();
		copy.set(CeruleanItemComponents.COLOR_TRIPLEX, triplex.fill(inventory.flow.resource().getColor()));

		inventory.spawnResult(copy);
	}
}
