package net.thevortex8196.runestones;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.thevortex8196.runestones.screen.ModScreenHandlers;
import net.thevortex8196.runestones.screen.RuneInfuserScreen;

public class RunestonesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.RUNE_INFUSER_SCREEN_HANDLER, RuneInfuserScreen::new);
    }
}
