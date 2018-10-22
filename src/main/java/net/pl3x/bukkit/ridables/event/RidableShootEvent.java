package net.pl3x.bukkit.ridables.event;

import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.projectile.CustomProjectile;

public abstract class RidableShootEvent extends RidableEvent {
    private final CustomProjectile projectile;

    public RidableShootEvent(RidableEntity entity, CustomProjectile projectile) {
        super(entity);
        this.projectile = projectile;
    }

    public CustomProjectile getProjectile() {
        return projectile;
    }
}
