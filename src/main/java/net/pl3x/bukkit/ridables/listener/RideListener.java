package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.util.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RideListener implements Listener {
    private final Ridables plugin;
    private final Set<UUID> override = new HashSet<>();

    public RideListener(Ridables plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onRideCreature(PlayerInteractAtEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return; // dont fire twice
        }

        Entity creature = event.getRightClicked();
        if (!plugin.creatures().isEnabled(creature)) {
            return; // not a valid creature
        }

        if (!creature.getPassengers().isEmpty()) {
            return; // creature already has rider
        }

        Player player = event.getPlayer();
        if (player.getVehicle() != null) {
            return; // player already riding something
        }

        if (Utils.isFood(creature.getType(), Utils.getItem(player, event.getHand()))) {
            return; // feed creature instead of riding it
        }

        if (!player.hasPermission("allow.ride." + creature.getType().name().toLowerCase())) {
            Lang.send(player, Lang.RIDE_NO_PERMISSION);
            return;
        }

        // add player as rider
        override.add(player.getUniqueId());
        creature.addPassenger(player);
        override.remove(player.getUniqueId());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!Config.UMNOUNT_ON_TELEPORT) {
            return; // disabled feature
        }

        Player player = event.getPlayer();
        if (override.contains(player.getUniqueId())) {
            return; // overridden
        }

        Entity creature = player.getVehicle();
        if (!plugin.creatures().isEnabled(creature)) {
            return; // not a valid creature
        }

        // eject player before teleportation
        creature.eject();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (!Config.CANCEL_COMMANDS_WHILE_RIDING) {
            return; // disabled feature
        }

        Player player = event.getPlayer();
        Entity creature = player.getVehicle();
        if (!plugin.creatures().isEnabled(creature)) {
            return; // not a valid creature
        }

        // disable commands while riding
        Lang.send(player, Lang.DISABLED_COMMANDS_WHILE_RIDING);
        event.setCancelled(true);
    }
}
