package org.rqmses.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BuyCommand implements CommandExecutor {

    // imaginäre HashMap
    public static Map<UUID, Integer> falcone = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = ((Player) commandSender).getPlayer();

            // Hier überprüfen, ob Spieler Teil der Falcone-Famiglia
            if (falcone.containsKey(Objects.requireNonNull(player).getUniqueId())) {
                player.sendMessage("Hi");
            }
        }
        return true;
    }
}
