package fmt.cerulean.block.entity;

import fmt.cerulean.client.particle.StarParticleType;
import fmt.cerulean.flow.FlowOutreach;
import fmt.cerulean.flow.FlowResource.Brightness;
import fmt.cerulean.flow.FlowResource.Color;
import fmt.cerulean.flow.FlowResources;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class WellBlockEntity extends BlockEntity implements FlowOutreach {
	private FlowState flow = FlowState.NONE;

	public WellBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.WELL, pos, state);
	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		if (!world.isClient && flow.empty()) {
			giveRandomStars();
		}
	}

	public void giveRandomStars() {
		java.util.Random random = new java.util.Random();
		flow = new FlowState(FlowResources.star(Color.values()[random.nextInt(Color.amount)], Brightness.values()[random.nextInt(Brightness.amount)]), 10_000);
	}

	public void tick(World world, BlockPos pos, BlockState state) {
	}

	public void clientTick(World world, BlockPos pos, BlockState state) {
		boolean piped = world.getBlockState(pos.offset(Direction.UP)).isOf(CeruleanBlocks.PIPE);
		if (!piped) {
			WellBlockEntity.spew(world, pos, Direction.UP, flow);
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (nbt.contains("Flow")) {
			flow = FlowState.fromNbt(nbt.getCompound("Flow"));
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("Flow", flow.toNbt());
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbt = new NbtCompound();
		writeNbt(nbt);
		return nbt;
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	public static void spew(World world, BlockPos pos, Direction direction, FlowState state) {
		if (state.empty()) {
			return;
		}
		for (int i = 0; i < Math.max(1, state.pressure() / 1000); i++) {
			Random random = world.getRandom();
			float x = pos.getX() + 0.5f + direction.getOffsetX() * 0.6f + skew(random, 0.2f);
			float y = pos.getY() + 0.5f + direction.getOffsetY() * 0.6f + skew(random, 0.2f);
			float z = pos.getZ() + 0.5f + direction.getOffsetZ() * 0.6f + skew(random, 0.2f);
			float vx = direction.getOffsetX() * 0.4f + skew(random, 0.2f);
			float vy = direction.getOffsetY() * 0.4f + skew(random, 0.2f);
			float vz = direction.getOffsetZ() * 0.4f + skew(random, 0.2f);
			float r = ((state.resource().getColor().color & 0xFF0000) >> 16) / 255f;
			float g = ((state.resource().getColor().color & 0x00FF00) >> 8) / 255f;
			float b = ((state.resource().getColor().color & 0x0000FF)) / 255f;
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

	@Override
	public FlowState getDistantExportedState(Direction direction) {
		return FlowState.NONE;
	}
}
