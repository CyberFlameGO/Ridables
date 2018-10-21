package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class LlamaConfig extends MobConfig {
    public double BASE_SPEED = 0.175D;
    public double RIDE_SPEED = 1.0D;
    public double MAX_HEALTH = -1.0D; // 15-30
    public double JUMP_POWER = 0.5D;
    public float STEP_HEIGHT = 1.0F;
    public boolean RIDABLE_IN_WATER = true;
    public boolean CARAVAN = true;

    public LlamaConfig() {
        super("llama.yml");
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
            addDefault("ride-starts-caravan", true);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        RIDE_SPEED = getDouble("ride-speed");
        MAX_HEALTH = getDouble("max-health");
        JUMP_POWER = getDouble("jump-power");
        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        CARAVAN = getBoolean("ride-starts-caravan");
    }
}
