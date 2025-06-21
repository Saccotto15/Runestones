package net.thevortex8196.runestones.recipe;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.thevortex8196.runestones.Runestones;
import net.thevortex8196.runestones.recipe.RuneInfuserRecipe;

public class ModRecipes {
    public static final RecipeSerializer<RuneInfuserRecipe> RUNE_INFUSER_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(Runestones.MOD_ID, "rune_infuser"),
            new RuneInfuserRecipe.Serializer());
    public static final RecipeType<RuneInfuserRecipe> RUNE_INFUSER_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(Runestones.MOD_ID, "rune_infuser"), new RecipeType<RuneInfuserRecipe>() {
                @Override
                public String toString() {
                    return "rune_infuser";
                }
            });

    public static void register() {
        Runestones.LOGGER.info("Registering Custom Recipes for " + Runestones.MOD_ID);
    }
}