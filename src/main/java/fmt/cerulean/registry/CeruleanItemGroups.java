package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class CeruleanItemGroups {
	private static final List<String> EXCLUDED = List.of(
			"mimic", "inky_void"
	);

	public static void init() {
		Registry.register(Registries.ITEM_GROUP, Cerulean.id("cerulean"), FabricItemGroup.builder()
				.displayName(Text.translatable("cerulean.group"))
				.icon(() -> new ItemStack(CeruleanBlocks.CORAL))
				.entries((ctx, e) -> {
					for (RegistryEntry<Item> entry : Registries.ITEM.getIndexedEntries()) {
						Identifier id = entry.getKey().get().getValue();
						if (id.getNamespace().equals(Cerulean.ID) && !EXCLUDED.contains(id.getPath())) {
							e.add(new ItemStack(Registries.ITEM.get(id)));
						}
					}
				}).build()
		);
	}
}
