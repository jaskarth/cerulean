package fmt.cerulean.flow.recipe;

import java.util.List;
import java.util.function.Predicate;

import fmt.cerulean.flow.FlowState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class PigmentInventory extends SimpleInventory {
	public final FlowState flow, opposing;
	public final World world;
	public final BlockPos pos;
	public int recipeProgress = 0;

	public PigmentInventory(FlowState flow, FlowState opposing, World world, BlockPos pos) {
		this(flow, opposing, world, pos, getItemEntities(world, pos));
	}

	public PigmentInventory(FlowState flow, FlowState opposing, World world, BlockPos pos, List<ItemEntity> entities) {
		super(entities.stream().map(e -> e.getStack()).toArray(i -> new ItemStack[i]));
		this.flow = flow;
		this.opposing = opposing;
		this.world = world;
		this.pos = pos;
	}

	private static List<ItemEntity> getItemEntities(World world, BlockPos pos) {
		Box box = Box.enclosing(pos, pos);
		return world.getEntitiesByClass(ItemEntity.class, box, e -> true);
	}

	public void killItems(Predicate<ItemStack> predicate, int amount) {
		int slain = 0;
		for (int i = 0; i < size(); i++) {
			if (slain >= amount) {
				return;
			}
			ItemStack stack = getStack(i);
			if (predicate.test(stack)) {
				int coveted = Math.min(amount, stack.getCount());
				stack.decrement(coveted);
				slain += coveted;
			}
		}
	}

	public void spawnResult(ItemStack stack) {
		ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, stack);
		world.spawnEntity(entity);
	}
}
