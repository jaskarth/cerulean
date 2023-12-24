package fmt.cerulean.world.gen.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;

import java.util.List;
import java.util.Random;

public abstract class Decorator {
	public abstract List<BlockPos> getPositions(StructureWorldAccess world, Random random, BlockPos pos);
}
