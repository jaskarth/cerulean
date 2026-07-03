package fmt.cerulean.client.render.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.ColorResolver;
import net.minecraft.world.chunk.light.LightingProvider;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FauxBlockSpace implements BlockRenderView {
	private final Map<BlockPos, BlockState> blocks = new HashMap<>();
	private final Map<BlockPos, Integer> lights = new HashMap<>();
	private final Map<BlockPos, Integer> bakedLights = new HashMap<>();

	public void addBlock(BlockPos pos, BlockState state) {
		blocks.put(pos, state);
	}

	public void addLightSource(BlockPos pos, int level) {
		lights.put(pos, level);
	}

	public void bakeLights() {
		for (Map.Entry<BlockPos, Integer> e : lights.entrySet()) {
			int r = e.getValue();

			for (int x = -r; x <= r; x++) {
				for (int z = -r; z <= r; z++) {
					for (int y = -r; y <= r; y++) {
						int v = r - (Math.abs(x) + Math.abs(y) + Math.abs(z));
						if (v <= 0) {
							continue;
						}

						BlockPos local = e.getKey().add(x, y, z);
						int ex = bakedLights.getOrDefault(local, 0);
						bakedLights.put(local, Math.max(v, ex));
					}
				}
			}
		}
	}

	public Map<BlockPos, BlockState> blocks() {
		return blocks;
	}


	@Override
	public float getBrightness(Direction direction, boolean shaded) {
		if (!shaded) {
			return 1.0F;
		} else {
			switch (direction) {
				case DOWN:
					return 0.5F;
				case UP:
					return 1.0F;
				case NORTH:
				case SOUTH:
					return 0.8F;
				case WEST:
				case EAST:
					return 0.6F;
				default:
					return 1.0F;
			}
		}
	}

	@Override
	public LightingProvider getLightingProvider() {
		return null;
	}

	@Override
	public int getLightLevel(LightType type, BlockPos pos) {
		if (type == LightType.SKY) {
			return 0;
		}

		return bakedLights.getOrDefault(pos, 0);
	}

	@Override
	public int getBaseLightLevel(BlockPos pos, int ambientDarkness) {
		return getLightLevel(LightType.BLOCK, pos);
	}

	@Override
	public int getColor(BlockPos pos, ColorResolver colorResolver) {
		return 0;
	}

	@Override
	public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
		return null;
	}

	@Override
	public BlockState getBlockState(BlockPos pos) {
		return blocks.getOrDefault(pos, Blocks.AIR.getDefaultState());
	}

	@Override
	public FluidState getFluidState(BlockPos pos) {
		return Fluids.EMPTY.getDefaultState();
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getBottomY() {
		return 0;
	}
}
