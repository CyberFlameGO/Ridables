package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class HorseConfig extends MobConfig {
    public double BASE_SPEED = -1.0D; // 0.1125–0.3375
    public double RIDE_SPEED = 1.0D;
    public double MAX_HEALTH = -1.0D; // 15-30
    public double JUMP_POWER = -1.0D; // 0.4-1.0
    public float STEP_HEIGHT = 1.0F;
    public boolean RIDABLE_IN_WATER = true;

    public HorseConfig() {
        super("horse.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("ride-speed", RIDE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("jump-power", JUMP_POWER);
            addDefault("step-height", STEP_HEIGHT);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        RIDE_SPEED = getDouble("ride-speed");
        MAX_HEALTH = getDouble("max-health");
        JUMP_POWER = getDouble("jump-power");
        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
    }
}
