package fmt.cerulean.world.gen.feature.decorator;

import fmt.cerulean.util.IntRange;
import fmt.cerulean.world.gen.feature.Decorator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChanceHeightmapDecorator extends Decorator {
	private final int chance;

	public ChanceHeightmapDecorator(int chance) {
		this.chance = chance;
	}

	@Override
	public List<BlockPos> getPositions(StructureWorldAccess world, Random random, BlockPos pos) {
		if (random.nextInt(chance) != 0) {
			return List.of();
		}

		List<BlockPos> list = new ArrayList<>();

		int dx = random.nextInt(16);
		int dz = random.nextInt(16);

		int y = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, pos.getX() + dx, pos.getZ() + dz);

		list.add(pos.add(dx, 0, dz).withY(y));

		return list;
	}
}
