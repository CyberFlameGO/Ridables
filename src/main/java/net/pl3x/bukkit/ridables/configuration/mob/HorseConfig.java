package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class HorseConfig extends MobConfig {
    public double BASE_SPEED = -1.0D; // 0.1125–0.3375
    public double MAX_HEALTH = -1.0D; // 15-30
    public float AI_JUMP_POWER = -1.0F; // 0.4-1.0
    public float AI_STEP_HEIGHT = 1.0F;
    public int AI_FOLLOW_RANGE = 16;
    public double RIDING_SPEED = 0.25D;
    public float RIDING_JUMP_POWER = -1.0F; // 0.4-1.0
    public float RIDING_STEP_HEIGHT = 1.0F;
    public boolean RIDING_RIDE_IN_WATER = true;
    public boolean RIDING_BABIES = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public HorseConfig() {
        super("horse.yml");
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
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
    }
}
