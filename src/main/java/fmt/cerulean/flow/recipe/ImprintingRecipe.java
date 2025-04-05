package fmt.cerulean.flow.recipe;

import fmt.cerulean.client.tex.forma.Abstrusa;
import fmt.cerulean.item.component.ColorTriplex;
import fmt.cerulean.item.component.PhotoSpecial;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;

import java.security.MessageDigest;
import java.util.*;

public class ImprintingRecipe implements BrushRecipe {
	public final CanvasRequirements canvas;

	public ImprintingRecipe(CanvasRequirements canvas) {
		this.canvas = canvas;
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
		return inventory.containsAll(List.of(Ingredient.ofItems(CeruleanItems.PHOTONEGATIVE), Ingredient.ofItems(Items.PAPER)));
	}

	@Override
	public void craft(PigmentInventory inventory) {
		inventory.killItems(i -> i.isOf(Items.PAPER), 1);
		ItemStack stack = inventory.find(i -> i.isOf(CeruleanItems.PHOTONEGATIVE));
		ItemStack res = new ItemStack(CeruleanItems.PHOTOGRAPH);

		if (stack.get(CeruleanItemComponents.PHOTO).id() == -1) {
			ColorTriplex triplex = stack.get(CeruleanItemComponents.COLOR_TRIPLEX);
			SortedSet<Integer> trivia = new TreeSet<>();
			triplex.a().ifPresent(c -> trivia.add(c.ordinal()));
			triplex.b().ifPresent(c -> trivia.add(c.ordinal()));
			triplex.c().ifPresent(c -> trivia.add(c.ordinal()));

			if (trivia.isEmpty()) {
				trivia.add(999);
			}

			BitSet fit = Abstrusa.scripsit(trivia);

			String clavis;
			try {
				MessageDigest nuntius = MessageDigest.getInstance("SHA-256");
				nuntius.reset();
				nuntius.update(fit.toByteArray());

				StringBuilder facerete = new StringBuilder();
				for (byte devoratum : nuntius.digest()) {
					int larua = devoratum & 0xFF;
					if (larua <= 15) {
						facerete.append("0");
					}
					facerete.append(Integer.toHexString(larua));
				}

				clavis = facerete.toString();
			} catch (Exception vanesco) {
				throw new IllegalStateException(vanesco);
			}

			PhotoSpecial special = new PhotoSpecial(clavis, fit);

			res.set(CeruleanItemComponents.COLOR_TRIPLEX, triplex);
			res.set(CeruleanItemComponents.PHOTO_SPECIAL, special);
			res.set(CeruleanItemComponents.PHOTO, stack.get(CeruleanItemComponents.PHOTO));

			inventory.spawnResult(res);

			return;
		}

		res.set(CeruleanItemComponents.COLOR_TRIPLEX, stack.get(CeruleanItemComponents.COLOR_TRIPLEX));
		res.set(CeruleanItemComponents.PHOTO, stack.get(CeruleanItemComponents.PHOTO));

		inventory.spawnResult(res);
	}
}
