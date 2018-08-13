package net.pl3x.bukkit.ridables.listener;

import net.minecraft.server.v1_13_R1.EnumHand;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ClickListener implements Listener {
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return; // dont fire twice
        }

        Entity vehicle = event.getPlayer().getVehicle();
        if (vehicle == null) {
            return; // not riding
        }

        RidableEntity ridable = RidableType.getRidable(vehicle);
        if (ridable == null) {
            return; // not ridable
        }

        switch (event.getAction()) {
            case LEFT_CLICK_BLOCK:
                ridable.onClick(event.getClickedBlock(), EnumHand.MAIN_HAND);
                break;
            case RIGHT_CLICK_BLOCK:
                ridable.onClick(event.getClickedBlock(), EnumHand.OFF_HAND);
                break;
            case LEFT_CLICK_AIR:
                ridable.onClick(EnumHand.MAIN_HAND);
                break;
            case RIGHT_CLICK_AIR:
                ridable.onClick(EnumHand.OFF_HAND);
                break;
        }
    }

    @EventHandler
    public void onPlayerRightClickEntity(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return; // dont fire twice
        }

        Entity vehicle = event.getPlayer().getVehicle();
        if (vehicle == null) {
            return; // not riding
        }

        RidableEntity ridable = RidableType.getRidable(vehicle);
        if (ridable == null) {
            return; // not ridable
        }

        Entity clicked = event.getRightClicked();
        if (clicked == vehicle) {
            return; // clicked own vehicle
        }

        ridable.onClick(clicked, EnumHand.OFF_HAND);
    }

    @EventHandler
    public void onPlayerLeftClickEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return; // not a player
        }

        Entity vehicle = event.getDamager().getVehicle();
        if (vehicle == null) {
            return; // not riding
        }

        RidableEntity ridable = RidableType.getRidable(vehicle);
        if (ridable == null) {
            return; // not ridable
        }

        Entity clicked = event.getEntity();
        if (clicked == vehicle) {
            return; // clicked own vehicle
        }

        ridable.onClick(clicked, EnumHand.MAIN_HAND);
    }
}
