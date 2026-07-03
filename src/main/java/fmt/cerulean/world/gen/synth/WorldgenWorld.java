package fmt.cerulean.world.gen.synth;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import org.jetbrains.annotations.Nullable;

public class WorldgenWorld implements DecoratingWorld {
	private final StructureWorldAccess world;

	public WorldgenWorld(StructureWorldAccess world) {
		this.world = world;
	}

	@Override
	public BlockState getBlockState(BlockPos pos) {
		return world.getBlockState(pos);
	}

	@Override
	public void setBlockState(BlockPos pos, BlockState state) {
		world.setBlockState(pos, state, 3);
	}

	@Override
	public int getTopY(Heightmap.Type heightmap, int x, int z) {
		return world.getTopY(heightmap, x, z);
	}

	@Override
	public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
		return world.getBlockEntity(pos);
	}

	@Override
	public long getSeed() {
		return world.getSeed();
	}

	@Override
	public boolean isGen() {
		return true;
	}
}
