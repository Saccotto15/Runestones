package net.thevortex8196.runestones.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.thevortex8196.runestones.Runestones;
import net.thevortex8196.runestones.block.ModBlocks;

public class ModCreativeTab {
    public static final ItemGroup RUNESTONES = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Runestones.MOD_ID, "runestones"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.HEART_RUNE))
                    .displayName(Text.translatable("creativetab.runestones"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.BLANK_RUNE);
                        entries.add(ModItems.LUCK_RUNE);
                        entries.add(ModItems.DASH_RUNE);
                        entries.add(ModItems.HEART_RUNE);
                        entries.add(ModItems.STRENGTH_RUNE);
                        entries.add(ModItems.SKY_RUNE);
                        entries.add(ModBlocks.RUNE_INFUSER);
                    }).build());

    public static void register() {
    }

}
