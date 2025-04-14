package fmt.cerulean.world;

import fmt.cerulean.block.StrongboxBlock;
import fmt.cerulean.block.entity.MimicBlockEntity;
import fmt.cerulean.block.entity.StrongboxBlockEntity;
import fmt.cerulean.entity.MemoryFrameEntity;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.Conscious;
import fmt.cerulean.util.PaintingDuck;
import fmt.cerulean.world.data.CeruleanWorldState;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.border.WorldBorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DreamscapeTeleporter {
	private static final Direction[] HORIZONTAL = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

	private static class TeleportGroup {
		BlockPos origin;
		private BlockBox target;
		private List<ServerPlayerEntity> players = new ArrayList<>();
	}

	public static boolean isSpecialSleep(ServerPlayerEntity player) {
		BlockBox box = box(player.getBoundingBox().expand(10, 6, 10));
		List<PaintingEntity> paintings = player.getServerWorld().getEntitiesByClass(PaintingEntity.class, Box.from(box), e -> true);
		boolean found = false;

		for (PaintingEntity painting : paintings) {
			if (painting instanceof PaintingDuck duck && duck.manifestsInDreams()) {
				if (found) {
					// Found two portal paintings?
					found = false;
					break;
				}

				found = true;
			}
		}

		return found;
	}

	public static void handleTeleport(ServerWorld world) {
		List<TeleportGroup> group = new ArrayList<>();
		for (ServerPlayerEntity p : new ArrayList<>(world.getPlayers())) {
			if (p.isSleeping()) {
				TeleportGroup gp = new TeleportGroup();
				gp.players.add(p);
				gp.target = box(p.getBoundingBox().expand(10, 6, 10));
				gp.origin = p.getBlockPos();
				group.add(gp);
			}
		}

		while (true) {
			boolean changed = false;

			for (int i = 0; i < group.size(); i++) {
				TeleportGroup a = group.get(i);
				for (int j = i + 1; j < group.size(); j++) {
					TeleportGroup b = group.get(j);
					if (a.target.intersects(b.target)) {
						a.target = a.target.encompass(b.target);
						a.players.addAll(b.players);
						group.remove(j);
						changed = true;
						break;
					}
				}

				if (changed) {
					break;
				}
			}
			if (!changed) {
				break;
			}
		}

		for (TeleportGroup g : group) {
			BlockPos originPos = g.origin;
			BlockBox target = g.target;
			List<PaintingEntity> paintings = world.getEntitiesByClass(PaintingEntity.class, Box.from(target), e -> true);
			boolean found = false;

			PaintingEntity portalPainting = null;
			for (PaintingEntity painting : paintings) {
				if (painting instanceof PaintingDuck duck && duck.manifestsInDreams()) {
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

			PaintingDuck quack = (PaintingDuck) portalPainting;
			Vec3i dims = quack.getManifestationShape().getDimensions();
			if (dims.getX() > 5 && dims.getY() > 5 && dims.getZ() > 5) {
				target = quack.getManifestationShape().offset(originPos.getX(), originPos.getY(), originPos.getZ());
			}
			BlockPos teleportPos = quack.getTeleportTarget();

			int startX = target.getMinX();
			int startY = target.getMinY();
			int startZ = target.getMaxZ();

			WorldBorder border = world.getWorldBorder();
			int minX = (int) border.getBoundWest() + 25;
			int minZ = (int) border.getBoundNorth() + 25;
			int maxX = (int) border.getBoundEast() - target.getBlockCountX() - 25;
			int maxZ = (int) border.getBoundSouth() - target.getBlockCountZ() - 25;
			int minY = -48;
			int maxY = 280;

			int endX = world.random.nextInt(maxX - minX) + minX;
			int endY = world.random.nextInt(maxY - minY) + minY;
			int endZ = world.random.nextInt(maxZ - minZ) + minZ;

			int transX = endX - startX;
			int transY = endY - startY;
			int transZ = endZ - startZ;

			ServerWorld dreamscape = world.getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, CeruleanDimensions.DREAMSCAPE));

			if (dreamscape == null) {
				throw new RuntimeException("I closed my eyes but nothing appeared...");
			}

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
				a[j + 2] = world.getBlockState(otherA.up(j));
				b[j + 2] = world.getBlockState(otherB.up(j));
				c[j + 2] = world.getBlockState(paintingOrigin.up(j));
			}

			// Check support blocks
			boolean ok = true;
			for (int j = -2; j <= 1; j++) {
				// Ignore corners
				if (j != -2 && j != 1) {
					if (!a[j + 2].isOpaqueFullCube(world, otherA.up(j))) {
						ok = false;
						break;
					}

					if (!b[j + 2].isOpaqueFullCube(world, otherB.up(j))) {
						ok = false;
						break;
					}
				}

				if (!c[j + 2].isOpaqueFullCube(world, paintingOrigin.up(j))) {
					ok = false;
					break;
				}
			}

			if (!ok) {
				continue;
			}

			// reset with translated
			paintingOrigin = portalPainting.getBlockPos().add(transX, transY, transZ);
			moveDir = portalPainting.getHorizontalFacing().getOpposite();
			paintingOrigin = paintingOrigin.offset(moveDir);

			otherA = paintingOrigin.offset(moveDir.rotateYClockwise());
			otherB = paintingOrigin.offset(moveDir.rotateYCounterclockwise());

			BlockBox expanded = target.expand(1);

			// Facade copy
			for (int x = expanded.getMinX(); x <= expanded.getMaxX(); x++) {
				for (int z = expanded.getMinZ(); z <= expanded.getMaxZ(); z++) {
					for (int y = expanded.getMinY(); y <= expanded.getMaxY(); y++) {
						BlockPos oldPos = new BlockPos(x, y, z);
						BlockPos newPos = oldPos.add(transX, transY, transZ);
						if (expanded.contains(oldPos) && !target.contains(oldPos)) {
							dreamscape.setBlockState(newPos, CeruleanBlocks.INKY_VOID.getDefaultState(), Block.SKIP_DROPS);
						} else {
							dreamscape.setBlockState(newPos, filterState(world, oldPos), Block.SKIP_DROPS);
							handlePostState(dreamscape, world, oldPos, newPos);
						}
					}
				}
			}

			boolean alone = g.players.size() == 1;

			for (int i = 0; i < 20; i++) {
				// Central path
				dreamscape.setBlockState(paintingOrigin.offset(moveDir, i), Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, Math.min((i + 1) * 3, 15)), Block.SKIP_DROPS);
				dreamscape.setBlockState(paintingOrigin.down().offset(moveDir, i), Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, Math.min((i + 1) * 3, 15)), Block.SKIP_DROPS);

				// Surrounding blocks

				if (i > 0) {
					place(dreamscape, paintingOrigin.up().offset(moveDir, i), c[3], i - 1, moveDir, alone);
					place(dreamscape, paintingOrigin.down(2).offset(moveDir, i), c[0], i - 1, moveDir, alone);

					for (int j = -1; j <= 0; j++) {
						place(dreamscape, otherA.up(j).offset(moveDir, i), a[j + 2], i - 1, moveDir, alone);
						place(dreamscape, otherB.up(j).offset(moveDir, i), b[j + 2], i - 1, moveDir, alone);
					}
				}
			}

			place(dreamscape, paintingOrigin.offset(moveDir, 20), c[2],  20, moveDir, alone);
			place(dreamscape, paintingOrigin.down().offset(moveDir, 20), c[1], 20, moveDir, alone);

			CeruleanWorldState worldState = CeruleanWorldState.get(world);

			boolean spawnMemory = false;
			for (ServerPlayerEntity player : g.players) {
				spawnMemory |= (worldState.getFor(player).worldworn && !worldState.getFor(player).truthful);
			}

			if (spawnMemory) {
				List<BlockPos> memoryLocations = new ArrayList<>();
				for (int x = -8; x <= 8; x++) {
					for (int z = -8; z <= 8; z++) {
						memoryLocations.add(originPos.add(transX, transY, transZ).add(x, 1, z));
					}
				}
				Collections.shuffle(memoryLocations);


				BlockPos targetPos = originPos.add(transX, transY, transZ).up();

				List<Pair<BlockPos, Direction>> candidates = new ArrayList<>();

				for (BlockPos memoryLocation : memoryLocations) {
					for (Direction d : HORIZONTAL) {
						BlockPos local = memoryLocation.offset(d);

						if (local.isWithinDistance(originPos.add(transX, transY, transZ), 3)) {
							continue;
						}

						if (local.isWithinDistance(paintingOrigin, 2)) {
							continue;
						}

						BlockState state = dreamscape.getBlockState(memoryLocation);
						if (dreamscape.isAir(local)
								&& state.isSideSolidFullSquare(dreamscape, memoryLocation, d)
								&& !(state.getBlock() instanceof BlockEntityProvider)
								&& dreamscape.getLightLevel(local) <= 12
								&& dreamscape.getLightLevel(local) >= 7
						) {
							BlockHitResult res = dreamscape.raycast(new RaycastContext(local.toCenterPos().offset(d, 1.0E-5F),
									targetPos.toCenterPos(), RaycastContext.ShapeType.VISUAL,
									RaycastContext.FluidHandling.NONE, ShapeContext.absent()));

							if (res.getBlockPos().equals(targetPos)) {
								candidates.add(new Pair<>(local, d));
							}
						}
					}
				}

				if (!candidates.isEmpty()) {
					Pair<BlockPos, Direction> v = candidates.getFirst();

					dreamscape.spawnEntity(MemoryFrameEntity.place(dreamscape, v.getLeft(), v.getRight()));
				}
			}

			// Reset occupied state
			for (ServerPlayerEntity p : g.players) {
				p.getSleepingPosition().filter(p.getWorld()::isChunkLoaded).ifPresent(pos -> {
					BlockState blockState = p.getWorld().getBlockState(pos);
					if (blockState.getBlock() instanceof BedBlock) {
						p.getWorld().setBlockState(pos, blockState.with(BedBlock.OCCUPIED, false), Block.NOTIFY_ALL);
					}
				});

				((Conscious)p).cerulean$setTeleportTarget(teleportPos);

				p.teleportTo(new TeleportTarget(dreamscape, p.getPos().add(transX, transY, transZ), Vec3d.ZERO, p.getYaw(), p.getPitch(), e -> {}));
			}
		}
	}

	private static BlockState filterState(ServerWorld world, BlockPos local) {
		BlockState st = world.getBlockState(local);
		if (st.isOf(Blocks.PISTON) || st.isOf(Blocks.MOVING_PISTON) || st.isOf(Blocks.STICKY_PISTON) || st.isOf(Blocks.PISTON_HEAD)) {
			return Blocks.AIR.getDefaultState();
		}

		if (st.isOf(CeruleanBlocks.STRONGBOX)) {
			return CeruleanBlocks.STRONGBOX.getDefaultState().with(StrongboxBlock.WEAK, true);
		}

		return st;
	}

	private static void handlePostState(ServerWorld newWorld, ServerWorld oldWorld, BlockPos oldPos, BlockPos newPos) {
		BlockEntity be = newWorld.getBlockEntity(newPos);
		if (be instanceof StrongboxBlockEntity strongbox) {
			strongbox.originalWorld = oldWorld.getRegistryKey();
			strongbox.originalBlockPos = oldPos;
			strongbox.markDirty();
		}
	}

	private static void place(ServerWorld world, BlockPos pos, BlockState state, int dist, Direction facing, boolean alone) {
		world.setBlockState(pos, CeruleanBlocks.MIMIC.getDefaultState(), Block.SKIP_DROPS);
		MimicBlockEntity.set(world.getBlockEntity(pos), state, dist, facing, alone);
	}

	private static BlockBox box(Box box) {
		return new BlockBox(
				MathHelper.floor(box.minX),
				MathHelper.floor(box.minY),
				MathHelper.floor(box.minZ),
				MathHelper.floor(box.maxX),
				MathHelper.floor(box.maxY),
				MathHelper.floor(box.maxZ)
		);
	}
}
