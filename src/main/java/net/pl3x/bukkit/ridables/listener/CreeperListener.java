package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class CreeperListener implements Listener {
    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (Config.CREEPER_EXPLOSION_GRIEF) {
            return; // explosion griefing is allowed
        }

        if (event.getEntityType() != EntityType.CREEPER) {
            return; // not a creeper
        }

        RidableEntity ridable = RidableType.getRidable(event.getEntity());
        if (ridable == null) {
            return; // not a ridable creeper
        }

        if (ridable.getRider() == null) {
            return; // no rider present
        }

        // prevent block damage
        event.blockList().clear();
    }

    @EventHandler
    public void onCreeperHurtPlayer(EntityDamageByEntityEvent event) {
        if (Config.CREEPER_EXPLOSION_DAMAGE < 0) {
            return; // let the game handle damage
        }

        Entity damager = event.getDamager();
        if (damager.getType() != EntityType.CREEPER) {
            return; // not a creeper
        }

        RidableEntity ridable = RidableType.getRidable(damager);
        if (ridable == null) {
            return; // not a ridable creeper
        }

        if (ridable.getRider() == null) {
            return; // no rider present
        }

        event.setDamage(Config.CREEPER_EXPLOSION_DAMAGE);
    }
}
