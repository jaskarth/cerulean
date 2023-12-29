package fmt.cerulean.flow;

import net.minecraft.util.math.Direction;

public interface FlowOutreach {
	
	FlowState getExportedState(Direction direction);
	
	FlowState getDistantExportedState(Direction direction);
}
