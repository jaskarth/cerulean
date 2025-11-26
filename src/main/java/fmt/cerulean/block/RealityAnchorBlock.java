package fmt.cerulean.block;

import com.mojang.serialization.MapCodec;
import fmt.cerulean.block.base.PipeConnectable;
import fmt.cerulean.block.entity.PipeDetectorBlockEntity;
import fmt.cerulean.block.entity.RealityAnchorBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.world.data.AnchorState;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RealityAnchorBlock extends BlockWithEntity implements PipeConnectable {
	public static final MapCodec<RealityAnchorBlock> CODEC = createCodec(RealityAnchorBlock::new);

	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

	public RealityAnchorBlock(Settings settings) {
		super(settings);

		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			if (world.getBlockEntity(pos) instanceof RealityAnchorBlockEntity rbe) {
				player.openHandledScreen(rbe);
			}

			return ActionResult.CONSUME;
		}
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient) {
			AnchorState.get((ServerWorld) world).remove(world, pos);
		}
		BlockHelper.emitStars(world, pos, 400);

		return super.onBreak(world, pos, state, player);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new RealityAnchorBlockEntity(pos, state);
	}

	@Override
	public boolean doesPipeConnect(BlockState state, Direction direction) {
		return state.get(FACING).getOpposite() == direction;
	}

	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return validateTicker(type, CeruleanBlockEntities.REALITY_ANCHOR, world.isClient ? null : RealityAnchorBlockEntity::serverTick);
	}
}
