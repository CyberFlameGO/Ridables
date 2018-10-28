package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class EndermiteConfig extends MobConfig {
    public double BASE_SPEED = 0.25D;
    public double MAX_HEALTH = 8.0D;
    public double AI_ATTACK_DAMAGE = 2.0D;
    public double AI_FOLLOW_RANGE = 16.0D;
    public float AI_JUMP_POWER = 0.5F;
    public float AI_STEP_HEIGHT = 0.6F;
    public double RIDING_SPEED = 1.0D;
    public float RIDING_JUMP_POWER = 0.5F;
    public float RIDING_STEP_HEIGHT = 0.6F;
    public boolean RIDING_RIDE_IN_WATER = true;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public EndermiteConfig() {
        super("endermite.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ai.attack-damage", AI_ATTACK_DAMAGE);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("ai.jump-power", AI_JUMP_POWER);
            addDefault("ai.step-height", AI_STEP_HEIGHT);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.jump-power", RIDING_JUMP_POWER);
            addDefault("riding.step-height", RIDING_STEP_HEIGHT);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            addDefault("riding.saddle.require", RIDING_SADDLE_REQUIRE);
            addDefault("riding.saddle.consume", RIDING_SADDLE_CONSUME);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_ATTACK_DAMAGE = getDouble("ai.attack-damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        AI_JUMP_POWER = (float) getDouble("ai.jump-power");
        AI_STEP_HEIGHT = (float) getDouble("ai.step-height");
        RIDING_SPEED = getDouble("riding.ride-speed");
        RIDING_JUMP_POWER = (float) getDouble("riding.jump-power");
        RIDING_STEP_HEIGHT = (float) getDouble("riding.step-height");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
        RIDING_SADDLE_REQUIRE = getBoolean("riding.saddle.require");
        RIDING_SADDLE_CONSUME = getBoolean("riding.saddle.consume");
    }
}
