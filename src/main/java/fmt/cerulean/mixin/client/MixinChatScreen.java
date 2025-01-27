package fmt.cerulean.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.datafixers.util.Either;

import fmt.cerulean.block.base.Obedient;
import fmt.cerulean.net.packet.InfluencePacket;
import fmt.cerulean.registry.CeruleanItems;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

@Mixin(ChatScreen.class)
public class MixinChatScreen {
	
	@Inject(at = @At("HEAD"), method = "sendMessage", cancellable = true)
	public void cerulean$sendMessage(String chatText, boolean addToHistory, CallbackInfo info) {
		MinecraftClient client = MinecraftClient.getInstance();
		PlayerEntity player = client.player;
		if (player.isHolding(CeruleanItems.BRUSH)) {
			if (player.isInCreativeMode()) {
				HitResult result = client.crosshairTarget;
				if (result.getType() == HitResult.Type.BLOCK && result instanceof BlockHitResult bhr) {
					BlockState state = client.world.getBlockState(bhr.getBlockPos());
					Block block = state.getBlock();
					BlockEntity be = client.world.getBlockEntity(bhr.getBlockPos());
					if (Obedient.willCede(block) || Obedient.willCede(be)) {
						ClientPlayNetworking.send(new InfluencePacket(Either.left(bhr.getBlockPos()), chatText));
					} else {
						player.sendMessage(Text.literal("The fabric of the universe bucks at your suggestion"), true);
					}
				} else if (result.getType() == HitResult.Type.ENTITY && result instanceof EntityHitResult ehr) {
					Entity entity = ehr.getEntity();
					if (Obedient.willCede(entity)) {
						ClientPlayNetworking.send(new InfluencePacket(Either.right(entity.getId()), chatText));
					} else {
						player.sendMessage(Text.literal("The world disagrees"), true);
					}
				}
			} else {
				player.sendMessage(Text.literal("Reality is not so easily influenced"), true);
			}
			info.cancel();
		}
	}
}
