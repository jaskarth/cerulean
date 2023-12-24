package fmt.cerulean.block;

import com.mojang.serialization.MapCodec;
import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.WorldAccess;

public class ReedsBlock extends AbstractPlantStemBlock {
	public static final MapCodec<ReedsBlock> CODEC = createCodec(ReedsBlock::new);
	public static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 15.0, 12.0);

	@Override
	public MapCodec<ReedsBlock> getCodec() {
		return CODEC;
	}

	public ReedsBlock(AbstractBlock.Settings settings) {
		super(settings, Direction.UP, SHAPE, false, 0.1);
	}

	@Override
	protected int getGrowthLength(Random random) {
		return 1;
	}

	@Override
	protected Block getPlant() {
		return CeruleanBlocks.REEDS_PLANT;
	}

	@Override
	protected boolean chooseStemState(BlockState state) {
		return state.isAir();
	}

	@Override
	public BlockState getRandomGrowthState(WorldAccess world) {
		return this.getDefaultState().with(AGE, 15);
	}
}
