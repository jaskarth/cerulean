package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import fmt.cerulean.block.PipeBlock;
import fmt.cerulean.block.WellBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class CeruleanBlocks {
	public static final Block HYDROTHERMAL_VENT = register("hydrothermal_vent",
		new WellBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE)));
	public static final Block STAR_WELL = register("star_well",
		new WellBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE)));
	public static final Block PIPE = register("pipe",
		new PipeBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

	public static void init() {
	}
	
	private static Block register(String path, Block block) {
		Registry.register(Registries.BLOCK, Cerulean.id(path), block);
		Registry.register(Registries.ITEM, Cerulean.id(path), new BlockItem(block, new Item.Settings()));
		return block;
	}
}
