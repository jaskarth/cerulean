package fmt.cerulean.block.entity;

import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MimicBlockEntity extends BlockEntity {
	public BlockState state;
	public int dist;
	public Direction facing = Direction.NORTH;

	public MimicBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.MIMIC, pos, state);
	}

	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return this.createNbt();
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		RegistryEntryLookup<Block> registryEntryLookup = this.world != null
				? this.world.createCommandRegistryWrapper(RegistryKeys.BLOCK)
				: Registries.BLOCK.getReadOnlyWrapper();

		state = NbtHelper.toBlockState(registryEntryLookup, nbt.getCompound("Block"));
		dist = nbt.getInt("Dist");
		facing = Direction.byId(nbt.getByte("Dir"));
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		nbt.put("Block", NbtHelper.fromBlockState(this.state == null ? Blocks.BEDROCK.getDefaultState() : state));
		nbt.putInt("Dist", dist);
		nbt.putByte("Dir", (byte) facing.getId());
	}

	public static void set(BlockEntity be, BlockState state, int dist, Direction facing) {
		MimicBlockEntity mbe = (MimicBlockEntity) be;
		mbe.state = state;
		mbe.dist = dist;
		mbe.facing = facing;
		mbe.markDirty();

		World world = mbe.getWorld();
		if (world instanceof ServerWorld sw) {
			sw.getChunkManager().markForUpdate(mbe.getPos());
		}
	}
}
