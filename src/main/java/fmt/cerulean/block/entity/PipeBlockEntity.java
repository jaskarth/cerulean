package fmt.cerulean.block.entity;

import fmt.cerulean.block.PipeBlock;
import fmt.cerulean.flow.FlowOutreach;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PipeBlockEntity extends BlockEntity implements FlowOutreach {
	private FlowState current = FlowState.NONE, next;
	private long lastUpdate = Long.MIN_VALUE;
	private int threshold = 0;
	private Direction inputDir;

	public PipeBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.PIPE, pos, state);
	}

	public void clientTick(World world, BlockPos pos, BlockState state) {
		if (!current.empty()) {
			world.addParticle(ParticleTypes.BUBBLE, pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f, 0, 0.1f, 0);
			int connections = 0;
			Direction unconnected = null;
			for (Direction dir : Util.DIRECTIONS) {
				if (state.get(ConnectingBlock.FACING_PROPERTIES.get(dir))) {
					boolean connect = PipeBlock.canConnect(world.getBlockState(pos.offset(dir)), dir.getOpposite());
					connections++;
					if (!connect) {
						unconnected = dir;
					}
				}
			}
			if (connections == 2 && unconnected != null) {
				WellBlockEntity.spew(world, pos, unconnected, current);
			}
		}
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		threshold = threshold * 18 / 20;
		updateNext();
		Direction bestDir = null;
		FlowState bestFlow = FlowState.NONE;
		for (Direction dir : Util.DIRECTIONS) {
			if (world.getBlockEntity(pos.offset(dir)) instanceof FlowOutreach flow) {
				FlowState fs = flow.getExportedState(dir.getOpposite());
				if (fs.pressure() > bestFlow.pressure()) {
					bestFlow = fs;
					bestDir = dir; 
				}
			}
		}
		int connections = getConnectionCount();
		bestFlow = depressure(bestFlow, connections);
		if (inputDir != null) {
			if (bestDir == inputDir && bestFlow.equals(current)) {
				return;
			}
			if (!world.isChunkLoaded(pos.offset(inputDir)) && current.pressure() >= bestFlow.pressure()) {
				return;
			}
			inputDir = bestDir;
		}
		if (bestFlow.pressure() < threshold) {
			return;
		}
		if (bestDir == null) {
			threshold = current.pressure() * 2;
		} else {
			threshold = 0;
		}
		current = bestFlow;
		inputDir = bestDir;
		markDirty();
		if (world instanceof ServerWorld sw) {
			sw.getChunkManager().markForUpdate(pos);
		}
	}

	private int getConnectionCount() {
		int c = 0;
		for (Direction dir : Util.DIRECTIONS) {
			if (getCachedState().get(ConnectingBlock.FACING_PROPERTIES.get(dir))) {
				// Real
				c++;
			}
		}
		return c;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (nbt.contains("Input")) {
			inputDir = Direction.byId(nbt.getInt("Input"));
		}
		if (nbt.contains("Flow")) {
			current = FlowState.fromNbt(nbt.getCompound("Flow"));
		}
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbt = new NbtCompound();
		if (inputDir != null) {
			nbt.putInt("Input", inputDir.getId());
		}
		nbt.put("Flow", current.toNbt());
		return nbt;
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	private FlowState depressure(FlowState state, int connections) {
		int div = Math.max(1, connections - 1);
		return new FlowState(state.pressure() / div * 19 / 20);
	}

	private void updateNext() {
		long time = world.getTime();
		if (next == null || time > lastUpdate) {
			lastUpdate = time;
			next = current;
		}
	}

	@Override
	public FlowState getExportedState(Direction dir) {
		updateNext();
		if (dir == inputDir) {
			return FlowState.NONE;
		}
		return next;
	}
}
