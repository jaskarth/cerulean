package fmt.cerulean.block.entity;

import fmt.cerulean.block.entity.base.SyncedBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class MirageBlockEntity extends SyncedBlockEntity {
	public BlockState state;
	public boolean aware;

	public MirageBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.MIRAGE, pos, state);
	}

	@Override
	public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);

		RegistryEntryLookup<Block> registryEntryLookup = registryLookup.createRegistryLookup().getOrThrow(RegistryKeys.BLOCK);

		state = NbtHelper.toBlockState(registryEntryLookup, nbt.getCompound("Block"));
		aware = nbt.getBoolean("A");
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);

		nbt.put("Block", NbtHelper.fromBlockState(this.state == null ? Blocks.BEDROCK.getDefaultState() : state));
		nbt.putBoolean("A", aware);
	}

	public static void set(BlockEntity be, BlockState state) {
		MirageBlockEntity mbe = (MirageBlockEntity) be;
		mbe.state = state;
		mbe.markDirty();
	}
}
