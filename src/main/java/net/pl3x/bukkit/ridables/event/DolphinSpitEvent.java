package net.pl3x.bukkit.ridables.event;

import net.pl3x.bukkit.ridables.entity.RidableDolphin;
import net.pl3x.bukkit.ridables.entity.projectile.DolphinSpit;

public class DolphinSpitEvent extends RidableShootEvent {
    public DolphinSpitEvent(RidableDolphin dolphin, DolphinSpit spit) {
        super(dolphin, spit);
    }

    @Override
    public RidableDolphin getRidable() {
        return (RidableDolphin) entity;
    }

    @Override
    public DolphinSpit getProjectile() {
        return (DolphinSpit) super.getProjectile();
    }
}
