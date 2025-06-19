package net.thevortex8196.runestones.util;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.thevortex8196.runestones.Runestones;

import java.util.Arrays;

public class ModDataComponents {
    public static ComponentType<String[]> RUNESTONES;
    public static ComponentType<Integer> MAX_RUNESTONES;

    public static void register() {
        RUNESTONES = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(Runestones.MOD_ID, "runestones"),
                ComponentType.<String[]>builder()
                        .codec(Codec.STRING.listOf().xmap(list -> list.toArray(new String[0]), Arrays::asList))
                        .build()
        );
        MAX_RUNESTONES = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(Runestones.MOD_ID, "max_runestones"),
                ComponentType.<Integer>builder()
                        .codec(Codec.INT)
                        .build()
        );
    }
}
