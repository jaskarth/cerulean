package fmt.cerulean.item;

import fmt.cerulean.block.entity.WellBlockEntity;
import fmt.cerulean.client.particle.StarParticleType;
import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.flow.FlowResources;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.net.CeruleanNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class LorbItem extends Item {
	public LorbItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);

		user.getItemCooldownManager().set(this, 20);

		HitResult res = user.raycast(32, 1, false);
		if (world.isClient) {
			Vec3d start = user.getCameraPosVec(1);
			Vec3d end = res.getPos();

			double dist = start.distanceTo(end);
			Random random = world.random;
			FlowState state = new FlowState(FlowResources.star(FlowResource.Color.CHARTREUSE, FlowResource.Brightness.BRILLIANT), -1);
			for (int i = 0; i < dist * 2; i++) {
				double d = i / (dist * 2);
				double x = MathHelper.lerp(d, start.x, end.x);
				double y = MathHelper.lerp(d, start.y, end.y);
				double z = MathHelper.lerp(d, start.z, end.z);

				StarParticleType star = WellBlockEntity.createParticle(state.resource(), true, false, random);
				double vx = random.nextGaussian() * 0.02;
				double vy = random.nextGaussian() * 0.02;
				double vz = random.nextGaussian() * 0.02;

				world.addParticle(star, true, x, y, z, vx, vy, vz);
			}

			Box box = user.getBoundingBox().stretch(user.getRotationVec(1).multiply(32)).expand(1.0, 1.0, 1.0);
			EntityHitResult ehit = ProjectileUtil.raycast(user, start, end, box, entityx -> !entityx.isSpectator() && entityx.canHit(), 32);

			if (ehit != null) {
				PacketByteBuf buf = PacketByteBufs.create();
				buf.writeVarInt(ehit.getEntity().getId());
				ClientPlayNetworking.send(CeruleanNetworking.MAGIC_ATTACK, buf);
			}
		} else {
			if (!user.getAbilities().creativeMode) {
				stack.decrement(1);
			}
		}

		return TypedActionResult.success(stack, world.isClient());
	}
}
