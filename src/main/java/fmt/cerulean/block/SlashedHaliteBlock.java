package fmt.cerulean.block;

import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanFluids;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkSectionCache;

import java.util.*;

public class SlashedHaliteBlock extends Block {
	public SlashedHaliteBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		Block block = null;
		BlockState setState = null;
		BlockPos offsetPos = null;

		try (ChunkSectionCache cache = new ChunkSectionCache(world)) {
			List<Direction> dirs = new ArrayList<>(List.of(DIRECTIONS));

			Collections.shuffle(dirs, new java.util.Random(random.nextInt()));

			for (Direction d : dirs) {
				BlockPos boxOrigin = pos.offset(d, 3);
				offsetPos = pos.offset(d);
				BlockBox box = new BlockBox(boxOrigin.getX() - 2, boxOrigin.getY() - 2, boxOrigin.getZ() - 2, boxOrigin.getX() + 2, boxOrigin.getY() + 2, boxOrigin.getZ() + 2);
				int c = countConnected(cache, offsetPos, box);
				if (c == 0) {
					continue;
				}

				double chance = 20.0 / Math.sqrt(c);
				if (Math.random() * 20 > chance) {
					BlockState offsetState = world.getBlockState(offsetPos);
					if (offsetState.isAir() || offsetState.isOf(CeruleanBlocks.POLYETHYLENE)) {
						block = CeruleanBlocks.HALITE_OUTCROPPING_SMALL;
					} else if (offsetState.isOf(CeruleanBlocks.HALITE_OUTCROPPING_SMALL) && offsetState.get(AmethystClusterBlock.FACING) == d) {
						block = CeruleanBlocks.HALITE_OUTCROPPING_MEDIUM;
					} else if (offsetState.isOf(CeruleanBlocks.HALITE_OUTCROPPING_MEDIUM) && offsetState.get(AmethystClusterBlock.FACING) == d) {
						block = CeruleanBlocks.HALITE_OUTCROPPING_LARGE;
					} else if (offsetState.isOf(CeruleanBlocks.HALITE_OUTCROPPING_LARGE) && offsetState.get(AmethystClusterBlock.FACING) == d) {
						block = CeruleanBlocks.HALITE_OUTCROPPING;
					}

					if (block != null) {
						setState = block.getDefaultState()
								.with(HaliteOutcroppingBlock.FACING, d)
								.with(HaliteOutcroppingBlock.PLASTICLOGGED, true);
						break;
					}
				}
			}
		}

		if (setState != null) {
			world.setBlockState(offsetPos, setState);
		}
	}

	private static int countConnected(ChunkSectionCache cache, BlockPos start, BlockBox bound) {
		LongSet seen = new LongOpenHashSet();

		int amt = 0;

		LongArrayFIFOQueue stack = new LongArrayFIFOQueue();
		stack.enqueue(start.asLong());
		seen.add(start.asLong());

		while (!stack.isEmpty()) {
			BlockPos p = BlockPos.fromLong(stack.dequeueLastLong());

			if (cache.getBlockState(p).getFluidState().getFluid() == CeruleanFluids.POLYETHYLENE) {
				amt++;

				for (Direction d : DIRECTIONS) {
					BlockPos l = p.offset(d);
					long v = l.asLong();
					if (bound.contains(l) && seen.add(v)) {
						stack.enqueue(v);
					}
				}
			}
		}

		return amt;
	}
}
