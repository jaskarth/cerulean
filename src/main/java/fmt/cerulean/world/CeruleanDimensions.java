package fmt.cerulean.world;

import fmt.cerulean.Cerulean;
import fmt.cerulean.util.Vec2d;
import fmt.cerulean.util.Vec2i;
import fmt.cerulean.util.Voronoi;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;

public class CeruleanDimensions {
	public static final Identifier DREAMSCAPE = Cerulean.id("dreamscape");
	public static final Identifier SKIES = Cerulean.id("skies");

	public static BlockPos findSkiesSpawn(ServerWorld world, BlockPos origin) {
		// https://stackoverflow.com/questions/398299/looping-in-a-spiral
		int X = 8;
		int Z = 8;

		int x = 0;
		int z = 0;
		int dx = 0;
		int dz = -1;

		int maxLen = X;
		int maxI = maxLen * maxLen;

		Voronoi vn = new Voronoi(world.getSeed());

		// sssh. all's right in the world.
		for (int i = 0; i < maxI; i++){
			if ((-X / 2 <= x) && (x <= X / 2) && (-Z / 2 <= z) && (z <= Z / 2)) {
				BlockPos pos = new BlockPos(origin.getX() + (x << 4), 0, origin.getZ() + (z << 4));

				int vx = pos.getX();
				int vz = pos.getZ();

				double scale = 48.0 * 4;
				Vec2d cp = vn.getCellPos(vx / scale, vz / scale, scale);
				Vec2i center = cp.floor();

				BlockPos out = new BlockPos(center.x() + 4, 0, center.z() + 4);

				int topY = world.getChunk(out).sampleHeightmap(Heightmap.Type.WORLD_SURFACE, center.x(), center.z()) + 2;

				return out.withY(topY);
			}

			if( (x == z) || ((x < 0) && (x == -z)) || ((x > 0) && (x == 1 - z))) {
				maxLen = dx;
				dx = -dz;
				dz = maxLen;
			}

			x += dx;
			z += dz;
		}

		return null;
	}
}
