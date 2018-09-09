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
import org.bukkit.scheduler.BukkitRunnable;

public class RidableListener implements Listener {
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
