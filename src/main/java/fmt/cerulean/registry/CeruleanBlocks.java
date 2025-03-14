package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import fmt.cerulean.block.*;
import fmt.cerulean.mixin.BlockSettingsAccessor;
import fmt.cerulean.util.SixSideOffsetter;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public final class CeruleanBlocks {
	public static final Block STAR_WELL = register("star_well",
		new WellBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE)));
	public static final Block PIPE = register("pipe",
		new OxidizablePipeBlock(Oxidizable.OxidationLevel.UNAFFECTED, AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static final Block EXPOSED_PIPE = register("exposed_pipe",
			new OxidizablePipeBlock(Oxidizable.OxidationLevel.EXPOSED, AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static final Block WEATHERED_PIPE = register("weathered_pipe",
			new OxidizablePipeBlock(Oxidizable.OxidationLevel.WEATHERED, AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static final Block OXIDIZED_PIPE = register("oxidized_pipe",
			new OxidizablePipeBlock(Oxidizable.OxidationLevel.OXIDIZED, AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static final Block WAXED_PIPE = register("waxed_pipe",
		new PipeBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static final Block WAXED_EXPOSED_PIPE = register("waxed_exposed_pipe",
			new PipeBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static final Block WAXED_WEATHERED_PIPE = register("waxed_weathered_pipe",
			new PipeBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static final Block WAXED_OXIDIZED_PIPE = register("waxed_oxidized_pipe",
			new PipeBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static final Block FUCHSIA_PIPE = register("fuchsia_pipe",
			new PipeBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static final Block LUSTROUS_PIPE = register("lustrous_pipe",
			new PipeBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static final Block DUCTILE_PIPE = register("ductile_pipe",
			new PipeBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static final Block CHIMERIC_PIPE = register("chimeric_pipe",
			new PipeBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static final Block SPACEROCK = register("spacerock",
			new Block(AbstractBlock.Settings.copy(Blocks.COBBLESTONE)));

	public static final Block CORAL = register("coral",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
					.mapColor(MapColor.ORANGE)
					.noCollision()
					.replaceable()
					.breakInstantly()
					.sounds(BlockSoundGroup.GRASS)
					.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block SKYGRASS = register("skygrass",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
					.mapColor(MapColor.DARK_GREEN)
					.noCollision()
					.replaceable()
					.breakInstantly()
					.sounds(BlockSoundGroup.GRASS)
					.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block SPARKBLOSSOM = register("sparkblossom",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
					.mapColor(MapColor.DARK_GREEN)
					.noCollision()
					.breakInstantly()
					.luminance(s -> 12)
					.sounds(BlockSoundGroup.GRASS)
					.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block GLITTERING_SPARKBLOSSOM = register("glittering_sparkblossom",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
							.mapColor(MapColor.DARK_GREEN)
							.noCollision()
							.breakInstantly()
							.luminance(s -> 10)
							.sounds(BlockSoundGroup.GRASS)
							.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block SPARKLESSBLOSSOM = register("sparklessblossom",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
					.mapColor(MapColor.DARK_GREEN)
					.noCollision()
					.breakInstantly()
					.sounds(BlockSoundGroup.GRASS)
					.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block STICKBLOSSOM = register("stickblossom",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
							.mapColor(MapColor.LIGHT_BLUE_GRAY)
							.noCollision()
							.breakInstantly()
							.luminance(s -> 14)
							.sounds(BlockSoundGroup.GRASS)
							.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block GLITTERING_STICKBLOSSOM = register("glittering_stickblossom",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
							.mapColor(MapColor.LIGHT_BLUE_GRAY)
							.noCollision()
							.breakInstantly()
							.luminance(s -> 14)
							.sounds(BlockSoundGroup.GRASS)
							.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block STICKBLOSSOMLESS = register("stickblossomless",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
							.mapColor(MapColor.LIGHT_BLUE_GRAY)
							.noCollision()
							.breakInstantly()
							.sounds(BlockSoundGroup.GRASS)
							.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block LUNARIUM = register("lunarium",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
					.mapColor(MapColor.WHITE)
					.noCollision()
					.breakInstantly()
					.sounds(BlockSoundGroup.GRASS)
					.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block REEDS = registerBlockOnly("reeds",
			new ReedsBlock(AbstractBlock.Settings.create()
					.mapColor(MapColor.BROWN)
					.ticksRandomly()
					.noCollision()
					.breakInstantly()
					.sounds(BlockSoundGroup.GRASS)
					.pistonBehavior(PistonBehavior.DESTROY)));

	public static final Block REEDS_PLANT = registerBlockOnly("reeds_plant",
			new ReedsPlantBlock(AbstractBlock.Settings.create()
					.mapColor(MapColor.BROWN)
					.ticksRandomly()
					.noCollision()
					.breakInstantly()
					.sounds(BlockSoundGroup.GRASS)
					.pistonBehavior(PistonBehavior.DESTROY)));

	public static final Block MIMIC = register("mimic",
			new MimicBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK).noBlockBreakParticles()));

	public static final Block FAUX = register("faux",
			new FauxBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK).noBlockBreakParticles().suffocates(Blocks::never)));

	public static final Block MIRAGE = register("mirage",
			new MirageBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK).noBlockBreakParticles().replaceable()));

	public static final Block INKY_VOID = register("inky_void",
			new InkyVoidBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK)));

	public static final Block SORTED_BOOKSHELF = register("sorted_bookshelf",
			new Block(AbstractBlock.Settings.copy(Blocks.BOOKSHELF)));

	public static final Block SPACEBRICKS = register("spacebricks",
			new Block(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)));

	public static final Block SPACEBRICK_WALL = register("spacebrick_wall",
			new WallBlock(AbstractBlock.Settings.copyShallow(SPACEBRICKS).solid()));

	public static final Block SPACEBRICK_STAIRS = register("spacebrick_stairs",
			new StairsBlock(SPACEBRICKS.getDefaultState(), AbstractBlock.Settings.copyShallow(SPACEBRICKS)));

	public static final Block SPACEBRICK_SLAB = register("spacebrick_slab",
			new SlabBlock(AbstractBlock.Settings.copyShallow(SPACEBRICKS)));

	public static final Block POLISHED_SPACEROCK = register("polished_spacerock",
			new Block(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)));

	public static final Block CHISELED_SPACEROCK = register("chiseled_spacerock",
			new Block(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)));

	public static final Block SMOOTH_SPACEROCK = register("smooth_spacerock",
			new Block(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)));

	public static final Block SPACEROCK_SLAB = register("spacerock_slab",
			new SlabBlock(AbstractBlock.Settings.copyShallow(SPACEROCK)));

	public static final Block SPACEROCK_WALL = register("spacerock_wall",
			new WallBlock(AbstractBlock.Settings.copyShallow(SPACEROCK).solid()));

	public static final Block SPACEROCK_STAIRS = register("spacerock_stairs",
			new StairsBlock(SPACEBRICKS.getDefaultState(), AbstractBlock.Settings.copyShallow(SPACEBRICKS)));

	public static final Block STRONGBOX = register("strongbox",
			new StrongboxBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS).strength(10.0F, 300.0F).nonOpaque()));

	public static final Block LUSTROUS_BLOCK = register("lustrous_block",
			new Block(AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK)));

	public static final Block DUCTILE_BLOCK = register("ductile_block",
			new Block(AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK)));

	public static final Block SELF_COLLAPSING_CUBE = register("self_collapsing_cube",
			new SelfCollapsingCube(AbstractBlock.Settings.copy(Blocks.BARREL).solid()));

	public static final Block ADDRESS_PLAQUE = register("address_plaque",
			new AddressPlaqueBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).nonOpaque().noCollision()));

	public static final Block FLAG = register("flag",
			new FlagBlock(AbstractBlock.Settings.copy(Blocks.SPRUCE_SIGN).nonOpaque().noCollision()));

	public static final Block POLYETHYLENE = registerBlockOnly("polyethylene", new PolyethylyneBlock(
			CeruleanFluids.POLYETHYLENE,
			AbstractBlock.Settings.create()
					.liquid()
					.nonOpaque()
					.replaceable()
					.noCollision()
					.strength(100.0F)
					.dropsNothing()
					.pistonBehavior(PistonBehavior.DESTROY)
					.sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
	));

	public static final Block KALE = registerBlockOnly("kale",
			new KaleBlock(AbstractBlock.Settings.create()
					.mapColor(MapColor.GREEN)
					.ticksRandomly()
					.noCollision()
					.breakInstantly()
					.sounds(BlockSoundGroup.GRASS)
					.pistonBehavior(PistonBehavior.DESTROY)));

	public static final Block KALE_PLANT = registerBlockOnly("kale_plant",
			new KalePlantBlock(AbstractBlock.Settings.create()
					.mapColor(MapColor.GREEN)
					.ticksRandomly()
					.noCollision()
					.breakInstantly()
					.sounds(BlockSoundGroup.GRASS)
					.pistonBehavior(PistonBehavior.DESTROY)));

	public static final Block REALIZED_POLYETHYLENE = registerBlockOnly(
			"realized_polyethylene",
			new FluidBlock(
					(FlowableFluid) CeruleanFluids.REALIZED_POLYETHYLENE,
					AbstractBlock.Settings.create()
							.mapColor(MapColor.GRAY)
							.replaceable()
							.noCollision()
							.strength(100.0F)
							.pistonBehavior(PistonBehavior.DESTROY)
							.dropsNothing()
							.liquid()
							.sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
			)
	);

	public static final Block OAK_GAPDOOR = register("oak_gapdoor",
			new GapDoorBlock(BlockSetType.OAK, AbstractBlock.Settings.copy(Blocks.OAK_TRAPDOOR)));

	public static final Block HALITE_BLOCK = register("halite_block",
			new HaliteBlock(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK).mapColor(MapColor.GREEN)));

	public static final Block SLASHED_HALITE_BLOCK = register("slashed_halite_block",
			new SlashedHaliteBlock(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK)
					.ticksRandomly()
					.mapColor(MapColor.GREEN)));

	public static final Block HALITE_OUTCROPPING = register("halite_outcropping",
			new HaliteOutcroppingBlock(
					7.0F,
					3.0F,
					AbstractBlock.Settings.create()
							.mapColor(MapColor.GREEN)
							.solid()
							.nonOpaque()
							.sounds(BlockSoundGroup.AMETHYST_CLUSTER)
							.strength(1.5F)
							.pistonBehavior(PistonBehavior.DESTROY)));

	public static final Block HALITE_OUTCROPPING_LARGE = register("halite_outcropping_large",
			new HaliteOutcroppingBlock(7.0F, 3.0F, AbstractBlock.Settings.copy(HALITE_OUTCROPPING)));

	public static final Block HALITE_OUTCROPPING_MEDIUM = register("halite_outcropping_medium",
			new HaliteOutcroppingBlock(4.0F, 3.0F, AbstractBlock.Settings.copy(HALITE_OUTCROPPING)));

	public static final Block HALITE_OUTCROPPING_SMALL = register("halite_outcropping_small",
			new HaliteOutcroppingBlock(3.0F, 4.0F, AbstractBlock.Settings.copy(HALITE_OUTCROPPING)));

	public static final Block ITEM_DETECTOR = register("item_detector",
			new ItemDetectorBlock(AbstractBlock.Settings.copy(Blocks.OBSERVER)));

	public static final Block PIPE_DETECTOR = register("pipe_detector",
			new PipeDetectorBlock(AbstractBlock.Settings.copy(Blocks.OBSERVER)));

	public static final Block DESTINY_DETECTOR = register("destiny_detector",
			new DestinyDetectorBlock(AbstractBlock.Settings.copy(Blocks.STONE).ticksRandomly()));

	public static final Block LIGHT_CORE = register("light_core",
			new LightCoreBlock(AbstractBlock.Settings.copy(Blocks.HEAVY_CORE)
					.strength(1.0F)
					.luminance(s -> 15)
			));

	public static void init() {
		OxidizableBlocksRegistry.registerOxidizableBlockPair(PIPE, EXPOSED_PIPE);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(EXPOSED_PIPE, WEATHERED_PIPE);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(WEATHERED_PIPE, OXIDIZED_PIPE);

		OxidizableBlocksRegistry.registerWaxableBlockPair(PIPE, WAXED_PIPE);
		OxidizableBlocksRegistry.registerWaxableBlockPair(EXPOSED_PIPE, WAXED_EXPOSED_PIPE);
		OxidizableBlocksRegistry.registerWaxableBlockPair(WEATHERED_PIPE, WAXED_WEATHERED_PIPE);
		OxidizableBlocksRegistry.registerWaxableBlockPair(OXIDIZED_PIPE, WAXED_OXIDIZED_PIPE);
	}
	
	private static Block register(String path, Block block) {
		Registry.register(Registries.BLOCK, Cerulean.id(path), block);
		Registry.register(Registries.ITEM, Cerulean.id(path), new BlockItem(block, new Item.Settings()));
		return block;
	}

	private static Block registerBlockOnly(String path, Block block) {
		Registry.register(Registries.BLOCK, Cerulean.id(path), block);
		return block;
	}

	private static AbstractBlock.Settings offsetter(AbstractBlock.Settings settings) {

		((BlockSettingsAccessor)settings).setOffsetter(new SixSideOffsetter());

		return settings;
	}
}
