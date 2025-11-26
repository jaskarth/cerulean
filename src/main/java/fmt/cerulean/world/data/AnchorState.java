package fmt.cerulean.world.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AnchorState extends PersistentState {
	private final Map<RegistryKey<World>, Map<BlockPos, Integer>> loads = new HashMap<>();

	public void putLevel(World world, BlockPos pos, int level) {
		loads.computeIfAbsent(world.getRegistryKey(), k -> new HashMap<>()).put(pos, level);
		markDirty();
	}

	public void remove(World world, BlockPos pos) {
		loads.computeIfAbsent(world.getRegistryKey(), k -> new HashMap<>()).remove(pos);
		markDirty();
	}

	public @Nullable Map<BlockPos, Integer> getFor(World world) {
		return loads.get(world.getRegistryKey());
	}

	public static AnchorState get(ServerWorld world) {
		world = world.getServer().getOverworld();

		return world.getPersistentStateManager().getOrCreate(
				new PersistentState.Type<>(
						() -> new AnchorState(),
						(nbt, wrapper) -> readNbt(nbt),
						// Fabric API handles null datafix types
						null
				),
				"cerulean_anchor_state");
	}

	public static AnchorState readNbt(NbtCompound nbt) {
		AnchorState state = new AnchorState();
		for (String id : nbt.getKeys()) {
			Map<BlockPos, Integer> map = new HashMap<>();
			NbtCompound data = nbt.getCompound(id);
			for (String p : data.getKeys()) {
				map.put(BlockPos.fromLong(Long.parseLong(p)), data.getInt(p));
			}

			RegistryKey<World> key = RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(id));
			state.loads.put(key, map);
		}

		return state;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		for (Map.Entry<RegistryKey<World>, Map<BlockPos, Integer>> entry : loads.entrySet()) {
			NbtCompound data = new NbtCompound();
			for (Map.Entry<BlockPos, Integer> e : entry.getValue().entrySet()) {
				data.putInt(Long.toString(e.getKey().asLong()), e.getValue());
			}

			String id = entry.getKey().getValue().toString();
			nbt.put(id, data);
		}

		return nbt;
	}
}
