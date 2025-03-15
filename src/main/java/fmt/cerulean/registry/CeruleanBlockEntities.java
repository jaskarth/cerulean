package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import fmt.cerulean.block.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CeruleanBlockEntities {
	public static final BlockEntityType<WellBlockEntity> WELL = register("well", WellBlockEntity::new, CeruleanBlocks.STAR_WELL);
	public static final BlockEntityType<PipeBlockEntity> PIPE = register("pipe", PipeBlockEntity::new,
			CeruleanBlocks.PIPE, CeruleanBlocks.EXPOSED_PIPE, CeruleanBlocks.WEATHERED_PIPE, CeruleanBlocks.OXIDIZED_PIPE,
			CeruleanBlocks.WAXED_PIPE, CeruleanBlocks.WAXED_EXPOSED_PIPE, CeruleanBlocks.WAXED_WEATHERED_PIPE, CeruleanBlocks.WAXED_OXIDIZED_PIPE,
			CeruleanBlocks.FUCHSIA_PIPE, CeruleanBlocks.LUSTROUS_PIPE, CeruleanBlocks.DUCTILE_PIPE, CeruleanBlocks.CHIMERIC_PIPE,
			CeruleanBlocks.IRON_VALVE, CeruleanBlocks.CHIMERIC_VALVE);
	public static final BlockEntityType<MimicBlockEntity> MIMIC = register("mimic", MimicBlockEntity::new, CeruleanBlocks.MIMIC);
	public static final BlockEntityType<FauxBlockEntity> FAUX = register("faux", FauxBlockEntity::new, CeruleanBlocks.FAUX);
	public static final BlockEntityType<StrongboxBlockEntity> STRONGBOX = register("strongbox", StrongboxBlockEntity::new, CeruleanBlocks.STRONGBOX);
	public static final BlockEntityType<SelfCollapsingCubeEntity> SELF_COLLAPSING_CUBE = register("self_collapsing_cube", SelfCollapsingCubeEntity::new, CeruleanBlocks.SELF_COLLAPSING_CUBE);
	public static final BlockEntityType<AddressPlaqueBlockEntity> ADDRESS_PLAQUE = register("address_plaque", AddressPlaqueBlockEntity::new, CeruleanBlocks.ADDRESS_PLAQUE);
	public static final BlockEntityType<FlagBlockEntity> FLAG = register("flag", FlagBlockEntity::new, CeruleanBlocks.FLAG);
	public static final BlockEntityType<ItemDetectorBlockEntity> ITEM_DETECTOR = register("item_detector", ItemDetectorBlockEntity::new, CeruleanBlocks.ITEM_DETECTOR);
	public static final BlockEntityType<PipeDetectorBlockEntity> PIPE_DETECTOR = register("pipe_detector", PipeDetectorBlockEntity::new, CeruleanBlocks.PIPE_DETECTOR);
	public static final BlockEntityType<MirageBlockEntity> MIRAGE = register("mirage", MirageBlockEntity::new, CeruleanBlocks.MIRAGE);
	public static final BlockEntityType<ProjectorBlockEntity> PROJECTOR = register("projector", ProjectorBlockEntity::new, CeruleanBlocks.PROJECTOR);

	public static void init() {
	}
	
	private static <T extends BlockEntity> BlockEntityType<T> register(String path, FabricBlockEntityTypeBuilder.Factory<T> function, Block... blocks) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, Cerulean.id(path), FabricBlockEntityTypeBuilder.create(function, blocks).build());
	}
}
