package fmt.cerulean.client.render;

import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;

import java.util.OptionalDouble;

public abstract class CeruleanRenderTypes extends RenderLayer {
	public static final RenderLayer TRANSLUCENT_BOX = of(
			"translucent_box", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
			VertexFormat.DrawMode.QUADS, 2097152, true, true,
			translucent(TRANSLUCENT_PROGRAM)
					.layering(POLYGON_OFFSET_LAYERING)
					.build(true)
	);

	public static final RenderLayer TRANSLUCENT_BOX_ADDITIVE = of(
			"translucent_box", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
			VertexFormat.DrawMode.QUADS, 2097152, true, true,
			translucent(TRANSLUCENT_PROGRAM)
					.layering(POLYGON_OFFSET_LAYERING)
					.transparency(ADDITIVE_TRANSPARENCY)
					.build(true)
	);

	public static final RenderLayer LINES_WIDER = of(
			"lines_wider",
			VertexFormats.LINES,
			VertexFormat.DrawMode.LINES,
			1536,
			RenderLayer.MultiPhaseParameters.builder()
					.program(LINES_PROGRAM)
					.lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(3.75)))
					.layering(VIEW_OFFSET_Z_LAYERING)
					.transparency(TRANSLUCENT_TRANSPARENCY)
					.target(ITEM_ENTITY_TARGET)
					.writeMaskState(ALL_MASK)
					.cull(DISABLE_CULLING)
					.build(false)
	);

	public static RenderLayer.MultiPhaseParameters.Builder translucent(RenderPhase.ShaderProgram program) {
		return RenderLayer.MultiPhaseParameters.builder()
				.lightmap(ENABLE_LIGHTMAP)
				.program(program)
				.texture(new RenderPhase.Texture(Identifier.of("textures/misc/white.png"), false, false))
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.target(TRANSLUCENT_TARGET)
				.cull(RenderPhase.DISABLE_CULLING);
	}

	private CeruleanRenderTypes(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}
}
