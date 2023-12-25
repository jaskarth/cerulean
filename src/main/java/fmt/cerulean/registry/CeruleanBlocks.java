package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import fmt.cerulean.block.*;
import fmt.cerulean.mixin.BlockSettingsAccessor;
import fmt.cerulean.util.SixSideOffsetter;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

import java.util.Optional;

public final class CeruleanBlocks {
	public static final Block HYDROTHERMAL_VENT = register("hydrothermal_vent",
		new WellBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE)));
	public static final Block STAR_WELL = register("star_well",
		new WellBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE)));
	public static final Block PIPE = register("pipe",
		new PipeBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));
	public static final Block SPACEROCK = register("spacerock",
			new Block(AbstractBlock.Settings.copy(Blocks.COBBLESTONE)));

	public static final Block CORAL = register("coral",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
					.mapColor(MapColor.ORANGE)
					.noCollision()
					.breakInstantly()
					.sounds(BlockSoundGroup.GRASS)
					.pistonBehavior(PistonBehavior.DESTROY))));

	public static final Block SKYGRASS = register("skygrass",
			new CeruleanPlantBlock(offsetter(
					AbstractBlock.Settings.create()
					.mapColor(MapColor.DARK_GREEN)
					.noCollision()
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
			new MimicBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK)));

	public static final Block INKY_VOID = register("inky_void",
			new InkyVoidBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK)));

	public static void init() {
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

		((BlockSettingsAccessor)settings).setOffsetter(Optional.of(new SixSideOffsetter()));

		return settings;
	}
}
