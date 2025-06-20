package net.thevortex8196.runestones.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.thevortex8196.runestones.Runestones;

public class ModBlocks {

    public static final Block RUNE_INFUSER = registerBlock("rune_infuser",
            new RuneInfuser(AbstractBlock.Settings.create()));

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Runestones.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Runestones.MOD_ID, name), block);
    }

    public static void  register() {
    }
}
