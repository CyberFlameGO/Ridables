package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.util.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class WaterBucketListener implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCollectCreature(PlayerInteractAtEntityEvent event) {
        Entity creature = event.getRightClicked();
        if (creature.isDead() || !creature.isValid()) {
            return; // creature already removed from world
        }

        Bucket bucket = Bucket.getBucket(creature.getType());
        if (bucket == null) {
            return; // not a supported creature
        }

        if (!creature.getPassengers().isEmpty()) {
            return; // creature has a rider
        }

        Player player = event.getPlayer();
        ItemStack hand = Utils.getItem(player, event.getHand());
        if (hand == null || hand.getType() != Material.WATER_BUCKET) {
            return; // not a water bucket
        }

        Entity vehicle = player.getVehicle();
        if (vehicle != null && vehicle.getUniqueId().equals(creature.getUniqueId())) {
            return; // player is riding this creature
        }

        if (!player.hasPermission("allow.collect." + creature.getType().name().toLowerCase())) {
            Lang.send(player, Lang.COLLECT_NO_PERMISSION);
            return;
        }

        // remove creature
        creature.remove();
        System.out.println("ding");

        // give player creature's bucket
        Utils.setItem(player, bucket.getItemStack(), event.getHand());

        // prevent water from placing
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlaceCreature(PlayerBucketEmptyEvent event) {
        if (event.getBucket() != Material.COD_BUCKET) {
            return; // not a valid creature bucket
        }

        // get the bucket used
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        EquipmentSlot hand = EquipmentSlot.HAND;
        Bucket bucket = Bucket.getBucket(itemStack);
        if (bucket == null) {
            itemStack = player.getInventory().getItemInOffHand();
            hand = EquipmentSlot.OFF_HAND;
            bucket = Bucket.getBucket(itemStack);
            if (bucket == null) {
                return; // not a valid creature bucket
            }
        }

        RidableType ridable = RidableType.getRidable(bucket.getEntityType());
        if (ridable == null) {
            return; // not a valid creature
        }

        // spawn the creature
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());
        ridable.spawn(Utils.buildLocation(block.getLocation(), player.getLocation()));

        // handle the bucket in hand
        if (player.getGameMode() != GameMode.CREATIVE) {
            Utils.setItem(player, Utils.subtract(itemStack), hand);
        }

        // place water at location
        block.setType(Material.WATER, true);

        // do not spawn a cod!
        event.setCancelled(true);
    }
}
