package net.thevortex8196.runestones.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.thevortex8196.runestones.Runestones;
import net.thevortex8196.runestones.item.ModItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

public class RunestonesCommand {

    private static final List<Item> ALL_RUNES = List.of(
            ModItems.LUCK_RUNE,
            ModItems.DASH_RUNE,
            ModItems.HEART_RUNE,
            ModItems.STRENGTH_RUNE,
            ModItems.SKY_RUNE
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("runestones")
                .requires(src -> src.hasPermissionLevel(2))
                .executes(RunestonesCommand::giveRunes));
    }

    private static int giveRunes(CommandContext<ServerCommandSource> ctx) {
        var source = ctx.getSource();
        List<ServerPlayerEntity> players = new ArrayList<>(source.getServer()
                .getPlayerManager()
                .getPlayerList());

        if (players.isEmpty()) {
            source.sendError(Text.literal("No players online to receive runes."));
            return 0;
        }

        // Shuffle players and runes
        Collections.shuffle(players);
        List<Item> runes = new ArrayList<>(ALL_RUNES);
        Collections.shuffle(runes);

        // We'll give at most one rune per player and up to 5 players
        int giveCount = Math.min(5, players.size());
        for (int i = 0; i < giveCount; i++) {
            ServerPlayerEntity player = players.get(i);
            Item rune = runes.get(i);
            ItemStack stack = new ItemStack(rune);

            boolean added = player.getInventory().insertStack(stack);
            if (!added) {
                player.dropItem(stack, false);
            }
            player.sendMessage(
                    Text.literal("You have been granted a rune!"),
                    false
            );
            Runestones.LOGGER.info("Gave {} to {}", rune, player.getName());
        }

        return 1;
    }
}
