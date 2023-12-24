package fmt.cerulean.world.gen.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;

import java.util.Random;

public abstract class Decoration {
	public abstract void generate(StructureWorldAccess world, Random random, BlockPos pos);
}
