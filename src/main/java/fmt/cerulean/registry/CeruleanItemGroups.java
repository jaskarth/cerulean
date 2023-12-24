package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CeruleanItemGroups {

	public static void init() {
		Registry.register(Registries.ITEM_GROUP, Cerulean.id("cerulean"), FabricItemGroup.builder()
				.displayName(Text.translatable("cerulean.group"))
				.icon(() -> new ItemStack(CeruleanBlocks.CORAL))
				.entries((ctx, e) -> {
					for (Identifier id : Registries.ITEM.getIds()) {
						if (id.getNamespace().equals(Cerulean.ID)) {
							e.add(new ItemStack(Registries.ITEM.get(id)));
						}
					}
				}).build()
		);
	}
}
