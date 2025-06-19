package net.thevortex8196.runestones.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.thevortex8196.runestones.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {}

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.LUCK_RUNE, Models.GENERATED);
        itemModelGenerator.register(ModItems.DASH_RUNE, Models.GENERATED);
        itemModelGenerator.register(ModItems.HEART_RUNE, Models.GENERATED);
        itemModelGenerator.register(ModItems.STRENGTH_RUNE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SKY_RUNE, Models.GENERATED);
    }
}
