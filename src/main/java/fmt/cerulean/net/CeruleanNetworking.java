package fmt.cerulean.net;

import fmt.cerulean.Cerulean;
import fmt.cerulean.net.packet.InfluencePacket;
import fmt.cerulean.net.packet.CeruleanStateSyncPacket;
import fmt.cerulean.net.packet.CloseBehindPacket;
import fmt.cerulean.net.packet.MagicAttackPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

public class CeruleanNetworking {
	public static final Identifier DS_SYNC = Cerulean.id("ds_sync");
	public static final Identifier CLOSE_BEHIND = Cerulean.id("close_behind");
	public static final Identifier MAGIC_ATTACK = Cerulean.id("magic_attack");
	public static final Identifier INFLUENCE = Cerulean.id("influence");

	public static void init() {
		// s2c
		PayloadTypeRegistry.playS2C().register(CeruleanStateSyncPacket.ID, CeruleanStateSyncPacket.CODEC);

		// c2s
		PayloadTypeRegistry.playC2S().register(CloseBehindPacket.ID, CloseBehindPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(MagicAttackPacket.ID, MagicAttackPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(InfluencePacket.ID, InfluencePacket.CODEC);
	}
}
