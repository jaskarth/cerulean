package fmt.cerulean.flow.recipe;

import fmt.cerulean.flow.FlowState;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class BevvyTastingBrushRecipe implements BrushRecipe {

	@Override
	public int getCraftTime() {
		return 100;
	}

	@Override
	public int getRequiredFlowInputs() {
		return 1;
	}

	@Override
	public FlowState getProcessedFlow(FlowState flow, int process) {
		return flow.brightened(0.9f);
	}

	@Override
	public boolean canCraft(PigmentInventory inventory) {
		World world = inventory.world;
		if (world.getBlockEntity(inventory.pos) instanceof BrewingStandBlockEntity bsbe) {
			for (int i = 0; i < 3; i++) {
				ItemStack stack = bsbe.getStack(i);
				PotionContentsComponent potionContents = stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, null);
				if (stack.getItem() instanceof PotionItem && potionContents != null) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void craft(PigmentInventory inventory) {
		World world = inventory.world;
		if (world.getBlockEntity(inventory.pos) instanceof BrewingStandBlockEntity bsbe) {
			int quality = 0;
			IntList candidates = new IntArrayList();
			for (int i = 0; i < 3; i++) {
				ItemStack stack = bsbe.getStack(i);
				Item item = stack.getItem();
				PotionContentsComponent potionContents = stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, null);
				if (item instanceof PotionItem && potionContents != null) {
					candidates.add(i);
					quality += 10;
					if (item instanceof ThrowablePotionItem) {
						quality += 7;
						if (item instanceof LingeringPotionItem) {
							quality += 17;
						}
					}
					RegistryEntry<Potion> entry = potionContents.potion().get();
					Potion p = entry.value();
					if (p.hasInstantEffect()) {
						quality += 5;
					}
					String cool = entry.getKey().get().getValue().getPath();
					if (cool.startsWith("long_")) {
						quality += 8;
					} else if (cool.startsWith("strong_")) {
						quality += 12;
					}
				}
			}
			if (world.random.nextInt(quality) == 0 && !candidates.isEmpty()) {
				world.playSound(null, inventory.pos, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.BLOCKS, 0.4f, 1.2f);
				world.playSound(null, inventory.pos, SoundEvents.ITEM_HONEY_BOTTLE_DRINK, SoundCategory.BLOCKS, 0.8f, 1.4f);
				bsbe.setStack(candidates.getInt(world.random.nextInt(candidates.size())), new ItemStack(Items.GLASS_BOTTLE));
			}
		}
	}
}
