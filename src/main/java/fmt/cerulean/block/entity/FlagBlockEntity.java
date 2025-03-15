package fmt.cerulean.block.entity;

import java.util.Map;
import java.util.function.Consumer;

import fmt.cerulean.block.FlagBlock;
import fmt.cerulean.block.base.Addressable;
import fmt.cerulean.block.base.Obedient;
import fmt.cerulean.block.entity.base.SyncedBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.world.data.MailWorldState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FlagBlockEntity extends SyncedBlockEntity implements Addressable, Obedient {
	public String address = "1 foo bar";

	public FlagBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.FLAG, pos, state);
	}

	@Override
	public String getAddress() {
		return address;
	}

	public static void tick(World world, BlockPos pos, BlockState state, FlagBlockEntity fbe) {
		if (world instanceof ServerWorld serverWorld) {
			MailWorldState mail = MailWorldState.get(serverWorld);
			boolean present = mail.mailboxes.containsKey(fbe.address);
			if (present != state.get(FlagBlock.POWERED)) {
				world.setBlockState(pos, state.with(FlagBlock.POWERED, present), Block.NOTIFY_ALL);
				Direction dir = state.get(FlagBlock.FACING);
				world.updateNeighborsExcept(pos.offset(dir), state.getBlock(), dir.getOpposite());
			}
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt.putString("address", address);
	}

	@Override
	protected void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		address = nbt.getString("address");
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(WrapperLookup registryLookup) {
		return this.createNbt(registryLookup);
	}

	@Override
	public Map<String, Consumer<String>> cede() {
		return Map.of(
			"address", s -> { this.address = s; markDirty(); }
		);
	}
	
}
