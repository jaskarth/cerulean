package fmt.cerulean.block;

import com.mojang.serialization.MapCodec;
import fmt.cerulean.block.entity.ProjectorBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ProjectorBlock extends BlockWithEntity {
	public static final MapCodec<ProjectorBlock> CODEC = createCodec(ProjectorBlock::new);

	public ProjectorBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ProjectorBlockEntity(pos, state);
	}
}
