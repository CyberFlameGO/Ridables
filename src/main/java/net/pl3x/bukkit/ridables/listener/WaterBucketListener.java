package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.util.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Cod;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WaterBucketListener implements Listener {
    public static final Set<UUID> override = new HashSet<>();

    private final Ridables plugin;

    public WaterBucketListener(Ridables plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCollectCreature(PlayerInteractAtEntityEvent event) {
        Entity creature = event.getRightClicked();
        if (creature.isDead() || !creature.isValid()) {
            return; // creature already removed from world
        }

        RidableType ridable = RidableType.getRidableType(creature.getType());
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
        ItemStack hand = ItemUtil.getItem(player, event.getHand());
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
        ItemUtil.setItem(player, bucket.getItemStack(), event.getHand());

        // prevent water from placing in PlayerBucketEmptyEvent which fires right after this
        override.add(player.getUniqueId());

        // remove override on next tick in case PlayerBucketEmptyEvent doesnt fire
        new BukkitRunnable() {
            @Override
            public void run() {
                override.remove(player.getUniqueId());
            }
        }.runTaskLater(plugin, 1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlaceCreature(PlayerBucketEmptyEvent event) {
        if (event.getBucket() != Material.COD_BUCKET) {
            return; // not a valid creature bucket
        }

        Player player = event.getPlayer();
        if (override.remove(player.getUniqueId())) {
            // this prevents the new cod bucket from emptying water in the same right click as collecting the creature
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

        RidableType ridable = RidableType.getRidableType(bucket.getEntityType());
        if (ridable == null) {
            return; // not a valid creature
        }

        // spawn the creature
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());
        Location loc = block.getLocation();
        loc.setX(loc.getBlockX() + 0.5);
        loc.setY(loc.getBlockY() + 0.5);
        loc.setZ(loc.getBlockZ() + 0.5);
        loc.setYaw(player.getLocation().getYaw());
        loc.setPitch(player.getLocation().getPitch());
        ridable.spawn(loc);

        // handle the bucket in hand
        ItemUtil.subtract(itemStack);
        if (itemStack.getAmount() <= 0) {
            // replace with empty bucket
            ItemUtil.setItem(player, new ItemStack(Material.BUCKET), hand);
        } else {
            // add subtracted amount back
            ItemUtil.setItem(player, itemStack, hand);
            // add empty bucket to inventory
            player.getInventory().addItem(new ItemStack(Material.BUCKET))
                    // or drop to ground if inventory is full
                    .values().forEach(leftover -> player.getWorld().dropItem(player.getLocation(), leftover));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpawnCodFish(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.COD) {
            return; // not a cod
        }

        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            return; // not from a bucket
        }

        Cod codFish = (Cod) event.getEntity();
        if (Bucket.isFromBucket(codFish)) {
            event.setCancelled(true);
        }
    }
}
