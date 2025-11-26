package fmt.cerulean.flow;

import net.minecraft.util.math.Direction;

public interface FlowOutreach extends FlowUser {
	
	FlowState getExportedState(Direction direction);
	
	FlowState getDistantExportedState(Direction direction);
}
