package fmt.cerulean.flow.recipe;

import fmt.cerulean.flow.FlowResource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParadigmBrushRecipe implements BrushRecipe{

	public final FlowResource.Color color;
	public final Block source;
	public final Block destination;
	public final ItemStack collateral;

	public ParadigmBrushRecipe(FlowResource.Color color, Block source, Block destination, ItemStack collateral) {
		this.color = color;
		this.source = source;
		this.destination = destination;
		this.collateral = collateral;
	}

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
		if (inventory.flow.resource().getColor() != color) {
			return false;
		}

		World world = inventory.world;
		for (int i = 0; i < 6; i++) {
			BlockPos pos = inventory.pos.offset(inventory.direction, i);
			if (world.getBlockState(pos).isOf(this.source)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void craft(PigmentInventory inventory) {
		World world = inventory.world;
		for (int i = 0; i < 6; i++) {
			BlockPos pos = inventory.pos.offset(inventory.direction, i);
			BlockState state = world.getBlockState(pos);
			if (state.isOf(this.source)) {
				BlockState dest = this.destination.getDefaultState();

				// Ugly hack
				for (Property prop : state.getProperties()) {
					if (dest.contains(prop)) {
						dest = dest.with(prop, state.get(prop));
					}
				}

				world.setBlockState(pos, dest);
				if (collateral != null) {
					Block.dropStack(world, pos, this.collateral.copy());
				}
				return;
			}
		}
	}
}
