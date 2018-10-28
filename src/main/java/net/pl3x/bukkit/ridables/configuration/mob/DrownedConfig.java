package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class DrownedConfig extends MobConfig {
    public double BASE_SPEED = 0.23D;
    public double MAX_HEALTH = 20.0D;
    public double AI_MELEE_DAMAGE = 3.0D;
    public float AI_TRIDENT_DAMAGE = 8.0F;
    public double AI_FOLLOW_RANGE = 16.0D;
    public float AI_JUMP_POWER = 0.5F;
    public float AI_STEP_HEIGHT = 0.6F;
    public double RIDING_SPEED = 1.0D;
    public float RIDING_JUMP_POWER = 0.5F;
    public float RIDING_STEP_HEIGHT = 0.6F;
    public boolean RIDING_RIDE_IN_WATER = true;
    public boolean RIDING_BABIES = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;
    public int RIDING_SHOOT_COOLDOWN = 20;
    public double RIDING_SHOOT_SPEED = 1.0D;
    public double RIDING_SHOOT_DAMAGE = 8.0D;
    public boolean RIDING_SHOOT_CHANNELING = true;
    public boolean RIDING_SHOOT_REQUIRE_TRIDENT = false;

    public DrownedConfig() {
        super("drowned.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ai.melee-damage", AI_MELEE_DAMAGE);
            addDefault("ai.trident-damage", AI_TRIDENT_DAMAGE);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("ai.jump-power", AI_JUMP_POWER);
            addDefault("ai.step-height", AI_STEP_HEIGHT);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.jump-power", RIDING_JUMP_POWER);
            addDefault("riding.step-height", RIDING_STEP_HEIGHT);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            addDefault("riding.ride-babies", RIDING_BABIES);
            addDefault("riding.saddle.require", RIDING_SADDLE_REQUIRE);
            addDefault("riding.saddle.consume", RIDING_SADDLE_CONSUME);
            addDefault("riding.shoot.cooldown", RIDING_SHOOT_COOLDOWN);
            addDefault("riding.shoot.speed", RIDING_SHOOT_SPEED);
            addDefault("riding.shoot.damage", RIDING_SHOOT_DAMAGE);
            addDefault("riding.shoot.channeling", RIDING_SHOOT_CHANNELING);
            addDefault("riding.shoot.require-trident", RIDING_SHOOT_REQUIRE_TRIDENT);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_MELEE_DAMAGE = getDouble("ai.melee-damage");
        AI_MELEE_DAMAGE = getDouble("ai.trident-damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        AI_JUMP_POWER = (float) getDouble("ai.jump-power");
        AI_STEP_HEIGHT = (float) getDouble("ai.step-height");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_JUMP_POWER = (float) getDouble("riding.jump-power");
        RIDING_STEP_HEIGHT = (float) getDouble("riding.step-height");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
        RIDING_BABIES = getBoolean("riding.ride-babies");
        RIDING_SADDLE_REQUIRE = getBoolean("riding.saddle.require");
        RIDING_SADDLE_CONSUME = getBoolean("riding.saddle.consume");
        RIDING_SHOOT_COOLDOWN = (int) getDouble("riding.shoot.cooldown");
        RIDING_SHOOT_SPEED = getDouble("riding.shoot.speed");
        RIDING_SHOOT_DAMAGE = getDouble("riding.shoot.damage");
        RIDING_SHOOT_CHANNELING = getBoolean("riding.shoot.channeling");
        RIDING_SHOOT_REQUIRE_TRIDENT = getBoolean("riding.shoot.require-trident");
    }
}
