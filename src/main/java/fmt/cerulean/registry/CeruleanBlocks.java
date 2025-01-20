package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import fmt.cerulean.block.*;
import fmt.cerulean.mixin.BlockSettingsAccessor;
import fmt.cerulean.util.SixSideOffsetter;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

import java.util.Optional;

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

	public static final Block SPARKLESSBLOSSOM = register("sparklessblossom",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
					.mapColor(MapColor.DARK_GREEN)
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
