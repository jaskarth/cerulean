package fmt.cerulean.mixin;

import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(AbstractBlock.Settings.class)
public interface BlockSettingsAccessor {
	@Accessor
	void setOffsetter(AbstractBlock.Offsetter offsetter);
}
