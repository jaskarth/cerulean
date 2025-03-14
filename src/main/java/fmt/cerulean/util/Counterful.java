package fmt.cerulean.util;

import fmt.cerulean.world.data.DimensionState;
import net.minecraft.entity.player.PlayerEntity;

public interface Counterful {
	DimensionState getState();

	static DimensionState get(PlayerEntity player) {
		return ((Counterful)player).getState();
	}
}
