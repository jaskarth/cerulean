package fmt.cerulean.flow.recipe;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fmt.cerulean.Cerulean;
import fmt.cerulean.flow.FlowResource.Brightness;
import fmt.cerulean.flow.FlowResource.Color;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class BrushRecipes {
	public static final Map<Identifier, BrushRecipe> BY_ID = Maps.newHashMap();
	public static final Map<BrushRecipe, Identifier> GET_ID = Maps.newHashMap();
	private static final Set<Color> ALL_COLORS = Stream.of(Color.values()).collect(Collectors.toSet());
	private static final Set<Brightness> ALL_BRIGHTNESSES = Stream.of(Brightness.values()).collect(Collectors.toSet());
	private static final Set<Brightness> ALL_BRIGHTNESSES_EXCEPT_DIM = Stream.of(Brightness.values()).filter(b -> b != Brightness.DIM).collect(Collectors.toSet());
	private static final Set<Brightness> ALL_BRIGHTNESSES_EXCEPT_BRILLIANT = Stream.of(Brightness.values()).filter(b -> b != Brightness.BRILLIANT).collect(Collectors.toSet());
	public static final List<BrushRecipe> SOLO_RECIPES = Lists.newArrayList();
	public static final List<BrushRecipe> DUAL_RECIPES = Lists.newArrayList();

	public static void init() {
		addRecipe("kelp_agoraphobia", new AgoraphobicGardeningBrushRecipe(
			CanvasRequirements.of(Set.of(Blocks.KELP, Blocks.KELP_PLANT), Set.of(Color.LILAC), ALL_BRIGHTNESSES),
			Blocks.KELP,
			Color.ASH,
			0.03f
		));
		addRecipe("bamboo_agoraphobia", new AgoraphobicGardeningBrushRecipe(
			CanvasRequirements.of(Blocks.BAMBOO, Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES),
			Blocks.BAMBOO,
			Color.TURQUOISE,
			0.05f
		));
		addRecipe("wheat_unblight", new UnblightBrushRecipe(
			CanvasRequirements.of(
				Blocks.WHEAT, Set.of(Color.ROSE), ALL_BRIGHTNESSES
			), (CropBlock) Blocks.WHEAT, Color.CHARTREUSE));

		addRecipe("carrots_unblight", new UnblightBrushRecipe(
			CanvasRequirements.of(
				Blocks.CARROTS, Set.of(Color.CERULEAN), ALL_BRIGHTNESSES
			), (CropBlock) Blocks.CARROTS, Color.VIRIDIAN));

		addRecipe("beetroots_unblight", new UnblightBrushRecipe(
				CanvasRequirements.of(
				Blocks.BEETROOTS, Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES
			), (CropBlock) Blocks.BEETROOTS, Color.ROSE));

		addRecipe("potatoes_unblight", new UnblightBrushRecipe(
			CanvasRequirements.of(
				Blocks.POTATOES, Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES
			), (CropBlock) Blocks.POTATOES, Color.LILAC));

		addRecipe("nether_wart_unblight", new UnblightBrushRecipe(
			CanvasRequirements.of(
				Blocks.NETHER_WART, Set.of(Color.ASH), ALL_BRIGHTNESSES
			), Blocks.NETHER_WART, Color.CERULEAN));

		addRecipe("glimmercrumb", new ParadigmBrushRecipe(Color.ROSE, CeruleanBlocks.SPARKBLOSSOM, CeruleanBlocks.SPARKLESSBLOSSOM, new ItemStack(CeruleanItems.GLIMMERCRUMB)));
		addRecipe("sorted_bookshelf", new ParadigmBrushRecipe(Color.TURQUOISE, Blocks.BOOKSHELF, CeruleanBlocks.SORTED_BOOKSHELF, ItemStack.EMPTY));
		addRecipe("exposed_copper_ingot", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(Items.COPPER_INGOT), new ItemStack(CeruleanItems.EXPOSED_COPPER_INGOT)));
		addRecipe("weathered_copper_ingot", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.EXPOSED_COPPER_INGOT), new ItemStack(CeruleanItems.WEATHERED_COPPER_INGOT)));
		addRecipe("oxidized_copper_ingot", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.WEATHERED_COPPER_INGOT), new ItemStack(CeruleanItems.OXIDIZED_COPPER_INGOT)));
		addRecipe("oxidized_carrot", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(Items.CARROT), new ItemStack(CeruleanItems.OXIDIZED_CARROT)));
		addRecipe("orb", new InspirationBrushRecipe(CanvasRequirements.of(Set.of(Color.ROSE), ALL_BRIGHTNESSES, Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES),
			40, List.of(Ingredient.ofItems(CeruleanItems.OXIDIZED_COPPER_INGOT), Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB), Ingredient.ofItems(Items.REDSTONE)), new ItemStack(CeruleanItems.ORB)));
		addRecipe("movrb", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.ASH), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.ORB), new ItemStack(CeruleanItems.MOVRB)));
		addRecipe("jorb", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.CERULEAN), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.ORB), new ItemStack(CeruleanItems.JORB)));
		addRecipe("korb", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.LILAC), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.ORB), new ItemStack(CeruleanItems.KORB)));
		addRecipe("lorb", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.ORB), new ItemStack(CeruleanItems.LORB)));

		addRecipe("white_dye_darkening", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.ASH), ALL_BRIGHTNESSES),
				40, Ingredient.ofItems(Items.WHITE_DYE), new ItemStack(Items.LIGHT_GRAY_DYE)));

		addRecipe("light_gray_dye_darkening", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.ASH), ALL_BRIGHTNESSES),
				40, Ingredient.ofItems(Items.LIGHT_GRAY_DYE), new ItemStack(Items.GRAY_DYE)));

		addRecipe("gray_dye_darkening", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.ASH), ALL_BRIGHTNESSES),
				40, Ingredient.ofItems(Items.GRAY_DYE), new ItemStack(Items.BLACK_DYE)));

		addRecipe("iron_ingot", new InspirationBrushRecipe(
			CanvasRequirements.of(
				Set.of(Color.ASH), ALL_BRIGHTNESSES,
				Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES
			), 56, List.of(
				Ingredient.ofItems(Items.RAW_IRON),
				Ingredient.ofItems(Items.RAW_IRON),
				Ingredient.ofItems(Items.RAW_IRON),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB)
			), new ItemStack(Items.IRON_INGOT, 8)
		));

		addRecipe("gold_ingot", new InspirationBrushRecipe(
			CanvasRequirements.of(
				Set.of(Color.ASH), ALL_BRIGHTNESSES,
				Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES
			), 56, List.of(
				Ingredient.ofItems(Items.RAW_GOLD),
				Ingredient.ofItems(Items.RAW_GOLD),
				Ingredient.ofItems(Items.RAW_GOLD),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB)
			), new ItemStack(Items.GOLD_INGOT, 8)
		));

		addRecipe("copper_ingot", new InspirationBrushRecipe(
			CanvasRequirements.of(
				Set.of(Color.ASH), ALL_BRIGHTNESSES,
				Set.of(Color.ROSE), ALL_BRIGHTNESSES
			), 56, List.of(
				Ingredient.ofItems(Items.RAW_COPPER),
				Ingredient.ofItems(Items.RAW_COPPER),
				Ingredient.ofItems(Items.RAW_COPPER),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB)
			), new ItemStack(Items.COPPER_INGOT, 8)
		));

		addRecipe("coke_iron_ingot", new InspirationBrushRecipe(
			CanvasRequirements.of(
				Set.of(Color.ASH), ALL_BRIGHTNESSES,
				Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES
			), 56, List.of(
				Ingredient.ofItems(Items.RAW_IRON),
				Ingredient.ofItems(Items.RAW_IRON),
				Ingredient.ofItems(Items.RAW_IRON),
				Ingredient.ofItems(CeruleanItems.GLITTERING_COAL)
			), new ItemStack(Items.IRON_INGOT, 4)
		));

		addRecipe("coke_gold_ingot", new InspirationBrushRecipe(
			CanvasRequirements.of(
				Set.of(Color.ASH), ALL_BRIGHTNESSES,
				Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES
			), 56, List.of(
				Ingredient.ofItems(Items.RAW_GOLD),
				Ingredient.ofItems(Items.RAW_GOLD),
				Ingredient.ofItems(Items.RAW_GOLD),
				Ingredient.ofItems(CeruleanItems.GLITTERING_COAL)
			), new ItemStack(Items.GOLD_INGOT, 4)
		));

		addRecipe("coke_copper_ingot", new InspirationBrushRecipe(
			CanvasRequirements.of(
				Set.of(Color.ASH), ALL_BRIGHTNESSES,
				Set.of(Color.ROSE), ALL_BRIGHTNESSES
			), 56, List.of(
				Ingredient.ofItems(Items.RAW_COPPER),
				Ingredient.ofItems(Items.RAW_COPPER),
				Ingredient.ofItems(Items.RAW_COPPER),
				Ingredient.ofItems(CeruleanItems.GLITTERING_COAL)
			), new ItemStack(Items.COPPER_INGOT, 4)
		));

		addRecipe("book", new InspirationBrushRecipe(
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

		addRecipe("blaze_powder", new InspirationBrushRecipe.Uninspired(
			CanvasRequirements.of(
				Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES
			), 20, List.of(
				Ingredient.ofItems(Items.FIRE_CHARGE)
			), new ItemStack(Items.BLAZE_POWDER, 3)
		));

		addRecipe("slime_ball", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.LILAC), ALL_BRIGHTNESSES,
						Set.of(Color.ASH), ALL_BRIGHTNESSES
				), 30, List.of(
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB)
		), new ItemStack(Items.SLIME_BALL)
		));

		addRecipe("gunpowder", new InspirationBrushRecipe(
			CanvasRequirements.of(
					Set.of(Color.ASH), ALL_BRIGHTNESSES,
					Set.of(Color.ASH), ALL_BRIGHTNESSES
			), 30, List.of(
				Ingredient.ofItems(Items.SUGAR),
				Ingredient.ofItems(Items.SUGAR),
				Ingredient.ofItems(Items.CHARCOAL)
			), new ItemStack(Items.GUNPOWDER, 3)
		));

		addRecipe("quartz", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.CERULEAN), ALL_BRIGHTNESSES,
						Set.of(Color.ROSE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.AMETHYST_SHARD)
		), new ItemStack(Items.QUARTZ)
		));

		addRecipe("rail", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES,
						Set.of(Color.LILAC), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.IRON_NUGGET),
				Ingredient.ofItems(Items.IRON_NUGGET),
				Ingredient.ofItems(Items.STICK)
		), new ItemStack(Items.RAIL)
		));

		addRecipe("ochre_froglight", new InspirationBrushRecipe(
			CanvasRequirements.of(
				Set.of(Color.CERULEAN), ALL_BRIGHTNESSES_EXCEPT_DIM,
				Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES_EXCEPT_DIM
			), 36, List.of(
				Ingredient.ofItems(Items.PUMPKIN),
				Ingredient.ofItems(Items.PUMPKIN),
				Ingredient.ofItems(Items.PUMPKIN),
				Ingredient.ofItems(Items.PUMPKIN),
				Ingredient.ofItems(Items.PUMPKIN),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB)
			), new ItemStack(Items.OCHRE_FROGLIGHT, 6)
		));

		addRecipe("verdant_froglight", new InspirationBrushRecipe(
			CanvasRequirements.of(
				Set.of(Color.CERULEAN), ALL_BRIGHTNESSES_EXCEPT_DIM,
				Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES_EXCEPT_DIM
			), 36, List.of(
				Ingredient.ofItems(Items.MELON),
				Ingredient.ofItems(Items.MELON),
				Ingredient.ofItems(Items.MELON),
				Ingredient.ofItems(Items.MELON),
				Ingredient.ofItems(Items.MELON),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB)
			), new ItemStack(Items.VERDANT_FROGLIGHT, 6)
		));

		addRecipe("pearlescent_froglight", new InspirationBrushRecipe(
			CanvasRequirements.of(
				Set.of(Color.CERULEAN), ALL_BRIGHTNESSES_EXCEPT_DIM,
				Set.of(Color.ROSE), ALL_BRIGHTNESSES_EXCEPT_DIM
			), 36, List.of(
				Ingredient.ofItems(Items.CHORUS_FLOWER),
				Ingredient.ofItems(Items.CHORUS_FLOWER),
				Ingredient.ofItems(Items.CHORUS_FLOWER),
				Ingredient.ofItems(Items.CHORUS_FLOWER),
				Ingredient.ofItems(Items.CHORUS_FLOWER),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB)
			), new ItemStack(Items.PEARLESCENT_FROGLIGHT, 6)
		));

		addRecipe("glittering_coal", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES,
						Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.GOLD_NUGGET),
				Ingredient.ofItems(Items.GOLD_NUGGET),
				Ingredient.ofItems(Items.COAL)
		), new ItemStack(CeruleanItems.GLITTERING_COAL)
		));

		addRecipe("fuchsia_ingot", new InspirationBrushRecipe.Uninspired(
			CanvasRequirements.of(
				Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT)
			), 60, List.of(
				Ingredient.ofItems(Items.GOLD_INGOT),
				Ingredient.ofItems(Items.REDSTONE),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB)
			), new ItemStack(CeruleanItems.FUCHSIA_INGOT)
		));

		addRecipe("candy_apple", new InspirationBrushRecipe.Uninspired(
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

		addRecipe("wait", new InspirationBrushRecipe.Uninspired(
			CanvasRequirements.of(
				Set.of(Color.LILAC), ALL_BRIGHTNESSES
			), 5000, List.of(
				Ingredient.ofItems(Items.MUSIC_DISC_STAL)
			), new ItemStack(Items.MUSIC_DISC_WAIT)
		));
		// start simple color changing
		addRecipe("lily_of_the_valley_filtering", new AgoraphobicGardeningBrushRecipe(
			CanvasRequirements.of(Blocks.LILY_OF_THE_VALLEY, Set.of(Color.ROSE), ALL_BRIGHTNESSES_EXCEPT_DIM),
			Blocks.LILY_OF_THE_VALLEY,
			Color.LILAC,
			0f
		));
		addRecipe("dandelion_filtering", new AgoraphobicGardeningBrushRecipe(
			CanvasRequirements.of(Blocks.DANDELION, Set.of(Color.ASH), ALL_BRIGHTNESSES_EXCEPT_DIM),
			Blocks.DANDELION,
			Color.CHARTREUSE,
			0f
		));
		addRecipe("rose_bush_filtering", new AgoraphobicGardeningBrushRecipe(
			CanvasRequirements.of(Blocks.ROSE_BUSH, Set.of(Color.CERULEAN), ALL_BRIGHTNESSES_EXCEPT_DIM),
			Blocks.ROSE_BUSH,
			Color.ROSE,
			0f
		));
		addRecipe("allium_filtering", new AgoraphobicGardeningBrushRecipe(
			CanvasRequirements.of(Blocks.ALLIUM, Set.of(Color.LILAC), ALL_BRIGHTNESSES_EXCEPT_DIM),
			Blocks.ALLIUM,
			Color.VIRIDIAN,
			0f
		));
		addRecipe("blue_orchid_filtering", new AgoraphobicGardeningBrushRecipe(
			CanvasRequirements.of(Blocks.BLUE_ORCHID, Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES_EXCEPT_DIM),
			Blocks.BLUE_ORCHID,
			Color.TURQUOISE,
			0f
		));
		addRecipe("cornflower_filtering", new AgoraphobicGardeningBrushRecipe(
			CanvasRequirements.of(Blocks.CORNFLOWER, Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES_EXCEPT_DIM),
			Blocks.CORNFLOWER,
			Color.CERULEAN,
			0f
		));
		addRecipe("azure_bluet_filtering", new AgoraphobicGardeningBrushRecipe(
			CanvasRequirements.of(Blocks.AZURE_BLUET, Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES_EXCEPT_DIM),
			Blocks.AZURE_BLUET,
			Color.ASH,
			0f
		));
		// end simple color changing
		addRecipe("berry_flavoring", new BerryFlavoringBrushRecipe());
		addRecipe("bevvy_tasting", new BevvyTastingBrushRecipe());
		addRecipe("manifestation", new ManifestationBrushRecipe());
		addRecipe("anxiety_manifestation", new AnxietyManifestationBrushRecipe());
		addRecipe("cindering_afterglow", new CinderingAfterglowBrushRecipe(CanvasRequirements.of(
			Blocks.CAMPFIRE,
			ALL_COLORS,
			ALL_BRIGHTNESSES_EXCEPT_BRILLIANT
		), false));
		addRecipe("cindering_afterglow_twice", new CinderingAfterglowBrushRecipe(CanvasRequirements.of(
			Blocks.SOUL_CAMPFIRE,
			ALL_COLORS,
			Set.of(Brightness.DIM)
		), true));
	}

	private static void toolRecipes() {
		addRecipe("fuchsia_sword", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_SWORD)
		), new ItemStack(CeruleanItems.FUCHSIA_SWORD)
		));

		addRecipe("fuchsia_pickaxe", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_PICKAXE)
		), new ItemStack(CeruleanItems.FUCHSIA_PICKAXE)
		));

		addRecipe("fuchsia_axe", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_AXE)
		), new ItemStack(CeruleanItems.FUCHSIA_AXE)
		));

		addRecipe("fuchsia_shovel", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_SHOVEL)
		), new ItemStack(CeruleanItems.FUCHSIA_SHOVEL)
		));

		addRecipe("fuchsia_hoe", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_HOE)
		), new ItemStack(CeruleanItems.FUCHSIA_HOE)
		));
	}

	private static void addRecipe(String path, BrushRecipe recipe) {
		Identifier id = Cerulean.id(path);
		if (BY_ID.containsKey(id)) {
			throw new IllegalArgumentException("Brush recipe " + id + " is already registered!");
		}
		BY_ID.put(id, recipe);
		GET_ID.put(recipe, id);
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
