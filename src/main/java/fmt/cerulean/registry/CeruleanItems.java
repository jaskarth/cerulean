package fmt.cerulean.registry;

import java.util.Map;

import com.google.common.collect.Maps;

import fmt.cerulean.Cerulean;
import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.flow.FlowResource.Brightness;
import fmt.cerulean.flow.FlowResource.Color;
import fmt.cerulean.flow.FlowResources;
import fmt.cerulean.item.*;
import fmt.cerulean.util.FuchsiaToolMaterial;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CeruleanItems {
	public static final Item BERRIES = register("berries", new BerryItem(CeruleanBlocks.REEDS, new Item.Settings()
			.food(new FoodComponent.Builder().nutrition(4).saturationModifier(0.1f).alwaysEdible().build())
	));

	public static final Item GLIMMERCRUMB = register("glimmercrumb", new Item(new Item.Settings()
			.food(new FoodComponent.Builder().nutrition(8).saturationModifier(0.2f).build())
	));

	public static final Item ORB = register("orb", new Item(new Item.Settings()));
	public static final Item MOVRB = register("movrb", new MovrbItem(new Item.Settings()));
	public static final Item JORB = register("jorb", new JorbItem(new Item.Settings()));
	public static final Item KORB = register("korb", new KorbItem(new Item.Settings()));
	public static final Item LORB = register("lorb", new LorbItem(new Item.Settings()));
	public static final Item GLITTERING_COAL = register("glittering_coal", new Item(new Item.Settings()));
	public static final Item OXIDIZED_CARROT = register("oxidized_carrot", new Item(new Item.Settings()
			.food(new FoodComponent.Builder()
					.nutrition(3)
					.saturationModifier(0.1f)
					.statusEffect(new StatusEffectInstance(StatusEffects.POISON, 400, 1), 1.0F)
					.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0), 1.0F)
					.build()
			)));
	public static final Item EXPOSED_COPPER_INGOT = register("exposed_copper_ingot", new Item(new Item.Settings()));
	public static final Item WEATHERED_COPPER_INGOT = register("weathered_copper_ingot", new Item(new Item.Settings()));
	public static final Item OXIDIZED_COPPER_INGOT = register("oxidized_copper_ingot", new Item(new Item.Settings()));
	public static final Item FUCHSIA_INGOT = register("fuchsia_ingot", new Item(new Item.Settings()));
	public static final Item FUCHSIA_SWORD = register("fuchsia_sword", new SwordItem(FuchsiaToolMaterial.INSTANCE,  new Item.Settings()
			.attributeModifiers(SwordItem.createAttributeModifiers(FuchsiaToolMaterial.INSTANCE, 3, -2.4F))));
	public static final Item FUCHSIA_SHOVEL = register("fuchsia_shovel", new ShovelItem(FuchsiaToolMaterial.INSTANCE, new Item.Settings()
			.attributeModifiers(ShovelItem.createAttributeModifiers(FuchsiaToolMaterial.INSTANCE, 1.5F, -3.0F))));
	public static final Item FUCHSIA_PICKAXE = register("fuchsia_pickaxe", new PickaxeItem(FuchsiaToolMaterial.INSTANCE, new Item.Settings()
			.attributeModifiers(PickaxeItem.createAttributeModifiers(FuchsiaToolMaterial.INSTANCE, 1, -2.8F))));
	public static final Item FUCHSIA_AXE = register("fuchsia_axe", new AxeItem(FuchsiaToolMaterial.INSTANCE, new Item.Settings()
			.attributeModifiers(AxeItem.createAttributeModifiers(FuchsiaToolMaterial.INSTANCE, 6.0F, -3.0F))));
	public static final Item FUCHSIA_HOE = register("fuchsia_hoe", new HoeItem(FuchsiaToolMaterial.INSTANCE, new Item.Settings()
			.attributeModifiers(HoeItem.createAttributeModifiers(FuchsiaToolMaterial.INSTANCE, 0, -3.0F))));
	public static final Item CANDY_APPLE = register("candy_apple", new Item(new Item.Settings()
			.food(new FoodComponent.Builder()
					.nutrition(8)
					.saturationModifier(0.2f)
					.alwaysEdible()
					.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0), 1.0F)
					.statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 3600, 2), 1.0F)
					.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 3600, 1), 1.0F)
					.statusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 3600, 0), 1.0F)
					.build()
	)));
	public static final Item EMPTY_DRUM = register("empty_drum", new Item(new Item.Settings()));
	public static final Item POLYETHYLENE_DRUM = register("polyethylene_drum", new Item(new Item.Settings()));
	public static final Item VACUUM_PUMP = register("vacuum_pump", new VacuumPumpItem(new Item.Settings().maxCount(1)));
	public static final Item DEPRESSURIZER = register("depressurizer", new DepressurizerItem(new Item.Settings().maxCount(1)));
	public static final Item EYE_OF_VENDOR = register("eye_of_vendor", new EyeOfVendorItem(new Item.Settings()));
	public static final Item HALITE = register("halite", new Item(new Item.Settings()
			.food(new FoodComponent.Builder().nutrition(1).saturationModifier(0.2f).snack().build())
	));
	public static final Item CRUSHED_HALITE = register("crushed_halite", new Item(new Item.Settings()
			.food(new FoodComponent.Builder().nutrition(1).saturationModifier(0.2f).snack().build())
	));
	public static final Item HALITE_SALAD = register("halite_salad", new Item(new Item.Settings()
			.food(new FoodComponent.Builder().nutrition(6).saturationModifier(0.8f).usingConvertsTo(Items.BOWL).build())
	));

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

		FuelRegistry.INSTANCE.add(GLITTERING_COAL, 3200);
	}

	private static Item register(String path, Item item) {
		return Registry.register(Registries.ITEM, Cerulean.id(path), item);
	}
}
