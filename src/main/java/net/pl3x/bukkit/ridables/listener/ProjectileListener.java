package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.projectile.EntityGhastFireball;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ProjectileListener implements Listener {
    @EventHandler
    public void onGhastFireballExplode(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager.getType() != EntityType.FIREBALL) {
            return; // not a fireball
        }

        net.minecraft.server.v1_13_R1.Entity nmsEntity = ((CraftEntity) damager).getHandle();
        if (!(nmsEntity instanceof EntityGhastFireball)) {
            return; // not a ridable ghast's fireball
        }

        event.setDamage(Config.GHAST_SHOOT_DAMAGE);
    }
}
