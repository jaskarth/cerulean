package fmt.cerulean.world.gen.feature.decorator;

import fmt.cerulean.world.gen.synth.DecoratingWorld;
import fmt.cerulean.world.gen.synth.IslandParameters;
import fmt.cerulean.world.gen.feature.Decorator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;

import java.util.List;
import java.util.Random;

public class IslandDecorator extends Decorator {
	@Override
	public List<BlockPos> getPositions(DecoratingWorld world, Random random, BlockPos pos) {
		if (random.nextInt(20) != 0) {
			return List.of();
		}

		IslandParameters p = IslandParameters.get(world.getSeed(), pos.add(8, 0, 8));

		return List.of(pos.add(random.nextInt(16), (14 + p.startOff()) * 8 + (random.nextInt(30) - random.nextInt(30)), random.nextInt(16)));
	}
}
