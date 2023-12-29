package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import fmt.cerulean.block.entity.MimicBlockEntity;
import fmt.cerulean.block.entity.PipeBlockEntity;
import fmt.cerulean.block.entity.WellBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CeruleanBlockEntities {
	public static final BlockEntityType<WellBlockEntity> WELL = register("well", WellBlockEntity::new, CeruleanBlocks.STAR_WELL);
	public static final BlockEntityType<PipeBlockEntity> PIPE = register("pipe", PipeBlockEntity::new, CeruleanBlocks.PIPE);
	public static final BlockEntityType<MimicBlockEntity> MIMIC = register("mimic", MimicBlockEntity::new, CeruleanBlocks.MIMIC);

	public static void init() {
	}
	
	private static <T extends BlockEntity> BlockEntityType<T> register(String path, FabricBlockEntityTypeBuilder.Factory<T> function, Block... blocks) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, Cerulean.id(path), FabricBlockEntityTypeBuilder.create(function, blocks).build());
	}
}
