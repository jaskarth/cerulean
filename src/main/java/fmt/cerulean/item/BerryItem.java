package fmt.cerulean.item;

import fmt.cerulean.flow.FlowState;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class BerryItem extends AliasedBlockItem {
	
	public BerryItem(Block block, Settings settings) {
		super(block, settings);
	}

	public static ItemStack fromFlow(FlowState state) {
		ItemStack stack = new ItemStack(CeruleanItems.BERRIES);
		stack.set(CeruleanItemComponents.FLOW_STATE, state);
		return stack;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		FlowState flow = stack.get(CeruleanItemComponents.FLOW_STATE);
		if (flow != null && !flow.empty()) {
			if (user instanceof PlayerEntity player && world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.SKIES)) {
				player.sendMessage(Text.translatable("message.cerulean.flow_flavor", flow.resource().getBrightness().text(), flow.resource().getColor().text()), true);
			}
		}
		return super.finishUsing(stack, world, user);
	}
}
