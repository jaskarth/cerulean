package fmt.cerulean.flow.recipe;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import fmt.cerulean.flow.FlowResource.Brightness;
import fmt.cerulean.flow.FlowResource.Color;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class BrushRecipes {
	private static final Set<Brightness> ALL_BRIGHTNESSES = Stream.of(Brightness.values()).collect(Collectors.toSet());
	public static final List<BrushRecipe> SOLO_RECIPES = Lists.newArrayList();
	public static final List<BrushRecipe> DUAL_RECIPES = Lists.newArrayList();

	public static void init() {
		addRecipe(new BerryFlavoringBrushRecipe());
		addRecipe(new InspirationBrushRecipe(
			CanvasRequirements.of(
				Set.of(Color.ASH), ALL_BRIGHTNESSES,
				Set.of(Color.LILAC), ALL_BRIGHTNESSES
			), 40, s -> s.isOf(Items.COARSE_DIRT), (a, b) -> new ItemStack(Items.DIRT)));
		addRecipe(new TallPlantFilteringBrushRecipe(
			CanvasRequirements.of(Blocks.KELP_PLANT, Set.of(Color.CHARTREUSE, Color.LILAC), ALL_BRIGHTNESSES),
			flow -> flow.colored(Color.ASH),
			0.1f
		));
		addRecipe(new UnblightBrushRecipe(
			CanvasRequirements.of(
				Blocks.WHEAT, Set.of(Color.ROSE), ALL_BRIGHTNESSES
			), (CropBlock) Blocks.WHEAT, s -> s.colored(Color.CHARTREUSE)));
	}

	private static void addRecipe(BrushRecipe recipe) {
		switch (recipe.getRequiredFlowInputs()) {
			case 1 -> SOLO_RECIPES.add(recipe);
			case 2 -> DUAL_RECIPES.add(recipe);
			default -> throw new UnsupportedOperationException("Only solo and dual brush recipes are supported");
		}
	}

	public static BrushRecipe getFirstValid(PigmentInventory inventory) {
		List<BrushRecipe> checked;
		if (inventory.opposing.empty()) {
			checked = SOLO_RECIPES;
		} else {
			checked = DUAL_RECIPES;
		}
		for (BrushRecipe recipe : checked) {
			if (recipe.matches(inventory, inventory.world)) {
				return recipe;
			}
		}
		return null;
	}
}
