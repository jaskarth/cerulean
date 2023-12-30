package fmt.cerulean.compat.emi;

import java.util.List;

import com.google.common.collect.Lists;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fmt.cerulean.block.entity.WellBlockEntity;
import fmt.cerulean.client.particle.StarParticleType;
import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.item.StarItem;
import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

public class EmiBrushRecipe extends BasicEmiRecipe {
	private static final EmiStack PIPE = EmiStack.of(CeruleanBlocks.PIPE);
	public final List<EmiIngredient> inputStars, inputItems, outputItems;
	public final EmiIngredient outputStar;
	public final List<BlockState> blocks;
	public final List<OrderedText> info;
	public boolean nonsense = false;

	public EmiBrushRecipe(Identifier id, List<EmiIngredient> inputStars, List<EmiIngredient> inputItems, EmiIngredient outputStar, List<EmiIngredient> outputItems, List<BlockState> blocks, Text info) {
		this(id, inputStars, inputItems, outputStar, outputItems, blocks, split(info));
	}

	private EmiBrushRecipe(Identifier id, List<EmiIngredient> inputStars, List<EmiIngredient> inputItems, EmiIngredient outputStar, List<EmiIngredient> outputItems, List<BlockState> blocks, List<OrderedText> info) {
		super(CeruleanEmiPlugin.BRUSHING, id, 100, 18 + (inputItems.isEmpty() ? 0 : 20) + (outputItems.isEmpty() ? 0 : 20) + info.size() * 12);
		this.inputStars = inputStars;
		this.inputItems = inputItems;
		this.outputStar = outputStar;
		this.outputItems = outputItems;
		inputs.addAll(inputStars);
		inputs.addAll(inputItems);
		if (!outputStar.isEmpty()) {
			outputs.addAll(outputItems.stream().flatMap(i -> i.getEmiStacks().stream()).toList());
			outputs.addAll(outputStar.getEmiStacks());
		} else {
			outputs.addAll(outputStar.getEmiStacks());
			outputs.addAll(outputItems.stream().flatMap(i -> i.getEmiStacks().stream()).toList());
		}
		if (outputs.size() > outputItems.size() + 1) {
			nonsense = true;
		}
		this.blocks = blocks;
		this.info = info;
	}

	private static List<OrderedText> split(Text t) {
		if (t == null) {
			return List.of();
		}
		MinecraftClient client = MinecraftClient.getInstance();
		return client.textRenderer.wrapLines(t, 100);
	}

	@Override
	public boolean supportsRecipeTree() {
		return !nonsense && super.supportsRecipeTree();
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		MinecraftClient client = MinecraftClient.getInstance();
		Random random = client.world.random;
		List<GuiParticle> particles = Lists.newArrayList();
		int starY = inputItems.isEmpty() ? 0 : 20;
		if (inputStars.size() > 0) {
			widgets.addSlot(PIPE, 0, starY).drawBack(false);
			widgets.addSlot(inputStars.get(0), 18, starY).drawBack(false);
			addParticles(particles, inputStars.get(0), starY, true, false, random);
		}
		int cx = getDisplayWidth() / 2 - inputItems.size() * 9;
		for (EmiIngredient i : inputItems) {
			widgets.addSlot(i, cx, 0);
			cx += 18;
		}
		if (!outputStar.isEmpty()) {
			widgets.addSlot(outputStar, 64, starY).drawBack(false);
			widgets.addSlot(PIPE, 82, starY).drawBack(false);
			addParticles(particles, outputStar, starY, false, true, random);
		}
		if (!blocks.isEmpty()) {
			widgets.addDrawable(getDisplayWidth() / 2, starY, 0, 0, (draw, mouseX, mouseY, delta) -> {
				draw.getMatrices().translate(0, 10, 0);
				draw.getMatrices().scale(16, -16, 1);
				draw.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(25));
				draw.getMatrices().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-45));
				BlockState state = blocks.get(0);
				client.getBlockRenderManager().renderBlockAsEntity(state, draw.getMatrices(), draw.getVertexConsumers(), LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
			});
		}
		if (inputStars.size() > 1) {
			widgets.addSlot(inputStars.get(1), 64, starY).drawBack(false);
			widgets.addSlot(PIPE, 82, starY).drawBack(false);
			addParticles(particles, inputStars.get(1), starY, false, false, random);
		}
		cx = getDisplayWidth() / 2 - outputItems.size() * 9;
		for (EmiIngredient o : outputItems) {
			widgets.addSlot(o, cx, starY + 20);
			cx += 18;
		}
		int infoY = starY + 18 + 3 + (outputItems.isEmpty() ? 0 : 20);
		for (OrderedText i : info) {
			int tw = client.textRenderer.getWidth(i);
			widgets.addText(i, (getDisplayWidth() - tw) / 2, infoY, 0xFFFFFFFF, true);
			infoY += 12;
		}
		widgets.addDrawable(0, 0, 0, 0, (draw, mouseX, mouseY, delta) -> {
			long time = client.world.getTime();
			for (int i = 0; i < particles.size(); i++) {
				GuiParticle p = particles.get(i);
				int proc = (int) ((time - p.time) % 40);
				if (proc < 24) {
					float mod = proc / 20f;
					int x = (int) (p.x + (p.dx - p.x) * mod);
					int y = (int) (p.y + (p.dy - p.y) * mod);
					float a = 1;
					if (proc >= 20) {
						a = 1 - ((proc - 20) / 4f);
					}
					draw.setShaderColor(p.r, p.g, p.b, a);
					draw.fill(x - 1, y - 1, x + 1, y + 1, 0xffffffff);
					draw.setShaderColor(1, 1, 1, 1);
				}
			}
		});
	}

	private void addParticles(List<GuiParticle> particles, EmiIngredient stack, int sy, boolean left, boolean output, Random random) {
		sy += 10;
		List<FlowResource> flows = Lists.newArrayList();
		for (EmiStack s : stack.getEmiStacks()) {
			if (s.getItemStack().getItem() instanceof StarItem si) {
				flows.add(si.resource);
			}
		}
		if (flows.isEmpty()) {
			return;
		}
		for (int i = 0; i < 40; i++) {
			GuiParticle p = new GuiParticle();
			int cx = getDisplayWidth() / 2 - 4 + random.nextInt(8);
			int cy = sy - 5 + random.nextInt(10);
			int fx = 0, fy = 0;
			if (left) {
				fx = 16 + random.nextInt(6);
				fy = sy - 5 + random.nextInt(10);
			} else {
				fx = getDisplayWidth() - 16;
				fy = sy - 5 + random.nextInt(10);
			}
			if (!output) {
				p.x = fx;
				p.y = fy;
				p.dx = cx;
				p.dy = cy;
			} else {
				p.x = cx;
				p.y = cy;
				p.dx = fx;
				p.dy = fy;
			}
			p.time = i;
			StarParticleType star = WellBlockEntity.createParticle(flows.get(random.nextInt(flows.size())), false, random);
			p.r = star.red;
			p.g = star.green;
			p.b = star.blue;
			particles.add(p);
		}
	}

	private static class GuiParticle {
		float r, g, b;
		float x, y, dx, dy;
		long time;
	}
}
