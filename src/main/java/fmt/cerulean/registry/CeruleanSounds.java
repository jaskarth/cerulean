package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class CeruleanSounds {
    public static final SoundEvent SHUTTER = register("shutter");

    public static void init() {

    }

    private static SoundEvent register(String name) {
        return Registry.register(Registries.SOUND_EVENT, Cerulean.id(name), SoundEvent.of(Cerulean.id(name)));
    }
}
