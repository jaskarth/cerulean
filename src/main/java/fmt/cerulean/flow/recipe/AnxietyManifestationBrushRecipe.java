package fmt.cerulean.flow.recipe;

import fmt.cerulean.flow.FlowResource.Color;
import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AnxietyManifestationBrushRecipe implements BrushRecipe {

	@Override
	public int getCraftTime() {
		return 40;
	}

	@Override
	public int getRequiredFlowInputs() {
		return 1;
	}

	@Override
	public boolean canCraft(PigmentInventory inventory) {
		return inventory.flow.resource().getColor() == Color.VIRIDIAN && inventory.containsAny(s -> s.getItem() instanceof MiningToolItem);
	}

	@Override
	public void craft(PigmentInventory inventory) {
		ItemStack stack = inventory.find(s -> s.getItem() instanceof MiningToolItem);
		if (!stack.isEmpty() && stack.getItem() instanceof MiningToolItem ti) {
			inventory.keepAlive(stack);
			World world = inventory.world;
			for (int i = 1; i < 6; i++) {
				try {
					BlockPos pos = inventory.pos.offset(inventory.direction, i);
					BlockState state = world.getBlockState(pos);
					if (!ignoreStateForBreaking(state) && ti.isCorrectForDrops(stack, state)) {
						world.breakBlock(pos, true);
						stack.damage(1, (ServerWorld) world, null, u -> {});
						return;
					}
				} catch (Exception e) {
					// oops!
				}
			}
		}
	}

	private static boolean ignoreStateForBreaking(BlockState state) {
		if (state.isOf(CeruleanBlocks.HALITE_OUTCROPPING_SMALL) || state.isOf(CeruleanBlocks.HALITE_OUTCROPPING_MEDIUM) || state.isOf(CeruleanBlocks.HALITE_OUTCROPPING_LARGE)) {
			return true;
		}

		if (state.isOf(Blocks.COCOA) && state.get(CocoaBlock.AGE) != 2) {
			return true;
		}

		return state.getBlock() instanceof SaplingBlock;
	}
}
