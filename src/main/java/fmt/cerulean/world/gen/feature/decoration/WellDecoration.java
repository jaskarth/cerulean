package fmt.cerulean.world.gen.feature.decoration;

import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.Vec2d;
import fmt.cerulean.util.Vec2i;
import fmt.cerulean.util.Voronoi;
import fmt.cerulean.world.gen.feature.Decoration;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;

import java.util.Random;

public class WellDecoration extends Decoration {
	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos pos) {
		Voronoi vn = new Voronoi(world.getSeed());

		int x = pos.getX();
		int z = pos.getZ();

		double scale = 48.0 * 4;
		Vec2d cp = vn.getCellPos(x / scale, z / scale, scale);
		Vec2i center = cp.floor();

		if (center.x() >= pos.getX() && center.z() >= pos.getZ() && center.x() <= pos.getX() + 15 && center.z() <= pos.getZ() + 15) {
			int topY = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, center.x(), center.z());
			world.setBlockState(new BlockPos(center.x(), topY - 1, center.z()), CeruleanBlocks.STAR_WELL.getDefaultState(), 3);
		}
	}
}
