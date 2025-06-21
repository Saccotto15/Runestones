package net.thevortex8196.runestones.compat;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.thevortex8196.runestones.block.ModBlocks;
import net.thevortex8196.runestones.recipe.ModRecipes;
import net.thevortex8196.runestones.recipe.RuneInfuserRecipe;
import net.thevortex8196.runestones.screen.RuneInfuserScreen;

public class RunestonesREIClient implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new RuneInfuserCategory());

        registry.addWorkstations(RuneInfuserCategory.RUNE_INFUSER, EntryStacks.of(ModBlocks.RUNE_INFUSER));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(RuneInfuserRecipe.class, ModRecipes.RUNE_INFUSER_TYPE,
                RuneInfuserDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 90,
                        ((screen.height - 166) / 2) + 35, 22, 15), RuneInfuserScreen.class,
                RuneInfuserCategory.RUNE_INFUSER);
    }
}
