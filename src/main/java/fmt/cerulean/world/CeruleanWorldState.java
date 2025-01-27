package fmt.cerulean.world;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CeruleanWorldState extends PersistentState {
	private final Map<UUID, State> states = new HashMap<>();

	@Override
	public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		NbtCompound data = new NbtCompound();
		for (Map.Entry<UUID, State> e : states.entrySet()) {
			NbtCompound entry = new NbtCompound();
			e.getValue().writeNbt(entry);
			data.put(e.getKey().toString(), entry);
		}

		nbt.put("Data", data);

		return nbt;
	}
	
	public State getFor(ServerPlayerEntity player) {
		UUID uuid = player.getUuid();
		return states.computeIfAbsent(uuid, u -> {
			State state = new State();
			markDirty();
			return state;
		});
	}

	public static CeruleanWorldState get(ServerWorld world) {
		world = world.getServer().getOverworld();

		return world.getPersistentStateManager().getOrCreate(
				new PersistentState.Type<>(
						() -> new CeruleanWorldState(),
						(nbt, wrapper) -> readNbt(nbt),
						// Fabric API handles null datafix types
						null
				),
				"cerulean_state");
	}

	public static CeruleanWorldState readNbt(NbtCompound nbt) {
		CeruleanWorldState state = new CeruleanWorldState();
		NbtCompound data = nbt.getCompound("Data");

		for (String key : data.getKeys()) {
			State value = new State();
			value.readNbt(data.getCompound(key));
			state.states.put(UUID.fromString(key), value);
		}

		return state;
	}

	public static class State {
		public boolean truthful = false;
		public boolean worldworn = false;

		protected void writeNbt(NbtCompound nbt) {
			nbt.putBoolean("Truth", truthful);
			nbt.putBoolean("Worn", worldworn);
		}

		protected void readNbt(NbtCompound nbt) {
			truthful = nbt.getBoolean("Truth");
			worldworn = nbt.getBoolean("Worn");
		}
	}
}
