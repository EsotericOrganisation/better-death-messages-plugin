package org.rolypolyvole.villagerdeaths;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class VillagerDeaths extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onVillagerDeath(EntityDeathEvent event) {
        if (!event.getEntity().getType().equals(EntityType.VILLAGER)) {
            return;
        }

        boolean announceToAll = Objects.equals(getConfig().getString("announce-to"), "everyone");
        int announceRadius = Math.abs(getConfig().getInt("announce-radius"));

        Location location = event.getEntity().getLocation();

        int x = (int) Math.round(location.getX());
        int y = (int) Math.round(location.getY());
        int z = (int) Math.round(location.getZ());

        Component message = createMessage(x, y, z);

        if (announceToAll) {
            Bukkit.broadcast(message);
        } else {
            for (Player player : location.getWorld().getPlayers()) {
                if (location.distanceSquared(player.getLocation()) <= announceRadius * announceRadius) {
                    player.sendMessage(message);
                }
            }
        }
    }

    private Component createMessage(int x, int y, int z) {
        return Component.text()
            .append(Component.text("A villager has died!", NamedTextColor.YELLOW))
            .append(Component.space())
            .append(Component.text("[", TextColor.color(255, 165, 0)))
            .append(Component.text(x, NamedTextColor.GREEN))
            .append(Component.text(", ", NamedTextColor.WHITE))
            .append(Component.text(y, NamedTextColor.GREEN))
            .append(Component.text(", ", NamedTextColor.WHITE))
            .append(Component.text(z, NamedTextColor.GREEN))
            .append(Component.text("]", TextColor.color(255, 165, 0)))
            .build();

    }
}
