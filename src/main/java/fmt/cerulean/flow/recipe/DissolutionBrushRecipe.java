package fmt.cerulean.flow.recipe;

import fmt.cerulean.flow.FlowResource.Color;
import fmt.cerulean.item.PetrichorItem;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.List;

public class DissolutionBrushRecipe implements BrushRecipe {
    public final Color color;
    public final Block block;
    public final boolean advanced;

    public DissolutionBrushRecipe(Color color, Block block, boolean advanced) {
        this.color = color;
        this.block = block;
        this.advanced = advanced;
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

        if (!inventory.containsAll(List.of(Ingredient.ofItems(CeruleanItems.PETRICHOR)))) {
            return false;
        }

        World world = inventory.world;
        for (int i = 0; i < 6; i++) {
            BlockPos pos = inventory.pos.offset(inventory.direction, i);
            BlockState state = world.getBlockState(pos);
            if (state.isOf(this.block)) {
                return true;
            } else if (state.isSolidBlock(world, pos)) {
                return false;
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
            if (state.isOf(this.block)) {
                PetrichorItem.useOnGround(world, pos);
                world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, pos, 15);
                inventory.killItems(Ingredient.ofItems(CeruleanItems.PETRICHOR), 1);

                return;
            } else if (state.isSolidBlock(world, pos)) {
                return;
            }
        }
    }
}
