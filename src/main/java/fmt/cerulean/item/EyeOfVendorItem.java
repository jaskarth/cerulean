package fmt.cerulean.item;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EyeOfVendorItem extends Item {
	private static final String[] UNHOLY = {
			"A leaden heart.",
			"Mundane.",
			"Undecipherable echoes."
	};

	public EyeOfVendorItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();

		Class<? extends Block> heart = block.getClass();
		// TODO: probably should be getName?
		InputStream stream = heart.getResourceAsStream(heart.getSimpleName().replace('.', '/') + ".class");
		if (stream != null && context.getPlayer() != null) {
			if (!context.getWorld().isClient) {
				byte[] bytes = null;
				try {
					bytes = stream.readAllBytes();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				ClassReader reader = new ClassReader(bytes);
				ClassScrying scrying = new ClassScrying();
				reader.accept(scrying, ClassReader.EXPAND_FRAMES);

				context.getPlayer().sendMessage(Text.literal("Peering into " + Formatting.GOLD + heart.getName() + Formatting.RESET + ":"));

				for (String truth : scrying.truths) {
					context.getPlayer().sendMessage(Text.literal("- " + Formatting.GREEN + truth));
				}
			}

			return ActionResult.success(context.getWorld().isClient);

		} else {

		}

		return ActionResult.FAIL;
	}

	public static class ClassScrying extends ClassVisitor {
		protected List<String> truths = new ArrayList<>();

		protected ClassScrying() {
			super(Opcodes.ASM9);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
			MappingResolver scripture = FabricLoader.getInstance().getMappingResolver();
//			scripture.getCurrentRuntimeNamespace()
			truths.add(name);

			return super.visitMethod(access, name, descriptor, signature, exceptions);
		}

		@Override
		public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
			return super.visitField(access, name, descriptor, signature, value);
		}
	}
}
