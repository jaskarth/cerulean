package fmt.cerulean.item;

import fmt.cerulean.block.entity.WellBlockEntity;
import fmt.cerulean.client.particle.StarParticleType;
import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.flow.FlowResources;
import fmt.cerulean.flow.FlowState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class KorbItem extends Item {
	public KorbItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);

		user.getItemCooldownManager().set(this, 20);
		if (!world.isClient) {
			if (!user.getAbilities().creativeMode) {
				stack.decrement(1);
			}

			Box box = user.getBoundingBox().expand(4.0, 2.0, 4.0);
			List<LivingEntity> list = world.getNonSpectatingEntities(LivingEntity.class, box);
			list.removeIf(l -> l instanceof PlayerEntity);
			for (LivingEntity e : list) {
				if (e.isAffectedBySplashPotions()) {
					e.damage(e.getDamageSources().magic(), 6);
				}
			}
		} else {
			Vec3d center = user.getBoundingBox().getCenter();
			Random random = world.getRandom();

			double x = center.x;
			double y = center.y;
			double z = center.z;
			// TODO: should spawn on the server!
			FlowState state = new FlowState(FlowResources.star(FlowResource.Color.LILAC, FlowResource.Brightness.BRILLIANT), -1);
			for (int i = 0; i < 200; i++) {
				StarParticleType star = WellBlockEntity.createParticle(state, false, random);
				double vx = random.nextGaussian() * 0.15;
				double vy = random.nextGaussian() * 0.15;
				double vz = random.nextGaussian() * 0.15;

				world.addParticle(star, true, x, y, z, vx, vy, vz);
			}
		}

		return TypedActionResult.success(stack, world.isClient());
	}
}
