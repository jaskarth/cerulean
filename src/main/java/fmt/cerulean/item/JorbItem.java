package fmt.cerulean.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class JorbItem extends Item {
	public JorbItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);

		user.getItemCooldownManager().set(this, 8);
		if (world.isClient) {
			user.addVelocity(new Vec3d(0, 2, 0));
		} else {
			if (!user.getAbilities().creativeMode) {
				stack.decrement(1);
			}
		}

		return TypedActionResult.success(stack, world.isClient());
	}
}
