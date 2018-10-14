package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class GhastConfig extends MobConfig {
    public float SPEED = 1.0F;
    public boolean RIDABLE_IN_WATER = true;
    public int SHOOT_COOLDOWN = 20;
    public float SHOOT_SPEED = 1.0F;
    public float SHOOT_DAMAGE = 5.0F;
    public boolean SHOOT_GRIEF = true;

    public GhastConfig() {
        super("ghast.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("speed", SPEED);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            addDefault("shoot.cooldown", SHOOT_COOLDOWN);
            addDefault("shoot.speed", SHOOT_SPEED);
            addDefault("shoot.damage", SHOOT_DAMAGE);
            addDefault("shoot.grief", SHOOT_GRIEF);
            save();
        }

        SPEED = (float) getDouble("speed");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        SHOOT_COOLDOWN = (int) getDouble("shoot.cooldown");
        SHOOT_SPEED = (float) getDouble("shoot.speed");
        SHOOT_DAMAGE = (float) getDouble("shoot.damage");
        SHOOT_GRIEF = getBoolean("shoot.grief");
    }
}
