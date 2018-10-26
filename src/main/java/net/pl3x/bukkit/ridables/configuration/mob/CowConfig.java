package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class CowConfig extends MobConfig {
    public double BASE_SPEED = 0.2D;
    public double MAX_HEALTH = 10.0D;
    public float AI_STEP_HEIGHT = 0.6F;
    public double RIDING_SPEED = 1.0D;
    public float RIDING_JUMP_POWER = 0.5F;
    public float RIDING_STEP_HEIGHT = 0.6F;
    public boolean RIDING_RIDE_IN_WATER = true;

    public CowConfig() {
        super("cow.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ai.step-height", AI_STEP_HEIGHT);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.jump-power", RIDING_JUMP_POWER);
            addDefault("riding.step-height", RIDING_STEP_HEIGHT);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_STEP_HEIGHT = (float) getDouble("ai.step-height");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_JUMP_POWER = (float) getDouble("riding.jump-power");
        RIDING_STEP_HEIGHT = (float) getDouble("riding.step-height");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
    }
}
