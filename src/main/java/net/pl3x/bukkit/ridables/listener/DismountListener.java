package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.util.Logger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DismountListener implements Listener {
    public static final Set<UUID> override = new HashSet<>();

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

        // warn super old Spigot builds about the error which is about to happen
        if (!(event instanceof Cancellable)) {
            Logger.error("######################################################");
            Logger.error("# Your Spigot version is too old!                    #");
            Logger.error("# Please re-run BuildTools to get an updated server! #");
            Logger.error("# The version you are using does NOT contain a       #");
            Logger.error("# cancellable EntityDismountEvent!                   #");
            Logger.error("# Update your server for the following error to      #");
            Logger.error("# go away and make water creatures ridable!          #");
            Logger.error("######################################################");
            // do not return, let the error happen so it draws attention
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
