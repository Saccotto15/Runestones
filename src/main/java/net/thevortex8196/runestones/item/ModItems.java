package net.thevortex8196.runestones.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.thevortex8196.runestones.Runestones;

public class ModItems {

    public static final Item BLANK_RUNE = registerItem("blank_rune", new RuneItem(new Item.Settings()));
    public static final Item LUCK_RUNE = registerItem("luck_rune", new RuneItem(new Item.Settings()));
    public static final Item DASH_RUNE = registerItem("dash_rune", new RuneItem(new Item.Settings()));
    public static final Item HEART_RUNE = registerItem("heart_rune", new RuneItem(new Item.Settings()));
    public static final Item STRENGTH_RUNE = registerItem("strength_rune", new RuneItem(new Item.Settings()));
    public static final Item SKY_RUNE = registerItem("sky_rune", new RuneItem(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Runestones.MOD_ID, name), item);
    }

    public static void register() {
    }
}
