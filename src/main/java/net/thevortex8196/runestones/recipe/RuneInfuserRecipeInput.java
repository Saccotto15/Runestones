package net.thevortex8196.runestones.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record  RuneInfuserRecipeInput(ItemStack blank_rune_stack, ItemStack input_stack, ItemStack diamond_stack) implements RecipeInput {

    @Override
    public ItemStack getStackInSlot(int slot) {
        return switch (slot) {
            case 0 -> blank_rune_stack;
            case 1 -> input_stack;
            case 2 -> diamond_stack;
            default -> null;
        };
    }

    @Override
    public int getSize() {
        return 3;
    }
}
