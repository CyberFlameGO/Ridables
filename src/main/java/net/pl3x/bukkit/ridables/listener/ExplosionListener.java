package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class ExplosionListener implements Listener {
    @EventHandler
    public void onExplosionDamageEntity(EntityDamageByEntityEvent event) {
        RidableEntity ridable = RidableType.getRidable(event.getDamager());
        if (ridable == null) {
            return; // not caused by a ridable
        }

        if (ridable.getRider() == null) {
            return; // no rider present
        }

        RidableType type = ridable.getType();
        if (type == RidableType.CREEPER) {
            event.setDamage(EntityDamageEvent.DamageModifier.BASE, Config.CREEPER_EXPLOSION_DAMAGE);
        }
    }
}
