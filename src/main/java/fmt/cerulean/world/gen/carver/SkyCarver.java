package fmt.cerulean.world.gen.carver;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CarvingMask;
import net.minecraft.world.gen.chunk.AquiferSampler;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.Random;
import java.util.function.Function;

public abstract class SkyCarver {
	public abstract boolean shouldCarve(Random random, int x, int z);

	public abstract void carve(HeightContext ctx, Chunk chunk, Random random, ChunkPos pos);

	protected boolean carveRegion(
			HeightContext context,
			Chunk chunk,
			double x,
			double y,
			double z,
			double width,
			double height,
			Carver.SkipPredicate skipPredicate
	) {
		ChunkPos chunkPos = chunk.getPos();
		double d = (double)chunkPos.getCenterX();
		double e = (double)chunkPos.getCenterZ();
		double f = 16.0 + width * 2.0;
		if (!(Math.abs(x - d) > f) && !(Math.abs(z - e) > f)) {
			int i = chunkPos.getStartX();
			int j = chunkPos.getStartZ();
			int k = Math.max(MathHelper.floor(x - width) - i - 1, 0);
			int l = Math.min(MathHelper.floor(x + width) - i, 15);
			int m = Math.max(MathHelper.floor(y - height) - 1, context.getMinY() + 1);
			int n = 7;
			int o = Math.min(MathHelper.floor(y + height) + 1, context.getMinY() + context.getHeight() - 1 - n);
			int p = Math.max(MathHelper.floor(z - width) - j - 1, 0);
			int q = Math.min(MathHelper.floor(z + width) - j, 15);
			boolean bl = false;
			BlockPos.Mutable mutable = new BlockPos.Mutable();

			for(int r = k; r <= l; ++r) {
				int s = chunkPos.getOffsetX(r);
				double g = ((double)s + 0.5 - x) / width;

				for(int t = p; t <= q; ++t) {
					int u = chunkPos.getOffsetZ(t);
					double h = ((double)u + 0.5 - z) / width;
					if (!(g * g + h * h >= 1.0)) {

						for(int v = o; v > m; --v) {
							double w = ((double)v - 0.5 - y) / height;
							if (!skipPredicate.shouldSkip(null, g, w, h, v)) {
								mutable.set(s, v, u);
								bl |= this.carveAtPoint( chunk, mutable);
							}
						}
					}
				}
			}

			return bl;
		} else {
			return false;
		}
	}

	protected boolean carveAtPoint(
			Chunk chunk,
			BlockPos.Mutable pos
	) {
		BlockState blockState = chunk.getBlockState(pos);

		if (!this.canCarve(blockState)) {
			return false;
		} else {
			BlockState blockState2 = this.getState();
			if (blockState2 == null) {
				return false;
			} else {
				chunk.setBlockState(pos, blockState2, false);

				return true;
			}
		}
	}

	protected abstract boolean canCarve(BlockState state);

	protected abstract BlockState getState();
}
