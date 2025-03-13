package fmt.cerulean.block.entity;

import fmt.cerulean.block.PipeBlock;
import fmt.cerulean.client.particle.StarParticleType;
import fmt.cerulean.flow.FlowOutreach;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.flow.recipe.BrushRecipe;
import fmt.cerulean.flow.recipe.BrushRecipes;
import fmt.cerulean.flow.recipe.PigmentInventory;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.AxisDirection;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class PipeBlockEntity extends BlockEntity implements FlowOutreach {
	private FlowState current = FlowState.NONE, next = null, spewed = FlowState.NONE;
	private long lastUpdate = Long.MIN_VALUE;
	private int threshold = 0;
	private Direction inputDir;
	private BrushRecipe activeRecipe;
	private int recipeProgress;
	// Client
	private boolean processing;

	public PipeBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.PIPE, pos, state);
	}

	public void clientTick(World world, BlockPos pos, BlockState state) {
		if (!current.empty()) {
			Random random = world.getRandom();
			for (Direction dir : Util.DIRECTIONS) {
				if (state.get(ConnectingBlock.FACING_PROPERTIES.get(dir))) {
					if (dir != inputDir) {
						float backoff = dir.getOpposite() == inputDir ? -0.5f : 0f;
						float x = pos.getX() + 0.5f + dir.getOffsetX() * backoff + WellBlockEntity.skew(random, 0.3f);
						float y = pos.getY() + 0.5f + dir.getOffsetY() * backoff + WellBlockEntity.skew(random, 0.3f);
						float z = pos.getZ() + 0.5f + dir.getOffsetZ() * backoff + WellBlockEntity.skew(random, 0.3f);
						float vx = dir.getOffsetX() * 0.2f + WellBlockEntity.skew(random, 0.05f);
						float vy = dir.getOffsetY() * 0.2f + WellBlockEntity.skew(random, 0.05f);
						float vz = dir.getOffsetZ() * 0.2f + WellBlockEntity.skew(random, 0.05f);
						StarParticleType star = WellBlockEntity.createParticle(current, true, random);
						world.addParticle(star, x, y, z, vx, vy, vz);
					}
				}
			}
			Direction spew = getSpewDirection();
			if (spew != null) {
				BlockPos actPos = pos.offset(spew);
				if (processing) {
					if (world.getRandom().nextInt(2) == 0) {
						float x = actPos.getX() + 0.5f + WellBlockEntity.skew(random, 1.1f);
						float y = actPos.getY() + 0.5f + WellBlockEntity.skew(random, 1.1f);
						float z = actPos.getZ() + 0.5f + WellBlockEntity.skew(random, 1.1f);
						float vx = WellBlockEntity.skew(random, 0.5f);
						float vy = WellBlockEntity.skew(random, 0.5f);
						float vz = WellBlockEntity.skew(random, 0.5f);
						world.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, vx, vy, vz);
					}
				}
				if (processing && !spewed.empty()) {
					for (int i = 0; i < Math.max(1, spewed.pressure() / 1000); i++) {
						float x = pos.getX() + 0.5f + spew.getOffsetX() * 0.5f + WellBlockEntity.skew(random, 0.3f);
						float y = pos.getY() + 0.5f + spew.getOffsetY() * 0.5f + WellBlockEntity.skew(random, 0.3f);
						float z = pos.getZ() + 0.5f + spew.getOffsetZ() * 0.5f + WellBlockEntity.skew(random, 0.3f);
						float vx = spew.getOffsetX() * 0.2f + WellBlockEntity.skew(random, 0.05f);
						float vy = spew.getOffsetY() * 0.2f + WellBlockEntity.skew(random, 0.05f);
						float vz = spew.getOffsetZ() * 0.2f + WellBlockEntity.skew(random, 0.05f);
						StarParticleType star = WellBlockEntity.createParticle(random.nextBoolean() ? spewed : current, true, random);
						world.addParticle(star, x, y, z, vx, vy, vz);
					}
				} else {
					WellBlockEntity.spew(world, pos, spew, current);
				}
			}
		}
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		updateFlow();
		updateRecipe();
	}

	@SuppressWarnings("deprecation")
	private void updateFlow() {
		BlockState state = getCachedState();
		threshold = threshold * 18 / 20;
		updateNext();
		Direction bestDir = null;
		FlowState bestFlow = FlowState.NONE;
		for (Direction dir : Util.DIRECTIONS) {
			if (state.get(ConnectingBlock.FACING_PROPERTIES.get(dir))) {
				if (world.getBlockEntity(pos.offset(dir)) instanceof FlowOutreach flow) {
					FlowState fs = flow.getExportedState(dir.getOpposite());
					if (fs.pressure() > bestFlow.pressure()) {
						bestFlow = fs;
						bestDir = dir; 
					}
				} else if (world.getBlockEntity(pos.offset(dir, 2)) instanceof FlowOutreach flow) {
					FlowState fs = flow.getDistantExportedState(dir.getOpposite());
					if (fs.pressure() > bestFlow.pressure()) {
						bestFlow = fs;
						bestDir = dir; 
					}
				}
			}
		}
		int connections = getConnectionCount();
		bestFlow = depressure(bestFlow, connections);
		if (inputDir != null) {
			if (bestDir == inputDir && bestFlow.equals(current)) {
				return;
			}
			if (!world.isChunkLoaded(pos.offset(inputDir, 2)) && current.pressure() >= bestFlow.pressure()) {
				return;
			}
			inputDir = bestDir;
		}
		if (bestFlow.empty() && current.empty()) {
			return;
		}
		if (threshold > 0 && bestFlow.pressure() < threshold) {
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

	private void updateRecipe() {
		FlowState lastSpew = spewed;
		boolean lastProcessing = activeRecipe != null;
		Direction spew = getSpewDirection();
		if (spew != null && !current.empty()) {
			FlowState opposing = world.getBlockEntity(pos.offset(spew, 2), CeruleanBlockEntities.PIPE)
				.filter(p -> p.getSpewDirection() == spew.getOpposite())
				.map(p -> p.getExportedState(spew.getOpposite()))
				.orElse(FlowState.NONE);
			// Only process recipes with combining inputs from one side
			if (!opposing.empty() && spew.getDirection() == AxisDirection.POSITIVE) {
				return;
			}
			PigmentInventory inventory = new PigmentInventory(current, opposing, world, pos.offset(spew), spew);
			inventory.recipeProgress = recipeProgress;
			if (activeRecipe != null) {
				if (!activeRecipe.matches(inventory.asInput(), world)) {
					activeRecipe = null;
					spewed = FlowState.NONE;
				}
			}
			if (activeRecipe == null) {
				inventory.recipeProgress = 0;
				activeRecipe = BrushRecipes.getFirstValid(inventory);
				recipeProgress = 0;
			}
			if (activeRecipe != null) {
				recipeProgress++;
				if (recipeProgress >= activeRecipe.getCraftTime()) {
					activeRecipe.craft(inventory);
					recipeProgress = 0;
				}
				spewed = activeRecipe.getProcessedFlow(current, recipeProgress);
			} else {
				activeRecipe = null;
				spewed = FlowState.NONE;
			}
		} else {
			activeRecipe = null;
			spewed = FlowState.NONE;
		}
		if (!lastSpew.equals(spewed) || (lastProcessing != (activeRecipe != null))) {
			markDirty();
			if (world instanceof ServerWorld sw) {
				sw.getChunkManager().markForUpdate(pos);
			}
		}
	}

	private Direction getSpewDirection() {
		BlockState state = getCachedState();
		int connections = 0;
		Direction unconnected = null;
		for (Direction dir : Util.DIRECTIONS) {
			if (state.get(ConnectingBlock.FACING_PROPERTIES.get(dir))) {
				boolean connect = PipeBlock.canConnect(state, world.getBlockState(pos.offset(dir)), dir.getOpposite());
				connections++;
				if (!connect && dir != inputDir) {
					unconnected = dir;
				}
			}
		}
		if (connections == 2 && unconnected != null) {
			return unconnected;
		}
		return null;
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
	public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		if (nbt.contains("Input")) {
			inputDir = Direction.byId(nbt.getInt("Input"));
		}
		if (nbt.contains("Flow")) {
			current = FlowState.fromNbt(nbt.getCompound("Flow"));
		}
		if (nbt.contains("Spewed")) {
			spewed = FlowState.fromNbt(nbt.getCompound("Spewed"));
		}
		if (nbt.contains("Processing")) {
			processing = nbt.getBoolean("Processing");
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		if (inputDir != null) {
			nbt.putInt("Input", inputDir.getId());
		}
		nbt.put("Flow", current.toNbt());
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		NbtCompound nbt = new NbtCompound();
		nbt.put("Spewed", spewed.toNbt());
		nbt.putBoolean("Processing", activeRecipe != null);
		writeNbt(nbt, registryLookup);
		return nbt;
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	private FlowState depressure(FlowState state, int connections) {
		int div = Math.max(1, connections - 1);
		int p = state.pressure() / div - 10;
		if (PipeBlock.isEfficient(getCachedState())) {
			p = p * 29 / 30;
		} else {
			p = p * 19 / 20;
		}
		if (p < 1000) {
			p -= 50;
		}
		p = Math.max(0, p);
		return new FlowState(state.resource(), p);
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

	@Override
	public FlowState getDistantExportedState(Direction dir) {
		if (dir == getSpewDirection() && activeRecipe != null) {
			return activeRecipe.getProcessedFlow(current, recipeProgress);
		}
		return FlowState.NONE;
	}
}
