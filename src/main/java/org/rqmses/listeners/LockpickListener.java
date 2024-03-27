package org.rqmses.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LockpickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getOpenInventory().title().equals(Component.text("Kasse"))) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            String display = Objects.requireNonNull(((TextComponent) Objects.requireNonNull(event.getCurrentItem()).getItemMeta().displayName())).content();
            if (Objects.requireNonNull(event.getCurrentItem()).getType() == Material.HOPPER) {
                int n = Integer.parseInt(Objects.requireNonNull(((TextComponent) event.getCurrentItem().getItemMeta().displayName())).content().substring(2, 3));
                if (n == a) {
                    if (a == c) {
                        if (c > 7) {
                            // Nachricht an alle Frakmitglieder -> Gelungen
                            event.getInventory().close();
                            win(player);
                        } else {
                            a = 1;
                            c += 1;
                            next(player);
                        }
                    } else {
                        a += 1;
                        refresh(player, event.getSlot() - 10);
                    }
                } else {
                    if (f == 0) {
                        // Alarm an alle Polizisten mit BizName
                    }
                    if (f == 2) {
                        // Nachricht an alle Frakmitglieder -> Fehlgeschlagen
                        event.getInventory().close();
                    } else {
                        f += 1;
                        a = 1;
                        c -= 1;
                        next(player);
                    }
                }
            } else if (display.endsWith("€")) {
                int v = Integer.parseInt(display.substring(0, display.lastIndexOf("€")));
                value += v;
                // Passend formatieren
                player.sendMessage("Du hast " + v + "€ aus der Kasse genommen.");

                player.getInventory().close();
                checkout.setItem(event.getSlot(), air);
                player.openInventory(checkout);
            }
        }
    }

    @EventHandler
    public void onPickup(InventoryPickupItemEvent event) {
        if (((Player) Objects.requireNonNull(event.getInventory().getHolder())).getOpenInventory().title().equals(Component.text("Kasse"))) {
            event.setCancelled(true);
        }
    }

    public static int c = 0;
    public static int a = 0;
    public static int f = 0;

    public static Integer value;
    private static Inventory lock;
    private static Inventory checkout;
    private static List<ItemStack> upper = new ArrayList<>();
    private static List<ItemStack> middle = new ArrayList<>();
    private static List<ItemStack> lower = new ArrayList<>();
    private static List<Integer> sequence = new ArrayList<>();

    private static final ItemStack air = new ItemStack(Material.AIR, 1);
    private static final ItemStack keys = new ItemStack(Material.HOPPER, 1);
    private static final ItemStack frame = new ItemStack(Material.IRON_BARS, 1);

    private static void show(Player player) {
        for (int n = 0; n < 27; n++) {
            if (n > 0 && n < 9) {
                lock.setItem(n, upper.get(n - 1));
            }
            if (n == 9) {
                lock.setItem(n, middle.get(0));
            }
            if (n > 9 && n < 18) {
                if (sequence.size() > (n - 10)) {
                    if (middle.get(n - 9) == air) {
                        lock.setItem(n, air);
                    } else {
                        ItemStack key = middle.get(n - 9);
                        ItemMeta keysmeta = key.getItemMeta();
                        keysmeta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', "&" + (sequence.get(n - 10) + 1) + sequence.get(n - 10))));
                        key.setItemMeta(keysmeta);
                        lock.setItem(n, key);
                    }
                }
            }
            if (n > 18) {
                lock.setItem(n, lower.get(n - 19));
            }
        }

        player.openInventory(lock);
    }

    private static void refresh(Player player, int i) {
        ItemMeta keysmeta = keys.getItemMeta();
        keysmeta.displayName(Component.text("Zylinder").color(TextColor.color(Color.SILVER.asRGB())));
        keys.setItemMeta(keysmeta);
        if (upper.get(i) == air) {
            upper.set(i, keys);
        } else {
            lower.set(i, keys);
        }
        middle.set(i + 1, air);

        show(player);
    }

    public static void next(Player player) {
        lock = Bukkit.createInventory(player, 27, Component.text("Kasse"));

        upper = new ArrayList<>();
        middle = new ArrayList<>();
        lower = new ArrayList<>();
        sequence = new ArrayList<>();

        ItemStack block = new ItemStack(Material.IRON_BLOCK);
        ItemMeta blockmeta = block.getItemMeta();
        blockmeta.displayName(Component.text("Dietrich"));
        block.setItemMeta(blockmeta);
        middle.add(block);
        ItemMeta framemeta = frame.getItemMeta();
        framemeta.displayName(Component.text("Schloss").color(TextColor.color(Color.GRAY.asRGB())));
        frame.setItemMeta(framemeta);
        for (int l = 0; l < 8; l++) {
            upper.add(frame);
            if (l < c) {
                middle.add(keys);
            } else {
                middle.add(air);
            }
            lower.add(frame);
        }
        for (int k = 0; k < c; k++) {
            if (new Random().nextInt(2) == 0) {
                upper.set(k, air);
            } else {
                lower.add(k, air);
            }

            sequence.add(k + 1);
        }
        Collections.shuffle(sequence);

        show(player);
    }

    private static void win(Player player) {
        checkout = Bukkit.createInventory(player, 36, Component.text("Kasse"));
        for (int m = 0; m < 36; m++) {
            float x = new Random().nextFloat();
            int v;
            ItemStack money;
            if (x < 0.30) {
                v = 10;
                money = new ItemStack(Material.RED_BANNER);
            } else if (x < 0.5) {
                v = 20;
                money = new ItemStack(Material.BLUE_BANNER);
            } else if (x < 0.7) {
                v = 50;
                money = new ItemStack(Material.ORANGE_BANNER);
            } else if (x < 0.85) {
                v = 100;
                money = new ItemStack(Material.GREEN_BANNER);
            } else if (x < 0.95) {
                v = 200;
                money = new ItemStack(Material.YELLOW_BANNER);
            } else {
                v = 500;
                money = new ItemStack(Material.PURPLE_BANNER);
            }
            ItemMeta moneymeta = money.getItemMeta();
            moneymeta.displayName(Component.text(v + "€").color(TextColor.color(Color.OLIVE.asRGB())));
            money.setItemMeta(moneymeta);
            checkout.setItem(m, money);
        }

        player.openInventory(checkout);
    }
}
