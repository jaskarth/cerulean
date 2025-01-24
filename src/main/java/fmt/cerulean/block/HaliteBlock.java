package fmt.cerulean.block;

import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HaliteBlock extends Block {
	public HaliteBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (stack.isIn(ItemTags.SWORDS)) {
			world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
			stack.damage(1, player, LivingEntity.getSlotForHand(hand));
			world.setBlockState(pos, CeruleanBlocks.SLASHED_HALITE_BLOCK.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW);
		}

		return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
	}
}
