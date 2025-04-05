package fmt.cerulean.net;

import fmt.cerulean.Cerulean;
import fmt.cerulean.net.packet.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

public class CeruleanNetworking {
	public static final Identifier DS_SYNC = Cerulean.id("ds_sync");
	public static final Identifier CLOSE_BEHIND = Cerulean.id("close_behind");
	public static final Identifier MAGIC_ATTACK = Cerulean.id("magic_attack");
	public static final Identifier INFLUENCE = Cerulean.id("influence");
	public static final Identifier UPLOAD_MEMORY = Cerulean.id("upload_memory");
	public static final Identifier SUPPLY_MEMORY = Cerulean.id("supply_memory");
	public static final Identifier REQUEST_MEMORY = Cerulean.id("request_memory");
	public static final Identifier STARING = Cerulean.id("staring");
	public static final Identifier WINS = Cerulean.id("wins");
	public static final Identifier WIN = Cerulean.id("win");

	public static void init() {
		// s2c
		PayloadTypeRegistry.playS2C().register(CeruleanStateSyncPacket.ID, CeruleanStateSyncPacket.CODEC);
		PayloadTypeRegistry.playS2C().register(SupplyMemoryPacket.ID, SupplyMemoryPacket.CODEC);
		PayloadTypeRegistry.playS2C().register(WinsPacket.ID, WinsPacket.CODEC);

		// c2s
		PayloadTypeRegistry.playC2S().register(CloseBehindPacket.ID, CloseBehindPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(MagicAttackPacket.ID, MagicAttackPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(InfluencePacket.ID, InfluencePacket.CODEC);
		PayloadTypeRegistry.playC2S().register(UploadMemoryPacket.ID, UploadMemoryPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(RequestMemoryPacket.ID, RequestMemoryPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(StaringPacket.ID, StaringPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(WinPacket.ID, WinPacket.CODEC);
	}
}
