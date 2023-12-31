package fmt.cerulean.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MovrbItem extends Item {
	public MovrbItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);

		user.getItemCooldownManager().set(this, 8);
		if (world.isClient) {
			Vec3d rot = user.getRotationVec(1);
			user.addVelocity(rot.multiply(1.8).add(0, 0.15, 0));
		} else {
			if (!user.getAbilities().creativeMode) {
				stack.decrement(1);
			}
		}

		return TypedActionResult.success(stack, world.isClient());
	}
}
