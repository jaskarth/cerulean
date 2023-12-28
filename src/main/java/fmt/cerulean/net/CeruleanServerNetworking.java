package fmt.cerulean.net;

import fmt.cerulean.block.entity.MimicBlockEntity;
import fmt.cerulean.registry.CeruleanBlocks;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CeruleanServerNetworking {
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(CeruleanNetworking.CLOSE_BEHIND, (s, p, h, buf, rs) -> {
			s.execute(() -> {
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

						world.setBlockState(oOrigin.up().offset(moveDir, i), CeruleanBlocks.MIMIC.getDefaultState());
						MimicBlockEntity.set(world.getBlockEntity(oOrigin.up().offset(moveDir, i)), c[3], ndist, moveDir, true);
						world.setBlockState(oOrigin.down(2).offset(moveDir, i), CeruleanBlocks.MIMIC.getDefaultState());
						MimicBlockEntity.set(world.getBlockEntity(oOrigin.down(2).offset(moveDir, i)), c[0], ndist, moveDir, true);

						for (int j = -1; j <= 0; j++) {
							world.setBlockState(otherA.up(j).offset(moveDir, i), CeruleanBlocks.MIMIC.getDefaultState());
							MimicBlockEntity.set(world.getBlockEntity(otherA.up(j).offset(moveDir, i)), a[j + 2], ndist, moveDir, true);
							world.setBlockState(otherB.up(j).offset(moveDir, i), CeruleanBlocks.MIMIC.getDefaultState());
							MimicBlockEntity.set(world.getBlockEntity(otherB.up(j).offset(moveDir, i)), b[j + 2], ndist, moveDir, true);
						}
					}

					world.setBlockState(oOrigin.offset(moveDir, amt), CeruleanBlocks.MIMIC.getDefaultState());
					MimicBlockEntity.set(world.getBlockEntity(oOrigin.offset(moveDir, amt)), c[2], 20, moveDir, true);
					world.setBlockState(oOrigin.down().offset(moveDir, amt), CeruleanBlocks.MIMIC.getDefaultState());
					MimicBlockEntity.set(world.getBlockEntity(oOrigin.down().offset(moveDir, amt)), c[1], 20, moveDir, true);
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
