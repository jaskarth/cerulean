package fmt.cerulean.client.render.world;

import fmt.cerulean.block.CeruleanPlantBlock;
import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockRenderView;

import java.util.HashMap;
import java.util.Map;

public class WaitingRoom {
	private static WaitingRoom CACHED = null;

	public static WaitingRoom get() {
		if (CACHED == null) {
			CACHED = new WaitingRoom();
		}

		return CACHED;
	}

	private final FauxBlockSpace space = new FauxBlockSpace();

	public WaitingRoom() {
		init();
	}

	private void init() {
		// ssssh....

		for (int x = -3; x <= 4; x++) {
			for (int z = -5; z <= 3; z++) {
				space.addBlock(new BlockPos(x, 0, z), CeruleanBlocks.SPACEBRICKS.getDefaultState());
				space.addBlock(new BlockPos(x, 4, z), CeruleanBlocks.SPACEBRICKS.getDefaultState());
			}
		}

		for (int z = -5; z <= 3; z++) {
			for (int y = 0; y <= 4; y += 1) {
				space.addBlock(new BlockPos(-4, y, z), CeruleanBlocks.SPACEBRICKS.getDefaultState());

				if ((z == -3 || z == -2) && (y == 1 || y == 2)) {
					continue;
				}

				space.addBlock(new BlockPos(5, y, z), CeruleanBlocks.SPACEBRICKS.getDefaultState());
			}
		}

		for (int x = -3; x <= 4; x++) {
			for (int y = 0; y <= 4; y += 1) {
				space.addBlock(new BlockPos(x, y, -6), CeruleanBlocks.SPACEBRICKS.getDefaultState());
				space.addBlock(new BlockPos(x, y, 4), CeruleanBlocks.SPACEBRICKS.getDefaultState());
			}
		}

		// extrusion

		for (int x = 6; x < 9; x++) {
			for (int z = -3; z <= -2; z++) {
				space.addBlock(new BlockPos(x, 0, z), CeruleanBlocks.SPACEBRICKS.getDefaultState());
				space.addBlock(new BlockPos(x, 3, z), CeruleanBlocks.SPACEBRICKS.getDefaultState());
			}
		}

		for (int x = 6; x <= 9; x++) {
			for (int y = 0; y <= 3; y += 1) {
				space.addBlock(new BlockPos(x, y, -4), CeruleanBlocks.SPACEBRICKS.getDefaultState());
			}
		}

		// chairs

		space.addBlock(new BlockPos(2, 1, -5), CeruleanBlocks.SPACEBRICK_STAIRS.getDefaultState());
		space.addBlock(new BlockPos(3, 1, -5), CeruleanBlocks.SPACEBRICK_STAIRS.getDefaultState());

		space.addBlock(new BlockPos(-1, 1, -5), CeruleanBlocks.SPACEBRICK_STAIRS.getDefaultState());
		space.addBlock(new BlockPos(-2, 1, -5), CeruleanBlocks.SPACEBRICK_STAIRS.getDefaultState());

		space.addBlock(new BlockPos(4, 1, 1), CeruleanBlocks.SPACEBRICK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST));
		space.addBlock(new BlockPos(4, 1, 2), CeruleanBlocks.SPACEBRICK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST));

		// light fixtures

		space.addBlock(new BlockPos(-1, 1, 3), CeruleanBlocks.SPARKBLOSSOM.getDefaultState());
		space.addBlock(new BlockPos(0, 3, -5), CeruleanBlocks.SPARKBLOSSOM.getDefaultState().with(CeruleanPlantBlock.FACING, Direction.DOWN));
		space.addBlock(new BlockPos(5, 2, -2), CeruleanBlocks.GLITTERING_SPARKBLOSSOM.getDefaultState().with(CeruleanPlantBlock.FACING, Direction.DOWN));

		space.addLightSource(new BlockPos(-1, 1, 3), 12);
		space.addLightSource(new BlockPos(0, 3, -5), 12);
		space.addLightSource(new BlockPos(5, 2, -2), 10);

		space.bakeLights();
	}

	public Map<BlockPos, BlockState> blocksSolid() {
		Map<BlockPos, BlockState> map = new HashMap<>();
		for (Map.Entry<BlockPos, BlockState> e : space.blocks().entrySet()) {
			if (!e.getValue().isOf(CeruleanBlocks.SPARKBLOSSOM) && !e.getValue().isOf(CeruleanBlocks.GLITTERING_SPARKBLOSSOM)) {
				map.put(e.getKey(), e.getValue());
			}
		}

		return map;
	}

	public Map<BlockPos, BlockState> blocksCutout() {
		Map<BlockPos, BlockState> map = new HashMap<>();
		for (Map.Entry<BlockPos, BlockState> e : space.blocks().entrySet()) {
			if (e.getValue().isOf(CeruleanBlocks.SPARKBLOSSOM) || e.getValue().isOf(CeruleanBlocks.GLITTERING_SPARKBLOSSOM)) {
				map.put(e.getKey(), e.getValue());
			}
		}

		return map;
	}

	public Map<BlockPos, BlockState> blocks() {
		return space.blocks();
	}

	// Y component is not server accurate!
	public Vec3d origin() {
		return new Vec3d(2 + .70, -2.62, -3 - .47);
	}

	public float yaw() {
		return 2035.80f;
	}

	public float pitch() {
		return -7.80f;
	}

	public BlockRenderView world() {
		return space;
	}
}
