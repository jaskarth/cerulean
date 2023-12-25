package fmt.cerulean.world;

import fmt.cerulean.net.CeruleanNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class DimensionState {
	public int melancholy = 0;
	public int dissonance = 0;
	public int ennui = 0;
	public int indifference = 0;

	public void reset() {
		melancholy = 0;
		dissonance = 0;
		ennui = 0;
		indifference = 0;
	}

	public NbtCompound nbt() {
		NbtCompound out = new NbtCompound();
		out.putInt("M", melancholy);
		out.putInt("D", dissonance);
		out.putInt("E", ennui);
		out.putInt("I", indifference);

		return out;
	}

	public void sync(ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeNbt(nbt());

		ServerPlayNetworking.send(player, CeruleanNetworking.DS_SYNC, buf);
	}

	public void read(NbtCompound nbt) {
		this.melancholy = nbt.getInt("M");
		this.dissonance = nbt.getInt("D");
		this.ennui = nbt.getInt("E");
		this.indifference = nbt.getInt("I");
	}
}
