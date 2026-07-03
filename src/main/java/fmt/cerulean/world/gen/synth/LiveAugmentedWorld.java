package fmt.cerulean.world.gen.synth;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class LiveAugmentedWorld implements DecoratingWorld {
	private final StructureWorldAccess world;
	private final Map<BlockPos, BlockState> blocks;
	private final long seed;

	public LiveAugmentedWorld(StructureWorldAccess world, Map<BlockPos, BlockState> blocks, long seed) {
		this.world = world;
		this.blocks = blocks;
		this.seed = seed;
	}

	@Override
	public BlockState getBlockState(BlockPos pos) {
		return blocks.getOrDefault(pos, Blocks.AIR.getDefaultState());
	}

	@Override
	public void setBlockState(BlockPos pos, BlockState state) {
		blocks.put(pos.toImmutable(), state);
	}

	@Override
	public int getTopY(Heightmap.Type heightmap, int x, int z) {
		// TODO: Probably should probe blocks?
		return world.getTopY(heightmap, x, z);
	}

	@Override
	public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
		return world.getBlockEntity(pos);
	}

	@Override
	public long getSeed() {
		return seed;
	}

	@Override
	public boolean isGen() {
		return false;
	}
}
