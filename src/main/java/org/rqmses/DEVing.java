package org.rqmses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.rqmses.commands.RobCommand;
import org.rqmses.listeners.LockpickListener;

import java.util.Objects;

public final class DEVing extends JavaPlugin {
    public static DEVing DEVING;

    @Override
    public void onEnable() {
        DEVING = this;

        registerCommands();
        registerHandlers();

        // Lediglich provisorisch
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            RobCommand.falcone.put(player.getUniqueId(), 2);
        }
        RobCommand.bizlist.put("Supermarkt", new Location(Bukkit.getWorld("world"), 0, 68, 0));
    }

    public void registerCommands() {
        Objects.requireNonNull(this.getCommand("rob")).setExecutor(new RobCommand());
    }

    public void registerHandlers() {
        Bukkit.getServer().getPluginManager().registerEvents(new LockpickListener(), this);
    }
}

