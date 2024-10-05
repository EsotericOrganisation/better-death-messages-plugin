package org.esoteric_organisation.better_death_messages_plugin.event;

import org.bukkit.entity.Cat;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.esoteric_organisation.better_death_messages_plugin.BetterDeathMessagesPlugin;

public class EntityDamageListener implements Listener {

    private final BetterDeathMessagesPlugin plugin;

    public EntityDamageListener(BetterDeathMessagesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Tameable tamableEntity)) {
            return;
        }

        if (!(tamableEntity instanceof Cat || tamableEntity instanceof Wolf || tamableEntity instanceof Parrot)) {
            return;
        }

        if (event.getDamage() >= tamableEntity.getHealth()) {
            tamableEntity.setOwner(null);

            PersistentDataContainer dataContainer = tamableEntity.getPersistentDataContainer();
            dataContainer.set(plugin.getShouldAnnounceEntityDeathKey(), PersistentDataType.BOOLEAN, true);
        }
    }
}
