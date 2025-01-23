package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import fmt.cerulean.fluid.PolyethylyneFluid;
import fmt.cerulean.fluid.RealizedPolyethyleneFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CeruleanFluids {
	public static final Fluid POLYETHYLENE = register("polyethylene", new PolyethylyneFluid());
	public static final Fluid REALIZED_POLYETHYLENE = register("realized_polyethylene", new RealizedPolyethyleneFluid.Still());
	public static final Fluid REALIZED_POLYETHYLENE_FLOWING = register("flowing_realized_polyethylene", new RealizedPolyethyleneFluid.Flowing());

	public static void init() {

	}

	private static Fluid register(String path, Fluid fluid) {
		return Registry.register(Registries.FLUID, Cerulean.id(path), fluid);
	}
}
