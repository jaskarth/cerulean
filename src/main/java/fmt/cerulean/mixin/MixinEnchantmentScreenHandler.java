package fmt.cerulean.mixin;

import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnchantmentScreenHandler.class)
public class MixinEnchantmentScreenHandler {
	// TODO: this injection point sucks
	@Redirect(method = "method_17411", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;calculateRequiredExperienceLevel(Lnet/minecraft/util/math/random/Random;IILnet/minecraft/item/ItemStack;)I"))
	private int cerulean$sortedBookshelfBoost(Random random, int slotIndex, int bookshelfCount, ItemStack stack, ItemStack wtf, World world, BlockPos pos) {
		int extra = 0;
		for(BlockPos blockPos : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
			if (world.getBlockState(pos.add(blockPos)).isOf(CeruleanBlocks.SORTED_BOOKSHELF)
					&& world.getBlockState(pos.add(blockPos.getX() / 2, blockPos.getY(), blockPos.getZ() / 2))
					.isIn(BlockTags.ENCHANTMENT_POWER_TRANSMITTER)) {
				++extra;
			}
		}

		return EnchantmentHelper.calculateRequiredExperienceLevel(random, slotIndex, bookshelfCount + extra, stack);
	}
}
