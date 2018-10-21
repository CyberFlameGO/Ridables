package net.pl3x.bukkit.ridables.listener;

import net.minecraft.server.v1_13_R2.EnumHand;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.entity.RidableBlaze;
import net.pl3x.bukkit.ridables.entity.RidableCreeper;
import net.pl3x.bukkit.ridables.entity.RidableEnderDragon;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableGhast;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.projectile.CustomFireball;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class RidableListener implements Listener {
    @EventHandler
    public void onClickEnderDragon(PlayerInteractAtEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return; // dont fire twice
        }

        Entity dragon = event.getRightClicked();
        if (dragon.isDead() || !dragon.isValid() || dragon.getType() != EntityType.COMPLEX_PART) {
            return; // not a valid dragon entity
        }

        RidableEntity ridable = RidableType.getRidable(((ComplexEntityPart) dragon).getParent());
        if (ridable == null) {
            return; // ridable dragon not enabled
        }

        // processInteract on dragon
        RidableEnderDragon nmsDragon = (RidableEnderDragon) ridable;
        if (nmsDragon.a(((CraftPlayer) event.getPlayer()).getHandle(), EnumHand.MAIN_HAND)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFireballExplosionDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.FIREBALL) {
            return; // not a fireball
        }

        net.minecraft.server.v1_13_R2.Entity nmsEntity = ((CraftEntity) event.getDamager()).getHandle();
        if (!(nmsEntity instanceof CustomFireball)) {
            return; // not our custom fireball
        }

        CustomFireball fireball = (CustomFireball) nmsEntity;
        if (fireball.getEntity() instanceof RidableGhast) {
            if (fireball.getRider() == null) {
                event.setDamage(EntityDamageEvent.DamageModifier.BASE, RidableGhast.CONFIG.AI_FIREBALL_EXPLOSION_DAMAGE);
            } else {
                event.setDamage(EntityDamageEvent.DamageModifier.BASE, RidableGhast.CONFIG.SHOOT_FIREBALL_EXPLOSION_DAMAGE);
            }
        } else if (fireball.getEntity() instanceof RidableBlaze) {
            if (fireball.getRider() == null) {
                event.setDamage(EntityDamageEvent.DamageModifier.BASE, RidableBlaze.CONFIG.AI_SHOOT_EXPLOSION_DAMAGE);
            } else {
                event.setDamage(EntityDamageEvent.DamageModifier.BASE, RidableBlaze.CONFIG.RIDING_SHOOT_EXPLOSION_DAMAGE);
            }
        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        RidableEntity ridable = RidableType.getRidable(event.getDamager());
        if (ridable == null) {
            return; // not caused by a ridable
        }

        if (ridable.getRider() == null) {
            return; // no rider present
        }

        if (ridable.getType() == RidableType.CREEPER) {
            event.setDamage(EntityDamageEvent.DamageModifier.BASE, RidableCreeper.CONFIG.EXPLOSION_DAMAGE);
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
