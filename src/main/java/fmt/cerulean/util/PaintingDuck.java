package fmt.cerulean.util;

import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;

public interface PaintingDuck {
	
	boolean lethargic();

	boolean manifestsInDreams();

	BlockBox getManifestationShape();

	BlockPos getTeleportTarget();
}
