package fmt.cerulean.item;

import fmt.cerulean.client.screen.SolitaireScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CardsItem extends Item {

	public CardsItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (world.isClient) {
			MinecraftClient client = MinecraftClient.getInstance();
			client.setScreen(new SolitaireScreen());
		}
		return TypedActionResult.success(stack);
	}
}
