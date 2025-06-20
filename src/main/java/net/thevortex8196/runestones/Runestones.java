package net.thevortex8196.runestones;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.thevortex8196.runestones.block.ModBlockEntities;
import net.thevortex8196.runestones.block.ModBlocks;
import net.thevortex8196.runestones.command.ModCommands;
import net.thevortex8196.runestones.item.ModCreativeTab;
import net.thevortex8196.runestones.item.ModItems;
import net.thevortex8196.runestones.screen.ModScreenHandlers;
import net.thevortex8196.runestones.util.ModDataComponents;
import net.thevortex8196.runestones.datagen.ModLootTableModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Runestones implements ModInitializer {
	public static final String MOD_ID = "runestones";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initialising Runestones by TheVortex8196.");

		ModCreativeTab.register();

		ModItems.register();
		ModBlocks.register();

		ModDataComponents.register();

		ModLootTableModifier.register();

		ModBlockEntities.register();
		ModScreenHandlers.register();

		ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, originalAmount, amountTaken, x) -> {
			if (source == entity.getDamageSources().fall()) {
				var main = entity.getEquippedStack(EquipmentSlot.MAINHAND);
				if (main.getItem() instanceof Item) {
					String[] runes = main.get(ModDataComponents.RUNESTONES);
					if (runes != null && Arrays.asList(runes).contains("sky")) {
						// Calculate extra to heal (half)
						float reduction = originalAmount * 0.6f;
						entity.heal(reduction);
					}
				}
			}
		});
	}
}