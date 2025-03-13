package fmt.cerulean.world.gen.feature.decorator;

import fmt.cerulean.util.ImprovedChunkRandom;
import fmt.cerulean.world.gen.feature.Decorator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;

import java.util.List;
import java.util.Random;

public class RegionDecorator extends Decorator {

	private final int size;
	private final int buffer;
	private final Decorator inner;
	private final long salt;

	public RegionDecorator(int size, int buffer, Decorator inner, long salt) {
		this.size = size;
		this.buffer = buffer;
		this.inner = inner;
		this.salt = salt;
	}

	@Override
	public List<BlockPos> getPositions(StructureWorldAccess world, Random random, BlockPos pos) {
		int chunkX = pos.getX() >> 4;
		int chunkZ = pos.getZ() >> 4;
		int rx = Math.floorDiv(chunkX, size);
		int rz = Math.floorDiv(chunkZ, size);

		ImprovedChunkRandom regionRandom = new ImprovedChunkRandom(world.getSeed());
		regionRandom.setPopulationSeed(world.getSeed(), rx, rz, salt);

		int cx = regionRandom.nextInt(size - buffer);
		int cz = regionRandom.nextInt(size - buffer);

		if (chunkX == (rx * size + cx) && chunkZ == (rz * size + cz)) {
			return inner.getPositions(world, random, pos);
		}

		return List.of();
	}
}
