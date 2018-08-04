package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Lang;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class RideListener implements Listener {
    private final Ridables plugin;

    public RideListener(Ridables plugin) {
        this.plugin = plugin;
    }

    // use high priority to avoid
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onRideCreature(PlayerInteractAtEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return; // dont fire twice
        }

        Entity creature = event.getRightClicked();
        if (!plugin.creatures().isEnabled(creature.getType())) {
            return; // not a valid creature
        }

        if (!creature.getPassengers().isEmpty()) {
            return; // creature already has rider
        }

        Player player = event.getPlayer();
        if (player.getVehicle() != null) {
            return; // player already riding something
        }

        if (!player.hasPermission("allow.ride." + creature.getType().name().toLowerCase())) {
            Lang.send(player, Lang.RIDE_NO_PERMISSION);
            return;
        }

        // add player as rider
        creature.addPassenger(player);
    }
}
