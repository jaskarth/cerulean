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
	private static final Set<Color> ALL_COLORS_EXCEPT_ASH = Stream.of(Color.values()).filter(c -> c != Color.ASH).collect(Collectors.toSet());
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
		addRecipe("glittercrumb", new ParadigmBrushRecipe(Color.ROSE, CeruleanBlocks.GLITTERING_SPARKBLOSSOM, CeruleanBlocks.SPARKLESSBLOSSOM, new ItemStack(CeruleanItems.GLITTERCRUMB)));
		addRecipe("stick_glimmercrumb", new ParadigmBrushRecipe(Color.ROSE, CeruleanBlocks.STICKBLOSSOM, CeruleanBlocks.STICKBLOSSOMLESS, new ItemStack(CeruleanItems.GLIMMERCRUMB)));
		addRecipe("stick_glittercrumb", new ParadigmBrushRecipe(Color.ROSE, CeruleanBlocks.GLITTERING_STICKBLOSSOM, CeruleanBlocks.STICKBLOSSOMLESS, new ItemStack(CeruleanItems.GLITTERCRUMB)));
		addRecipe("sorted_bookshelf", new ParadigmBrushRecipe(Color.TURQUOISE, Blocks.BOOKSHELF, CeruleanBlocks.SORTED_BOOKSHELF, ItemStack.EMPTY));
		addRecipe("glass_to_ice", new ParadigmBrushRecipe(Color.TURQUOISE, Blocks.GLASS, Blocks.ICE, ItemStack.EMPTY));
		addRecipe("glass_to_snow", new ParadigmBrushRecipe(Color.CERULEAN, Blocks.GLASS, Blocks.SNOW_BLOCK, ItemStack.EMPTY));
		addRecipe("packed_ice", new ParadigmBrushRecipe(Color.ASH, Blocks.ICE, Blocks.PACKED_ICE, ItemStack.EMPTY));
		addRecipe("blue_ice", new ParadigmBrushRecipe(Color.CERULEAN, Blocks.PACKED_ICE, Blocks.BLUE_ICE, ItemStack.EMPTY));
		addRecipe("crying_obsidian", new ParadigmBrushRecipe(Color.CERULEAN, Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN, ItemStack.EMPTY));
		addRecipe("decrying_obsidian", new ParadigmBrushRecipe(Color.ASH, Blocks.CRYING_OBSIDIAN, Blocks.OBSIDIAN, ItemStack.EMPTY));
		addRecipe("gravel", new ParadigmBrushRecipe(Color.CERULEAN, Blocks.COBBLESTONE, Blocks.GRAVEL, ItemStack.EMPTY));
		addRecipe("sand", new ParadigmBrushRecipe(Color.CHARTREUSE, Blocks.GRAVEL, Blocks.SAND, new ItemStack(Items.FLINT)));
		addRecipe("glass", new ParadigmBrushRecipe(Color.CERULEAN, Blocks.SAND, Blocks.GLASS, ItemStack.EMPTY));
		addRecipe("deepslate", new ParadigmBrushRecipe(Color.ASH, Blocks.STONE, Blocks.DEEPSLATE, ItemStack.EMPTY));
		addRecipe("blackstone", new ParadigmBrushRecipe(Color.TURQUOISE, Blocks.DEEPSLATE, Blocks.BLACKSTONE, ItemStack.EMPTY));
		addRecipe("exposed_copper_ingot", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(Items.COPPER_INGOT), new ItemStack(CeruleanItems.EXPOSED_COPPER_INGOT)));
		addRecipe("weathered_copper_ingot", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.EXPOSED_COPPER_INGOT), new ItemStack(CeruleanItems.WEATHERED_COPPER_INGOT)));
		addRecipe("oxidized_copper_ingot", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(CeruleanItems.WEATHERED_COPPER_INGOT), new ItemStack(CeruleanItems.OXIDIZED_COPPER_INGOT)));
		addRecipe("oxidized_carrot", new InspirationBrushRecipe.Uninspired(CanvasRequirements.of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES),
			40, Ingredient.ofItems(Items.CARROT), new ItemStack(CeruleanItems.OXIDIZED_CARROT)));
		addRecipe("orb", new InspirationBrushRecipe(CanvasRequirements.of(Set.of(Color.ROSE), ALL_BRIGHTNESSES, Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES),
			40, List.of(Ingredient.ofItems(CeruleanItems.OXIDIZED_COPPER_INGOT), Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB, CeruleanItems.GLITTERCRUMB), Ingredient.ofItems(Items.REDSTONE)), new ItemStack(CeruleanItems.ORB)));
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
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB, CeruleanItems.GLITTERCRUMB)
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
				), 10, List.of(
				Ingredient.ofItems(Items.IRON_NUGGET),
				Ingredient.ofItems(Items.IRON_NUGGET),
				Ingredient.ofItems(Items.STICK)
		), new ItemStack(Items.RAIL, 2)
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
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB, CeruleanItems.GLITTERCRUMB)
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
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB, CeruleanItems.GLITTERCRUMB)
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
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB, CeruleanItems.GLITTERCRUMB)
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

		addRecipe("glistering_melon", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES,
						Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.GOLD_NUGGET),
				Ingredient.ofItems(Items.GOLD_NUGGET),
				Ingredient.ofItems(Items.MELON_SLICE)
		), new ItemStack(Items.GLISTERING_MELON_SLICE)
		));

		addRecipe("fuchsia_ingot", new InspirationBrushRecipe.Uninspired(
			CanvasRequirements.of(
				Set.of(Color.ROSE), ALL_BRIGHTNESSES_EXCEPT_DIM
			), 60, List.of(
				Ingredient.ofItems(Items.GOLD_INGOT),
				Ingredient.ofItems(Items.REDSTONE),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB, CeruleanItems.GLITTERCRUMB)
			), new ItemStack(CeruleanItems.FUCHSIA_INGOT)
		));

		addRecipe("lustrous_ingot", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 60, List.of(
				Ingredient.ofItems(Items.IRON_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB, CeruleanItems.GLITTERCRUMB)
		), new ItemStack(CeruleanItems.LUSTROUS_INGOT, 2)
		));

		addRecipe("crushed_halite", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						ALL_COLORS, Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.HALITE)
		), new ItemStack(CeruleanItems.CRUSHED_HALITE, 2)
		));

		addRecipe("candy_apple", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.APPLE),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB, CeruleanItems.GLITTERCRUMB)
		), new ItemStack(CeruleanItems.CANDY_APPLE)
		));

		toolRecipes();

		addRecipe("strongbox", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanBlocks.SMOOTH_SPACEROCK),
				Ingredient.ofItems(CeruleanItems.GLASS_LENS)
		), new ItemStack(CeruleanBlocks.STRONGBOX)
		));

		addRecipe("wait", new InspirationBrushRecipe.Uninspired(
			CanvasRequirements.of(
				Set.of(Color.LILAC), ALL_BRIGHTNESSES
			), 5000, List.of(
				Ingredient.ofItems(Items.MUSIC_DISC_STAL)
			), new ItemStack(Items.MUSIC_DISC_WAIT)
		));

		addRecipe("disc_5", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 555, List.of(
				Ingredient.ofItems(Items.DISC_FRAGMENT_5),
				Ingredient.ofItems(Items.DISC_FRAGMENT_5),
				Ingredient.ofItems(Items.DISC_FRAGMENT_5),
				Ingredient.ofItems(Items.DISC_FRAGMENT_5),
				Ingredient.ofItems(Items.DISC_FRAGMENT_5)
		), new ItemStack(Items.MUSIC_DISC_5)
		));

		addRecipe("eye_of_vendor", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.CERULEAN), Set.of(Brightness.BRILLIANT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.REFLECTIVE_LENS),
				Ingredient.ofItems(Items.ENDER_EYE)
		), new ItemStack(CeruleanItems.EYE_OF_VENDOR)
		));

		addRecipe("eye_of_mender", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 60, List.of(
				Ingredient.ofItems(Items.EXPERIENCE_BOTTLE),
				Ingredient.ofItems(Items.ENDER_EYE)
		), new ItemStack(CeruleanItems.EYE_OF_MENDER, 4)
		));

		addRecipe("eye_of_return_to_sender", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.LILAC), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.STAMP),
				Ingredient.ofItems(Items.ENDER_EYE)
		), new ItemStack(CeruleanItems.EYE_OF_RETURN_TO_SENDER)
		));

		addRecipe("address_plaque", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.CERULEAN), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.STAMP),
				Ingredient.ofItems(CeruleanItems.DUCTILE_INGOT)
		), new ItemStack(CeruleanBlocks.ADDRESS_PLAQUE)
		));

		addRecipe("flag", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.CERULEAN), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.STAMP),
				Ingredient.ofItems(CeruleanItems.DUCTILE_ROD),
				Ingredient.ofItems(Items.REDSTONE)
		), new ItemStack(CeruleanBlocks.FLAG)
		));

		addRecipe("self_collapsing_cube", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.LILAC), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 40, List.of(
				Ingredient.ofItems(CeruleanBlocks.OAK_GAPDOOR),
				Ingredient.ofItems(CeruleanBlocks.OAK_GAPDOOR),
				Ingredient.ofItems(CeruleanBlocks.OAK_GAPDOOR),
				Ingredient.ofItems(CeruleanBlocks.OAK_GAPDOOR),
				Ingredient.ofItems(CeruleanBlocks.OAK_GAPDOOR),
				Ingredient.ofItems(CeruleanBlocks.OAK_GAPDOOR),
				Ingredient.ofItems(CeruleanItems.GLITTER),
				Ingredient.ofItems(Items.STRING),
				Ingredient.ofItems(Items.REDSTONE)
		), new ItemStack(CeruleanBlocks.SELF_COLLAPSING_CUBE)
		));

		addRecipe("camera", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.TURQUOISE), Set.of(Brightness.BRILLIANT),
						Set.of(Color.CERULEAN), Set.of(Brightness.BRILLIANT)
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.REFLECTIVE_LENS),
				Ingredient.ofItems(CeruleanItems.EYE_OF_VENDOR),
				Ingredient.ofItems(CeruleanItems.LUSTROUS_INGOT),
				Ingredient.ofItems(CeruleanItems.LUSTROUS_INGOT),
				Ingredient.ofItems(CeruleanItems.HALITE)
		), new ItemStack(CeruleanItems.CAMERA)
		));

		addRecipe("film", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.ASH), ALL_BRIGHTNESSES_EXCEPT_DIM,
						Set.of(Color.CERULEAN), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.GLITTERCRUMB),
				Ingredient.ofItems(CeruleanItems.HALITE),
				Ingredient.ofItems(CeruleanItems.LUSTROUS_INGOT)
		), new ItemStack(CeruleanItems.FILM, 2)
		));

		addRecipe("cards", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.LILAC), Set.of(Brightness.BRILLIANT),
						Set.of(Color.CERULEAN), Set.of(Brightness.BRILLIANT)
				), 42, List.of(
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(CeruleanItems.GLITTERCRUMB),
				Ingredient.ofItems(CeruleanItems.GLITTER)
		), new ItemStack(CeruleanItems.CARDS, 1)
		));

		addRecipe("photonegative_coloring", new TriviaStainingRecipe(
				CanvasRequirements.of(Blocks.AIR, ALL_COLORS, ALL_BRIGHTNESSES),
				CeruleanItems.PHOTONEGATIVE, CeruleanItems.PHOTONEGATIVE, false
		));

		addRecipe("glitter_0", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ASH), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.CRUSHED_HALITE)
		), new ItemStack(CeruleanItems.UNINITIATED_GLITTER, 1)
		));

		addRecipe("glitter_1", new TriviaStainingRecipe(
				CanvasRequirements.of(ALL_COLORS_EXCEPT_ASH, Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)),
				CeruleanItems.UNINITIATED_GLITTER, CeruleanItems.IMBIBED_GLITTER, true
		));

		addRecipe("glitter_2", new TriviaStainingRecipe(
				CanvasRequirements.of(ALL_COLORS_EXCEPT_ASH, Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)),
				CeruleanItems.IMBIBED_GLITTER, CeruleanItems.AWAKENED_GLITTER, true
		));

		addRecipe("glitter_3", new TriviaStainingRecipe(
				CanvasRequirements.of(ALL_COLORS_EXCEPT_ASH, Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)),
				CeruleanItems.AWAKENED_GLITTER, CeruleanItems.GLITTER, true
		));

		addRecipe("photo_imprinting", new ImprintingRecipe(
				CanvasRequirements.of(CeruleanBlocks.POLYETHYLENE, ALL_COLORS, ALL_BRIGHTNESSES)
		));

		addRecipe("stamp", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 40, List.of(
				Ingredient.ofItems(Items.PAPER),
				Ingredient.ofItems(CeruleanItems.CRUSHED_HALITE)
		), new ItemStack(CeruleanItems.STAMP, 8)
		));

		addRecipe("empathy_stress", new EmpathyBrushRecipe(
				CanvasRequirements.of(
					Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES_EXCEPT_DIM,
					Set.of(Color.CERULEAN), ALL_BRIGHTNESSES_EXCEPT_DIM
				), false
		));

		addRecipe("empathy_relief", new EmpathyBrushRecipe(
				CanvasRequirements.of(
					CeruleanBlocks.ADDRESS_PLAQUE,
					Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES_EXCEPT_DIM,
					Set.of(Color.CERULEAN), ALL_BRIGHTNESSES_EXCEPT_DIM
				), true
		));

		addRecipe("glass_lens", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						ALL_COLORS, Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)
				), 40, List.of(
				Ingredient.ofItems(Items.GLASS_PANE),
				Ingredient.ofItems(Items.GLASS_PANE),
				Ingredient.ofItems(Items.GLASS_PANE),
				Ingredient.ofItems(Items.GLASS_PANE)
		), new ItemStack(CeruleanItems.GLASS_LENS, 1)
		));

		addRecipe("item_detector", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.CERULEAN), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT),
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)
				), 40, List.of(
				Ingredient.ofItems(Blocks.OBSERVER),
				Ingredient.ofItems(CeruleanItems.GLASS_LENS),
				Ingredient.ofItems(CeruleanItems.GLASS_LENS)
		), new ItemStack(CeruleanBlocks.ITEM_DETECTOR, 1)
		));

		addRecipe("pipe_detector", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.ASH), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT),
						Set.of(Color.ASH), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)
				), 40, List.of(
				Ingredient.ofItems(Blocks.OBSERVER),
				Ingredient.ofItems(CeruleanItems.GLASS_LENS),
				Ingredient.ofItems(CeruleanItems.GLASS_LENS)
		), new ItemStack(CeruleanBlocks.PIPE_DETECTOR, 1)
		));

		addRecipe("make_glittercrumb", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.LILAC), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT),
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.BREADCRUMBS),
				Ingredient.ofItems(CeruleanItems.GLITTER),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB, CeruleanItems.GLITTERCRUMB)
		), new ItemStack(CeruleanItems.GLITTERCRUMB, 2)
		));

		addRecipe("breadcrumbs", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.LILAC), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.BREAD)
		), new ItemStack(CeruleanItems.BREADCRUMBS, 2)
		));

		addRecipe("pumpkin_pie", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ASH), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.BREADCRUMBS),
				Ingredient.ofItems(Items.SUGAR),
				Ingredient.ofItems(Items.EGG),
				Ingredient.ofItems(Blocks.PUMPKIN)
		), new ItemStack(Items.PUMPKIN_PIE, 4)
		));

		addRecipe("ductile_rod", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.CHARTREUSE), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.DUCTILE_INGOT)
		), new ItemStack(CeruleanItems.DUCTILE_ROD, 2)
		));

		addRecipe("stickblossom", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.VIRIDIAN), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.DUCTILE_ROD),
				Ingredient.ofItems(CeruleanItems.DUCTILE_ROD),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB)
		), new ItemStack(CeruleanBlocks.STICKBLOSSOM, 1)
		));

		addRecipe("glittering_stickblossom", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.VIRIDIAN), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.DUCTILE_ROD),
				Ingredient.ofItems(CeruleanItems.DUCTILE_ROD),
				Ingredient.ofItems(CeruleanItems.GLITTERCRUMB)
		), new ItemStack(CeruleanBlocks.GLITTERING_STICKBLOSSOM, 1)
		));

		addRecipe("sparkling_water", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Blocks.WATER,
						Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.GLASS_BOTTLE),
				Ingredient.ofItems(CeruleanItems.GLITTER)
		), new ItemStack(CeruleanItems.SPARKLING_WATER, 1)
		));

		addRecipe("oxidation_potion", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.GLASS_BOTTLE),
				Ingredient.ofItems(CeruleanItems.OXIDIZED_CARROT),
				Ingredient.ofItems(CeruleanItems.OXIDIZED_CARROT),
				Ingredient.ofItems(CeruleanItems.OXIDIZED_CARROT),
				Ingredient.ofItems(CeruleanItems.OXIDIZED_CARROT)
		), new ItemStack(CeruleanItems.OXIDATION_POTION, 1)
		));

		addRecipe("splash_oxidation_potion", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.OXIDATION_POTION),
				Ingredient.ofItems(CeruleanItems.OXIDATION_POTION)
		), new ItemStack(CeruleanItems.SPLASH_OXIDATION_POTION, 1)
		));

		addRecipe("vacuum_pump", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.CERULEAN), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_INGOT)
		), new ItemStack(CeruleanItems.VACUUM_PUMP, 1)
		));

		addRecipe("depressurizer", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.CERULEAN), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.GOLD_INGOT)
		), new ItemStack(CeruleanItems.DEPRESSURIZER, 1)
		));

		addRecipe("drum", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT)
		), new ItemStack(CeruleanItems.EMPTY_DRUM, 1)
		));

		addRecipe("light_core", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ASH), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Blocks.HEAVY_CORE),
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB, CeruleanItems.GLITTERCRUMB)
		), new ItemStack(CeruleanBlocks.LIGHT_CORE, 4)
		));

		addRecipe("destiny_detector", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ASH), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(CeruleanBlocks.SMOOTH_SPACEROCK),
				Ingredient.ofItems(CeruleanItems.GLASS_LENS),
				Ingredient.ofItems(Items.REDSTONE)
		), new ItemStack(CeruleanBlocks.DESTINY_DETECTOR, 1)
		));

		addRecipe("oak_gapdoor", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ASH), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Blocks.OAK_TRAPDOOR)
		), new ItemStack(CeruleanBlocks.OAK_GAPDOOR, 1)
		));

		addRecipe("amethyst_shard", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Blocks.AMETHYST_BLOCK),
				Ingredient.ofItems(CeruleanItems.GLITTERCRUMB)
		), new ItemStack(Items.AMETHYST_SHARD, 3)
		));

		addRecipe("amethyst_block_from_halite", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.LILAC), ALL_BRIGHTNESSES,
						Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.AMETHYST_SHARD),
				Ingredient.ofItems(CeruleanItems.HALITE),
				Ingredient.ofItems(CeruleanItems.HALITE),
				Ingredient.ofItems(CeruleanItems.HALITE),
				Ingredient.ofItems(CeruleanItems.HALITE)
		), new ItemStack(Blocks.AMETHYST_BLOCK, 1)
		));

		addRecipe("mossy_cobblestone", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Blocks.COBBLESTONE)
		), new ItemStack(Blocks.MOSSY_COBBLESTONE, 1)
		));

		addRecipe("mossy_stone_bricks", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Blocks.STONE_BRICKS)
		), new ItemStack(Blocks.MOSSY_STONE_BRICKS, 1)
		));

		addRecipe("sugar", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.SUGAR_CANE)
		), new ItemStack(Items.SUGAR, 3)
		));

		addRecipe("string", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(CeruleanItems.GLIMMERCRUMB, CeruleanItems.GLITTERCRUMB),
				Ingredient.ofItems(Items.PAPER)
		), new ItemStack(Items.STRING, 3)
		));

		addRecipe("feather", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.CERULEAN), ALL_BRIGHTNESSES,
						Set.of(Color.LILAC), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.STRING)
		), new ItemStack(Items.FEATHER, 1)
		));

		addRecipe("leather", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES,
						Set.of(Color.LILAC), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.ROTTEN_FLESH),
				Ingredient.ofItems(Items.ROTTEN_FLESH),
				Ingredient.ofItems(Items.ROTTEN_FLESH),
				Ingredient.ofItems(Items.ROTTEN_FLESH)
		), new ItemStack(Items.LEATHER, 1)
		));

		addRecipe("leather_2", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES,
						Set.of(Color.LILAC), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.WHEAT),
				Ingredient.ofItems(Items.STRING),
				Ingredient.ofItems(Items.RABBIT_HIDE)
		), new ItemStack(Items.LEATHER, 1)
		));

		addRecipe("rabbit_hide", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.ROSE), ALL_BRIGHTNESSES,
						Set.of(Color.CHARTREUSE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.LEATHER)
		), new ItemStack(Items.RABBIT_HIDE, 2)
		));

		addRecipe("rabbit_foot", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.CERULEAN), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT),
						Set.of(Color.TURQUOISE), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)
				), 40, List.of(
				Ingredient.ofItems(Items.LEATHER),
				Ingredient.ofItems(Items.RABBIT_HIDE),
				Ingredient.ofItems(CeruleanItems.OXIDATION_POTION),
				Ingredient.ofItems(CeruleanItems.OXIDIZED_CARROT),
				Ingredient.ofItems(CeruleanItems.OXIDIZED_CARROT),
				Ingredient.ofItems(CeruleanItems.GLITTER)
		), new ItemStack(Items.RABBIT_FOOT, 1)
		));

		addRecipe("ink_sac", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES,
						Set.of(Color.ROSE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Items.BLACK_DYE),
				Ingredient.ofItems(Items.LEATHER)
		), new ItemStack(Items.INK_SAC, 2)
		));

		addRecipe("name_tag", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.CERULEAN), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT),
						Set.of(Color.VIRIDIAN), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)
				), 40, List.of(
				Ingredient.ofItems(Items.STRING),
				Ingredient.ofItems(CeruleanItems.OXIDATION_POTION),
				Ingredient.ofItems(CeruleanItems.GLITTER),
				Ingredient.ofItems(Items.BAMBOO),
				Ingredient.ofItems(CeruleanItems.DUCTILE_INGOT)
		), new ItemStack(Items.NAME_TAG, 1)
		));

		addRecipe("clay", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.TURQUOISE), ALL_BRIGHTNESSES,
						Set.of(Color.ROSE), ALL_BRIGHTNESSES
				), 40, List.of(
				Ingredient.ofItems(Blocks.SAND),
				Ingredient.ofItems(CeruleanItems.CRUSHED_HALITE)
		), new ItemStack(Items.CLAY_BALL, 2)
		));

		addRecipe("red_sand", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.LILAC), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT),
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)
				), 40, List.of(
				Ingredient.ofItems(Blocks.SAND),
				Ingredient.ofItems(Blocks.SAND),
				Ingredient.ofItems(Blocks.SAND),
				Ingredient.ofItems(Blocks.SAND)
		), new ItemStack(Blocks.RED_SAND, 1)
		));

		addRecipe("red_sand_filtering", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT),
						Set.of(Color.VIRIDIAN), Set.of(Brightness.BRILLIANT, Brightness.CANDESCENT)
				), 40, List.of(
				Ingredient.ofItems(Blocks.RED_SAND),
				Ingredient.ofItems(Blocks.RED_SAND),
				Ingredient.ofItems(Blocks.RED_SAND),
				Ingredient.ofItems(Blocks.RED_SAND)
		), new ItemStack(Items.IRON_NUGGET, 1)
		));

		addRecipe("red_sandstone_filtering", new InspirationBrushRecipe(
				CanvasRequirements.of(
						Set.of(Color.ROSE), Set.of(Brightness.BRILLIANT),
						Set.of(Color.TURQUOISE), Set.of(Brightness.BRILLIANT)
				), 40, List.of(
				Ingredient.ofItems(Blocks.RED_SANDSTONE),
				Ingredient.ofItems(Blocks.RED_SANDSTONE),
				Ingredient.ofItems(Blocks.RED_SANDSTONE),
				Ingredient.ofItems(Blocks.RED_SANDSTONE)
		), new ItemStack(Items.GOLD_NUGGET, 1)
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
						Set.of(Color.ROSE), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_SWORD)
		), new ItemStack(CeruleanItems.FUCHSIA_SWORD)
		));

		addRecipe("fuchsia_pickaxe", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_PICKAXE)
		), new ItemStack(CeruleanItems.FUCHSIA_PICKAXE)
		));

		addRecipe("fuchsia_axe", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_AXE)
		), new ItemStack(CeruleanItems.FUCHSIA_AXE)
		));

		addRecipe("fuchsia_shovel", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_SHOVEL)
		), new ItemStack(CeruleanItems.FUCHSIA_SHOVEL)
		));

		addRecipe("fuchsia_hoe", new InspirationBrushRecipe.Uninspired(
				CanvasRequirements.of(
						Set.of(Color.ROSE), ALL_BRIGHTNESSES_EXCEPT_DIM
				), 60, List.of(
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT),
				Ingredient.ofItems(Items.IRON_HOE)
		), new ItemStack(CeruleanItems.FUCHSIA_HOE)
		));
	}

	private static void addRecipe(String path, BrushRecipe recipe) {
		path = "/" + path;
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
			if (recipe.matches(inventory.asInput(), inventory.world)) {
				return recipe;
			}
		}
		return null;
	}
}
