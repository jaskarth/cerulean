package fmt.cerulean.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveLensItem extends Item {
	private static final List<String> WISDOM = new ArrayList<>();

	public ReflectiveLensItem(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		ClientWorld world = MinecraftClient.getInstance().world;
		if (world != null) {
			tooltip.add(Text.literal(Formatting.DARK_GRAY + WISDOM.get(Math.floorMod(world.getTime(), WISDOM.size()))));
		}

		super.appendTooltip(stack, context, tooltip, type);
	}

	private static void devour(Class<?> clazz) {
		InputStream stream = clazz.getResourceAsStream(clazz.getSimpleName().replace('.', '/') + ".class");
		byte[] bytes = null;
		try {
			bytes = stream.readAllBytes();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		ClassReader reader = new ClassReader(bytes);
		ClassNode node = new ClassNode();
		reader.accept(node, ClassReader.EXPAND_FRAMES);

		for (MethodNode method : node.methods) {
			for (AbstractInsnNode insn : method.instructions) {
				if (insn.getOpcode() > 0) {
					WISDOM.add(Printer.OPCODES[insn.getOpcode()].toLowerCase());
				}
			}
		}
	}

	static {
		devour(ReflectiveLensItem.class);
	}
}
