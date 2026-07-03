package fmt.cerulean.item;

import fmt.cerulean.block.CeruleanPlantBlock;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PetrichorItem extends Item {
	public PetrichorItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		BlockPos blockPos2 = blockPos.offset(context.getSide());

		BlockState blockState = world.getBlockState(blockPos);
		boolean bl = blockState.isOf(CeruleanBlocks.SPACEROCK) || blockState.isOf(CeruleanBlocks.COBBLED_ROCK_CANDY);
		if (bl && useOnGround(world, blockPos2)) {
			if (!world.isClient) {
				context.getStack().decrementUnlessCreative(1, context.getPlayer());
				context.getPlayer().emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
				world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, blockPos2, 15);
			}

			return ActionResult.success(world.isClient);
		} else {
			return ActionResult.PASS;
		}
	}

	public static boolean useOnGround(World world, BlockPos pos) {
		if (world.isClient) {
			return true;
		}

		boolean placed = false;

		Random random = world.random;

		List<Direction> ds = new ArrayList<>(Arrays.asList(Util.DIRECTIONS));

		for (int i = 0; i < 128; i++) {
			int dist = MathHelper.clamp((i / 16) + 3, 0, 8);
			BlockPos local = pos.add(
					random.nextInt(dist) - random.nextInt(dist),
					random.nextInt(dist) - random.nextInt(dist),
					random.nextInt(dist) - random.nextInt(dist)
			);


			BlockState curState = world.getBlockState(local);
			for (Direction d : ds) {
                BlockState offset = world.getBlockState(local.offset(d));
                if (curState.isAir() || curState.isOf(CeruleanBlocks.POLYETHYLENE)) {
                    if (offset.isOf(CeruleanBlocks.SPACEROCK) || offset.isOf(CeruleanBlocks.COBBLED_ROCK_CANDY)) {
                        world.setBlockState(local, getBlockState(random, offset.isOf(CeruleanBlocks.COBBLED_ROCK_CANDY))
                                .with(CeruleanPlantBlock.PLASTICLOGGED, curState.isOf(CeruleanBlocks.POLYETHYLENE))
                                .with(CeruleanPlantBlock.FACING, d.getOpposite()));
                        placed = true;
                    }
				}
			}

			if (random.nextInt(4) == 0) {
				Collections.shuffle(ds);
			}

		}

		return placed;
	}

	private static BlockState getBlockState(Random random, boolean isRockCandy) {
		int rng = random.nextInt(40) + (isRockCandy ? 0 : 2);
		if (rng == 0) {
			return CeruleanBlocks.BLUESHADE.getDefaultState();
		} else if (rng == 1) {
			return CeruleanBlocks.STARBLOOM.getDefaultState();
		} else if (rng == 2) {
			return CeruleanBlocks.SPARKLESSBLOSSOM.getDefaultState();
		} else if (rng <= 4) {
			return CeruleanBlocks.LUNARIUM.getDefaultState();
		} else if (rng <= 9) {
			return CeruleanBlocks.CORAL.getDefaultState();
		}
		return CeruleanBlocks.SKYGRASS.getDefaultState();
	}
}
