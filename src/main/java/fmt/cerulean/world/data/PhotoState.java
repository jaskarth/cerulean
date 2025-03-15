package fmt.cerulean.world.data;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PhotoState extends PersistentState {
	private int lastAllocatedId = 0;
	private final Map<UUID, PhotoAllocation> allocation = new HashMap<>();
	private final Map<Integer, PhotoMeta> metas = new HashMap<>();
	private final ServerPhotoStore store;

	public PhotoState(ServerWorld world) {
		this.store = new ServerPhotoStore(world);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		nbt.putInt("LastAlloc", lastAllocatedId);

		NbtCompound alloc = new NbtCompound();
		for (Map.Entry<UUID, PhotoAllocation> e : allocation.entrySet()) {
			NbtCompound entry = new NbtCompound();
			e.getValue().writeNbt(entry);
			alloc.put(e.getKey().toString(), entry);
		}

		nbt.put("Alloc", alloc);

		NbtList list = new NbtList();

		for (PhotoMeta meta : metas.values()) {
			list.add(meta.write());
		}

		nbt.put("Meta", list);

		return nbt;
	}

	public static PhotoState get(ServerWorld world) {
		ServerWorld overworld = world.getServer().getOverworld();

		return overworld.getPersistentStateManager().getOrCreate(
				new PersistentState.Type<>(
						() -> new PhotoState(overworld),
						(nbt, wrapper) -> readNbt(nbt, overworld),
						// Fabric API handles null datafix types
						null
				),
				"cerulean_photos");
	}

	public boolean canAllocFor(ServerPlayerEntity player) {
		int allocated = allocation.computeIfAbsent(player.getUuid(),  k -> new PhotoAllocation()).list.size();
		markDirty();

		if (player.getServerWorld().getServer() instanceof DedicatedServer || player.getServerWorld().getServer().getCurrentPlayerCount() > 1) {
			return allocated < 36;
		}

		return true;

	}

	public int allocNextId(ServerPlayerEntity player) {
		lastAllocatedId++;
		allocation.computeIfAbsent(player.getUuid(),  k -> new PhotoAllocation()).list.add(lastAllocatedId);
		markDirty();

		return lastAllocatedId;
	}

	public static PhotoState readNbt(NbtCompound nbt, ServerWorld world) {
		PhotoState state = new PhotoState(world);

		state.lastAllocatedId = nbt.getInt("LastAlloc");

		NbtCompound data = nbt.getCompound("Alloc");

		for (String key : data.getKeys()) {
			PhotoAllocation alloc = new PhotoAllocation();
			alloc.readNbt(data.getCompound(key));

			state.allocation.put(UUID.fromString(key), alloc);
		}

		NbtList meta = nbt.getList("Meta", NbtElement.COMPOUND_TYPE);
		for (NbtElement el : meta) {
			PhotoMeta photoMeta = new PhotoMeta();
			photoMeta.read((NbtCompound) el);
			state.metas.put(photoMeta.id, photoMeta);
		}

		return state;
	}

	public static class PhotoAllocation {
		public IntList list = new IntArrayList();

		protected void writeNbt(NbtCompound nbt) {
			NbtIntArray intArray = new NbtIntArray(list.toIntArray());
			nbt.put("Photos", intArray);
		}

		protected void readNbt(NbtCompound nbt) {
			list = new IntArrayList(nbt.getIntArray("Photos"));
		}
	}

	public void add(int id, byte[] data, PhotoMeta meta) {
		store.add(id, data);

		metas.put(id, meta);
		markDirty();
	}

	public @Nullable PhotoMeta getMeta(int id) {
		return metas.get(id);
	}

	public ServerPhotoStore getStore() {
		return store;
	}
}
