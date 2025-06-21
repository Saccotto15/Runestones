package net.thevortex8196.runestones.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public record RuneInfuserRecipe(Ingredient blankRunestoneItem, Ingredient inputItem, Ingredient diamondItem, ItemStack output) implements Recipe<RuneInfuserRecipeInput> {

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.blankRunestoneItem);
        list.add(this.inputItem);
        list.add(this.diamondItem);
        return list;
    }

    @Override
    public boolean matches(RuneInfuserRecipeInput input, World world) {
        if(world.isClient()) {
            return false;
        }

        return blankRunestoneItem.test(input.getStackInSlot(0)) && inputItem.test(input.getStackInSlot(1)) && diamondItem.test(input.getStackInSlot(2));
    }



    @Override
    public ItemStack craft(RuneInfuserRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.RUNE_INFUSER_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.RUNE_INFUSER_TYPE;
    }

    public static class Serializer implements RecipeSerializer<RuneInfuserRecipe> {
        public static final MapCodec<RuneInfuserRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("blank_runestone").forGetter(RuneInfuserRecipe::blankRunestoneItem),
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("input").forGetter(RuneInfuserRecipe::inputItem),
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("diamond").forGetter(RuneInfuserRecipe::diamondItem),
                ItemStack.CODEC.fieldOf("result").forGetter(RuneInfuserRecipe::output)
        ).apply(inst, RuneInfuserRecipe::new));

        public static final PacketCodec<RegistryByteBuf, RuneInfuserRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, RuneInfuserRecipe::blankRunestoneItem,
                        Ingredient.PACKET_CODEC, RuneInfuserRecipe::inputItem,
                        Ingredient.PACKET_CODEC, RuneInfuserRecipe::diamondItem,
                        ItemStack.PACKET_CODEC, RuneInfuserRecipe::output,
                        RuneInfuserRecipe::new);

        @Override
        public MapCodec<RuneInfuserRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, RuneInfuserRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}
