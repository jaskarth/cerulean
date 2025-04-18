package fmt.cerulean.block.entity;

import fmt.cerulean.block.base.Obedient;
import fmt.cerulean.block.entity.base.SyncedBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ProjectorBlockEntity extends SyncedBlockEntity implements Obedient {
	public String name = "";
	public float transX;
	public float transY;
	public float transZ;
	public float xp;
	public float yp;
	public float zp;
	public float scaleX = 1;
	public float scaleY = 1;
	public float scaleZ = 1;
	public int alpha = 255;

	public ProjectorBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.PROJECTOR, pos, state);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		name = nbt.getString("name");
		transX = nbt.getFloat("transX");
		transY = nbt.getFloat("transY");
		transZ = nbt.getFloat("transZ");
		xp = nbt.getFloat("xp");
		yp = nbt.getFloat("yp");
		zp = nbt.getFloat("zp");
		scaleX = nbt.getFloat("scaleX");
		scaleY = nbt.getFloat("scaleY");
		scaleZ = nbt.getFloat("scaleZ");
		alpha = nbt.getInt("alpha");
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		nbt.putString("name", name);
		nbt.putFloat("transX", transX);
		nbt.putFloat("transY", transY);
		nbt.putFloat("transZ", transZ);
		nbt.putFloat("xp", xp);
		nbt.putFloat("yp", yp);
		nbt.putFloat("zp", zp);
		nbt.putFloat("scaleX", scaleX);
		nbt.putFloat("scaleY", scaleY);
		nbt.putFloat("scaleZ", scaleZ);
		nbt.putInt("alpha", alpha);
	}

	@Override
	public Map<String, Consumer<String>> cede() {
		Map<String, Consumer<String>> map = new HashMap<>();

		map.put("name", intuition -> name = intuition);
		map.put("transX", intuition -> transX = Obedient.guess(intuition));
		map.put("transY", intuition -> transY = Obedient.guess(intuition));
		map.put("transZ", intuition -> transZ = Obedient.guess(intuition));
		map.put("xp", intuition -> xp = Obedient.guess(intuition));
		map.put("yp", intuition -> yp = Obedient.guess(intuition));
		map.put("zp", intuition -> zp = Obedient.guess(intuition));
		map.put("scaleX", intuition -> scaleX = Obedient.guess(intuition));
		map.put("scaleY", intuition -> scaleY = Obedient.guess(intuition));
		map.put("scaleZ", intuition -> scaleZ = Obedient.guess(intuition));
		map.put("alpha", intuition -> alpha = Obedient.count(intuition));

		return map;
	}
}
