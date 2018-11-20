package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class SpiderConfig extends MobConfig {
    public double BASE_SPEED = 0.3D;
    public double MAX_HEALTH = 16.0D;
    public float AI_JUMP_POWER = 0.42F;
    public double AI_MELEE_DAMAGE = 2.0D;
    public double AI_FOLLOW_RANGE = 16.0D;
    public double RIDING_SPEED = 0.3D;
    public float RIDING_JUMP_POWER = 0.5F;
    public boolean RIDING_RIDE_IN_WATER = true;
    public boolean RIDING_CLIMB_WALLS = true;
    public double RIDING_CLIMB_SPEED = 1.0D;
    public boolean RIDING_ENABLE_MOVE_EVENT = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public SpiderConfig() {
        super("spider.yml");
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
            addDefault("ai.melee-damage", AI_MELEE_DAMAGE);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.jump-power", RIDING_JUMP_POWER);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            addDefault("riding.climb-walls", RIDING_CLIMB_WALLS);
            addDefault("riding.climb-speed", RIDING_CLIMB_SPEED);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_JUMP_POWER = (float) getDouble("ai.jump-power");
        AI_MELEE_DAMAGE = getDouble("ai.melee-damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_JUMP_POWER = (float) getDouble("riding.jump-power");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
        RIDING_CLIMB_WALLS = getBoolean("riding.climb-walls");
        RIDING_CLIMB_SPEED = getDouble("riding.climb-speed");
        RIDING_ENABLE_MOVE_EVENT = isSet("riding.enable-move-event") ? getBoolean("riding.enable-move-event") : Config.RIDING_ENABLE_MOVE_EVENT;
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
    }
}
