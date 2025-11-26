package fmt.cerulean.mixin;

import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class MixinBedBlock {
	@Inject(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z", ordinal = 0), cancellable = true)
	private void cerulean$bedInDimension(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
		if (world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			player.sendMessage(Text.translatable("block.minecraft.bed.wide_awake"), true);
			cir.setReturnValue(ActionResult.SUCCESS);
		} else if (world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.SKIES)) {
			player.sendMessage(Text.translatable("block.minecraft.bed.unhelpful"), true);
			cir.setReturnValue(ActionResult.SUCCESS);
		}
	}
}
