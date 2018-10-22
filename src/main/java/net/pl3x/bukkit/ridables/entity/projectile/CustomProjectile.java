package net.pl3x.bukkit.ridables.entity.projectile;

import net.pl3x.bukkit.ridables.entity.RidableEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

public interface CustomProjectile {
    RidableEntity getRidable();

    Mob getMob();

    Player getRider();
}
