package fmt.cerulean.item;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class EyeOfMenderItem extends Item {

	public EyeOfMenderItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (!world.isClient) {
			Random random = world.random;
			for (int i = 0; i < 3 + random.nextInt(3); i++) {
				world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1f, 1.3f);
				int o = 3 + random.nextInt(5);
				Vec3d pos = user.getPos().add(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5);
				ExperienceOrbEntity.spawn((ServerWorld) world, pos, o);
			}
		}
		stack.decrement(1);
		return TypedActionResult.success(stack);
	}
}
