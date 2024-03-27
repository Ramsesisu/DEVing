package org.rqmses.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.rqmses.DEVing;
import org.rqmses.listeners.LockpickListener;

import java.util.*;

public class RobCommand implements CommandExecutor {

    // imaginäre Frak-Liste (entfernen)
    public static Map<UUID, Integer> falcone = new HashMap<>();
    // imaginäre Business-Liste (entfernen)
    public static Map<String, Location> bizlist = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = ((Player) commandSender).getPlayer();

            // Hier überprüfen, ob Spieler Teil der Falcone-Famiglia
            if (falcone.containsKey(Objects.requireNonNull(player).getUniqueId())) {
                // Hier überprüfen, ob Spieler Rang-2 oder höher
                if (falcone.get(player.getUniqueId()) >= 2) {
                    // Hier überprüfen, ob Spieler in 3-Block Nähe von Hologramm
                    String biz = null;
                    for (String name : bizlist.keySet()) {
                        if (bizlist.get(name).distance(player.getLocation()) <= 3) {
                            biz = name;
                        }
                    }
                    if (biz != null) {
                        // Nachricht an alle Frakmitglieder
                        Bukkit.getServer().broadcast(Component.text(player.getName() + " beginnt die " + biz + "-Kasse aufzubrechen."));
                        // dazu passendes /me für alle Spieler in der Nähe

                        LockpickListener.c = 1;
                        LockpickListener.a = 1;
                        LockpickListener.f = 0;
                        LockpickListener.value = 0;
                        LockpickListener.next(player);

                        // Plugin-Referenz anpassen
                        Bukkit.getScheduler().runTaskLater(DEVing.DEVING, () -> {
                            if (Objects.requireNonNull(player).getOpenInventory().title().equals(Component.text("Kasse"))) {
                                player.closeInventory();

                                // Nachricht(en) an alle Frakmitglieder (formatieren)
                                if (LockpickListener.value > 0) {
                                    Bukkit.getServer().broadcast(Component.text(player.getName() + " hat erfolgreich " + LockpickListener.value + "€ aus der Kasse gestohlen."));
                                    // Dem Spieler [LockpickListener.value] zum Bargeld hinzufügen
                                    // Der Staatskasse [LockpickListener.value] vom Staatsgeld entfernen
                                } else {
                                    Bukkit.getServer().broadcast(Component.text(player.getName() + " hat es nicht geschafft Geld aus der Kasse zu stehlen."));
                                }
                            }
                        }, 40 * 20L);
                    } else {
                        // Als Fehlermeldung konvertieren
                        player.sendMessage("Du bist nicht in der Nähe einer Kasse!");
                    }
                } else {
                    // Als Fehlermeldung konvertieren
                    player.sendMessage("Du musst mindestens Rang-2 sein um diesen Befehl zu verwenden!");
                }
            }
        }
        return true;
    }
}
