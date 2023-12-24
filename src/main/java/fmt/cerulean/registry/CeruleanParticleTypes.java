package fmt.cerulean.registry;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import fmt.cerulean.Cerulean;
import fmt.cerulean.client.particle.StarParticleType;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CeruleanParticleTypes {
	public static final ParticleType<StarParticleType> STAR = register("star", FabricParticleTypes.complex(new ParticleEffect.Factory<StarParticleType>() {
		@Override
		public StarParticleType read(ParticleType<StarParticleType> type, StringReader reader) throws CommandSyntaxException {
			throw new UnsupportedOperationException("Unimplemented method 'read'");
		}
		@Override
		public StarParticleType read(ParticleType<StarParticleType> type, PacketByteBuf buf) {
			throw new UnsupportedOperationException("Unimplemented method 'read'");
		}
	}));

	public static void init() {
	}

	private static <T extends ParticleEffect, P extends ParticleType<T>> P register(String id, P type) {
		return Registry.register(Registries.PARTICLE_TYPE, Cerulean.id(id), type);
	}
}
