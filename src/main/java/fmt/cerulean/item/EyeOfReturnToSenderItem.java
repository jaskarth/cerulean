package fmt.cerulean.item;

import fmt.cerulean.item.component.ReturnToSenderComponent;
import fmt.cerulean.registry.CeruleanItemComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

public class EyeOfReturnToSenderItem extends Item {

	public EyeOfReturnToSenderItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		ReturnToSenderComponent comp = stack.get(CeruleanItemComponents.RETURN_TO_SENDER);
		if (comp != null && comp.target().isPresent()) {
			GlobalPos pos = comp.target().get();
			if (pos.dimension().equals(world.getRegistryKey())) {
				if (!world.isClient) {
					double x = pos.pos().getX() + 0.5;
					double y = pos.pos().getY();
					double z = pos.pos().getZ() + 0.5;
					user.teleport(x, y, z, false);
					world.playSound(null, x, y, z, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1f, 1.5f);
					world.playSound(null, x, y, z, SoundEvents.ENTITY_PLAYER_TELEPORT, SoundCategory.BLOCKS, 0.6f, 0.8f);
				}
				stack.decrement(1);
				return TypedActionResult.success(stack);
			}
		}
		return super.use(world, user, hand);
	}
}
