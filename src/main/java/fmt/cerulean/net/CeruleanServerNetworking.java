package fmt.cerulean.net;

import fmt.cerulean.block.base.Obedient;
import fmt.cerulean.block.entity.MimicBlockEntity;
import fmt.cerulean.item.component.ColorTriplex;
import fmt.cerulean.item.component.PhotoComponent;
import fmt.cerulean.net.packet.*;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.world.data.PhotoMeta;
import fmt.cerulean.world.data.PhotoState;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;

import java.util.HashSet;
import java.util.Set;

public class CeruleanServerNetworking {
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(MagicAttackPacket.ID, (payload, ctx) -> {
			ctx.server().execute(() -> {
				Entity entity = ctx.player().getServerWorld().getEntityById(payload.id());
				entity.damage(entity.getDamageSources().magic(), 12);
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(CloseBehindPacket.ID, (payload, ctx) -> {
			ctx.server().execute(() -> {
				ServerPlayerEntity p = ctx.player();
				ServerWorld world = p.getServerWorld();

				BlockPos pos = p.getLandingPos();
				BlockEntity be = world.getBlockEntity(pos);
				if (be instanceof MimicBlockEntity mbe) {
					Direction dir = mbe.facing;
					int dist = mbe.dist;

					Direction moveDir = dir.getOpposite();

					BlockPos oOrigin = pos.up(2);
					BlockPos otherA = oOrigin.offset(moveDir.rotateYClockwise());
					BlockPos otherB = oOrigin.offset(moveDir.rotateYCounterclockwise());

					BlockState[] a = new BlockState[4];
					BlockState[] c = new BlockState[4];
					BlockState[] b = new BlockState[4];

					for (int j = -2; j <= 1; j++) {
						a[j + 2] = state(world, otherA.up(j));
						b[j + 2] = state(world, otherB.up(j));
						c[j + 2] = state(world, oOrigin.up(j));
					}

					int amt = 20 - dist;
					amt = Math.max(amt, 5);
					for (int i = 1; i < amt; i++) {
						int ndist = dist + i;

						world.setBlockState(oOrigin.offset(moveDir, i), Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, Math.min(ndist * 3, 15)));
						world.setBlockState(oOrigin.down().offset(moveDir, i), Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, Math.min(ndist * 3, 15)));

						world.setBlockState(oOrigin.up().offset(moveDir, i), CeruleanBlocks.MIMIC.getDefaultState(), Block.FORCE_STATE);
						MimicBlockEntity.set(world.getBlockEntity(oOrigin.up().offset(moveDir, i)), c[3], ndist, moveDir, true);
						world.setBlockState(oOrigin.down(2).offset(moveDir, i), CeruleanBlocks.MIMIC.getDefaultState(), Block.FORCE_STATE);
						MimicBlockEntity.set(world.getBlockEntity(oOrigin.down(2).offset(moveDir, i)), c[0], ndist, moveDir, true);

						for (int j = -1; j <= 0; j++) {
							world.setBlockState(otherA.up(j).offset(moveDir, i), CeruleanBlocks.MIMIC.getDefaultState(), Block.FORCE_STATE);
							MimicBlockEntity.set(world.getBlockEntity(otherA.up(j).offset(moveDir, i)), a[j + 2], ndist, moveDir, true);
							world.setBlockState(otherB.up(j).offset(moveDir, i), CeruleanBlocks.MIMIC.getDefaultState(), Block.FORCE_STATE);
							MimicBlockEntity.set(world.getBlockEntity(otherB.up(j).offset(moveDir, i)), b[j + 2], ndist, moveDir, true);
						}
					}

					world.setBlockState(oOrigin.offset(moveDir, amt), CeruleanBlocks.MIMIC.getDefaultState(), Block.FORCE_STATE);
					MimicBlockEntity.set(world.getBlockEntity(oOrigin.offset(moveDir, amt)), c[2], 20, moveDir, true);
					world.setBlockState(oOrigin.down().offset(moveDir, amt), CeruleanBlocks.MIMIC.getDefaultState(), Block.FORCE_STATE);
					MimicBlockEntity.set(world.getBlockEntity(oOrigin.down().offset(moveDir, amt)), c[1], 20, moveDir, true);
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(InfluencePacket.ID, (payload, ctx) -> {
			ctx.server().execute(() -> {
				ServerWorld world = ctx.player().getServerWorld();
				payload.target().ifLeft(pos -> {
					BlockState state = world.getBlockState(pos);
					Block block = state.getBlock();
					if (Obedient.willCede(block)) {
						ctx.player().sendMessage(Obedient.cede((Obedient) block, payload.intuition()), true);
					} else {
						BlockEntity be = world.getBlockEntity(pos);
						if (Obedient.willCede(be)) {
							ctx.player().sendMessage(Obedient.cede((Obedient) be, payload.intuition()), true);
						}
					}
				}).ifRight(entityId -> {
					Entity entity = world.getEntityById(entityId);
					if (Obedient.willCede(entity)) {
						ctx.player().sendMessage(Obedient.cede((Obedient) entity, payload.intuition()), true);
					}
				});

			});
		});

		ServerPlayNetworking.registerGlobalReceiver(UploadMemoryPacket.ID, (payload, ctx) -> {
			byte[] data = payload.data();
			float yaw = payload.yaw();
			float pitch = payload.pitch();

			ctx.server().execute(() -> {
				ServerPlayerEntity player = ctx.player();

				int idx = player.getInventory().indexOf(new ItemStack(CeruleanItems.FILM));
				if (idx != -1) {
					player.getInventory().getStack(idx).decrement(1);
				}

				PhotoState photos = PhotoState.get(player.getServerWorld());
				player.getItemCooldownManager().set(CeruleanItems.CAMERA, 30);
				if (photos.canAllocFor(player)) {
					int id = photos.allocNextId(player);

					Vec3d vec = Vec3d.fromPolar(new Vec2f(pitch, yaw));
					Vec3d cursor = player.getBlockPos().toCenterPos();

					// It's fast. I promise.
					LongOpenHashSet set = new LongOpenHashSet();
					Set<BlockPos> pos = new HashSet<>();
					for (int i = 0; i <= 10; i++) {
						cursor = cursor.add(vec.multiply(3));

						int r = (int) MathHelper.clampedMap(i / 10.0, 0, 1, 4, 15);

						for (int x = -r; x <= r; x++) {
							for (int z = -r; z <= r; z++) {
								for (int y = -r; y <= r; y++) {
									BlockPos local = BlockPos.ofFloored(cursor).add(x, y, z);
									long l = local.asLong();
									if (set.add(l)) {
										if (player.getWorld().getBlockState(local).isOf(CeruleanBlocks.LUSTROUS_BLOCK)) {
											pos.add(local);
										}
									}
								}
							}
						}
					}

					PhotoMeta meta = new PhotoMeta();
					meta.id = id;
					meta.fulfilled = false;
					BlockBox encompass = BlockBox.encompassPositions(pos).orElse(null);
					if (encompass != null) {
						meta.neededBox = encompass;
					}
					meta.positions.addAll(pos);

					photos.add(id, data, meta);


					ItemStack stack = new ItemStack(CeruleanItems.PHOTONEGATIVE);
					stack.set(CeruleanItemComponents.PHOTO, PhotoComponent.create(id));
					stack.set(CeruleanItemComponents.COLOR_TRIPLEX, ColorTriplex.empty());

					if (!player.giveItemStack(stack)) {
						player.dropItem(stack, false);
					}
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(RequestMemoryPacket.ID, (payload, ctx) -> {
			int id = payload.id();

			ctx.server().execute(() -> {
				ServerPlayerEntity player = ctx.player();

				PhotoState photos = PhotoState.get(player.getServerWorld());
				byte[] data = photos.getStore().get(id);
				System.out.println("Supplying id " + id);
				if (data != null) {
					ServerPlayNetworking.send(player, new SupplyMemoryPacket(id, data));
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(StaringPacket.ID, (payload, ctx) -> {
			int id = payload.id();
			float yaw = payload.yaw();
			float pitch = payload.pitch();

			ctx.server().execute(() -> {
				ServerPlayerEntity player = ctx.player();

				PhotoState photos = PhotoState.get(player.getServerWorld());

				PhotoMeta meta = photos.getMeta(id);

				if (meta != null && !meta.fulfilled && !meta.positions.isEmpty()) {
					Vec3d rotVec = Vec3d.fromPolar(new Vec2f(pitch, yaw)).normalize();
					Vec3d center = meta.neededBox.getCenter().toCenterPos();
					Vec3d dist = new Vec3d(center.x - player.getX(), center.y - player.getEyeY(), center.z - player.getZ());
					double length = dist.length();
					dist = dist.normalize();
					double dot = rotVec.dotProduct(dist);
					Vec3i dim = meta.neededBox.getDimensions();
					// TODO: addend depends on FOV!
					double dx = dim.getX() + 1.5;
					double dy = dim.getY() + 1.5;
					double dz = dim.getZ() + 1.5;
					double angular = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
					double size = 0.01;
					if (length > angular && length < 20 && (dot > (1.0 - (size * length)))) {
//						System.out.println("e = " + dot + ", l = " + length + ", d = " + (size * length) +
//								", md = " + (1.0 - (size * length) + ", a =" + angular + ", b = " + (dot > (1.0 - (size * length)))));

						for (BlockPos pos : meta.positions) {
							if (player.getWorld().getBlockState(pos).isOf(CeruleanBlocks.LUSTROUS_BLOCK)) {
								player.getWorld().setBlockState(pos, CeruleanBlocks.DUCTILE_BLOCK.getDefaultState());
							}
						}
						meta.fulfilled = true;
						photos.markDirty();
					}
				}
			});
		});
	}

	private static BlockState state(ServerWorld world, BlockPos pos) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof MimicBlockEntity mbe) {
			return mbe.state;
		}

		return Blocks.BEDROCK.getDefaultState();
	}
}
