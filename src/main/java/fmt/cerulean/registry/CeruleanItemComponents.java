package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import fmt.cerulean.flow.FlowState;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CeruleanItemComponents {
	public static final ComponentType<FlowState> FLOW_STATE = register("flow", ComponentType.<FlowState>builder()
			.codec(FlowState.CODEC)
			.packetCodec(FlowState.PACKET_CODEC)
			.build());

	public static void init() {

	}

	private static <T> ComponentType<T> register(String id, ComponentType<T> type) {
		return Registry.register(Registries.DATA_COMPONENT_TYPE, Cerulean.id(id), type);
	}
}
