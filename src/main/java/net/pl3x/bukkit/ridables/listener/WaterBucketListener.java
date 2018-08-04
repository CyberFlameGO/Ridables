package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Lang;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class WaterBucketListener implements Listener {
    private final Ridables plugin;

    public WaterBucketListener(Ridables plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCollectCreature(PlayerInteractAtEntityEvent event) {
        Entity creature = event.getRightClicked();
        if (creature.isDead() || !creature.isValid()) {
            return; // creature already removed from world
        }

        ItemStack bucket = plugin.getBuckets().getBucket(creature.getType());
        if (bucket == null) {
            return; // not a supported creature
        }

        if (!creature.getPassengers().isEmpty()) {
            return; // creature has a rider
        }

        Player player = event.getPlayer();
        ItemStack hand = getItem(player, event.getHand());
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

        // give player creature's bucket
        setItem(player, bucket.clone(), event.getHand());

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
        ItemStack bucket = player.getInventory().getItemInMainHand();
        EntityType entityType = plugin.getBuckets().getEntityType(bucket);
        EquipmentSlot hand = EquipmentSlot.HAND;
        if (entityType == null) {
            bucket = player.getInventory().getItemInOffHand();
            entityType = plugin.getBuckets().getEntityType(bucket);
            hand = EquipmentSlot.OFF_HAND;
            if (entityType == null) {
                return; // not a valid creature bucket
            }
        }

        // spawn the creature
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());
        if (plugin.creatures().spawn(entityType, buildLocation(block.getLocation(), player.getLocation()))) {
            // handle the bucket in hand
            if (player.getGameMode() != GameMode.CREATIVE) {
                bucket.setAmount(Math.max(0, bucket.getAmount() - 1));
                setItem(player, bucket, hand);
            }

            // place water at location
            block.setType(Material.WATER, true);
        }

        // do not spawn a cod!
        event.setCancelled(true);
    }

    // methods to help spigot compatibility

    private ItemStack getItem(Player player, EquipmentSlot hand) {
        return hand == EquipmentSlot.OFF_HAND ?
                player.getInventory().getItemInOffHand() :
                player.getInventory().getItemInMainHand();
    }

    private void setItem(Player player, ItemStack itemStack, EquipmentSlot hand) {
        if (hand == EquipmentSlot.OFF_HAND) {
            player.getInventory().setItemInOffHand(itemStack);
        } else {
            player.getInventory().setItemInMainHand(itemStack);
        }
    }

    private Location buildLocation(Location loc, Location pLoc) {
        loc.setX(loc.getBlockX() + 0.5);
        loc.setY(loc.getBlockY() + 0.5);
        loc.setZ(loc.getBlockZ() + 0.5);
        loc.setYaw(pLoc.getYaw());
        loc.setPitch(pLoc.getPitch());
        return loc;
    }
}
