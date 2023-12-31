package fmt.cerulean.registry;

import java.util.Map;

import com.google.common.collect.Maps;

import fmt.cerulean.Cerulean;
import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.flow.FlowResource.Brightness;
import fmt.cerulean.flow.FlowResource.Color;
import fmt.cerulean.flow.FlowResources;
import fmt.cerulean.item.*;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CeruleanItems {
	public static final Item BERRIES = register("berries", new BerryItem(CeruleanBlocks.REEDS, new Item.Settings()
			.food(new FoodComponent.Builder().hunger(4).saturationModifier(0.1f).alwaysEdible().build())
	));

	public static final Item GLIMMERCRUMB = register("glimmercrumb", new Item(new Item.Settings()
			.food(new FoodComponent.Builder().hunger(8).saturationModifier(0.2f).alwaysEdible().build())
	));

	public static final Item ORB = register("orb", new Item(new Item.Settings()));
	public static final Item MOVRB = register("movrb", new MovrbItem(new Item.Settings()));
	public static final Item JORB = register("jorb", new JorbItem(new Item.Settings()));
	public static final Item KORB = register("korb", new KorbItem(new Item.Settings()));
	public static final Item LORB = register("lorb", new LorbItem(new Item.Settings()));
	public static final Item EXPOSED_COPPER_INGOT = register("exposed_copper_ingot", new Item(new Item.Settings()));
	public static final Item WEATHERED_COPPER_INGOT = register("weathered_copper_ingot", new Item(new Item.Settings()));
	public static final Item OXIDIZED_COPPER_INGOT = register("oxidized_copper_ingot", new Item(new Item.Settings()));

	// Just for EMI
	public static final Map<FlowResource, Item> STARS = Maps.newHashMap();

	public static void init() {
		for (Color color : Color.values()) {
			for (Brightness brightness : Brightness.values()) {
				FlowResource res = FlowResources.star(color, brightness);
				Item star = register(brightness.name + "_" + color.name + "_star", new StarItem(res, new Item.Settings()));
				STARS.put(res, star);
			}
		}
	}

	private static Item register(String path, Item item) {
		return Registry.register(Registries.ITEM, Cerulean.id(path), item);
	}
}
