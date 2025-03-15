package fmt.cerulean.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class OxidationPotionItem extends DrinkableBottleItem {
	public OxidationPotionItem(Settings settings) {
		super(settings);
	}

	protected void onDrink(LivingEntity user) {
		user.damage(user.getDamageSources().magic(), 4);
		user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 30 * 20, 1));
		user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 30 * 20));
		user.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 5 * 20));
		user.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 30 * 20));
	}
}
