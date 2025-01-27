package fmt.cerulean.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class CeruleanCriterion extends AbstractCriterion<CeruleanCriterion.Conditions> {
	@Override
	public Codec<Conditions> getConditionsCodec() {
		return Conditions.CODEC;
	}

	public void trigger(ServerPlayerEntity player, int v) {
		this.trigger(player, conditions -> conditions.value == v);
	}

	public record Conditions(Optional<LootContextPredicate> player, int value) implements AbstractCriterion.Conditions {
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				Codec.INT.fieldOf("value").forGetter(Conditions::value)
		).apply(instance, Conditions::new));
	}
}
