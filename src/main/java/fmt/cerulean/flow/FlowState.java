package fmt.cerulean.flow;

import net.minecraft.nbt.NbtCompound;

public record FlowState(FlowResource resource, int pressure) {
	public static final FlowState NONE = new FlowState(null, 0);

	public boolean empty() {
		return pressure <= 0 || resource == null;
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.put("Resource", FlowResources.toNbt(resource));
		nbt.putInt("Pressure", pressure);
		return nbt;
	}

	public static FlowState fromNbt(NbtCompound nbt) {
		FlowResource resource = FlowResources.fromNbt(nbt.getCompound("Resource"));
		if (resource == null) {
			return NONE;
		}
		int pressure = nbt.getInt("Pressure");
		return new FlowState(resource, pressure);
	}
}
