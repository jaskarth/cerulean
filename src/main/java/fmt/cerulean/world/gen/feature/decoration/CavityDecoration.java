package fmt.cerulean.world.gen.feature.decoration;

import fmt.cerulean.block.WellBlock;
import fmt.cerulean.block.entity.WellBlockEntity;
import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.flow.FlowResources;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.Util;
import fmt.cerulean.util.Vec2i;
import fmt.cerulean.world.gen.synth.DecoratingWorld;
import fmt.cerulean.world.gen.synth.IslandParameters;
import fmt.cerulean.world.gen.feature.Decoration;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;

import java.util.*;

public class CavityDecoration extends Decoration {
	private static final Decoration ROCK_CANDY_SPIRE = new CeruleanPlantDecoration(6, 6, 24, CeruleanBlocks.ROCK_CANDY_SPIRE);

	private static final Decoration STARBLOOM = new CeruleanPlantDecoration(6, 6, 32, CeruleanBlocks.STARBLOOM);
	private static final Decoration BLUESHADE = new CeruleanPlantDecoration(6, 6, 32, CeruleanBlocks.BLUESHADE);

	@Override
	public void generate(DecoratingWorld world, Random random, BlockPos pos) {
		IslandParameters params = IslandParameters.get(world.getSeed(), pos);
		Vec2i c = params.center();

		if (params.cavity() && pos.getX() >> 4 == c.x() >> 4 && pos.getZ() >> 4 == c.z() >> 4) {
			BlockPos p = new BlockPos(c.x(), (params.startOff() + 13) * 8, c.z());

			int rcAmt = 2 + random.nextInt(2);
			for (int i = 0; i < rcAmt; i++) {
				BlockPos local = p.add(
						random.nextInt(7) - random.nextInt(7),
						random.nextInt(12) - random.nextInt(12),
						random.nextInt(7) - random.nextInt(7)
				);

				ROCK_CANDY_SPIRE.generate(world, random, local);
			}

			int sbAmt = 2 + random.nextInt(3);
			for (int i = 0; i < sbAmt; i++) {
				BlockPos local = p.add(
						random.nextInt(7) - random.nextInt(7),
						random.nextInt(12) - random.nextInt(12),
						random.nextInt(7) - random.nextInt(7)
				);

				STARBLOOM.generate(world, random, local);
			}

			int bsAmt = 2 + random.nextInt(3);
			for (int i = 0; i < bsAmt; i++) {
				BlockPos local = p.add(
						random.nextInt(7) - random.nextInt(7),
						random.nextInt(12) - random.nextInt(12),
						random.nextInt(7) - random.nextInt(7)
				);

				BLUESHADE.generate(world, random, local);
			}

			if (!world.isGen()) {
				return;
			}

			// place secondary starwell
			for (int i = 0; i < 128; i++) {
				BlockPos local = p.add(
						random.nextInt(7) - random.nextInt(7),
						random.nextInt(12) - random.nextInt(12),
						random.nextInt(7) - random.nextInt(7)
				);
				if (world.getBlockState(local).isOf(CeruleanBlocks.SPACEROCK)) {
					List<Direction> ok = new ArrayList<>();
					for (Direction d : Util.DIRECTIONS) {
						if (world.getBlockState(local.offset(d)).isAir()) {
							ok.add(d);
						}
					}

					if (!ok.isEmpty()) {
						Direction d = ok.get(random.nextInt(ok.size()));

						world.setBlockState(local, CeruleanBlocks.STAR_WELL.getDefaultState().with(WellBlock.FACING, d));

						if (world.getBlockEntity(local) instanceof WellBlockEntity well) {
							FlowResource.Brightness brightness = switch (random.nextInt(10)) {
								case 0 -> FlowResource.Brightness.BRILLIANT;
								case 1, 2, 3 -> FlowResource.Brightness.INNOCUOUS;
								case 4, 5, 6 -> FlowResource.Brightness.CANDESCENT;
								default -> FlowResource.Brightness.WANING;
							};

							FlowState flow = new FlowState(FlowResources.star(FlowResource.Color.values()[random.nextInt(FlowResource.Color.amount)], brightness),
									random.nextInt(4_000, 9_000));

							well.setFlow(flow);
						}
						break;
					}
				}
			}
		}
	}
}
