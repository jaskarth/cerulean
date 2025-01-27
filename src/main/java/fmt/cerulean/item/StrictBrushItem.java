package fmt.cerulean.item;

import java.util.Set;

import fmt.cerulean.block.base.Instructor;
import fmt.cerulean.block.base.Obedient;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StrictBrushItem extends Item {

	public StrictBrushItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockPos pos = context.getBlockPos();
		BlockEntity be = context.getWorld().getBlockEntity(pos);
		if (Obedient.willCede(be)) {
			explain(context.getWorld(), (Obedient) be);
			return ActionResult.SUCCESS;
		}
		return super.useOnBlock(context);
	}
	
	public static void explain(World world, Obedient obedient) {
		if (world.isClient) {
			MinecraftClient client = MinecraftClient.getInstance();
			Set<String> keys = obedient.cede().keySet();
			for (String key : keys) {
				((Instructor) client.inGameHud).recall(key);
			}
		}
	}
}
