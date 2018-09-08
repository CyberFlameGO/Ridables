package net.pl3x.bukkit.ridables.listener;

import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RidableListener implements Listener {
    public static final Set<UUID> TP_OVERRIDE = new HashSet<>();

    private final Ridables plugin;

    public RidableListener(Ridables plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        if (RidableType.getRidable(entity) != null) {
            return; // already ridable
        }

        RidableType ridableType = RidableType.getRidableType(event.getEntityType());
        if (ridableType == null) {
            return; // not a valid ridable
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                net.minecraft.server.v1_13_R2.Entity oldEntity = ((CraftEntity) entity).getHandle();
                net.minecraft.server.v1_13_R2.Entity newEntity = ridableType.spawn(event.getLocation(),
                        oldEntity instanceof EntityAgeable && ((EntityAgeable) oldEntity).isBaby());
                newEntity.v(oldEntity); // copyDataFromOld
                entity.remove();
            }
        }.runTaskLater(plugin, 1);
    }

    @EventHandler
    public void onExplosionDamageEntity(EntityDamageByEntityEvent event) {
        RidableEntity ridable = RidableType.getRidable(event.getDamager());
        if (ridable == null) {
            return; // not caused by a ridable
        }

        if (ridable.getRider() == null) {
            return; // no rider present
        }

        if (ridable.getType() == RidableType.CREEPER) {
            event.setDamage(EntityDamageEvent.DamageModifier.BASE, Config.CREEPER_EXPLOSION_DAMAGE);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (TP_OVERRIDE.contains(player.getUniqueId())) {
            return; // overridden
        }

        Entity vehicle = player.getVehicle();
        if (vehicle == null) {
            return; // not riding
        }

        player.leaveVehicle(); // always exit vehicle (fixes random teleport bug)

        RidableEntity ridable = RidableType.getRidable(vehicle);
        if (ridable == null) {
            switch (vehicle.getType()) {
                case DONKEY:
                case HORSE:
                case MULE:
                    if (!player.hasPermission("allow.teleport." + vehicle.getType().name().toLowerCase())) {
                        return; // no permission
                    }
                    break;
                default:
                    return; // not ridable
            }
        } else if (!ridable.hasTeleportPerm(player)) {
            return; // no permission
        }

        if (!Config.UNMOUNT_ON_TELEPORT) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    // delay vehicle teleport to ensure player is not still on it
                    vehicle.teleport(event.getTo());
                }
            }.runTaskLater(plugin, 10);
            new BukkitRunnable() {
                @Override
                public void run() {
                    // delay adding rider back to ensure client has received new vehicle location
                    TP_OVERRIDE.add(player.getUniqueId());
                    vehicle.addPassenger(player);
                    TP_OVERRIDE.remove(player.getUniqueId());
                }
            }.runTaskLater(plugin, 20);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (!Config.CANCEL_COMMANDS_WHILE_RIDING) {
            return; // disabled feature
        }

        Player player = event.getPlayer();
        Entity vehicle = player.getVehicle();
        if (vehicle == null) {
            return; // not riding a creature
        }

        if (RidableType.getRidableType(vehicle.getType()) == null) {
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
