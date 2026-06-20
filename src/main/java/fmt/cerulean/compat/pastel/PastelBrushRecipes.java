package fmt.cerulean.compat.pastel;

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

public class PastelBrushRecipes {

	public static void init() {
		Random random = new Random(581481349);
		for (DyeColor dye : DyeColor.values()) {
			BrushRecipes.addRecipe("pastel_" + dye.getName() + "_stripping", new InspirationBrushRecipe.Uninspired(
					CanvasRequirements.of(Set.of(FlowResource.Color.values()[random.nextInt(7)]), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM), 50,
					Ingredient.ofStacks(new ItemStack(Registries.BLOCK.get(Identifier.of("pastel", dye.getName() + "_log")))),
					new ItemStack(getPastel(dye.getName() + "_pigment"), 3)
			));
		}

		BrushRecipes.addRecipe("pastel_amethyst_dust", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(Set.of(FlowResource.Color.LILAC), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM), 50,
				Ingredient.ofStacks(new ItemStack(Items.AMETHYST_SHARD)),
				new ItemStack(getPastel("amethyst_powder"), 2)
		));

		BrushRecipes.addRecipe("pastel_citrine_dust", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(Set.of(FlowResource.Color.CHARTREUSE), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM), 50,
				Ingredient.ofStacks(new ItemStack(getPastel("citrine_shard"))),
				new ItemStack(getPastel("citrine_powder"), 2)
		));

		BrushRecipes.addRecipe("pastel_topaz_dust", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(Set.of(FlowResource.Color.TURQUOISE), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM), 50,
				Ingredient.ofStacks(new ItemStack(getPastel("topaz_shard"))),
				new ItemStack(getPastel("topaz_powder"), 2)
		));

		BrushRecipes.addRecipe("pastel_onyx_dust", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(Set.of(FlowResource.Color.ASH), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM), 50,
				Ingredient.ofStacks(new ItemStack(getPastel("onyx_shard"))),
				new ItemStack(getPastel("onyx_powder"), 2)
		));

		BrushRecipes.addRecipe("pastel_moonstone_dust", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(Set.of(FlowResource.Color.CERULEAN), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM), 50,
				Ingredient.ofStacks(new ItemStack(getPastel("moonstone_shard"))),
				new ItemStack(getPastel("moonstone_powder"), 2)
		));

		BrushRecipes.addRecipe("pastel_four_leaf_clover", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(FlowResource.Color.CERULEAN), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM,
						Set.of(FlowResource.Color.VIRIDIAN), BrushRecipes.ALL_BRIGHTNESSES_EXCEPT_DIM
				), 50, List.of(
						Ingredient.ofItems(getPastel("clover")),
						Ingredient.ofItems(getPastel("clover")),
						Ingredient.ofItems(getPastel("clover")),
						Ingredient.ofItems(getPastel("clover"))
				),
				new ItemStack(getPastel("four_leaf_clover"), 1)
		));
	}

	private static @NotNull Item getPastel(String id) {
		return Registries.ITEM.get(Identifier.of("pastel", id));
	}
}
