package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class BatConfig extends MobConfig {
    public double BASE_SPEED = 1.0D;
    public double RIDE_SPEED = 1.0D;
    public double MAX_HEALTH = 6.0D;
    public double VERTICAL = 1.0F;
    public double GRAVITY = 0.04F;
    public boolean RIDABLE_IN_WATER = true;

    public BatConfig() {
        super("bat.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("ride-speed", RIDE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("vertical", VERTICAL);
            addDefault("gravity", GRAVITY);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        RIDE_SPEED = getDouble("ride-speed");
        MAX_HEALTH = getDouble("max-health");
        VERTICAL = getDouble("vertical");
        GRAVITY = getDouble("gravity");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
    }
}
