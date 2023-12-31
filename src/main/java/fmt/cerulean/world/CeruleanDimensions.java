package fmt.cerulean.world;

import fmt.cerulean.Cerulean;
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

		for (int i = 0; i < maxI; i++){
			if ((-X / 2 <= x) && (x <= X / 2) && (-Z / 2 <= z) && (z <= Z / 2)) {
				BlockPos bp = new BlockPos(origin.getX() + (x << 4) + 8, 0, origin.getZ() + (z << 4) + 8);
				Chunk chunk = world.getChunk(bp.getX() / 16, bp.getZ() / 16);
				int topY = chunk.sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, bp.getX(), bp.getZ()) + 1;

				if (topY > 80) {
					return bp.withY(topY);
				}
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
