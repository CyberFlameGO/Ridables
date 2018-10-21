package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class BatConfig extends MobConfig {
    public double BASE_SPEED = 0.7D;
    public double MAX_HEALTH = 6.0D;
    public double RIDING_SPEED = 1.0D;
    public double RIDING_VERTICAL = 1.0F;
    public double RIDING_GRAVITY = 0.04F;
    public boolean RIDING_RIDE_IN_WATER = true;

    public BatConfig() {
        super("bat.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.vertical", RIDING_VERTICAL);
            addDefault("riding.gravity", RIDING_GRAVITY);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_VERTICAL = getDouble("riding.vertical");
        RIDING_GRAVITY = getDouble("riding.gravity");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
    }
}
