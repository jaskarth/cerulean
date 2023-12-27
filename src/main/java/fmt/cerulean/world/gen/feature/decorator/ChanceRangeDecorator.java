package fmt.cerulean.world.gen.feature.decorator;

import fmt.cerulean.util.IntRange;
import fmt.cerulean.world.gen.feature.Decorator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChanceRangeDecorator extends Decorator {
	private final int chance;
	private final IntRange range;

	public ChanceRangeDecorator(int chance, IntRange range) {
		this.chance = chance;
		this.range = range;
	}

	@Override
	public List<BlockPos> getPositions(StructureWorldAccess world, Random random, BlockPos pos) {
		List<BlockPos> list = new ArrayList<>();
		if (random.nextInt(chance) == 0) {
			int dx = random.nextInt(16);
			int dz = random.nextInt(16);
			int y = this.range.random(random);

			BlockPos local = pos.add(dx, 0, dz).withY(y);

			list.add(local);
		}

		return list;
	}
}
