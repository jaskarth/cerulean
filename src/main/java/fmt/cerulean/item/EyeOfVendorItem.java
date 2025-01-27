package fmt.cerulean.item;

import com.mojang.serialization.Codec;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.world.CeruleanDimensions;
import io.netty.buffer.ByteBuf;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EyeOfVendorItem extends Item {
	private static final String[] UNHOLY = {
			"A leaden heart.",
			"Completely mundane.",
			"Undecipherable echoes.",
			"Nothing here.",
			"Not interesting."
	};

	public EyeOfVendorItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (user.isSneaking()) {
			if (!world.isClient && !world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
				ItemStack stack = user.getStackInHand(hand);
				Mode mode = getOrSetMode(stack);
				Mode newMode = mode == Mode.INDISCRIMINATE ? Mode.ATTUNED : Mode.INDISCRIMINATE;
				stack.set(CeruleanItemComponents.EYE_MODE, newMode);

				String name = newMode.name().toLowerCase(Locale.ROOT);
				user.sendMessage(Text.literal("Mode: " + name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1)), true);
			}
		}
		return super.use(world, user, hand);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		ItemStack stack = context.getStack();
		Mode mode = getOrSetMode(stack);

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
				ClassScrying scrying = new ClassScrying(mode);
				reader.accept(scrying, ClassReader.EXPAND_FRAMES);

				if (mode == Mode.INDISCRIMINATE) {
					context.getPlayer().sendMessage(Text.literal("Glancing at " + Formatting.LIGHT_PURPLE + heart.getName() + Formatting.RESET + ":"));

					for (String truth : scrying.fieldTruths) {
						context.getPlayer().sendMessage(Text.literal("- " + Formatting.AQUA + truth));
					}

					for (String truth : scrying.methodTruths) {
						context.getPlayer().sendMessage(Text.literal("- " + Formatting.GREEN + truth + "()"));
					}
				} else {
					if (scrying.untruths.isEmpty()) {
						context.getPlayer().sendMessage(Text.literal(UNHOLY[context.getWorld().random.nextInt(UNHOLY.length)]));
					} else {
						context.getPlayer().sendMessage(Text.literal("Peering into " + Formatting.LIGHT_PURPLE + heart.getName() + Formatting.RESET + ":"));

						for (Pair<String, Object> untruth : scrying.untruths) {
							Object right = untruth.getRight();
							if (right instanceof String s) {
								right = "\"" + s + "\"";
							}

							context.getPlayer().sendMessage(Text.literal("- " + Formatting.AQUA + untruth.getLeft() + Formatting.RESET + " = " + Formatting.GOLD + right));
						}
					}
				}
			}

			return ActionResult.success(context.getWorld().isClient);
		}

		return ActionResult.FAIL;
	}

	private static @NotNull Mode getOrSetMode(ItemStack stack) {
		Mode mode = stack.get(CeruleanItemComponents.EYE_MODE);
		if (mode == null) {
			mode = Mode.INDISCRIMINATE;
			stack.set(CeruleanItemComponents.EYE_MODE, Mode.INDISCRIMINATE);
		}
		return mode;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		ClientWorld world = MinecraftClient.getInstance().world;
		if (world != null && world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			tooltip.add(Text.literal(Formatting.GREEN + "Mode: Truth"));
			return;
		}
		Mode mode = stack.getOrDefault(CeruleanItemComponents.EYE_MODE, Mode.INDISCRIMINATE);
		String name = mode.name().toLowerCase(Locale.ROOT);
		tooltip.add(Text.literal(Formatting.GREEN + "Mode: " + name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1)));
	}

	public static class ClassScrying extends ClassVisitor {
		private final Mode mode;
		protected List<String> methodTruths = new ArrayList<>();
		protected List<String> fieldTruths = new ArrayList<>();
		protected List<Pair<String, Object>> untruths = new ArrayList<>();

		protected ClassScrying(Mode mode) {
			super(Opcodes.ASM9);
			this.mode = mode;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
			MappingResolver scripture = FabricLoader.getInstance().getMappingResolver();
//			scripture.getCurrentRuntimeNamespace()
			if (mode == Mode.INDISCRIMINATE) {
				methodTruths.add(name);
			}

			return super.visitMethod(access, name, descriptor, signature, exceptions);
		}

		@Override
		public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
			boolean careful = name.startsWith("_CERULEAN$$");
			if (mode == Mode.INDISCRIMINATE) {
				fieldTruths.add(careful ? cleanse(name) : name);
			} else {
				if (careful && (access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC) {
					// TODO: check for acc_synthetic
					untruths.add(new Pair<>(cleanse(name), value));
				}
			}

			return super.visitField(access, name, descriptor, signature, value);
		}

		private static String cleanse(String s) {
			return s.substring(11);
		}
	}

	public enum Mode {
		INDISCRIMINATE,
		ATTUNED;

		public static final Codec<Mode> CODEC = Codec.INT.xmap(i -> Mode.values()[i], Mode::ordinal);
		public static final PacketCodec<ByteBuf, Mode> PACKET_CODEC = PacketCodecs.INTEGER.xmap(i -> Mode.values()[i], Mode::ordinal);
	}
}
