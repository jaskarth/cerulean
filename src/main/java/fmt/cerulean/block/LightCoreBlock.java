package fmt.cerulean.block;

import fmt.cerulean.block.base.Plasticloggable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HeavyCoreBlock;
import net.minecraft.state.StateManager;

public class LightCoreBlock extends HeavyCoreBlock implements Plasticloggable {
	public LightCoreBlock(Settings settings) {
		super(settings);

		// TODO: set default state
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
	}


}
