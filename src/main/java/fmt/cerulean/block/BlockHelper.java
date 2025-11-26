package fmt.cerulean.block;

import fmt.cerulean.block.entity.WellBlockEntity;
import fmt.cerulean.client.particle.StarParticleType;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.flow.FlowUser;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BlockHelper {
	public static void emitStars(World world, BlockPos pos, int amt) {
		if (world.isClient) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof FlowUser user) {
				Random random = world.random;
				double x = pos.getX() + 0.5;
				double y = pos.getY() + 0.5;
				double z = pos.getZ() + 0.5;
				FlowState flow = user.getAnyFlow();
				if (!flow.empty()) {
					for (int i = 0; i < amt; i++) {
						StarParticleType star = WellBlockEntity.createParticle(flow.resource(), false, false, random);
						double vx = random.nextGaussian() * 0.12;
						double vy = random.nextGaussian() * 0.12;
						double vz = random.nextGaussian() * 0.12;

						world.addParticle(star, true, x, y, z, vx, vy, vz);
					}
				}
			}
		}
	}
}
