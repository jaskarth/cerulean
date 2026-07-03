package fmt.cerulean.block.entity;

import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class RedstoneParallaxBlockEntity extends BlockEntity {
	private int photoId = -1;

	public RedstoneParallaxBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.REDSTONE_PARALLAX, pos, state);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		photoId = nbt.getInt("PhotoId");
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		nbt.putInt("PhotoId", photoId);
	}

	public int getPhotoId() {
		return photoId;
	}

	public void setPhotoId(int id) {
		this.photoId = id;
		markDirty();
	}
}
