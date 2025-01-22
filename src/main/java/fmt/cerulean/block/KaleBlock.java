package fmt.cerulean.block;

import com.mojang.serialization.MapCodec;
import fmt.cerulean.block.base.Plasticloggable;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanFluids;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.KelpBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class KaleBlock extends AbstractPlantStemBlock implements Plasticloggable {
	public static final MapCodec<KaleBlock> CODEC = createCodec(KaleBlock::new);
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);

	public KaleBlock(Settings settings) {
		super(settings, Direction.UP, SHAPE, true, 0.14);
	}

	@Override
	protected MapCodec<? extends AbstractPlantStemBlock> getCodec() {
		return CODEC;
	}

	@Override
	protected Block getPlant() {
		return CeruleanBlocks.KALE_PLANT;
	}

	@Override
	protected int getGrowthLength(Random random) {
		return 1;
	}

	@Override
	protected boolean chooseStemState(BlockState state) {
		return state.isOf(CeruleanBlocks.POLYETHYLENE);
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return CeruleanFluids.POLYETHYLENE.getDefaultState();
	}

	@Override
	public boolean tryDrainPlastic(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, BlockState state) {
		world.breakBlock(pos, true);

		return true;
	}
}
