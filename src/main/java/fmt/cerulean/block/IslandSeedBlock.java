package fmt.cerulean.block;

import com.mojang.serialization.MapCodec;
import fmt.cerulean.block.entity.IslandSeedBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class IslandSeedBlock extends BlockWithEntity {
	public static final MapCodec<IslandSeedBlock> CODEC = createCodec(IslandSeedBlock::new);
	public IslandSeedBlock(Settings settings) {
		super(settings);
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
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			if (world.getBlockEntity(pos) instanceof IslandSeedBlockEntity isbe) {
				isbe.activate((ServerPlayerEntity) player);
			}

			return ActionResult.CONSUME;
		}
	}

	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return validateTicker(type, CeruleanBlockEntities.ISLAND_SEED, world.isClient ? null : IslandSeedBlockEntity::serverTick);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new IslandSeedBlockEntity(pos, state);
	}
}
