package fmt.cerulean.block;

import com.mojang.serialization.MapCodec;
import fmt.cerulean.block.entity.MirageBlockEntity;
import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MirageBlock extends BlockWithEntity {
	public static final MapCodec<MirageBlock> CODEC = createCodec(MirageBlock::new);

	public MirageBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	@Override
	protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.isInCreativeMode() && player.isHolding(this.asItem())) {
			if (stack.getItem() instanceof BlockItem bi && bi.getBlock() != this) {
				BlockState ns = bi.getBlock().getPlacementState(new ItemPlacementContext(player, hand, stack, hit));
				if (world.getBlockEntity(pos) instanceof MirageBlockEntity mbe) {
					if (!world.isClient) {
						mbe.state = ns;
						mbe.markDirty();
					}
					return ItemActionResult.SUCCESS;
				}
			}
		}
		return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new MirageBlockEntity(pos, state);
	}

	@Override
	protected float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 0.2F;
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.fullCube();
	}

	@Override
	protected VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (world instanceof ClientWorld) {
			if (MinecraftClient.getInstance().player.isHolding(CeruleanBlocks.MIRAGE.asItem())) {
				return VoxelShapes.fullCube();
			}
		}
		return VoxelShapes.empty();
	}
}
