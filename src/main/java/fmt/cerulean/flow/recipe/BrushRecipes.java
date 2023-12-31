package fmt.cerulean.flow.recipe;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import fmt.cerulean.flow.FlowResource.Brightness;
import fmt.cerulean.flow.FlowResource.Color;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;

public class BrushRecipes {
	private static final Set<Brightness> ALL_BRIGHTNESSES = Stream.of(Brightness.values()).collect(Collectors.toSet());
	private static final Set<Brightness> ALL_BRIGHTNESSES_EXCEPT_DIM = Stream.of(Brightness.values()).filter(b -> b != Brightness.DIM).collect(Collectors.toSet());
	public static final List<BrushRecipe> SOLO_RECIPES = Lists.newArrayList();
	public static final List<BrushRecipe> DUAL_RECIPES = Lists.newArrayList();

	public static void init() {
		addRecipe(new BerryFlavoringBrushRecipe());
		addRecipe(new ManifestationBrushRecipe());
		addRecipe(new AnxietyManifestationBrushRecipe());
		addRecipe(new InspirationBrushRecipe(
			CanvasRequirements.of(
				Set.of(Color.ASH), ALL_BRIGHTNESSES,
				Set.of(Color.LILAC), ALL_BRIGHTNESSES
			), 40, Ingredient.ofItems(Items.COARSE_DIRT), new ItemStack(Items.DIRT)));
		addRecipe(new TallPlantFilteringBrushRecipe(
			CanvasRequirements.of(Blocks.KELP_PLANT, Set.of(Color.CHARTREUSE, Color.LILAC), ALL_BRIGHTNESSES),
			flow -> flow.colored(Color.ASH),
			0.1f
		));
		addRecipe(new UnblightBrushRecipe(
			CanvasRequirements.of(
				Blocks.WHEAT, Set.of(Color.ROSE), ALL_BRIGHTNESSES_EXCEPT_DIM
			), (CropBlock) Blocks.WHEAT, s -> s.coloredDimmer(Color.CHARTREUSE)));

		addRecipe(new UnblightBrushRecipe(
			CanvasRequirements.of(
				Blocks.CARROTS, Set.of(Color.CERULEAN), ALL_BRIGHTNESSES_EXCEPT_DIM
			), (CropBlock) Blocks.CARROTS, s -> s.coloredDimmer(Color.VIRIDIAN)));

		addRecipe(new UnblightBrushRecipe(
				CanvasRequirements.of(
				Blocks.POTATOES, Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES_EXCEPT_DIM
			), (CropBlock) Blocks.POTATOES, s -> s.coloredDimmer(Color.ASH)));

		addRecipe(new UnblightBrushRecipe(
			CanvasRequirements.of(
				Blocks.BEETROOTS, Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES_EXCEPT_DIM
			), (CropBlock) Blocks.BEETROOTS, s -> s.coloredDimmer(Color.LILAC)));

		addRecipe(new UnblightBrushRecipe(
			CanvasRequirements.of(
				Blocks.TORCHFLOWER_CROP, Set.of(Color.ASH), ALL_BRIGHTNESSES_EXCEPT_DIM
			), (CropBlock) Blocks.TORCHFLOWER_CROP, s -> s.coloredDimmer(Color.TURQUOISE)));

		addRecipe(new ParadigmBrushRecipe(Color.ROSE, CeruleanBlocks.SPARKBLOSSOM, CeruleanBlocks.SPARKLESSBLOSSOM, new ItemStack(CeruleanItems.GLIMMERCRUMB)));
		addRecipe(new ParadigmBrushRecipe(Color.TURQUOISE, Blocks.BOOKSHELF, CeruleanBlocks.SORTED_BOOKSHELF, null));
		addRecipe(new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, s -> s.isOf(Items.COPPER_INGOT), (a, b) -> new ItemStack(CeruleanItems.EXPOSED_COPPER_INGOT)));
		addRecipe(new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, s -> s.isOf(CeruleanItems.EXPOSED_COPPER_INGOT), (a, b) -> new ItemStack(CeruleanItems.WEATHERED_COPPER_INGOT)));
		addRecipe(new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, s -> s.isOf(CeruleanItems.WEATHERED_COPPER_INGOT), (a, b) -> new ItemStack(CeruleanItems.OXIDIZED_COPPER_INGOT)));
		addRecipe(new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.ASH), ALL_BRIGHTNESSES),
			40, s -> s.isOf(CeruleanItems.ORB), (a, b) -> new ItemStack(CeruleanItems.MOVRB)));
		addRecipe(new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.CERULEAN), ALL_BRIGHTNESSES),
			40, s -> s.isOf(CeruleanItems.ORB), (a, b) -> new ItemStack(CeruleanItems.JORB)));
		addRecipe(new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.LILAC), ALL_BRIGHTNESSES),
			40, s -> s.isOf(CeruleanItems.ORB), (a, b) -> new ItemStack(CeruleanItems.KORB)));
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
