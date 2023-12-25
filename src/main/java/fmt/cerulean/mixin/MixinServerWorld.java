package fmt.cerulean.mixin;

import fmt.cerulean.Cerulean;
import fmt.cerulean.block.entity.MimicBlockEntity;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.world.CeruleanDimensions;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.*;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld extends World implements StructureWorldAccess {

	@Shadow @Final private List<ServerPlayerEntity> players;

	@Shadow @Final private MinecraftServer server;

	protected MixinServerWorld(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
		super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
	}

	@Inject(method = "wakeSleepingPlayers", at = @At("HEAD"))
	public void handleSleep(CallbackInfo ci) {
		for (ServerPlayerEntity p : new ArrayList<>(this.players)) {
			if (p.isSleeping()) {
				List<PaintingEntity> paintings = this.getEntitiesByClass(PaintingEntity.class, p.getBoundingBox().expand(10, 3, 10), e -> true);
				boolean found = false;

				PaintingEntity portalPainting = null;
				for (PaintingEntity painting : paintings) {
					if (painting.getVariant().matchesKey(RegistryKey.of(RegistryKeys.PAINTING_VARIANT, Cerulean.id("dreams")))) {
						if (found) {
							// Found two portal paintings?
							found = false;
							break;
						}

						portalPainting = painting;
						found = true;
					}
				}

				if (!found) {
					continue;
				}

				ServerWorld dreamscape = this.server.getWorld(RegistryKey.of(RegistryKeys.WORLD, CeruleanDimensions.DREAMSCAPE));

				if (dreamscape == null) {
					throw new RuntimeException("I closed my eyes but nothing appeared...");
				}

				BlockPos origin = p.getBlockPos();

				// [House](https://www.britannica.com/animal/horse) of leaves

				BlockPos paintingOrigin = portalPainting.getBlockPos();
				Direction moveDir = portalPainting.getHorizontalFacing().getOpposite();
				paintingOrigin = paintingOrigin.offset(moveDir);

				BlockPos otherA = paintingOrigin.offset(moveDir.rotateYClockwise());
				BlockPos otherB = paintingOrigin.offset(moveDir.rotateYCounterclockwise());

				// Consider this:
				// ACB
				// AXB
				// AXB
				// ACB
				//
				// A: otherA pos
				// B: otherB pos
				// X: hole
				// C: central pos

				BlockState[] a = new BlockState[4];
				BlockState[] c = new BlockState[4];
				BlockState[] b = new BlockState[4];

				for (int j = -2; j <= 1; j++) {
					a[j + 2] = this.getBlockState(otherA.up(j));
					b[j + 2] = this.getBlockState(otherB.up(j));
					c[j + 2] = this.getBlockState(paintingOrigin.up(j));
				}

				// Check support blocks
				boolean ok = true;
				for (int j = -2; j <= 1; j++) {
					// Ignore corners
					if (j != -2 && j != 1) {
						if (!a[j + 2].isOpaqueFullCube(this, otherA.up(j))) {
							ok = false;
							break;
						}

						if (!b[j + 2].isOpaqueFullCube(this, otherB.up(j))) {
							ok = false;
							break;
						}
					}

					if (!c[j + 2].isOpaqueFullCube(this, paintingOrigin.up(j))) {
						ok = false;
						break;
					}
				}

				if (!ok) {
					continue;
				}

				// Facade copy
				for (int x = -11; x <= 11; x++) {
					for (int z = -11; z <= 11; z++) {
						for (int y = -5; y <= 5; y++) {
							BlockPos local = origin.add(x, y, z);
							if (Math.abs(x) == 11 || Math.abs(z) == 11 || Math.abs(y) == 5) {
								dreamscape.setBlockState(local, CeruleanBlocks.INKY_VOID.getDefaultState());
							} else {
								dreamscape.setBlockState(local, this.getBlockState(local));
							}
						}
					}
				}

				for (int i = 0; i < 20; i++) {
					// Central path
					dreamscape.setBlockState(paintingOrigin.offset(moveDir, i), Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, Math.min((i + 1) * 3, 15)));
					dreamscape.setBlockState(paintingOrigin.down().offset(moveDir, i), Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, Math.min((i + 1) * 3, 15)));

					// Surrounding blocks

					if (i > 0) {
						dreamscape.setBlockState(paintingOrigin.up().offset(moveDir, i), CeruleanBlocks.MIMIC.getDefaultState());
						MimicBlockEntity.set(dreamscape.getBlockEntity(paintingOrigin.up().offset(moveDir, i)), c[3],  i - 1, moveDir);
						dreamscape.setBlockState(paintingOrigin.down(2).offset(moveDir, i), CeruleanBlocks.MIMIC.getDefaultState());
						MimicBlockEntity.set(dreamscape.getBlockEntity(paintingOrigin.down(2).offset(moveDir, i)), c[0],  i - 1, moveDir);

						for (int j = -1; j <= 0; j++) {
							dreamscape.setBlockState(otherA.up(j).offset(moveDir, i), CeruleanBlocks.MIMIC.getDefaultState());
							MimicBlockEntity.set(dreamscape.getBlockEntity(otherA.up(j).offset(moveDir, i)), a[j + 2],  i - 1, moveDir);
							dreamscape.setBlockState(otherB.up(j).offset(moveDir, i), CeruleanBlocks.MIMIC.getDefaultState());
							MimicBlockEntity.set(dreamscape.getBlockEntity(otherB.up(j).offset(moveDir, i)), b[j + 2],  i - 1, moveDir);
						}
					}
				}

				dreamscape.setBlockState(paintingOrigin.offset(moveDir, 20), CeruleanBlocks.MIMIC.getDefaultState());
				MimicBlockEntity.set(dreamscape.getBlockEntity(paintingOrigin.offset(moveDir, 20)), c[2],  20, moveDir);
				dreamscape.setBlockState(paintingOrigin.down().offset(moveDir, 20), CeruleanBlocks.MIMIC.getDefaultState());
				MimicBlockEntity.set(dreamscape.getBlockEntity(paintingOrigin.down().offset(moveDir, 20)), c[1],  20, moveDir);

				// Reset occupied state
				p.getSleepingPosition().filter(p.getWorld()::isChunkLoaded).ifPresent(pos -> {
					BlockState blockState = p.getWorld().getBlockState(pos);
					if (blockState.getBlock() instanceof BedBlock) {
						p.getWorld().setBlockState(pos, blockState.with(BedBlock.OCCUPIED, false), Block.NOTIFY_ALL);
					}
				});


				FabricDimensions.teleport(p, dreamscape, new TeleportTarget(p.getPos(), Vec3d.ZERO, p.getYaw(), p.getPitch()));
			}
		}
	}
}
