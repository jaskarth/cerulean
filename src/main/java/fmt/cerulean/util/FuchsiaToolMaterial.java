package fmt.cerulean.util;

import fmt.cerulean.registry.CeruleanItems;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

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
	public int getMiningLevel() {
		return MiningLevels.IRON;
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
