package fmt.cerulean.block;

import fmt.cerulean.block.entity.WellBlockEntity;
import fmt.cerulean.client.particle.StarParticleType;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class WellBlock extends Block implements BlockEntityProvider {
	public static final DirectionProperty FACING = Properties.FACING;

	public WellBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.UP));
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new WellBlockEntity(pos, state);
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (world.isClient) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof WellBlockEntity wbe) {
				Random random = world.random;
				double x = pos.getX() + 0.5;
				double y = pos.getY() + 0.5;
				double z = pos.getZ() + 0.5;
				FlowState flow = wbe.getExportedState(state.get(FACING));
				if (!flow.empty()) {
					for (int i = 0; i < 400; i++) {
						StarParticleType star = WellBlockEntity.createParticle(flow.resource(), false, false, random);
						double vx = random.nextGaussian() * 0.12;
						double vy = random.nextGaussian() * 0.12;
						double vz = random.nextGaussian() * 0.12;
	
						world.addParticle(star, true, x, y, z, vx, vy, vz);
					}
				}
			}
		}

		return super.onBreak(world, pos, state, player);
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

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getSide());
	}
}
