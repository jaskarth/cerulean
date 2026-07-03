package fmt.cerulean.world.gen.synth;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import org.jetbrains.annotations.Nullable;

public interface DecoratingWorld {
	BlockState getBlockState(BlockPos pos);

	void setBlockState(BlockPos pos, BlockState state);

	int getTopY(Heightmap.Type heightmap, int x, int z);

	@Nullable
	BlockEntity getBlockEntity(BlockPos pos);

	long getSeed();

	boolean isGen();
}
