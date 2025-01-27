package fmt.cerulean.registry;

import com.mojang.serialization.Codec;
import fmt.cerulean.Cerulean;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.fluid.CanisterFluidType;
import fmt.cerulean.item.EyeOfVendorItem;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CeruleanItemComponents {
	public static final ComponentType<FlowState> FLOW_STATE = register("flow", ComponentType.<FlowState>builder()
			.codec(FlowState.CODEC)
			.packetCodec(FlowState.PACKET_CODEC)
			.build());

	public static final ComponentType<Integer> FLUID_AMOUNT = register("fluid_amount", ComponentType.<Integer>builder()
			.codec(Codec.INT)
			.packetCodec(PacketCodecs.INTEGER)
			.build());

	public static final ComponentType<CanisterFluidType> FLUID_TYPE = register("fluid_type", ComponentType.<CanisterFluidType>builder()
			.codec(CanisterFluidType.CODEC)
			.packetCodec(CanisterFluidType.PACKET_CODEC)
			.build());

	public static final ComponentType<EyeOfVendorItem.Mode> EYE_MODE = register("eye_mode", ComponentType.<EyeOfVendorItem.Mode>builder()
			.codec(EyeOfVendorItem.Mode.CODEC)
			.packetCodec(EyeOfVendorItem.Mode.PACKET_CODEC)
			.build());

	public static void init() {

	}

	private static <T> ComponentType<T> register(String id, ComponentType<T> type) {
		return Registry.register(Registries.DATA_COMPONENT_TYPE, Cerulean.id(id), type);
	}
}
