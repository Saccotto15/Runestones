package net.thevortex8196.runestones;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.thevortex8196.runestones.command.ModCommands;
import net.thevortex8196.runestones.item.ModCreativeTab;
import net.thevortex8196.runestones.item.ModItems;
import net.thevortex8196.runestones.util.ModDataComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Runestones implements ModInitializer {
	public static final String MOD_ID = "runestones";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initialising Runestones by TheVortex8196.");

		ModCreativeTab.registerCreativeTab();
		ModItems.registerModItems();
		ModCommands.registerModCommands();
		ModDataComponents.register();

		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
			if (source == entity.getDamageSources().fall()) {
				var main = entity.getEquippedStack(EquipmentSlot.MAINHAND);
				if (main.getItem() instanceof Item) {
					String[] runes = main.get(ModDataComponents.RUNESTONES);
					if (runes != null && Arrays.asList(runes).contains("sky")) {
						return false; // Cancel fall damage
					}
				}
			}
			return true;
		});
	}
}