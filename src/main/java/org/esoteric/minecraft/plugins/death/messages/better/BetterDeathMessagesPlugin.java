package org.esoteric.minecraft.plugins.death.messages.better;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.esoteric.minecraft.plugins.death.messages.better.event.listeners.EntityDamageListener;
import org.esoteric.minecraft.plugins.death.messages.better.event.listeners.EntityDeathListener;
import org.esoteric.minecraft.plugins.death.messages.better.managers.EntityManager;

public final class BetterDeathMessagesPlugin extends JavaPlugin {

    private final NamespacedKey shouldAnnounceEntityDeathKey = new NamespacedKey(this, "should_announce_death");
    private JDA jda;
    private EntityManager entityManager;

    public JDA getJda() {
        return jda;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public NamespacedKey getShouldAnnounceEntityDeathKey() {
        return shouldAnnounceEntityDeathKey;
    }

    @Override
    public void onEnable() {
        YamlConfiguration configuration = (YamlConfiguration) getConfig();

        configuration.options().copyDefaults();
        saveDefaultConfig();

        ConfigurationSection discordMessageSettings = configuration.getConfigurationSection("messages.discord-messages");
        assert discordMessageSettings != null;

        boolean discordEnabled = discordMessageSettings.getBoolean("enabled");

        if (discordEnabled) {
            String token = discordMessageSettings.getString("bot-token");

            JDABuilder jdaBuilder = JDABuilder.createDefault(token);

            try {
                jda = jdaBuilder.build().awaitReady();
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }

        entityManager = new EntityManager(this);

        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(this), this);
    }
}
