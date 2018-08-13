package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.HandItem;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.util.ItemUtil;
import net.pl3x.bukkit.ridables.util.ReflectionUtil;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftLivingEntity;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RideListener implements Listener {
    public static final Set<UUID> override = new HashSet<>();
    private final Ridables plugin;

    public RideListener(Ridables plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onRideCreature(PlayerInteractAtEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return; // dont fire twice
        }

        Entity creature = event.getRightClicked();
        if (creature.isDead() || !creature.isValid()) {
            return; // creature already removed from world
        }

        if (creature.getType() == EntityType.COMPLEX_PART) {
            creature = ((ComplexEntityPart) creature).getParent();
        }

        if (creature.getType() == EntityType.LLAMA) {
            return; // do not force mount llamas
        }

        if (!creature.getPassengers().isEmpty()) {
            return; // creature already has rider
        }

        RidableEntity ridable = RidableType.getRidable(creature);
        if (ridable == null) {
            return; // not a valid creature
        }

        Player player = event.getPlayer();
        if (player.isSneaking()) {
            return; // player is holding shift
        }

        if (player.getVehicle() != null) {
            return; // player already riding something
        }

        ItemStack mainHand = ItemUtil.getItem(player, EquipmentSlot.HAND);
        ItemStack offHand = ItemUtil.getItem(player, EquipmentSlot.OFF_HAND);
        if (mainHand.getType() == Material.LEAD || offHand.getType() == Material.LEAD) {
            return; // do not ride when trying to leash
        }

        if (ridable.isActionableItem(mainHand) || ridable.isActionableItem(offHand)) {
            return; // feed creature instead of riding it
        }

        if (creature instanceof Tameable) {
            AnimalTamer owner = ((Tameable) creature).getOwner();
            if (owner == null || !player.getUniqueId().equals(owner.getUniqueId())) {
                return; // player doesnt own this creature
            }
        }

        if (!player.hasPermission("allow.ride." + creature.getType().name().toLowerCase())) {
            Lang.send(player, Lang.RIDE_NO_PERMISSION);
            return;
        }

        if (Config.REQUIRE_SADDLE) {
            HandItem saddle = ItemUtil.getItem(player, Material.SADDLE);
            if (saddle == null) {
                return; // saddle is required
            }
            if (Config.CONSUME_SADDLE) {
                ItemUtil.setItem(player, saddle.subtract(), saddle.getHand());
            }
        }

        // add player as rider
        override.add(player.getUniqueId());
        creature.addPassenger(player);
        override.remove(player.getUniqueId());
        ReflectionUtil.setJumping(((CraftLivingEntity) player).getHandle());
    }


    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (override.contains(player.getUniqueId())) {
            return; // overridden
        }

        Entity vehicle = player.getVehicle();
        if (vehicle == null) {
            return; // not riding
        }

        RidableEntity ridable = RidableType.getRidable(vehicle);
        if (ridable == null) {
            return; // not ridable
        }

        player.leaveVehicle();
        if (!Config.UNMOUNT_ON_TELEPORT) {
            vehicle.teleport(event.getTo());
            new BukkitRunnable() {
                @Override
                public void run() {
                    vehicle.addPassenger(player);
                }
            }.runTask(plugin);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (!Config.CANCEL_COMMANDS_WHILE_RIDING) {
            return; // disabled feature
        }

        Player player = event.getPlayer();
        if (RidableType.getRidableType(player.getVehicle().getType()) == null) {
            return; // not a valid creature
        }

        // disable commands while riding
        Lang.send(player, Lang.DISABLED_COMMANDS_WHILE_RIDING);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // ensure player unmounts creature so it doesn't despawn with player
        event.getPlayer().leaveVehicle();
    }
}
