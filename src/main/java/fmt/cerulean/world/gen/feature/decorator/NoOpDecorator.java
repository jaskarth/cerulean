package fmt.cerulean.world.gen.feature.decorator;

import fmt.cerulean.world.gen.feature.Decorator;
import fmt.cerulean.world.gen.synth.DecoratingWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;

import java.util.List;
import java.util.Random;

public class NoOpDecorator extends Decorator {
	@Override
	public List<BlockPos> getPositions(DecoratingWorld world, Random random, BlockPos pos) {
		return List.of(pos);
	}
}
