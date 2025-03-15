package fmt.cerulean.entity;

import fmt.cerulean.registry.CeruleanEntities;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.Optional;

public class OxidationPotionEntity extends ThrownItemEntity implements FlyingItemEntity {
	public OxidationPotionEntity(EntityType<? extends OxidationPotionEntity> entityType, World world) {
		super(entityType, world);
	}

	public OxidationPotionEntity(World world, LivingEntity owner) {
		super(CeruleanEntities.OXIDATION_POTION, owner, world);
	}

	public OxidationPotionEntity(World world, double x, double y, double z) {
		super(CeruleanEntities.OXIDATION_POTION, x, y, z, world);
	}

	@Override
	protected Item getDefaultItem() {
		return CeruleanItems.SPLASH_OXIDATION_POTION;
	}

	@Override
	protected double getGravity() {
		return 0.05;
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);

		if (!this.getWorld().isClient) {
			BlockPos pos = blockHitResult.getBlockPos();

			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					for (int y = -1; y <= 1; y++) {
						int manhattan = Math.abs(x) + Math.abs(z) + Math.abs(y);
						double chance;
						if (manhattan == 0) {
							chance = 1;
						} else if (manhattan == 1) {
							chance = 0.75;
						} else {
							chance = 0.5;
						}

						applyOxidation(pos.add(x, y, z), chance);
					}
				}
			}
		}
	}

	@Override
	protected void onCollision(HitResult hitResult) {
		super.onCollision(hitResult);

		if (!this.getWorld().isClient) {
			this.getWorld().syncWorldEvent(WorldEvents.SPLASH_POTION_SPLASHED, this.getBlockPos(), 0xFF4c9484);
			this.discard();
		}
	}

	private void applyOxidation(BlockPos pos, double chance) {
		if (random.nextDouble() < chance) {
			BlockState state = getWorld().getBlockState(pos);
			Optional<Block> incr = Oxidizable.getIncreasedOxidationBlock(state.getBlock());
			incr.ifPresent(block -> getWorld().setBlockState(pos, block.getStateWithProperties(state), Block.NOTIFY_LISTENERS));
		}
	}
}
