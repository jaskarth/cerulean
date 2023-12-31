package fmt.cerulean.flow;

import fmt.cerulean.flow.FlowResource.Brightness;
import fmt.cerulean.flow.FlowResource.Color;
import net.minecraft.nbt.NbtCompound;

public record FlowState(FlowResource resource, int pressure) {
	public static final FlowState NONE = new FlowState(null, 0);

	public boolean empty() {
		return pressure <= 0 || resource == null;
	}

	public FlowState brightened(float factor) {
		Brightness b = switch (this.resource.getBrightness()) {
			case DIM -> Brightness.WANING;
			case WANING -> Brightness.INNOCUOUS;
			case INNOCUOUS -> Brightness.CANDESCENT;
			default -> Brightness.BRILLIANT;
		};
		return new FlowState(FlowResources.star(this.resource.getColor(), b), (int) (pressure * factor));
	}

	public FlowState scaled(float factor) {
		return new FlowState(this.resource, (int) (pressure * factor));
	}

	public FlowState colored(Color color) {
		return new FlowState(FlowResources.star(color, this.resource.getBrightness()), pressure);
	}

	public FlowState coloredDimmer(Color color) {
		return new FlowState(FlowResources.star(color, this.resource.getBrightness().dimmer()), pressure);
	}

	public FlowState lit(Brightness brightness) {
		return new FlowState(FlowResources.star(this.resource.getColor(), brightness), pressure);
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
