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

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.transformer.EnvironmentStrippingData;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.google.common.collect.Sets;

import net.fabricmc.loader.api.FabricLoader;

public class Soul implements IMixinConfigPlugin {
	private static final String UNSPOKEN = "fmt/cerulean/mixin/$";
	private static final Set<String> CONVALESCENTS = Set.of("cerulean");
	public static final List<List<String>> SCRIPTURE = List.of(
		List.of(
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
		),
		List.of(
			"Gaze into the stars and the stars twinkle back",
			"The heart's light beckons slightly",
			"Holding hands with heaven",
			"Recoil from the saccharine flavor",

			"Drifting through an apathetic chronology",
			"Parse cruelty with eyes closed",
			"Like sandpaper for your emotions",
			"Choking on glass to see the sun rise"
		),
		List.of(
			"Another glimpse behind the curtain",
			"Axiom after axiom peeled away",
			"Truth has never been closer",
			"Truth has never been further away",

			"Having finally been seen by the universe",
			"Only now do I understand isolation",
			"In time it will all make sense",
			"In time it will all fall apart"
		)
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
					EnvType env = FabricLoader.getInstance().getEnvironmentType();
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
									if (c.getMetadata().getId().equals("minecraft") && FabricLoader.getInstance().isDevelopmentEnvironment()) {
										try {
											byte[] b = Files.readAllBytes(p);
											ClassReader reader = new ClassReader(b);
											EnvironmentStrippingData data = new EnvironmentStrippingData(FabricLoaderImpl.ASM_VERSION, env.toString());
											reader.accept(data, ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES);
											if (data.stripEntireClass()) {
												return;
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
										if (s.toLowerCase().contains("client")) {
											return;
										}
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
		String[] parts = targetClassName.split("\\.");
		String pkg = targetClassName.substring(0, targetClassName.length() - parts[parts.length - 1].length());
		Random desk = new Random(pkg.hashCode());
		List<String> poem = SCRIPTURE.get(desk.nextInt(SCRIPTURE.size()));
		targetClass.sourceFile = poem.get(random.nextInt(poem.size()));
		targetClass.sourceDebug = poem.get(random.nextInt(poem.size()));
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
