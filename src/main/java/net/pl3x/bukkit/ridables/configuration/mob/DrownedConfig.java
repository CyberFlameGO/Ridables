package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class DrownedConfig extends MobConfig {
    public double BASE_SPEED = 0.23D;
    public double RIDE_SPEED = 1.0D;
    public double MAX_HEALTH = 20.0D;
    public float JUMP_POWER = 0.5F;
    public float STEP_HEIGHT = 0.6F;
    public boolean RIDABLE_IN_WATER = true;
    public double AI_ATTACK_DAMAGE = 3.0D;
    public double AI_FOLLOW_RANGE = 16.0D;
    public int SHOOT_COOLDOWN = 20;
    public float SHOOT_SPEED = 1.0F;
    public float SHOOT_DAMAGE = 8.0F;
    public boolean SHOOT_CHANNELING = true;
    public boolean SHOOT_REQUIRE_TRIDENT = false;

    public DrownedConfig() {
        super("drowned.yml");
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
            addDefault("ai.attack-damage", AI_ATTACK_DAMAGE);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("shoot.cooldown", SHOOT_COOLDOWN);
            addDefault("shoot.speed", SHOOT_SPEED);
            addDefault("shoot.damage", SHOOT_DAMAGE);
            addDefault("shoot.channeling", SHOOT_CHANNELING);
            addDefault("shoot.require-trident", SHOOT_REQUIRE_TRIDENT);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        RIDE_SPEED = getDouble("ride-speed");
        MAX_HEALTH = getDouble("max-health");
        JUMP_POWER = (float) getDouble("jump-power");
        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        AI_ATTACK_DAMAGE = getDouble("ai.attack-damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        SHOOT_COOLDOWN = (int) getDouble("shoot.cooldown");
        SHOOT_SPEED = (float) getDouble("shoot.speed");
        SHOOT_DAMAGE = (float) getDouble("shoot.damage");
        SHOOT_CHANNELING = getBoolean("shoot.channeling");
        SHOOT_REQUIRE_TRIDENT = getBoolean("shoot.require-trident");
    }
}
