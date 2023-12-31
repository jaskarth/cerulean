package fmt.cerulean.world;

import fmt.cerulean.Cerulean;
import fmt.cerulean.block.entity.MimicBlockEntity;
import fmt.cerulean.registry.CeruleanBlocks;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.*;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.world.TeleportTarget;

import java.util.ArrayList;
import java.util.List;

public class DreamscapeTeleporter {
	private static class TeleportGroup {
		private BlockBox target;
		private List<ServerPlayerEntity> players = new ArrayList<>();
	}

	public static void handleTeleport(ServerWorld world) {
		List<TeleportGroup> group = new ArrayList<>();
		for (ServerPlayerEntity p : new ArrayList<>(world.getPlayers())) {
			if (p.isSleeping()) {
				TeleportGroup gp = new TeleportGroup();
				gp.players.add(p);
				gp.target = box(p.getBoundingBox().expand(6, 3, 6));
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
			BlockBox target = g.target;
			List<PaintingEntity> paintings = world.getEntitiesByClass(PaintingEntity.class, Box.from(target), e -> true);
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

			BlockBox expanded = target.expand(1);

			// Facade copy
			for (int x = expanded.getMinX(); x <= expanded.getMaxX(); x++) {
				for (int z = expanded.getMinZ(); z <= expanded.getMaxZ(); z++) {
					for (int y = expanded.getMinZ(); y <= expanded.getMaxY(); y++) {
						BlockPos local = new BlockPos(x, y, z);
						if (expanded.contains(local) && !target.contains(local)) {
							dreamscape.setBlockState(local, CeruleanBlocks.INKY_VOID.getDefaultState());
						} else {
							dreamscape.setBlockState(local, world.getBlockState(local));
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

			// Reset occupied state
			for (ServerPlayerEntity p : g.players) {
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
