package fmt.cerulean.block;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import fmt.cerulean.block.entity.FauxBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class FauxBlock extends BlockWithEntity  {
	public static final MapCodec<FauxBlock> CODEC = createCodec(FauxBlock::new);

	public FauxBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) {
			return validateTicker(type, CeruleanBlockEntities.FAUX, FauxBlockEntity::clientTick);
		}
		return null;
	}

	@Override
	protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.isInCreativeMode() && player.isHolding(this.asItem())) {
			if (stack.getItem() instanceof BlockItem bi && bi.getBlock() != this) {
				BlockState ns = bi.getBlock().getPlacementState(new ItemPlacementContext(player, hand, stack, hit));
				if (world.getBlockEntity(pos) instanceof FauxBlockEntity fbe) {
					if (!world.isClient) {
						fbe.state = ns;
						fbe.markDirty();
						if (world instanceof ServerWorld sw) {
							sw.getChunkManager().markForUpdate(pos);
						}
					}
					return ItemActionResult.SUCCESS;
				}
			}
		}
		return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return canReckon(world, pos) ? VoxelShapes.empty() : VoxelShapes.fullCube();
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return canReckon(world, pos) ? VoxelShapes.empty() : VoxelShapes.fullCube();
	}

	@Override
	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}

	private boolean canReckon(BlockView world, BlockPos pos) {
		if (world.getBlockEntity(pos) instanceof FauxBlockEntity fbe) {
			if (!fbe.getWorld().isClient) {
				return true;
			}
			return fbe.canReckon();
		}
		return false;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new FauxBlockEntity(pos, state);
	}
}
