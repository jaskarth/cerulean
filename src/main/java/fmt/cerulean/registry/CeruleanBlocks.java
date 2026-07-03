package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import fmt.cerulean.block.*;
import fmt.cerulean.block.PillarBlock;
import fmt.cerulean.mixin.BlockSettingsAccessor;
import fmt.cerulean.util.SixSideOffsetter;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ColorCode;

public final class CeruleanBlocks {
	// Building blocks
	public static final Block SPACEROCK = register("spacerock",
			new Block(AbstractBlock.Settings.copy(Blocks.COBBLESTONE)));

	public static final Block SPACEROCK_STAIRS = register("spacerock_stairs",
			new StairsBlock(SPACEROCK.getDefaultState(), AbstractBlock.Settings.copyShallow(SPACEROCK)));

	public static final Block SPACEROCK_SLAB = register("spacerock_slab",
			new SlabBlock(AbstractBlock.Settings.copyShallow(SPACEROCK)));

	public static final Block SPACEROCK_WALL = register("spacerock_wall",
			new WallBlock(AbstractBlock.Settings.copyShallow(SPACEROCK).solid()));

	public static final Block SPACEBRICKS = register("spacebricks",
			new Block(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)));

	public static final Block SPACEBRICK_STAIRS = register("spacebrick_stairs",
			new StairsBlock(SPACEBRICKS.getDefaultState(), AbstractBlock.Settings.copyShallow(SPACEBRICKS)));

	public static final Block SPACEBRICK_SLAB = register("spacebrick_slab",
			new SlabBlock(AbstractBlock.Settings.copyShallow(SPACEBRICKS)));

	public static final Block SPACEBRICK_WALL = register("spacebrick_wall",
			new WallBlock(AbstractBlock.Settings.copyShallow(SPACEBRICKS).solid()));

	public static final Block POLISHED_SPACEROCK = register("polished_spacerock",
			new Block(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)));

	public static final Block CHISELED_SPACEROCK = register("chiseled_spacerock",
			new Block(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)));

	public static final Block SPACEROCK_PILLAR = register("spacerock_pillar",
			new PillarBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)));

	public static final Block SMOOTH_SPACEROCK = register("smooth_spacerock",
			new Block(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)));

	public static final Block INKY_BRICKS = register("inky_bricks",
			new Block(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)));

	public static final Block INKY_BRICK_STAIRS = register("inky_brick_stairs",
			new StairsBlock(INKY_BRICKS.getDefaultState(), AbstractBlock.Settings.copyShallow(INKY_BRICKS)));

	public static final Block INKY_BRICK_SLAB = register("inky_brick_slab",
			new SlabBlock(AbstractBlock.Settings.copyShallow(INKY_BRICKS)));

	public static final Block INKY_BRICK_WALL = register("inky_brick_wall",
			new WallBlock(AbstractBlock.Settings.copyShallow(INKY_BRICKS).solid()));

	public static final Block POLISHED_INKY_BRICKS = register("polished_inky_bricks",
			new Block(AbstractBlock.Settings.copy(INKY_BRICKS)));

	public static final Block CHISELED_INKY_BRICKS = register("chiseled_inky_bricks",
			new Block(AbstractBlock.Settings.copy(INKY_BRICKS)));

	public static final Block INKY_BRICK_PILLAR = register("inky_brick_pillar",
			new PillarBlock(AbstractBlock.Settings.copy(INKY_BRICKS)));

	public static final Block SMOOTH_INKY_BRICKS = register("smooth_inky_bricks",
			new Block(AbstractBlock.Settings.copy(INKY_BRICKS)));

	public static final Block HALITE_BRICKS = register("halite_bricks",
			new Block(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)));

	public static final Block PETRICHORATED_BRICKS = register("petrichorated_bricks",
			new Block(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS)));

	public static final Block COBBLED_ROCK_CANDY = register("cobbled_rock_candy",
			new Block(AbstractBlock.Settings.copy(Blocks.COBBLESTONE)));

	// Environmental/storage

	public static final Block STAR_WELL = register("star_well",
		new WellBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE)));

	public static final Block SORTED_BOOKSHELF = register("sorted_bookshelf",
			new Block(AbstractBlock.Settings.copy(Blocks.BOOKSHELF)));

	public static final Block FUCHSIA_BLOCK = register("fuchsia_block",
			new Block(AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK)));

	public static final Block LUSTROUS_BLOCK = register("lustrous_block",
			new Block(AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK)));

	public static final Block DUCTILE_BLOCK = register("ductile_block",
			new Block(AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK)));

    public static final Block GLIMMERLIGHT = register("glimmerlight",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .strength(0.3F)
                    .sounds(BlockSoundGroup.FROGLIGHT)
                    .luminance(state -> 14)
                    .solidBlock(Blocks::never)
            ));

    public static final Block GLITTERLIGHT = register("glitterlight",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.MAGENTA)
                    .strength(0.3F)
                    .sounds(BlockSoundGroup.FROGLIGHT)
                    .luminance(state -> 12)
                    .solidBlock(Blocks::never)
            ));

	public static final Block CRUSHED_HALITE_BLOCK = register("crushed_halite_block",
			new ColoredFallingBlock(
					new ColorCode(0x96b672),
					AbstractBlock.Settings.create().mapColor(MapColor.PALE_GREEN).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sounds(BlockSoundGroup.SAND)
			)
	);

	// Foliage

	public static final Block SKYGRASS = register("skygrass",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
					.mapColor(MapColor.DARK_GREEN)
					.noCollision()
					.replaceable()
					.breakInstantly()
					.sounds(BlockSoundGroup.GRASS)
					.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block CORAL = register("coral",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
					.mapColor(MapColor.ORANGE)
					.noCollision()
					.replaceable()
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

	public static final Block STARBLOOM = register("starbloom",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
							.mapColor(MapColor.RED)
							.noCollision()
							.breakInstantly()
							.sounds(BlockSoundGroup.GRASS)
							.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block BLUESHADE = register("blueshade",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
							.mapColor(MapColor.BLUE)
							.noCollision()
							.breakInstantly()
							.sounds(BlockSoundGroup.GRASS)
							.pistonBehavior(PistonBehavior.DESTROY))));

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

	// Fauxliage
	public static final Block STICKBLOSSOM = register("stickblossom",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
							.mapColor(MapColor.LIGHT_BLUE_GRAY)
							.noCollision()
							.breakInstantly()
							.luminance(s -> 14)
							.sounds(BlockSoundGroup.METAL)
							.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block GLITTERING_STICKBLOSSOM = register("glittering_stickblossom",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
							.mapColor(MapColor.LIGHT_BLUE_GRAY)
							.noCollision()
							.breakInstantly()
							.luminance(s -> 14)
							.sounds(BlockSoundGroup.METAL)
							.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block STICKBLOSSOMLESS = register("stickblossomless",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
							.mapColor(MapColor.LIGHT_BLUE_GRAY)
							.noCollision()
							.breakInstantly()
							.sounds(BlockSoundGroup.METAL)
							.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block PLANTED_STICK = registerBlockOnly("planted_stick",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
							.mapColor(MapColor.LIGHT_BLUE_GRAY)
							.noCollision()
							.breakInstantly()
							.sounds(BlockSoundGroup.METAL)
							.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block ROCK_CANDY = registerBlockOnly("rock_candy",
			new RockCandyBlock(offsetter(
					AbstractBlock.Settings.create()
							.mapColor(MapColor.LIGHT_BLUE_GRAY)
							.noCollision()
							.breakInstantly()
							.ticksRandomly()
							.sounds(BlockSoundGroup.METAL)
							.pistonBehavior(PistonBehavior.DESTROY))));

	// Halite
	public static final Block HALITE_BLOCK = register("halite_block",
			new HaliteBlock(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK).mapColor(MapColor.GREEN)));

	public static final Block SLASHED_HALITE_BLOCK = register("slashed_halite_block",
			new SlashedHaliteBlock(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK)
					.ticksRandomly()
					.mapColor(MapColor.GREEN)));

	public static final Block HALITE_OUTCROPPING = register("halite_outcropping",
			new HaliteOutcroppingBlock(
					14.0F,
					3.0F,
					AbstractBlock.Settings.create()
							.mapColor(MapColor.GREEN)
							.solid()
							.nonOpaque()
							.sounds(BlockSoundGroup.AMETHYST_CLUSTER)
							.strength(1.5F)
							.pistonBehavior(PistonBehavior.DESTROY)));

	public static final Block HALITE_OUTCROPPING_LARGE = register("halite_outcropping_large",
			new HaliteOutcroppingBlock(12.0F, 3.0F, AbstractBlock.Settings.copy(HALITE_OUTCROPPING)));

	public static final Block HALITE_OUTCROPPING_MEDIUM = register("halite_outcropping_medium",
			new HaliteOutcroppingBlock(9.0F, 3.0F, AbstractBlock.Settings.copy(HALITE_OUTCROPPING)));

	public static final Block HALITE_OUTCROPPING_SMALL = register("halite_outcropping_small",
			new HaliteOutcroppingBlock(7.0F, 4.0F, AbstractBlock.Settings.copy(HALITE_OUTCROPPING)));

	public static final Block ROCK_CANDY_SPIRE = register("rock_candy_spire",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
							.mapColor(MapColor.DULL_PINK)
							.sounds(BlockSoundGroup.DEEPSLATE)
							.dynamicBounds()
							.pistonBehavior(PistonBehavior.DESTROY))));

	// Functional
	public static final Block DESTINY_DETECTOR = register("destiny_detector",
			new DestinyDetectorBlock(AbstractBlock.Settings.copy(Blocks.STONE).ticksRandomly()));

	public static final Block ITEM_DETECTOR = register("item_detector",
			new ItemDetectorBlock(AbstractBlock.Settings.copy(Blocks.OBSERVER)));

	public static final Block PIPE_DETECTOR = register("pipe_detector",
			new PipeDetectorBlock(AbstractBlock.Settings.copy(Blocks.OBSERVER)));

	public static final Block REDSTONE_PARALLAX = register("redstone_parallax",
			new RedstoneParallaxBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)));

	public static final Block ISLAND_SEED = register("island_seed",
			new IslandSeedBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)));

	public static final Block LIGHT_CORE = register("light_core",
			new LightCoreBlock(AbstractBlock.Settings.copy(Blocks.HEAVY_CORE)
					.strength(1.0F)
					.luminance(s -> 15)
			));

	public static final Block SELF_COLLAPSING_CUBE = register("self_collapsing_cube",
			new SelfCollapsingCube(AbstractBlock.Settings.copy(Blocks.BARREL).solid()));

	public static final Block STRONGBOX = register("strongbox",
			new StrongboxBlock(AbstractBlock.Settings.copy(Blocks.STONE_BRICKS).strength(10.0F, 300.0F).nonOpaque()));

	public static final Block ADDRESS_PLAQUE = register("address_plaque",
			new AddressPlaqueBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).nonOpaque().noCollision()));

	public static final Block FLAG = register("flag",
			new FlagBlock(AbstractBlock.Settings.copy(Blocks.SPRUCE_SIGN).nonOpaque().noCollision()));

	public static final Block PROJECTOR = register("projector",
			new ProjectorBlock(AbstractBlock.Settings.copy(Blocks.COBBLESTONE)));

	public static final Block OAK_GAPDOOR = register("oak_gapdoor",
			new GapDoorBlock(BlockSetType.OAK, AbstractBlock.Settings.copy(Blocks.OAK_TRAPDOOR)));

	public static final Block FUCHSIA_GAPDOOR = register("fuchsia_gapdoor",
			new FuchsiaGapDoorBlock(BlockSetType.IRON, AbstractBlock.Settings.copy(Blocks.IRON_TRAPDOOR)));

	public static final Block REALITY_ANCHOR = register("reality_anchor",
			new RealityAnchorBlock(AbstractBlock.Settings.copy(LUSTROUS_BLOCK)));

	public static final Block ITEM_INDUCTOR = register("item_inductor",
			new ItemInductorBlock(AbstractBlock.Settings.copy(DUCTILE_BLOCK)
					.nonOpaque()
					.allowsSpawning(Blocks::never)
			));

	public static final Block ITEM_EXPELLER = register("item_expeller",
			new ItemExpellerBlock(AbstractBlock.Settings.copy(LUSTROUS_BLOCK)
					.nonOpaque()
					.allowsSpawning(Blocks::never)
			));

	// Pipe
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

	public static final Block IRON_VALVE = register("iron_valve",
			new ValveBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static final Block CHIMERIC_VALVE = register("chimeric_valve",
			new ValveBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	// Shh
	public static final Block MIMIC = register("mimic",
			new MimicBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK).noBlockBreakParticles()));

	public static final Block FAUX = register("faux",
			new FauxBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK).noBlockBreakParticles().suffocates(Blocks::never)));

	public static final Block MIRAGE = register("mirage",
			new MirageBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK).mapColor(MapColor.CLEAR).noBlockBreakParticles().replaceable()));

	public static final Block INKY_VOID = register("inky_void",
			new InkyVoidBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK)));
	
	// Fluid
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
