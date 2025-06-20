package net.thevortex8196.runestones.datagen;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.thevortex8196.runestones.item.ModItems;

public class ModLootTableModifier {

    private static final RegistryKey<LootTable> TREASURE_KEY = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/buried_treasure"));
    private static final RegistryKey<LootTable> BASTION_KEY = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/bastion_treasure"));
    private static final RegistryKey<LootTable> ANCIENT_CITY_KEY = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "chests/ancient_city"));
    private static final RegistryKey<LootTable> BRUTE_KEY = EntityType.PIGLIN_BRUTE.getLootTableId();


    public static void register() {
        LootTableEvents.MODIFY.register((registryKey, builder, lootTableSource, wrapperLookup) -> {
            if (lootTableSource.isBuiltin() && (
                    registryKey.equals(TREASURE_KEY) ||
                    registryKey.equals(BASTION_KEY) ||
                    registryKey.equals(ANCIENT_CITY_KEY) ||
                    registryKey.equals(BRUTE_KEY)
            )) {
                LootPool pool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.4f))
                        .with(ItemEntry.builder(ModItems.BLANK_RUNE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1f, 1f)).build())
                        .build();
                builder.pool(pool);
            }
        });
    }
}
