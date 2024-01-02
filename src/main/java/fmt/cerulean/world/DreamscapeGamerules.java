package fmt.cerulean.world;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

public class DreamscapeGamerules extends GameRules {
	private final GameRules real;

	public DreamscapeGamerules(GameRules real) {
		this.real = real;
	}

	@Override
	public <T extends Rule<T>> T get(Key<T> key) {
		if (key == DO_TILE_DROPS) {
			return (T) new BooleanRule(BooleanRule.create(false), false);
		}
		if (key == DO_MOB_LOOT) {
			return (T) new BooleanRule(BooleanRule.create(false), false);
		}
		if (key == DO_FIRE_TICK) {
			return (T) new BooleanRule(BooleanRule.create(false), false);
		}
		return real.get(key);
	}

	@Override
	public NbtCompound toNbt() {
		return real.toNbt();
	}

	@Override
	public GameRules copy() {
		return real.copy();
	}

	@Override
	public void setAllValues(GameRules rules, @Nullable MinecraftServer server) {
		real.setAllValues(rules, server);
	}
}
