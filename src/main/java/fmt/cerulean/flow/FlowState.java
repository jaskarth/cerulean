package fmt.cerulean.flow;

import net.minecraft.nbt.NbtCompound;

public record FlowState(int pressure) {
	public static final FlowState NONE = new FlowState(0);

	public boolean empty() {
		return pressure <= 0;
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putInt("Pressure", pressure);
		return nbt;
	}

	public static FlowState fromNbt(NbtCompound nbt) {
		int pressure = nbt.getInt("Pressure");
		return new FlowState(pressure);
	}
}
