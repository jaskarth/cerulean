package fmt.cerulean.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import fmt.cerulean.Cerulean;
import fmt.cerulean.block.CeruleanPlantBlock;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.render.CameraFluid;
import fmt.cerulean.client.render.EmergencyOverlay;
import fmt.cerulean.client.render.Flashlight;
import fmt.cerulean.client.render.frame.CeruleanFramebuffers;
import fmt.cerulean.client.render.frame.WindowLikeFramebuffer;
import fmt.cerulean.client.render.item.Holdable;
import fmt.cerulean.client.render.world.WaitingRoom;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanFluids;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
	@Shadow @Final
	MinecraftClient client;

	@Shadow
	@Final
	public HeldItemRenderer firstPersonRenderer;

	@Shadow
	@Final
	private BufferBuilderStorage buffers;

	@Shadow
	@Final
	private LightmapTextureManager lightmapTextureManager;

	@Shadow
	public abstract void loadProjectionMatrix(Matrix4f projectionMatrix);

	@Shadow
	public abstract Matrix4f getBasicProjectionMatrix(double fov);

	@Shadow
	protected abstract double getFov(Camera camera, float tickDelta, boolean changingFov);

	@Shadow
	protected abstract void bobView(MatrixStack matrices, float tickDelta);

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private void cerulean$captureInGameHud(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, boolean bl, int i, int j, Window window, Matrix4f matrix4f, Matrix4fStack matrix4fStack, DrawContext drawContext) {
		if (client.player != null && client.player.getMainHandStack().isOf(CeruleanItems.ANCIENT_PHOTOGRAPH)) {
			WindowLikeFramebuffer fb = CeruleanFramebuffers.getMainCopy1();
			fb.begin();
			ClientState.cautious = true;
			client.inGameHud.render(drawContext, tickCounter);
			ClientState.cautious = false;
			fb.end();
		}
	}

	@Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void cerulean$captureHand(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci, MatrixStack matrices, Matrix4fStack matrix4fStack, boolean bl) {
		if (client.player != null && client.player.getMainHandStack().isOf(CeruleanItems.ANCIENT_PHOTOGRAPH)) {
			WindowLikeFramebuffer fb = CeruleanFramebuffers.getMainCopy1();
			fb.begin();

			((LightmapTextureManagerAccessor)this.lightmapTextureManager).setDirty(true);
			((Flashlight)this.lightmapTextureManager).cerulean$updateDreamscape(tickDelta);

			this.firstPersonRenderer.renderArmHoldingItem(matrices, this.buffers.getEntityVertexConsumers(),
					LightmapTextureManager.pack(10, 0), 0, 0, client.player.getMainArm());

			if (this.buffers.getEntityVertexConsumers() instanceof VertexConsumerProvider.Immediate imm) {
				imm.draw();
			}

			fb.end();

			((LightmapTextureManagerAccessor)this.lightmapTextureManager).setDirty(true);
			this.lightmapTextureManager.update(tickDelta);
		}
	}

	@Inject(method = "renderHand", at = @At("HEAD"))
	private void cerulean$theWorldInThePalmOfMyHand(Camera camera, float tickDelta, Matrix4f matrix4f_, CallbackInfo ci) {
        // Really, it's better not to look

		if (client.player != null && client.player.getMainHandStack().isOf(CeruleanItems.ANCIENT_PHOTOGRAPH)) {
			this.loadProjectionMatrix(this.getBasicProjectionMatrix(this.getFov(camera, tickDelta, true)));
			WindowLikeFramebuffer fb = CeruleanFramebuffers.getMainCopy1();
			fb.clear();
			MatrixStack matrixStack = new MatrixStack();
			matrixStack.push();

			BlockModelRenderer.enableBrightnessCache();

			WaitingRoom room = WaitingRoom.get();

			// Setup modelview stack with desired rotation
			Quaternionf q = new Quaternionf();
			q.rotationYXZ((float) Math.PI - (room.yaw()) * (float) (Math.PI / 180.0), room.pitch() * (float) (Math.PI / 180.0), 0.0F);

			Quaternionf quaternionf = q.conjugate(new Quaternionf());
			Matrix4f matrix4f2 = new Matrix4f().rotation(quaternionf);

			Matrix4fStack mv = RenderSystem.getModelViewStack();
			mv.pushMatrix();
			mv.identity();
			mv.mul(matrix4f2);
			RenderSystem.applyModelViewMatrix();

			RenderLayer renderLayer = RenderLayer.getSolid();

			VertexConsumer vertexConsumer = this.buffers.getEntityVertexConsumers().getBuffer(renderLayer);

			Vec3d origin = room.origin();

			((LightmapTextureManagerAccessor) this.lightmapTextureManager).setDirty(true);
			((Flashlight) this.lightmapTextureManager).cerulean$updateDreamscape(tickDelta);

			// Render solid
			BlockRenderView world = room.world();
			for (Map.Entry<BlockPos, BlockState> e : room.blocksSolid().entrySet()) {
				renderBlock(e, matrixStack, origin, world, vertexConsumer);
			}

			this.buffers.getEntityVertexConsumers().draw();

			// Render cutout
			renderLayer = RenderLayer.getCutoutMipped();
			vertexConsumer = this.buffers.getEntityVertexConsumers().getBuffer(renderLayer);
			for (Map.Entry<BlockPos, BlockState> e : room.blocksCutout().entrySet()) {
				renderBlock(e, matrixStack, origin, world, vertexConsumer);
			}

			this.buffers.getEntityVertexConsumers().draw();

			BlockModelRenderer.disableBrightnessCache();

			((LightmapTextureManagerAccessor) this.lightmapTextureManager).setDirty(true);
			this.lightmapTextureManager.update(tickDelta);

			mv.popMatrix();
			RenderSystem.applyModelViewMatrix();

			matrixStack.pop();

			fb.end();
		}
	}

	@Unique
	private void renderBlock(Map.Entry<BlockPos, BlockState> e, MatrixStack matrixStack, Vec3d origin, BlockRenderView world, VertexConsumer vertexConsumer) {
		matrixStack.push();
		BlockPos pos = e.getKey();
		BlockState state = e.getValue();
		matrixStack.translate(pos.getX() + origin.getX(), origin.getY() + pos.getY(), pos.getZ() + origin.getZ());

		this.client.getBlockRenderManager()
				.getModelRenderer()
				.render(world, this.client.getBlockRenderManager().getModel(state), state, pos,
						matrixStack, vertexConsumer, true, Random.create(), state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
		matrixStack.pop();
	}

	@Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
	public void cerulean$modifyFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
		Fluid f = ((CameraFluid)camera).cerulean$getFluid();
		if (f == CeruleanFluids.POLYETHYLENE || f == CeruleanFluids.REALIZED_POLYETHYLENE || f == CeruleanFluids.REALIZED_POLYETHYLENE_FLOWING) {
			cir.setReturnValue(cir.getReturnValueD() * MathHelper.lerp(this.client.options.getFovEffectScale().getValue(), 1.0, 0.85714287F));
		}
	}

	@Inject(method = "loadBlurPostProcessor", at = @At("TAIL"))
	private void cerulean$loadFramebuffer(ResourceFactory resourceFactory, CallbackInfo ci) {
		CeruleanFramebuffers.init(resourceFactory);
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw()V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void cerulean$beforeDraw(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, boolean bl, int i, int j, Window window, Matrix4f matrix4f, Matrix4fStack matrix4fStack, DrawContext context) {
		EmergencyOverlay.render(context);
	}
}
