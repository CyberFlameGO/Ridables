package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.event.ProjectileCollideEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;

public class ProjectileListener implements Listener {
    @EventHandler
    public void onHitRidableCreature(ProjectileCollideEvent event) {
        Entity creature = event.getCollidedWith();
        List<Entity> passengers = creature.getPassengers();
        if (passengers.isEmpty()) {
            return; // creature doesnt have a rider
        }

        RidableType ridable = RidableType.getRidableType(creature.getType());
        if (ridable == null) {
            return; // did not hit a ridable entity
        }

        ProjectileSource source = event.getEntity().getShooter();
        if (passengers.get(0) != source) {
            return; // was not shot by it's own rider
        }

        // do not allow rider to shoot its creature
        event.setCancelled(true);
    }
}
