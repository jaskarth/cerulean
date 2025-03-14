package fmt.cerulean.block.entity;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.Sets;

import fmt.cerulean.block.base.Obedient;
import fmt.cerulean.block.entity.base.SyncedBlockEntity;
import fmt.cerulean.client.render.block.FauxBlockEntityRenderer;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.registry.CeruleanBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FauxBlockEntity extends SyncedBlockEntity implements Obedient {
	private boolean reckon = false;
	private int demanifest = 7, manifest = 3;
	public BlockState state;
	public Direction facing = Direction.NORTH;
	public BlockPos beholdent = null;
	public Set<BlockPos> carefulNow = Set.of();

	public FauxBlockEntity(BlockPos pos, BlockState state) {
		super(CeruleanBlockEntities.FAUX, pos, state);
	}

	public void glanceAndBehold() {
		for (Direction dir : Direction.values()) {
			BlockPos pos = this.pos.offset(dir);
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof FauxBlockEntity fbe) {
				if (fbe.beholdent != null) {
					fbe.behold();
					return;
				}
			}
		}
		behold();
	}

	public void behold() {
		if (this.pos == null) {
			return;
		}
		if (!Objects.equals(this.pos, this.beholdent) && this.beholdent != null) {
			BlockEntity bbe = world.getBlockEntity(this.beholdent);
			if (bbe instanceof FauxBlockEntity fbe) {
				fbe.behold();
				return;
			}
		}
		Set<BlockPos> held = Sets.newHashSet();
		behold(held, this.pos);
		for (BlockPos pos : held) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof FauxBlockEntity fbe) {
				fbe.beholdent = this.pos;
				if (Objects.equals(pos, this.pos)) {
					fbe.carefulNow = held;
				} else {
					fbe.carefulNow = Set.of();
				}
			}
		}
	}

	public void behold(Set<BlockPos> held, BlockPos pos) {
		if (held.contains(pos)) {
			return;
		}
		if (world.getBlockState(pos).getBlock() != CeruleanBlocks.FAUX) {
			return;
		}
		held.add(pos);
		for (Direction dir : Direction.values()) {
			behold(held, pos.offset(dir));
		}
	}

	public boolean canReckon() {
		if (this.beholdent == null) {
			return false;
		}
		BlockEntity be = this.world.getBlockEntity(this.beholdent);
		if (be instanceof FauxBlockEntity fbe) {
			return fbe.reckon;
		}
		return false;
	}

	@Environment(EnvType.CLIENT)
	public static void clientTick(World world, BlockPos pos, BlockState state, FauxBlockEntity fbe) {
		if (fbe.beholdent == null || !(world.getBlockEntity(fbe.beholdent) instanceof FauxBlockEntity)) {
			fbe.behold();
		}
		// Perhaps introspection would let us move forward?
		if (Objects.equals(pos, fbe.beholdent)) {
			boolean impulse = FauxBlockEntityRenderer.adrenaline(pos);
			int detachment = FauxBlockEntityRenderer.intuitDetachment(pos);
			if (impulse == false && detachment > 2 && detachment < 14) {
				if (world.getRandom().nextInt(fbe.reckon ? fbe.manifest : fbe.demanifest) == 0) {
					for (BlockPos c : fbe.carefulNow) {
						if (FauxBlockEntityRenderer.adrenaline(c)) {
							return;
						}
					}
					fbe.reckon = !fbe.reckon;
				}
			}
		}
	}

	@Override
	public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);

		RegistryEntryLookup<Block> registryEntryLookup = registryLookup.createRegistryLookup().getOrThrow(RegistryKeys.BLOCK);

		state = NbtHelper.toBlockState(registryEntryLookup, nbt.getCompound("Block"));
		facing = Direction.byId(nbt.getByte("Dir"));
		if (nbt.contains("Demanifest")) {
			demanifest = nbt.getInt("Demanifest");
		}
		if (nbt.contains("Manifest")) {
			manifest = nbt.getInt("Manifest");
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);

		nbt.put("Block", NbtHelper.fromBlockState(this.state == null ? Blocks.BEDROCK.getDefaultState() : state));
		nbt.putByte("Dir", (byte) facing.getId());
		nbt.putInt("Demanifest", demanifest);
		nbt.putInt("Manifest", manifest);
	}

	public static void set(BlockEntity be, BlockState state, Direction facing) {
		FauxBlockEntity fbe = (FauxBlockEntity) be;
		fbe.state = state;
		fbe.facing = facing;
		fbe.markDirty();
	}

	public void shareImpulse(Consumer<FauxBlockEntity> consumer) {
		Set<BlockPos> held = Sets.newHashSet();
		behold(held, this.pos);
		for (BlockPos pos : held) {
			if (this.world.getBlockEntity(pos) instanceof FauxBlockEntity fbe) {
				consumer.accept(fbe);
				fbe.markDirty();
			}
		}
	}

	@Override
	public Map<String, Consumer<String>> cede() {
		return Map.of(
			"demanifest", intuition -> shareImpulse(fbe -> fbe.demanifest = Math.max(1, Obedient.count(intuition))),
			"manifest", intuition -> shareImpulse(fbe -> fbe.manifest = Math.max(1, Obedient.count(intuition)))
		);
	}
}
