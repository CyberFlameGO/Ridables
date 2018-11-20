package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class DonkeyConfig extends MobConfig {
    public double BASE_SPEED = 0.175D;
    public double MAX_HEALTH = -1.0D; // 15-30 range
    public float AI_JUMP_POWER = 0.5F;
    public float AI_STEP_HEIGHT = 1.0F;
    public int AI_FOLLOW_RANGE = 16;
    public double RIDING_SPEED = 0.175D;
    public float RIDING_JUMP_POWER = 0.5F;
    public float RIDING_STEP_HEIGHT = 1.0F;
    public boolean RIDING_RIDE_IN_WATER = true;
    public boolean RIDING_BABIES = false;
    public boolean RIDING_ENABLE_MOVE_EVENT = false;

    public DonkeyConfig() {
        super("donkey.yml");
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ai.jump-power", AI_JUMP_POWER);
            addDefault("ai.step-height", AI_STEP_HEIGHT);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.jump-power", RIDING_JUMP_POWER);
            addDefault("riding.step-height", RIDING_STEP_HEIGHT);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            addDefault("riding.ride-babies", RIDING_BABIES);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_JUMP_POWER = (float) getDouble("ai.jump-power");
        AI_STEP_HEIGHT = (float) getDouble("ai.step-height");
        AI_FOLLOW_RANGE = (int) getDouble("ai.follow-range");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_JUMP_POWER = (float) getDouble("riding.jump-power");
        RIDING_STEP_HEIGHT = (float) getDouble("riding.step-height");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
        RIDING_BABIES = getBoolean("riding.ride-babies");
        RIDING_ENABLE_MOVE_EVENT = isSet("riding.enable-move-event") ? getBoolean("riding.enable-move-event") : Config.RIDING_ENABLE_MOVE_EVENT;
    }
}
