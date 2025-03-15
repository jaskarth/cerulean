package fmt.cerulean.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class SparklingWaterItem extends DrinkableBottleItem {
	public SparklingWaterItem(Settings settings) {
		super(settings);
	}

	protected void onDrink(LivingEntity user) {
		user.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 15 * 20));
		user.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 15 * 20));
	}
}
