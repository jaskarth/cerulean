package fmt.cerulean.flow.recipe;

import fmt.cerulean.block.RockCandyBlock;
import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ParadigmBrushRecipe implements BrushRecipe {

	public final FlowResource.Color color;
	public final Block source;
	public final Block destination;
	public final ItemStack collateral;
	public final ItemStack principal;

	public ParadigmBrushRecipe(FlowResource.Color color, Block source, Block destination, ItemStack collateral) {
		this(color, source, destination, ItemStack.EMPTY, collateral);
	}

    public ParadigmBrushRecipe(FlowResource.Color color, Block source, Block destination, ItemStack principal, ItemStack collateral) {
        this.color = color;
        this.source = source;
        this.destination = destination;
        this.collateral = collateral;
        this.principal = principal;
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

        if (!principal.isEmpty()) {
            if (!inventory.containsAll(List.of(Ingredient.ofStacks(principal.copy())))) {
                return false;
            }
        }

		World world = inventory.world;
		for (int i = 0; i < 6; i++) {
			BlockPos pos = inventory.pos.offset(inventory.direction, i);
			if (isOurState(world.getBlockState(pos))) {
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
			if (isOurState(state)) {
				BlockState dest = this.destination.getDefaultState();

				// Ugly hack
				for (Property prop : state.getProperties()) {
					if (dest.contains(prop)) {
						dest = dest.with(prop, state.get(prop));
					}
				}

				world.setBlockState(pos, dest);
				if (!collateral.isEmpty()) {
					Block.dropStack(world, pos, this.collateral.copy());
				}
                if (!principal.isEmpty()) {
                    inventory.killItems(Ingredient.ofStacks(principal.copy()), principal.getCount());
                }
				return;
			}
		}
	}

    public BlockState getEffectiveSource() {
        BlockState state = this.source.getDefaultState();
        if (state.isOf(CeruleanBlocks.ROCK_CANDY)) {
            return state.with(RockCandyBlock.STAGE, 3);
        }

        return state;
    }

    private boolean isOurState(BlockState state) {
        if (state.isOf(this.source)) {
            // A temporary respite...
            if (this.source == CeruleanBlocks.ROCK_CANDY) {
                return state.get(RockCandyBlock.STAGE) == 3;
            }

            return true;
        }
        return false;
    }
}
