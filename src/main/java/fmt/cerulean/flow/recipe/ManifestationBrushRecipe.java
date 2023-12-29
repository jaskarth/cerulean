package fmt.cerulean.flow.recipe;

import fmt.cerulean.flow.FlowResource.Color;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ManifestationBrushRecipe implements BrushRecipe {

	@Override
	public int getCraftTime() {
		return 30;
	}

	@Override
	public int getRequiredFlowInputs() {
		return 1;
	}

	@Override
	public boolean canCraft(PigmentInventory inventory) {
		return inventory.flow.resource().getColor() == Color.LILAC && inventory.containsAny(s -> s.getItem() instanceof BlockItem);
	}

	@Override
	public void craft(PigmentInventory inventory) {
		ItemStack stack = inventory.find(s -> s.getItem() instanceof BlockItem);
		if (!stack.isEmpty() && stack.getItem() instanceof BlockItem bi) {
			World world = inventory.world;
			for (int i = 1; i < 6; i++) {
				try {
					BlockPos pos = inventory.pos.offset(inventory.direction, i);
					ItemPlacementContext ctx = new ItemPlacementContext(world, null, Hand.MAIN_HAND, stack, new BlockHitResult(pos.toCenterPos(), Direction.UP, pos, false));
					if (!world.getBlockState(pos).canReplace(ctx) ) {
						continue;
					}
					ActionResult result = bi.place(ctx);
					if (result.isAccepted()) {
						return;
					}
				} catch (Exception e) {
					// oops!
				}
			}
		}
	}
}
