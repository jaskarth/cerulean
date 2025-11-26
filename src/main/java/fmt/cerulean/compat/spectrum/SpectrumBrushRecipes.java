package fmt.cerulean.compat.spectrum;

import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.flow.recipe.BrushRecipes;
import fmt.cerulean.flow.recipe.CanvasRequirements;
import fmt.cerulean.flow.recipe.InspirationBrushRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class SpectrumBrushRecipes {

	public static void init() {
		Random random = new Random(581481349);
		for (DyeColor dye : DyeColor.values()) {
			BrushRecipes.addRecipe("spectrum_" + dye.getName() + "_stripping", new InspirationBrushRecipe.Uninspired(
					CanvasRequirements.of(Set.of(FlowResource.Color.values()[random.nextInt(7)]), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM), 50,
					Ingredient.ofStacks(new ItemStack(Registries.BLOCK.get(Identifier.of("spectrum", dye.getName() + "_log")))),
					new ItemStack(getSpectrum(dye.getName() + "_pigment"), 3)
			));
		}

		BrushRecipes.addRecipe("spectrum_amethyst_dust", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(Set.of(FlowResource.Color.LILAC), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM), 50,
				Ingredient.ofStacks(new ItemStack(Items.AMETHYST_SHARD)),
				new ItemStack(getSpectrum("amethyst_powder"), 2)
		));

		BrushRecipes.addRecipe("spectrum_citrine_dust", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(Set.of(FlowResource.Color.CHARTREUSE), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM), 50,
				Ingredient.ofStacks(new ItemStack(getSpectrum("citrine_shard"))),
				new ItemStack(getSpectrum("citrine_powder"), 2)
		));

		BrushRecipes.addRecipe("spectrum_topaz_dust", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(Set.of(FlowResource.Color.TURQUOISE), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM), 50,
				Ingredient.ofStacks(new ItemStack(getSpectrum("topaz_shard"))),
				new ItemStack(getSpectrum("topaz_powder"), 2)
		));

		BrushRecipes.addRecipe("spectrum_onyx_dust", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(Set.of(FlowResource.Color.ASH), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM), 50,
				Ingredient.ofStacks(new ItemStack(getSpectrum("onyx_shard"))),
				new ItemStack(getSpectrum("onyx_powder"), 2)
		));

		BrushRecipes.addRecipe("spectrum_moonstone_dust", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(Set.of(FlowResource.Color.CERULEAN), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM), 50,
				Ingredient.ofStacks(new ItemStack(getSpectrum("moonstone_shard"))),
				new ItemStack(getSpectrum("moonstone_powder"), 2)
		));

		BrushRecipes.addRecipe("spectrum_four_leaf_clover", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(FlowResource.Color.CERULEAN), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM,
						Set.of(FlowResource.Color.VIRIDIAN), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM
				), 50, List.of(
						Ingredient.ofItems(getSpectrum("clover")),
						Ingredient.ofItems(getSpectrum("clover")),
						Ingredient.ofItems(getSpectrum("clover")),
						Ingredient.ofItems(getSpectrum("clover"))
				),
				new ItemStack(getSpectrum("four_leaf_clover"), 1)
		));
	}

	private static @NotNull Item getSpectrum(String id) {
		return Registries.ITEM.get(Identifier.of("spectrum", id));
	}
}
