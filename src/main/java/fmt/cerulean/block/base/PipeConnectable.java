package fmt.cerulean.block.base;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

public interface PipeConnectable {
	boolean doesPipeConnect(BlockState state, Direction direction);
}
