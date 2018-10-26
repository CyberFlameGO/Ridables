package net.pl3x.bukkit.ridables.event;

import net.pl3x.bukkit.ridables.entity.monster.RidableBlaze;
import net.pl3x.bukkit.ridables.entity.projectile.CustomFireball;

public class BlazeShootFireballEvent extends RidableShootEvent {
    public BlazeShootFireballEvent(RidableBlaze blaze, CustomFireball fireball) {
        super(blaze, fireball);
    }

    @Override
    public RidableBlaze getRidable() {
        return (RidableBlaze) entity;
    }

    @Override
    public CustomFireball getProjectile() {
        return (CustomFireball) super.getProjectile();
    }
}
