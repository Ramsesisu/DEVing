package org.rqmses;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.rqmses.commands.BuyCommand;

import java.util.Objects;

public final class DEVing extends JavaPlugin {

    @Override
    public void onEnable() {
        registerCommands();
        registerHandlers();

        // Lediglich provisorisch
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            BuyCommand.falcone.put(player.getUniqueId(), 2);
        }
    }

    public void registerCommands() {
        Objects.requireNonNull(this.getCommand("rob")).setExecutor(new BuyCommand());
    }

    public void registerHandlers() {
    }
}

