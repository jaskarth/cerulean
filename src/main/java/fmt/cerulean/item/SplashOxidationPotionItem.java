package fmt.cerulean.item;

import fmt.cerulean.entity.OxidationPotionEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class SplashOxidationPotionItem extends Item implements ProjectileItem {
	public SplashOxidationPotionItem(Item.Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (!world.isClient) {
			OxidationPotionEntity potionEntity = new OxidationPotionEntity(world, user);
			potionEntity.setItem(itemStack);
			potionEntity.setVelocity(user, user.getPitch(), user.getYaw(), -20.0F, 0.5F, 1.0F);
			world.spawnEntity(potionEntity);
		}

		world.playSound(
				null,
				user.getX(),
				user.getY(),
				user.getZ(),
				SoundEvents.ENTITY_SPLASH_POTION_THROW,
				SoundCategory.PLAYERS,
				0.5F,
				0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
		);

		user.incrementStat(Stats.USED.getOrCreateStat(this));
		itemStack.decrementUnlessCreative(1, user);
		return TypedActionResult.success(itemStack, world.isClient());
	}

	@Override
	public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
		OxidationPotionEntity potionEntity = new OxidationPotionEntity(world, pos.getX(), pos.getY(), pos.getZ());
		potionEntity.setItem(stack);
		return potionEntity;
	}

	@Override
	public ProjectileItem.Settings getProjectileSettings() {
		return ProjectileItem.Settings.builder()
				.uncertainty(ProjectileItem.Settings.DEFAULT.uncertainty() * 0.5F)
				.power(ProjectileItem.Settings.DEFAULT.power() * 1.25F)
				.build();
	}
}
