package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CeruleanItems {
	public static final Item BERRIES = register("berries", new AliasedBlockItem(CeruleanBlocks.REEDS, new Item.Settings()
			.food(new FoodComponent.Builder().hunger(4).saturationModifier(0.1f).build())
	));


	public static void init() {
	}

	private static Item register(String path, Item item) {
		return Registry.register(Registries.ITEM, Cerulean.id(path), item);
	}
}
