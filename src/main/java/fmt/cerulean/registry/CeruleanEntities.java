package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import fmt.cerulean.entity.MemoryFrameEntity;
import fmt.cerulean.entity.OxidationPotionEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CeruleanEntities {
	public static final EntityType<MemoryFrameEntity> MEMORY_FRAME = register("memory_frame",
			EntityType.Builder.create(	MemoryFrameEntity::new, SpawnGroup.MISC)
					.dimensions(0.5F, 0.5F)
					.maxTrackingRange(10)
					.trackingTickInterval(Integer.MAX_VALUE).build("memory_frame")
			);

	public static final EntityType<OxidationPotionEntity> OXIDATION_POTION = register("oxiation_potion",
			EntityType.Builder.create((EntityType.EntityFactory<OxidationPotionEntity>) OxidationPotionEntity::new, SpawnGroup.MISC)
					.dimensions(0.25F, 0.25F)
					.maxTrackingRange(4)
					.trackingTickInterval(10)
					.build("oxiation_potion")
	);

	public static void init() {

	}

	private static <T extends Entity> EntityType<T> register(String id, EntityType<T> type) {
		return Registry.register(Registries.ENTITY_TYPE, Cerulean.id(id), type);
	}
}
