package fmt.cerulean.world.data;

import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class PhotoMeta {
	public int id = 0;
	public boolean fulfilled = false;
	public BlockBox neededBox = BlockBox.infinite();
	public Set<BlockPos> positions = new HashSet<>();

	public NbtCompound write() {
		NbtCompound nbt = new NbtCompound();
		nbt.putInt("Id", id);
		nbt.putBoolean("F", fulfilled);
		nbt.put("BB", BlockBox.CODEC.encodeStart(NbtOps.INSTANCE, this.neededBox)
				.getOrThrow());

		NbtList list = new NbtList();

		for (BlockPos p : positions) {
			list.add(NbtHelper.fromBlockPos(p));
		}

		nbt.put("P", list);

		return nbt;
	}

	public void read(NbtCompound nbt) {
		id = nbt.getInt("Id");
		fulfilled = nbt.getBoolean("F");
		neededBox = BlockBox.CODEC.parse(NbtOps.INSTANCE, nbt.get("BB")).getOrThrow();

		positions.clear();

		NbtList list = nbt.getList("P", NbtElement.INT_ARRAY_TYPE);
		for (NbtElement el : list) {
			NbtIntArray ary = (NbtIntArray) el;
			if (ary.size() == 3) {
				positions.add(new BlockPos(new BlockPos(ary.get(0).intValue(), ary.get(1).intValue(), ary.get(2).intValue())));
			}
		}
	}
}
