package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.Ridables;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DismountListener implements Listener {
    private final Ridables plugin;
    private final Set<UUID> override = new HashSet<>();

    public DismountListener(Ridables plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDismountCreature(EntityDismountEvent event) {
        Entity creature = event.getDismounted();
        switch (creature.getType()) {
            case DOLPHIN:
            case TURTLE:
                break;
            default:
                return; // not a water creature
        }

        if (creature.isDead()) {
            return; // creature died
        }

        if (event.getEntity().getType() != EntityType.PLAYER) {
            return; // not a player
        }

        Player player = (Player) event.getEntity();
        if (override.contains(player.getUniqueId())) {
            return; // overridden
        }

        if (player.isSneaking()) {
            return; // dismount from shift
        }

        if (player.isDead()) {
            return; // player died
        }

        // cancel dismount
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // ensure player unmounts creature so it doesn't despawn with player
        override.add(player.getUniqueId());
        player.leaveVehicle();
        override.remove(player.getUniqueId());
    }
}
