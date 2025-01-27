package fmt.cerulean.client.render.item;

import com.mojang.blaze3d.systems.RenderSystem;
import fmt.cerulean.Cerulean;
import fmt.cerulean.mixin.client.RenderSystemAccessor;
import fmt.cerulean.world.CeruleanDimensions;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class EyeOfVenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
	public static final Identifier REG = Cerulean.id("item/eye_of_vendor_reg");
	public static final Identifier DYN = Cerulean.id("item/eye_of_vendor_dyn");

	@Override
	public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		MinecraftClient client = MinecraftClient.getInstance();

		matrices.translate(0.5F, 0.5F, 0.5F);

		Vector3f[] lights = RenderSystemAccessor.getShaderLightDirections();
		Vector3f la = new Vector3f(lights[0]);
		Vector3f lb = new Vector3f(lights[1]);

		if (mode == ModelTransformationMode.GUI) {
			DiffuseLighting.disableGuiDepthLighting();
		}

		Identifier id;
		if (client.world == null || !client.world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			id = REG;
		} else {
			id = DYN;
		}

		client.getItemRenderer()
				.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, client.getBakedModelManager().getModel(id));

		if (vertexConsumers instanceof VertexConsumerProvider.Immediate imm) {
			imm.draw();
		}

		RenderSystem.setShaderLights(la, lb);
	}

	public static void registerModels(ModelLoadingPlugin.Context context) {
		context.addModels(REG, DYN);
	}
}
