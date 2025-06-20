package net.thevortex8196.runestones.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.thevortex8196.runestones.Runestones;

public class ModScreenHandlers {
    public static final ScreenHandlerType<RuneInfuserScreenHandler> RUNE_INFUSER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Runestones.MOD_ID, "rune_infuser_screen_handler"),
                    new ExtendedScreenHandlerType<>(RuneInfuserScreenHandler::new, BlockPos.PACKET_CODEC));


    public static void register() {
    }
}