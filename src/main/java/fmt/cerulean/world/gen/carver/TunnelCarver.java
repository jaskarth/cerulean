package fmt.cerulean.world.gen.carver;

import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.carver.Carver;

import java.util.Random;

public abstract class TunnelCarver extends SkyCarver {

	@Override
	public void carve(HeightContext ctx, Chunk chunk, Random random, ChunkPos pos) {
		double d = (double)pos.getOffsetX(random.nextInt(16));
		double e = random.nextInt(40) + 80;
		double f = (double)pos.getOffsetZ(random.nextInt(16));
		double g = (double)1;
		double h = (double)1;

		Carver.SkipPredicate pred = (hctx, scaledRelativeX, scaledRelativeY, scaledRelativeZ, y) -> {
			return scaledRelativeX * scaledRelativeX + scaledRelativeY * scaledRelativeY + scaledRelativeZ * scaledRelativeZ >= 1.0;
		};

		carveTunnels(ctx, d, e, f, chunk, random, pred);
	}

	private void carveTunnels(HeightContext ctx, double x, double y, double z, Chunk chunk, Random random, Carver.SkipPredicate pred) {
		float yawChange = 0.0F;
		float pitchChange = 0.0F;

		float yaw = random.nextFloat() * (float) (Math.PI * 2);
		float pitch = (random.nextFloat() - 0.5F) / 4.0F;

		double width = width(random);

		boolean carve = true;

		for (int j = 0; j < 112; j++) {
			double tunnelWidth = width;
			double tunnelHeight = tunnelWidth;
			float h = MathHelper.cos(pitch);
			x += (double)(MathHelper.cos(yaw) * h);
			y += (double)MathHelper.sin(pitch);
			z += (double)(MathHelper.sin(yaw) * h);
			pitch *= 0.7F;
			pitch += pitchChange * 0.1F;
			yaw += yawChange * 0.1F;
			pitchChange *= 0.9F;
			yawChange *= 0.85F;
			pitchChange += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			yawChange += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;

			if (boxCollide(chunk.getPos(), x, z) || y > 135 || y < 45) {
				return;
			}

			if ((j & 1) == 0) {
				carve = random.nextBoolean();
			}

			if (!carve) {
				continue;
			}

			this.carveRegion(ctx, chunk, x, y, z, tunnelWidth, tunnelHeight, pred);
		}
	}

	private static boolean boxCollide(ChunkPos pos, double x, double z) {
		// Check if this carving region has gone OOB

		if (x < (pos.x - 7) << 4 || x > (pos.x + 7) << 4) {
			return true;
		}

		if (z < (pos.z - 7) << 4 || z > (pos.z + 7) << 4) {
			return true;
		}

		return false;
	}

	protected abstract double width(Random random);
}
