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
	private static final Set<Color> ALL_COLORS = Stream.of(Color.values()).collect(Collectors.toSet());
	private static final Set<Brightness> ALL_BRIGHTNESSES = Stream.of(Brightness.values()).collect(Collectors.toSet());
	private static final Set<Brightness> ALL_BRIGHTNESSES_EXCEPT_DIM = Stream.of(Brightness.values()).filter(b -> b != Brightness.DIM).collect(Collectors.toSet());
	private static final Set<Brightness> ALL_BRIGHTNESSES_EXCEPT_BRILLIANT = Stream.of(Brightness.values()).filter(b -> b != Brightness.BRILLIANT).collect(Collectors.toSet());
	public static final List<BrushRecipe> SOLO_RECIPES = Lists.newArrayList();
	public static final List<BrushRecipe> DUAL_RECIPES = Lists.newArrayList();

	public static void init() {
		addRecipe(new BerryFlavoringBrushRecipe());
		addRecipe(new BevvyTastingBrushRecipe());
		addRecipe(new ManifestationBrushRecipe());
		addRecipe(new AnxietyManifestationBrushRecipe());
		addRecipe(new CinderingAfterglowBrushRecipe(CanvasRequirements.of(
			Blocks.CAMPFIRE,
			ALL_COLORS,
			ALL_BRIGHTNESSES_EXCEPT_BRILLIANT
		), false));
		addRecipe(new CinderingAfterglowBrushRecipe(CanvasRequirements.of(
			Blocks.SOUL_CAMPFIRE,
			ALL_COLORS,
			Set.of(Brightness.DIM)
		), true));
		addRecipe(new InspirationBrushRecipe(
			CanvasRequirements.of(
				Set.of(Color.ASH), ALL_BRIGHTNESSES,
				Set.of(Color.LILAC), ALL_BRIGHTNESSES
			), 40, Ingredient.ofItems(Items.COARSE_DIRT), new ItemStack(Items.DIRT)));
		addRecipe(new TallPlantFilteringBrushRecipe(
			CanvasRequirements.of(Set.of(Blocks.KELP, Blocks.KELP_PLANT), Set.of(Color.CHARTREUSE, Color.LILAC), ALL_BRIGHTNESSES),
			flow -> flow.colored(Color.ASH),
			0.03f
		));
		addRecipe(new TallPlantFilteringBrushRecipe(
			CanvasRequirements.of(Blocks.BAMBOO, Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES),
			flow -> flow.colored(Color.CERULEAN),
			0.1f
		));
		addRecipe(new UnblightBrushRecipe(
			CanvasRequirements.of(
				Blocks.WHEAT, Set.of(Color.ROSE), ALL_BRIGHTNESSES_EXCEPT_DIM
			), (CropBlock) Blocks.WHEAT, Color.CHARTREUSE));

		addRecipe(new UnblightBrushRecipe(
			CanvasRequirements.of(
				Blocks.CARROTS, Set.of(Color.CERULEAN), ALL_BRIGHTNESSES_EXCEPT_DIM
			), (CropBlock) Blocks.CARROTS, Color.VIRIDIAN));

		addRecipe(new UnblightBrushRecipe(
				CanvasRequirements.of(
				Blocks.POTATOES, Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES_EXCEPT_DIM
			), (CropBlock) Blocks.POTATOES, Color.ASH));

		addRecipe(new UnblightBrushRecipe(
			CanvasRequirements.of(
				Blocks.BEETROOTS, Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES_EXCEPT_DIM
			), (CropBlock) Blocks.BEETROOTS, Color.LILAC));

		addRecipe(new UnblightBrushRecipe(
			CanvasRequirements.of(
				Blocks.TORCHFLOWER_CROP, Set.of(Color.ASH), ALL_BRIGHTNESSES_EXCEPT_DIM
			), (CropBlock) Blocks.TORCHFLOWER_CROP, Color.TURQUOISE));

		addRecipe(new ParadigmBrushRecipe(Color.ROSE, CeruleanBlocks.SPARKBLOSSOM, CeruleanBlocks.SPARKLESSBLOSSOM, new ItemStack(CeruleanItems.GLIMMERCRUMB)));
		addRecipe(new ParadigmBrushRecipe(Color.TURQUOISE, Blocks.BOOKSHELF, CeruleanBlocks.SORTED_BOOKSHELF, ItemStack.EMPTY));
		addRecipe(new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(Items.COPPER_INGOT), new ItemStack(CeruleanItems.EXPOSED_COPPER_INGOT)));
		addRecipe(new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.EXPOSED_COPPER_INGOT), new ItemStack(CeruleanItems.WEATHERED_COPPER_INGOT)));
		addRecipe(new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.WEATHERED_COPPER_INGOT), new ItemStack(CeruleanItems.OXIDIZED_COPPER_INGOT)));
		addRecipe(new InspirationBrushRecipe(CanvasRequirements.of(Set.of(Color.ROSE), ALL_BRIGHTNESSES, Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES),
			40, List.of(Ingredient.ofItems(CeruleanItems.OXIDIZED_COPPER_INGOT), Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB), Ingredient.ofItems(Items.REDSTONE)), new ItemStack(CeruleanItems.ORB)));
		addRecipe(new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.ASH), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.ORB), new ItemStack(CeruleanItems.MOVRB)));
		addRecipe(new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.CERULEAN), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.ORB), new ItemStack(CeruleanItems.JORB)));
		addRecipe(new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.LILAC), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.ORB), new ItemStack(CeruleanItems.KORB)));
		addRecipe(new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.ORB), new ItemStack(CeruleanItems.LORB)));

		addRecipe(new InspirationBrushRecipe(
			CanvasRequirements.of(
				Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES,
				Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES
			), 30, List.of(
				Ingredient.ofItems(Items.BROWN_DYE),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER)
			), new ItemStack(Items.BOOK)
		));

		addRecipe(new InspirationBrushRecipe.Uninspired(
			CanvasRequirements.of(
				Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES
			), 20, List.of(
				Ingredient.ofItems(Items.FIRE_CHARGE)
			), new ItemStack(Items.BLAZE_POWDER, 3)
		));

		addRecipe(new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.LILAC), ALL_BRIGHTNESSES,
						Set.of(Color.ASH), ALL_BRIGHTNESSES
				), 30, List.of(
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB)
		), new ItemStack(Items.SLIME_BALL)
		));

		addRecipe(new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.CERULEAN), ALL_BRIGHTNESSES,
						Set.of(Color.ROSE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.AMETHYST_SHARD)
		), new ItemStack(Items.QUARTZ)
		));

		addRecipe(new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.ASH), ALL_BRIGHTNESSES,
						Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.IRON_NUGGET),
				Ingredient.ofItems(Items.IRON_NUGGET),
				Ingredient.ofItems(Items.STICK)
		), new ItemStack(Items.RAIL)
		));

		addRecipe(new InspirationBrushRecipe.Uninspired(
			CanvasRequirements.of(
				Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT)
			), 60, List.of(
				Ingredient.ofItems(Items.GOLD_INGOT),
				Ingredient.ofItems(Items.REDSTONE),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB)
			), new ItemStack(CeruleanItems.FUCHSIA_INGOT)
		));

		addRecipe(new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.APPLE),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB)
		), new ItemStack(CeruleanItems.CANDY_APPLE)
		));

		toolRecipes();

		addRecipe(new InspirationBrushRecipe.Uninspired(
			CanvasRequirements.of(
				Set.of(Color.LILAC), ALL_BRIGHTNESSES
			), 5000, List.of(
				Ingredient.ofItems(Items.MUSIC_DISC_STAL)
			), new ItemStack(Items.MUSIC_DISC_WAIT)
		));
	}

	private static void toolRecipes() {
		addRecipe(new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_SWORD)
		), new ItemStack(CeruleanItems.FUCHSIA_SWORD)
		));

		addRecipe(new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_PICKAXE)
		), new ItemStack(CeruleanItems.FUCHSIA_PICKAXE)
		));

		addRecipe(new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_AXE)
		), new ItemStack(CeruleanItems.FUCHSIA_AXE)
		));

		addRecipe(new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_SHOVEL)
		), new ItemStack(CeruleanItems.FUCHSIA_SHOVEL)
		));

		addRecipe(new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_HOE)
		), new ItemStack(CeruleanItems.FUCHSIA_HOE)
		));
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
