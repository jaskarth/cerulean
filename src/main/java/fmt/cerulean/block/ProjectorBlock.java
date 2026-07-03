package fmt.cerulean.block;

import com.mojang.serialization.MapCodec;
import fmt.cerulean.block.base.PipeConnectable;
import fmt.cerulean.block.entity.ProjectorBlockEntity;
import fmt.cerulean.block.entity.RealityAnchorBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ProjectorBlock extends BlockWithEntity implements PipeConnectable {
	public static final MapCodec<ProjectorBlock> CODEC = createCodec(ProjectorBlock::new);
	public static final DirectionProperty FACING = Properties.FACING;

	public ProjectorBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
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
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			if (world.getBlockEntity(pos) instanceof ProjectorBlockEntity proj) {
				player.openHandledScreen(proj);
			}

			return ActionResult.CONSUME;
		}
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ProjectorBlockEntity(pos, state);
	}

	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return validateTicker(type, CeruleanBlockEntities.PROJECTOR, world.isClient ? null : ProjectorBlockEntity::serverTick);
	}

	@Override
	public boolean doesPipeConnect(BlockState state, Direction direction) {
		return state.get(FACING).getOpposite() == direction;
	}
}
