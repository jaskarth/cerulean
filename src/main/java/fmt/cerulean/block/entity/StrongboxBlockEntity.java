package fmt.cerulean.block.entity;

import fmt.cerulean.block.entity.base.DreamscapeTickable;
import fmt.cerulean.block.entity.base.SyncedBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class StrongboxBlockEntity extends SyncedBlockEntity implements DreamscapeTickable {
	public boolean hasItem = true;
	public int breakProgress;

	public RegistryKey<World> originalWorld;
	public BlockPos originalBlockPos;

	public StrongboxBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.STRONGBOX, pos, state);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		hasItem = nbt.getBoolean("HasItem");
		breakProgress = nbt.getInt("BreakProgress");
		if (nbt.contains("WorldId")) {
			originalWorld = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(nbt.getString("WorldId")));
		}
		if (nbt.contains("WorldPos")) {
			originalBlockPos = NbtHelper.toBlockPos(nbt, "WorldPos").get();
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt.putBoolean("HasItem", hasItem);
		nbt.putInt("BreakProgress", breakProgress);
		if (originalWorld != null) {
			nbt.putString("WorldId", originalWorld.getValue().toString());
		}

		if (originalBlockPos != null) {
			nbt.put("WorldPos", NbtHelper.fromBlockPos(originalBlockPos));
		}
	}

	public static void serverTick(World world, BlockPos pos, BlockState state, StrongboxBlockEntity blockEntity) {
		if (blockEntity.originalWorld != null && blockEntity.originalBlockPos != null) {
			if (!(world.getServer().getWorld(blockEntity.originalWorld).getBlockEntity(blockEntity.originalBlockPos) instanceof StrongboxBlockEntity)) {
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW);
				return;
			}
		}

		if (!blockEntity.hasItem) {
			blockEntity.breakProgress++;

			if (blockEntity.breakProgress > 100) {
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW);
				world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(Blocks.GLASS.getDefaultState()));
				world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(Blocks.GLASS.getDefaultState()));
				if (blockEntity.originalWorld != null && blockEntity.originalBlockPos != null) {
					world.getServer().getWorld(blockEntity.originalWorld).setBlockState(blockEntity.originalBlockPos, Blocks.AIR.getDefaultState());
				}
			}

			blockEntity.markDirty();
		}
	}
}
