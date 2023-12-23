package fmt.cerulean.block;

import fmt.cerulean.block.entity.WellBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WellBlock extends Block implements BlockEntityProvider {

	public WellBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new WellBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (type == CeruleanBlockEntities.WELL) {
			if (world.isClient) {
				return (w, p, s, be) -> ((WellBlockEntity) be).clientTick(w, p, s);
			} else {
				return (w, p, s, be) -> ((WellBlockEntity) be).tick(w, p, s);
			}
		}
		return null;
	}
}
