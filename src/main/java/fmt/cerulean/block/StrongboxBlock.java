package fmt.cerulean.block;

import com.mojang.serialization.MapCodec;
import fmt.cerulean.block.entity.StrongboxBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StrongboxBlock extends BlockWithEntity {

	public static final BooleanProperty WEAK = BooleanProperty.of("weak");

	public static final MapCodec<StrongboxBlock> CODEC = createCodec(StrongboxBlock::new);

	public StrongboxBlock(Settings settings) {
		super(settings);

		this.setDefaultState(this.stateManager.getDefaultState().with(WEAK, false));
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(WEAK);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new StrongboxBlockEntity(pos, state);
	}

	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return validateTicker(type, CeruleanBlockEntities.STRONGBOX, world.isClient ? null : StrongboxBlockEntity::serverTick);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.CONSUME;
		}

		if (state.get(WEAK)) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof StrongboxBlockEntity strongbox) {
				if (strongbox.hasItem) {
					if (player.giveItemStack(new ItemStack(CeruleanItems.REFLECTIVE_LENS))) {
						strongbox.hasItem = false;
						strongbox.markDirty();
						return ActionResult.SUCCESS;
					}
				} else {
					return ActionResult.PASS;
				}
			}
		} else if (world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.SKIES)) {
			player.sendMessage(Text.translatable("info.cerulean.strongbox.unknowable." + world.random.nextInt(4)), true);
		}

		return ActionResult.CONSUME;
	}

	@Override
	protected float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return state.get(WEAK) ? 1.0F : 0.2F;
	}

	@Override
	protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
		return state.get(WEAK);
	}
}
