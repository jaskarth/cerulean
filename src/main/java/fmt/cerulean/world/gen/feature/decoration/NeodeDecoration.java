package fmt.cerulean.world.gen.feature.decoration;

import fmt.cerulean.block.HaliteOutcroppingBlock;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.Util;
import fmt.cerulean.world.gen.feature.Decoration;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.world.StructureWorldAccess;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class NeodeDecoration extends Decoration {
	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos pos) {
		int startY = -1;
		int endY = 0;
		boolean foundAny = false;

		for (int y = 0; y < 256; y++) {
//			if (foundAny) {
//				if (world.getBlockState(pos.withY(y)).isOf(CeruleanBlocks.SPACEROCK)) {
//					return;
//				}
//			}

			if (world.getBlockState(pos.withY(y)).isOf(CeruleanBlocks.POLYETHYLENE)) {
				foundAny = true;

				if (startY == -1) {
					startY = y;
				}

				endY = y;
			}
		}

		if (!foundAny) {
			return;
		}

		int change = ((endY - startY) / 2) + 1;
		int midpoint = startY + change;

		for (int i = 0; i < 16; i++) {
			int dy = random.nextInt(change) - random.nextInt(change);

			if (tryGenerate(world, random, pos.withY(midpoint + dy))) {
				return;
			}
		}
	}

	// /execute in cerulean:skies run tp @s -312.89 129.46 3999.53 629.25 19.95

	private static boolean tryGenerate(StructureWorldAccess world, Random random, BlockPos pos) {
		DoublePerlinNoiseSampler sampler = DoublePerlinNoiseSampler.create(new CheckedRandom(random.nextLong()), -4, 1.0);
		Set<BlockPos> set = new HashSet<>();
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = -4; y <= 4; y++) {
					BlockPos local = pos.add(x, y, z);
					double dx = x / 4.0;
					double dy = y / 4.0;
					double dz = z / 4.0;

					if ((dx * dx) + (dy * dy) + (dz * dz) < 1 + (sampler.sample(local.getX(), local.getY(), local.getZ()) * 0.25)) {
						if (world.getBlockState(local).isAir()) {
							return false;
						}

						set.add(local);
					}
				}
			}
		}

		for (BlockPos p : set) {
			boolean hasSet = false;
			for (Direction d : Util.DIRECTIONS) {
				BlockPos local = p.offset(d);
				if (world.getBlockState(local).isOf(CeruleanBlocks.POLYETHYLENE) && !set.contains(local)) {
					if (random.nextInt(8) == 0) {
						world.setBlockState(p, CeruleanBlocks.SLASHED_HALITE_BLOCK.getDefaultState(), 3);

						Block block = switch (random.nextInt(4)) {
							case 0 -> CeruleanBlocks.HALITE_OUTCROPPING_SMALL;
							case 1 -> CeruleanBlocks.HALITE_OUTCROPPING_MEDIUM;
							case 2 -> CeruleanBlocks.HALITE_OUTCROPPING_LARGE;
							case 3 -> CeruleanBlocks.HALITE_OUTCROPPING;
							default -> throw new IllegalStateException("Maths is broken");
						};

						world.setBlockState(p.offset(d), block.getDefaultState()
								.with(HaliteOutcroppingBlock.FACING, d)
								.with(HaliteOutcroppingBlock.PLASTICLOGGED, true), 3);

						hasSet = true;

						break;
					} else if (random.nextBoolean()) {
						world.setBlockState(p, CeruleanBlocks.HALITE_BLOCK.getDefaultState(), 3);
						hasSet = true;
						break;
					}
				}
			}

			if (!hasSet) {
				world.setBlockState(p, CeruleanBlocks.SPACEROCK.getDefaultState(), 3);
			}
		}

		return true;
	}
}
