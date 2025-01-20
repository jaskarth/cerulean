package fmt.cerulean.mixin.client;

import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.dimension.DimensionType;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightmapTextureManager.class)
public abstract class MixinLightmapTextureManager {
    @Shadow
    private boolean dirty;

    @Shadow @Final
    private MinecraftClient client;

    @Shadow @Final
    private NativeImageBackedTexture texture;

    @Shadow
    private float flickerIntensity;

    @Shadow
    protected abstract float easeOutQuart(float x);

    @Shadow
    protected static void clamp(Vector3f vec) {
    }

    @Shadow
    public static float getBrightness(DimensionType type, int lightLevel) {
        return 0;
    }

    @Shadow @Final
    private NativeImage image;

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void cerulean$updateCustomDimensions(float delta, CallbackInfo ci) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world != null && CeruleanDimensions.DREAMSCAPE.equals(world.getDimensionEntry().getKey().get().getValue())) {
            cerulean$updateDreamscape(delta);
            ci.cancel();
        }
    }

    private void cerulean$updateDreamscape(float delta) {
        if (this.dirty) {
            this.dirty = false;
            ClientWorld world = this.client.world;
            if (world != null) {

                float flicker = this.flickerIntensity + 1.5F;
                Vector3f res = new Vector3f();

                for(int skyLight = 0; skyLight < 16; ++skyLight) {
                    for(int blockLight = 0; blockLight < 16; ++blockLight) {
                        if (skyLight == 15) {
                            this.image.setColor(blockLight, skyLight, 0xFFFFFFFF);
                            continue;
                        }

                        float brightness = getBrightness(world.getDimension(), blockLight) * flicker;
                        float green = brightness * brightness * ((brightness * 0.8F + 0.1F) * 0.8F + 0.2F);
                        float blue = brightness * brightness * (brightness * brightness * 0.6F + 0.3F);
                        res.set(brightness * ((brightness * 0.9) - 0.05), green, blue);
                        clamp(res);

                        float gamma = this.client.options.getGamma().getValue().floatValue();
                        Vector3f eased = new Vector3f(this.easeOutQuart(res.x), this.easeOutQuart(res.y), this.easeOutQuart(res.z));
                        res.lerp(eased, Math.max(0.0F, gamma));
                        res.lerp(new Vector3f(0.75F, 0.75F, 0.75F), 0.04F);
                        clamp(res);
                        res.mul(255.0F);

                        int x = (int) res.x();
                        int y = (int) res.y();
                        int z = (int) res.z();
                        this.image.setColor(blockLight, skyLight, 0xFF000000 | z << 16 | y << 8 | x);
                    }
                }

                this.texture.upload();
            }
        }
    }
}