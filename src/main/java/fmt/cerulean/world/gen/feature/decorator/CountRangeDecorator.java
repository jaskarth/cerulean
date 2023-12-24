package fmt.cerulean.world.gen.feature.decorator;

import fmt.cerulean.util.IntRange;
import fmt.cerulean.world.gen.feature.Decorator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CountRangeDecorator extends Decorator {
	private final int count;
	private final IntRange range;

	public CountRangeDecorator(int count, IntRange range) {
		this.count = count;
		this.range = range;
	}

	@Override
	public List<BlockPos> getPositions(StructureWorldAccess world, Random random, BlockPos pos) {
		List<BlockPos> list = new ArrayList<>();
		for (int i = 0; i < this.count; i++) {
			int dx = random.nextInt(16);
			int dz = random.nextInt(16);
			int y = this.range.random(random);

			BlockPos local = pos.add(dx, 0, dz).withY(y);

			list.add(local);
		}

		return list;
	}
}
