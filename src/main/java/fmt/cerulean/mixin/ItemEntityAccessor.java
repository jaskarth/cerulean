package fmt.cerulean.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.ItemEntity;

@Mixin(ItemEntity.class)
public interface ItemEntityAccessor {
	
	@Accessor("itemAge")
	void setItemAge(int itemAge);
}
