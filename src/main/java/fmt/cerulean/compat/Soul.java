package fmt.cerulean.compat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Permission;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.google.common.collect.Sets;

import net.fabricmc.loader.api.FabricLoader;

public class Soul implements IMixinConfigPlugin {
	private static final String UNSPOKEN = "fmt/cerulean/mixin/$";
	private static final Set<String> CONVALESCENTS = Set.of("minecraft", "cerulean");
	private static final List<String> SCRIPTURE = List.of(
		"Find refuge",
		"Lights dimmed",
		"Eyes wide shut",
		"A world now silent",

		"Light flickers in the distance",
		"Can you see it?",
		"Can't you feel it?",
		"Home you never knew",

		"Wake up",
		"Don't stop dreaming"
	);
	private Random random = new Random();

	@Override
	public void onLoad(String mixinPackage) {
		String holy = "fmt/cerulean/compat";
		String unholy = "fmt/cerulean/mixin";
		Set<String> targets = Sets.newHashSet();
		try {
			FabricLoader.getInstance().getAllMods().stream()
				.filter(c -> {
					if (FabricLoader.getInstance().isDevelopmentEnvironment() && c.getMetadata().getId().equals("cerulean")) {
						return false;
					}
					return CONVALESCENTS.contains(c.getMetadata().getId());
				}).forEach(c -> {
					for (Path path : c.getRootPaths()) {
						int pathStart = path.toString().length();
						try {
							Files.walk(path).forEach(p -> {
								String s = p.toString();
								if (s.endsWith(".class")) {
									s = s.substring(pathStart, s.length() - 6);
									if (s.startsWith("/")) {
										s = s.substring(1);
									}
									if (!s.startsWith(holy) && !s.startsWith(unholy)) {
										targets.add("L" + s + ";");
									}
								}
							});
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
			});
		} catch (Throwable t) {
			System.err.println("There's nothing to dream about tonight");
			t.printStackTrace();
			return;
		}
		random = new Random(targets.hashCode());
		try {
			sculptHusk(targets);
		} catch (Throwable t) {
			System.err.println("Only nightmares tonight");
			t.printStackTrace();
			return;
		}
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return List.of(UNSPOKEN.split("/mixin/")[1]);
	}

	@SuppressWarnings("deprecation")
	private void sculptHusk(Collection<String> targets) throws Throwable {
		ClassWriter writer = new ClassWriter(0);
		writer.visit(52, Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT | Opcodes.ACC_INTERFACE, UNSPOKEN, null, "java/lang/Object", null);
		AnnotationVisitor mixinAnnot = writer.visitAnnotation("Lorg/spongepowered/asm/mixin/Mixin;", false);
		AnnotationVisitor value = mixinAnnot.visitArray("value");
		for (String target : targets) {
			value.visit(null, Type.getType(target));
		}
		value.visitEnd();
		mixinAnnot.visitEnd();
		writer.visitEnd();

		URL yoink = new URL("cerulean", null, -1, "/", new SyntheticStreamHandler("/" + UNSPOKEN + ".class", writer.toByteArray()));
		giveFabricANewToy(yoink);
	}


	private void giveFabricANewToy(URL url) throws Throwable {
		ClassLoader loader = getClass().getClassLoader();
		for (Method method : loader.getClass().getDeclaredMethods()) {
			if (method.getReturnType() == Void.TYPE && method.getParameterCount() == 1 && method.getParameterTypes()[0] == URL.class) {
				method.setAccessible(true);
				MethodHandle handle = MethodHandles.lookup().unreflect(method);
				handle.invoke(loader, url);
				return;
			}
		}
		throw new IllegalStateException("Fabric doesn't want any new toys");
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		targetClass.sourceFile = SCRIPTURE.get(random.nextInt(SCRIPTURE.size()));
		targetClass.sourceDebug = SCRIPTURE.get(random.nextInt(SCRIPTURE.size()));
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	private static class SyntheticStreamHandler extends URLStreamHandler {
		private final String path;
		private final byte[] bytes;
		
		private SyntheticStreamHandler(String path, byte[] bytes) {
			this.path = path;
			this.bytes = bytes;
		}

		@Override
		protected URLConnection openConnection(URL url) throws IOException {
			if (path.equals(url.getPath())) {
				return new SyntheticConnection(url);
			}
			return null;
		}

		private class SyntheticConnection extends URLConnection {

			private SyntheticConnection(URL url) {
				super(url);
			}

			@Override
			public void connect() throws IOException {
				throw new UnsupportedOperationException();
			}

			@Override
			public InputStream getInputStream() {
				return new ByteArrayInputStream(bytes);
			}

			@Override
			public Permission getPermission() {
				return null;
			}
		}
	}
}
