package fmt.cerulean.mixin.client;

import com.mojang.authlib.GameProfile;
import fmt.cerulean.client.tex.gen.Oxidator;
import fmt.cerulean.registry.CeruleanStatusEffects;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayerEntity extends PlayerEntity {
	@Shadow
	protected abstract @Nullable PlayerListEntry getPlayerListEntry();

	public MixinAbstractClientPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "getSkinTextures", at = @At("HEAD"), cancellable = true)
	private void cerulean$oxidize(CallbackInfoReturnable<SkinTextures> cir) {
		if (this.hasStatusEffect(CeruleanStatusEffects.OXIDIZED)) {
			PlayerListEntry playerListEntry = this.getPlayerListEntry();
			SkinTextures old = playerListEntry == null ? DefaultSkinHelper.getSkinTextures(this.getUuid()) : playerListEntry.getSkinTextures();
			Identifier id = Oxidator.oxidate(old.texture());
			cir.setReturnValue(new SkinTextures(id, old.textureUrl(), old.capeTexture(), old.elytraTexture(), old.model(), old.secure()));
		}
	}
}
