package fmt.cerulean.util;

import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

public class FuchsiaToolMaterial implements ToolMaterial {
	public static final FuchsiaToolMaterial INSTANCE = new FuchsiaToolMaterial();

	@Override
	public int getDurability() {
		return 220;
	}

	@Override
	public float getMiningSpeedMultiplier() {
		return 12.0F;
	}

	@Override
	public float getAttackDamage() {
		return 4.0F;
	}

	@Override
	public TagKey<Block> getInverseTag() {
		return BlockTags.INCORRECT_FOR_IRON_TOOL;
	}

	@Override
	public int getEnchantability() {
		return 22;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.ofItems(CeruleanItems.FUCHSIA_INGOT);
	}
}
