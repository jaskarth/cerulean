package fmt.cerulean.mixin;

import fmt.cerulean.block.RedstoneParallaxBlock;
import fmt.cerulean.block.entity.RedstoneParallaxBlockEntity;
import fmt.cerulean.item.component.PhotoComponent;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.util.Powerful;
import fmt.cerulean.world.data.PhotoMeta;
import fmt.cerulean.world.data.PhotoState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemFrameEntity.class)
public abstract class MixinItemFrameEntity extends AbstractDecorationEntity implements Powerful {
	@Unique
	private boolean isPowered = false;

	@Shadow
	public abstract ItemStack getHeldItemStack();

	protected MixinItemFrameEntity(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void tick() {
		super.tick();

		ItemStack stack = this.getHeldItemStack();
		if (!stack.isOf(CeruleanItems.PHOTOGRAPH)) {
			return;
		}

		PhotoComponent photo = stack.get(CeruleanItemComponents.PHOTO);
		if (photo == null) {
			return;
		}

		World world = this.getWorld();

		if (world.isClient) {
			isPowered = world.isReceivingRedstonePower(this.attachedBlockPos.offset(this.facing.getOpposite()));
		} else {
			PhotoState photos = PhotoState.get((ServerWorld) world);
			PhotoMeta meta = photos.getMeta(photo.id());
			if (meta == null) {
				return;
			}

			if (meta.powered.isEmpty()) {
				return;
			}

            boolean powered = world.isReceivingRedstonePower(this.attachedBlockPos.offset(this.facing.getOpposite()));
            if (!powered) {
                return;
            }

            for (BlockPos pos : meta.powered) {
                BlockState state = world.getBlockState(pos);
                if (state.isOf(CeruleanBlocks.REDSTONE_PARALLAX) && !state.get(RedstoneParallaxBlock.POWERED)) {
                    if (world.getBlockEntity(pos) instanceof RedstoneParallaxBlockEntity rpbe && rpbe.getPhotoId() == photo.id()) {
                        world.setBlockState(pos, state.with(RedstoneParallaxBlock.POWERED, true));
                    }
                }
            }
		}
	}

	@Override
	public boolean cerulean$isPowered() {
		return isPowered;
	}
}
