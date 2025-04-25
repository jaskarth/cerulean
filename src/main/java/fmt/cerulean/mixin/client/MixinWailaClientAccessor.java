package fmt.cerulean.mixin.client;

import fmt.cerulean.block.entity.FauxBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "mcp.mobius.waila.access.ClientAccessor")
public abstract class MixinWailaClientAccessor {

	@Shadow(remap = false)
	abstract BlockEntity getBlockEntity();

	// Sorry

	@Inject(method = "getBlock", at = @At("HEAD"), remap = false, cancellable = true)
	public void cerulean$getBlock(CallbackInfoReturnable<Block> cir) {
		if (getBlockEntity() instanceof FauxBlockEntity fbe) {
			if (fbe.state == null) {
				cir.setReturnValue(Blocks.BEDROCK);
				return;
			}
			cir.setReturnValue(fbe.state.getBlock());
		}
	}

	@Inject(method = "getBlockState", at = @At("HEAD"), remap = false, cancellable = true)
	public void cerulean$getBlockState(CallbackInfoReturnable<BlockState> cir) {
		if (getBlockEntity() instanceof FauxBlockEntity fbe) {
			if (fbe.state == null) {
				cir.setReturnValue(Blocks.BEDROCK.getDefaultState());
				return;
			}
			cir.setReturnValue(fbe.state);
		}
	}
}
