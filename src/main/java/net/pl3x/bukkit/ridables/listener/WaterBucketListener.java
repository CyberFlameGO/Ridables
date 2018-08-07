package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WaterBucketListener implements Listener {
    public static final Set<UUID> override = new HashSet<>();

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCollectCreature(PlayerInteractAtEntityEvent event) {
        Entity creature = event.getRightClicked();
        if (creature.isDead() || !creature.isValid()) {
            return; // creature already removed from world
        }

        RidableType ridable = RidableType.getRidable(creature.getType());
        if (ridable == null) {
            return; // not a supported creature
        }

        Bucket bucket = ridable.getWaterBucket();
        if (bucket == null) {
            return; // creature doesnt support water buckets
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

        // give player creature's bucket
        Utils.setItem(player, bucket.getItemStack(), event.getHand());

        // prevent water from placing in PlayerBucketEmptyEvent which fires right after this
        override.add(player.getUniqueId());

        // remove override on next tick in case PlayerBucketEmptyEvent doesnt fire
        Bukkit.getScheduler().runTaskLater(Ridables.getPlugin(Ridables.class),
                () -> override.remove(player.getUniqueId()), 1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlaceCreature(PlayerBucketEmptyEvent event) {
        if (event.getBucket() != Material.COD_BUCKET) {
            return; // not a valid creature bucket
        }

        Player player = event.getPlayer();
        if (override.remove(player.getUniqueId())) {
            // this prevents the new cod bucket from emptying water in the same right click
            // bug in CraftBukkit create a desync block of water here
            // https://hub.spigotmc.org/jira/browse/SPIGOT-4238
            event.setCancelled(true);
            return;
        }

        // get the bucket used
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
        Utils.subtract(itemStack);
        if (itemStack.getAmount() <= 0) {
            // replace with empty bucket
            Utils.setItem(player, new ItemStack(Material.BUCKET), hand);
            System.out.println("1");
        } else {
            System.out.println("2");
            // add subtracted amount back
            Utils.setItem(player, itemStack, hand);
            // add empty bucket to inventory
            player.getInventory().addItem(new ItemStack(Material.BUCKET))
                    // or drop to ground if inventory is full
                    .values().forEach(leftover -> player.getWorld().dropItem(player.getLocation(), leftover));
        }
    }

    @EventHandler
    public void onSpecialFishSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            return; // not spawned from a bucket (considered egg)
        }
        if (event.getEntityType() != EntityType.COD) {
            return; // not our special bucket
        }
        Bucket.BUCKETS.forEach(bucket -> {
            if (event.getEntity().getCustomName().equals(bucket.getItemStack().getItemMeta().getDisplayName())) {
                event.setCancelled(true); // do not spawn a cod from special bucket!
            }
        });
    }
}
