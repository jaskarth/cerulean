package fmt.cerulean.world.gen.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;

import java.util.List;
import java.util.Random;

public class ConfiguredDecoration extends Decoration {
	private final Decorator decor;
	private final Decoration deco;

	public ConfiguredDecoration(Decorator decor, Decoration deco) {
		this.decor = decor;
		this.deco = deco;
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos pos) {
		List<BlockPos> list = this.decor.getPositions(world, random, pos);

		for (BlockPos p : list) {
			this.deco.generate(world, random, p);
		}
	}
}
