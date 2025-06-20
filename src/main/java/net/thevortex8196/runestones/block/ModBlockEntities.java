package net.thevortex8196.runestones.block;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.thevortex8196.runestones.Runestones;

public class ModBlockEntities {

    public static final BlockEntityType<RuneInfuserBlockEntity> RUNE_INFUSER_BE =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of(Runestones.MOD_ID, "rune_infuser"),
                    BlockEntityType.Builder.create(RuneInfuserBlockEntity::new, ModBlocks.RUNE_INFUSER).build(null)
            );



    public static void register() {
    }
}