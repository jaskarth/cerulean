package fmt.cerulean.block;

import com.mojang.serialization.MapCodec;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanItems;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

public class ReedsPlantBlock extends AbstractPlantBlock {
	public static final BooleanProperty BERRIES = Properties.BERRIES;
	public static final MapCodec<ReedsPlantBlock> CODEC = createCodec(ReedsPlantBlock::new);
	public static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

	@Override
	public MapCodec<ReedsPlantBlock> getCodec() {
		return CODEC;
	}

	public ReedsPlantBlock(AbstractBlock.Settings settings) {
		super(settings, Direction.UP, SHAPE, false);

		this.setDefaultState(this.stateManager.getDefaultState().with(BERRIES, false));
	}

	@Override
	protected AbstractPlantStemBlock getStem() {
		return (AbstractPlantStemBlock) CeruleanBlocks.REEDS;
	}

	@Override
	protected BlockState copyState(BlockState from, BlockState to) {
		if (!to.contains(BERRIES)) {
			return to;
		}

		return to.with(BERRIES, (Boolean)from.get(BERRIES));
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		// 2/3 blocks cannot become berryful
		long l = HashCommon.mix(MathHelper.hashCode(pos.getX(), pos.getY(), pos.getZ()));
		if ((l & 3) != 0) {
			return;
		}

		if (random.nextInt(5) == 0) {
			world.setBlockState(pos, state.with(BERRIES, true), Block.NOTIFY_LISTENERS);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (state.get(BERRIES)) {
			Block.dropStack(world, pos, new ItemStack(CeruleanItems.BERRIES, 1));
			float f = MathHelper.nextBetween(world.random, 0.8F, 1.2F);
			world.playSound(null, pos, SoundEvents.BLOCK_CAVE_VINES_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, f);
			BlockState blockState = state.with(BERRIES, false);
			world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
			world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, blockState));
			return ActionResult.success(world.isClient);
		} else {
			return ActionResult.PASS;
		}
	}

	@Override
	public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
		return new ItemStack(CeruleanItems.BERRIES);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(BERRIES);
	}

	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
		return !state.get(BERRIES);
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		world.setBlockState(pos, state.with(BERRIES, true), Block.NOTIFY_LISTENERS);
	}
}
