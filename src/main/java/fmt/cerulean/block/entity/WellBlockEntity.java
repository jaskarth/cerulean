package fmt.cerulean.block.entity;

import fmt.cerulean.client.particle.StarParticleType;
import fmt.cerulean.flow.FlowOutreach;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class WellBlockEntity extends BlockEntity implements FlowOutreach {
	private final FlowState flow = new FlowState(10_000);

	public WellBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.WELL, pos, state);
	}

	public void tick(World world, BlockPos pos, BlockState state) {
	}

	public void clientTick(World world, BlockPos pos, BlockState state) {
		boolean piped = world.getBlockState(pos.offset(Direction.UP)).isOf(CeruleanBlocks.PIPE);
		if (!piped) {
			WellBlockEntity.spew(world, pos, Direction.UP, flow);
		}
	}

	public static void spew(World world, BlockPos pos, Direction direction, FlowState state) {
		for (int i = 0; i < Math.max(1, state.pressure() / 1000); i++) {
			Random random = world.getRandom();
			float x = pos.getX() + 0.5f + direction.getOffsetX() * 0.6f + skew(random, 0.2f);
			float y = pos.getY() + 0.5f + direction.getOffsetY() * 0.6f + skew(random, 0.2f);
			float z = pos.getZ() + 0.5f + direction.getOffsetZ() * 0.6f + skew(random, 0.2f);
			float vx = direction.getOffsetX() * 0.4f + skew(random, 0.2f);
			float vy = direction.getOffsetY() * 0.4f + skew(random, 0.2f);
			float vz = direction.getOffsetZ() * 0.4f + skew(random, 0.2f);
			float r = 0.4f + WellBlockEntity.skew(random, 0.2f);
			float g = 0.2f + WellBlockEntity.skew(random, 0.2f);
			float b = 0.8f + WellBlockEntity.skew(random, 0.2f);
			world.addParticle(new StarParticleType(r, g, b, false), x, y, z, vx, vy, vz);
		}
	}

	public static float skew(Random random, float range) {
		return random.nextFloat() * range - (range / 2);
	}

	@Override
	public FlowState getExportedState(Direction direction) {
		if (direction == Direction.UP) {
			return flow;
		}
		return FlowState.NONE;
	}
}
