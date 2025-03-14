package fmt.cerulean.block;

import com.mojang.serialization.MapCodec;

import fmt.cerulean.block.entity.SelfCollapsingCubeEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SelfCollapsingCube extends BlockWithEntity {
	public static final MapCodec<SelfCollapsingCube> CODEC = createCodec(SelfCollapsingCube::new);

	public SelfCollapsingCube(Settings settings) {
		super(settings);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		int x = (int) Math.round(hit.getPos().getX());
		int y = (int) Math.round(hit.getPos().getY());
		int z = (int) Math.round(hit.getPos().getZ());
		int slot = 0;
		slot += x == pos.getX() ? 0 : 1;
		slot += z == pos.getZ() ? 0 : 2;
		slot += y == pos.getY() ? 4 : 0;
		if (!world.isClient) {
			if (world.getBlockEntity(pos) instanceof SelfCollapsingCubeEntity scce) {
				scce.setFilterSlot(slot, player.getStackInHand(Hand.MAIN_HAND));
			}
		}

		return ActionResult.SUCCESS;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SelfCollapsingCubeEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (!world.isClient) {
			return validateTicker(type, CeruleanBlockEntities.SELF_COLLAPSING_CUBE, SelfCollapsingCubeEntity::tick);
		}
		return null;
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}
	
}
