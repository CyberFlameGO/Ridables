package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class ShulkerConfig extends MobConfig {
    public double BASE_SPEED = 0.7D;
    public double MAX_HEALTH = 30.0D;
    public double AI_FOLLOW_RANGE = 16.0D;
    public float AI_SHOOT_SPEED = 0.2F;
    public float AI_SHOOT_DAMAGE = 4.0F;
    public double RIDING_SPEED = 0.7D;
    public boolean RIDING_RIDE_IN_WATER = true;
    public int RIDING_PEEK_HEIGHT = 33;
    public int RIDING_SHOOT_COOLDOWN = 10;
    public float RIDING_SHOOT_SPEED = 1.0F;
    public float RIDING_SHOOT_DAMAGE = 4.0F;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public ShulkerConfig() {
        super("shulker.yml");
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("ai.shoot.speed", RIDING_SHOOT_SPEED);
            addDefault("ai.shoot.damage", RIDING_SHOOT_DAMAGE);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            addDefault("riding.peek-height", RIDING_PEEK_HEIGHT);
            addDefault("riding.shoot.cooldown", RIDING_SHOOT_COOLDOWN);
            addDefault("riding.shoot.speed", RIDING_SHOOT_SPEED);
            addDefault("riding.shoot.damage", RIDING_SHOOT_DAMAGE);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        AI_SHOOT_SPEED = (float) getDouble("ai.shoot.speed");
        AI_SHOOT_DAMAGE = (float) getDouble("ai.shoot.damage");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
        RIDING_PEEK_HEIGHT = (int) getDouble("riding.peek-height");
        RIDING_SHOOT_COOLDOWN = (int) getDouble("riding.shoot.cooldown");
        RIDING_SHOOT_SPEED = (float) getDouble("riding.shoot.speed");
        RIDING_SHOOT_DAMAGE = (float) getDouble("riding.shoot.damage");
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
    }
}
