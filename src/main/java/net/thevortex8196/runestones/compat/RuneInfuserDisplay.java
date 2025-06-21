package net.thevortex8196.runestones.compat;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.recipe.RecipeEntry;
import net.thevortex8196.runestones.recipe.RuneInfuserRecipe;

import java.util.List;

public class RuneInfuserDisplay extends BasicDisplay {
    public RuneInfuserDisplay(RecipeEntry<RuneInfuserRecipe> recipe) {
        super(recipe.value().getIngredients().stream()
                        .map(EntryIngredients::ofIngredient)
                        .toList(),
                List.of(EntryIngredients.of(recipe.value().getResult(null))));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return RuneInfuserCategory.RUNE_INFUSER;
    }
}