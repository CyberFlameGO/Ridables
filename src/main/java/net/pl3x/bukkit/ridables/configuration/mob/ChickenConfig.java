package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;
import net.pl3x.bukkit.ridables.util.Logger;

public class ChickenConfig extends MobConfig {
    public double BASE_SPEED = 0.25D;
    public double MAX_HEALTH = 4.0D;
    public float AI_JUMP_POWER = 0.42F;
    public float AI_STEP_HEIGHT = 0.6F;
    public double AI_FOLLOW_RANGE = 16.0D;
    public double RIDING_SPEED = 1.0D;
    public float RIDING_JUMP_POWER = 0.5F;
    public float RIDING_STEP_HEIGHT = 0.6F;
    public boolean RIDING_RIDE_IN_WATER = true;
    public boolean RIDING_BABIES = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;
    public boolean RIDING_DROP_EGGS = false;
    public int RIDING_DROP_EGGS_DELAY_MIN = 6000;
    public int RIDING_DROP_EGGS_DELAY_MAX = 12000;

    public ChickenConfig() {
        super("chicken.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ai.jump-power", AI_JUMP_POWER);
            addDefault("ai.step-height", AI_STEP_HEIGHT);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.jump-power", RIDING_JUMP_POWER);
            addDefault("riding.step-height", RIDING_STEP_HEIGHT);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            addDefault("riding.ride-babies", RIDING_BABIES);
            addDefault("riding.saddle.require", RIDING_SADDLE_REQUIRE);
            addDefault("riding.saddle.consume", RIDING_SADDLE_CONSUME);
            addDefault("riding.drop-eggs.enable", RIDING_DROP_EGGS);
            addDefault("riding.drop-eggs.delay.min", RIDING_DROP_EGGS_DELAY_MIN);
            addDefault("riding.drop-eggs.delay.max", RIDING_DROP_EGGS_DELAY_MAX);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_JUMP_POWER = (float) getDouble("ai.jump-power");
        AI_STEP_HEIGHT = (float) getDouble("ai.step-height");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_JUMP_POWER = (float) getDouble("riding.jump-power");
        RIDING_STEP_HEIGHT = (float) getDouble("riding.step-height");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
        RIDING_BABIES = getBoolean("riding.ride-babies");
        RIDING_SADDLE_REQUIRE = getBoolean("riding.saddle.require");
        RIDING_SADDLE_CONSUME = getBoolean("riding.saddle.consume");
        RIDING_DROP_EGGS = getBoolean("riding.drop-eggs.enable");
        RIDING_DROP_EGGS_DELAY_MIN = (int) getDouble("riding.drop-eggs.delay.min");
        RIDING_DROP_EGGS_DELAY_MAX = (int) getDouble("riding.drop-eggs.delay.max");

        if (RIDING_DROP_EGGS_DELAY_MIN > RIDING_DROP_EGGS_DELAY_MAX) {
            Logger.error("Chicken egg delay min and max values are backwards");
            Logger.error("Swapping values in memory");
            RIDING_DROP_EGGS_DELAY_MIN = RIDING_DROP_EGGS_DELAY_MIN ^ RIDING_DROP_EGGS_DELAY_MAX;
            RIDING_DROP_EGGS_DELAY_MAX = RIDING_DROP_EGGS_DELAY_MIN ^ RIDING_DROP_EGGS_DELAY_MAX;
            RIDING_DROP_EGGS_DELAY_MIN = RIDING_DROP_EGGS_DELAY_MIN ^ RIDING_DROP_EGGS_DELAY_MAX;
        }
    }
}
