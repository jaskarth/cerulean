package fmt.cerulean.world.data;

import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class PhotoMeta {
	public int id = 0;
	public boolean fulfilled = false;
	public BlockBox neededBox = BlockBox.infinite();
	public Set<BlockPos> positions = new HashSet<>();
	public Set<BlockPos> powered = new HashSet<>();

	public NbtCompound write() {
		NbtCompound nbt = new NbtCompound();
		nbt.putInt("Id", id);
		nbt.putBoolean("F", fulfilled);
		nbt.put("BB", BlockBox.CODEC.encodeStart(NbtOps.INSTANCE, this.neededBox)
				.getOrThrow());

		nbt.put("P", toList(positions));
		nbt.put("PP", toList(powered));

		return nbt;
	}

	private static NbtList toList(Set<BlockPos> set) {
		NbtList list = new NbtList();

		for (BlockPos p : set) {
			list.add(NbtHelper.fromBlockPos(p));
		}
		return list;
	}

	public void read(NbtCompound nbt) {
		id = nbt.getInt("Id");
		fulfilled = nbt.getBoolean("F");
		neededBox = BlockBox.CODEC.parse(NbtOps.INSTANCE, nbt.get("BB")).getOrThrow();

		toSet(positions, nbt.getList("P", NbtElement.INT_ARRAY_TYPE));
		toSet(powered, nbt.getList("PP", NbtElement.INT_ARRAY_TYPE));
	}

	private static void toSet(Set<BlockPos> set, NbtList list) {
		set.clear();

		for (NbtElement el : list) {
			NbtIntArray ary = (NbtIntArray) el;
			if (ary.size() == 3) {
				set.add(new BlockPos(new BlockPos(ary.get(0).intValue(), ary.get(1).intValue(), ary.get(2).intValue())));
			}
		}
	}
}
