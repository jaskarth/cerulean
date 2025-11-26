package fmt.cerulean.block.entity;

import fmt.cerulean.block.RealityAnchorBlock;
import fmt.cerulean.flow.FlowOutreach;
import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.flow.FlowUser;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.screen.RealityAnchorScreenHandler;
import fmt.cerulean.world.data.AnchorState;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class RealityAnchorBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, FlowUser {
	public static final ChunkTicketType<Unit> TICKET = ChunkTicketType.create("cerulean_anchor", (a, b) -> 0, 20);

	private int fullness;
	private int hash;

	private FlowState flow = FlowState.NONE;

	protected final PropertyDelegate propertyDelegate = new PropertyDelegate() {

		@Override
		public int get(int index) {
			if (index == 0) {
				return fullness;
			} else if (index == 1) {
				return hash;
			}

			return 0;
		}

		@Override
		public void set(int index, int value) {
			if (index == 0) {
				fullness = value;
			} else if (index == 1) {
				hash = value;
			}
		}

		@Override
		public int size() {
			return 2;
		}
	};

	public RealityAnchorBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.REALITY_ANCHOR, pos, state);

		hash = new Random().nextInt();
	}

	public static void serverTick(World world, BlockPos pos, BlockState state, RealityAnchorBlockEntity rbe) {
		Direction direction = state.get(RealityAnchorBlock.FACING);
		if (world.getBlockEntity(pos.offset(direction.getOpposite())) instanceof FlowOutreach flow) {
			FlowState exported = flow.getExportedState(direction);

			int newFullness;
			if (exported.pressure() < 1000) {
				newFullness = 0;
			} else if (exported.pressure() < 2500) {
				newFullness = 1;
			} else if (exported.pressure() < 5000) {
				newFullness = 2;
			} else if (exported.pressure() < 7500) {
				newFullness = 3;
			} else if (exported.pressure() < 10000) {
				newFullness = 4;
			} else {
				newFullness = 5;
			}
			rbe.flow = exported;

			if (rbe.fullness != newFullness) {
				rbe.fullness = newFullness;
				AnchorState.get((ServerWorld) world).putLevel(world, pos, getRadius(newFullness));
				rbe.markDirty();
			}
		} else {
			rbe.flow = FlowState.NONE;
			if (rbe.fullness != 0) {
				rbe.fullness = 0;
				AnchorState.get((ServerWorld) world).remove(world, pos);
				rbe.markDirty();
			}
		}

		if (world.getTime() % 20 == 0 && rbe.fullness > 0) {
			int r = getRadius(rbe.fullness);

			((ServerWorld) world).getChunkManager().addTicket(TICKET, new ChunkPos(pos), r, Unit.INSTANCE);
		}
	}

	private static int getRadius(int fullness) {
		int r;
		if (fullness <= 2) {
			r = 1;
		} else if (fullness <= 4) {
			r = 2;
		} else {
			r = 3;
		}
		return r;
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		fullness = nbt.getInt("Fullness");
		hash = nbt.getInt("Hash");
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		nbt.putInt("Fullness", fullness);
		nbt.putInt("Hash", hash);
	}

	@Override
	public Text getDisplayName() {
		return Text.empty();
	}

	@Override
	public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return new RealityAnchorScreenHandler(syncId, playerInventory, propertyDelegate);
	}

	@Override
	public FlowState getAnyFlow() {
		return flow;
	}
}
