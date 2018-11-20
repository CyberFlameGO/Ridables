package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class SlimeConfig extends MobConfig {
    public double BASE_SPEED = 0.4D;
    public double MAX_HEALTH_SMALL = 1.0D;
    public double MAX_HEALTH_MEDIUM = 4.0D;
    public double MAX_HEALTH_LARGE = 16.0D;
    public int AI_MELEE_DAMAGE_SMALL = 0;
    public int AI_MELEE_DAMAGE_MEDIUM = 2;
    public int AI_MELEE_DAMAGE_LARGE = 4;
    public float AI_JUMP_POWER = 0.42F;
    public double AI_FOLLOW_RANGE = 16.0D;
    public double RIDING_SPEED = 0.4D;
    public float RIDING_JUMP_POWER = 0.5F;
    public boolean RIDING_RIDE_IN_WATER = true;
    public boolean RIDING_ENABLE_MOVE_EVENT = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public SlimeConfig() {
        super("slime.yml");
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health.small", MAX_HEALTH_SMALL);
            addDefault("max-health.medium", MAX_HEALTH_MEDIUM);
            addDefault("max-health.large", MAX_HEALTH_LARGE);
            addDefault("ai.melee-damage.small", AI_MELEE_DAMAGE_SMALL);
            addDefault("ai.melee-damage.medium", AI_MELEE_DAMAGE_MEDIUM);
            addDefault("ai.melee-damage.large", AI_MELEE_DAMAGE_LARGE);
            addDefault("ai.jump-power", AI_JUMP_POWER);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.jump-power", RIDING_JUMP_POWER);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH_SMALL = getDouble("max-health.small");
        MAX_HEALTH_MEDIUM = getDouble("max-health.medium");
        MAX_HEALTH_LARGE = getDouble("max-health.large");
        AI_MELEE_DAMAGE_SMALL = (int) getDouble("ai.melee-damage.small");
        AI_MELEE_DAMAGE_MEDIUM = (int) getDouble("ai.melee-damage.medium");
        AI_MELEE_DAMAGE_LARGE = (int) getDouble("ai.melee-damage.large");
        AI_JUMP_POWER = (float) getDouble("ai.jump-power");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_JUMP_POWER = (float) getDouble("riding.jump-power");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
        RIDING_ENABLE_MOVE_EVENT = isSet("riding.enable-move-event") ? getBoolean("riding.enable-move-event") : Config.RIDING_ENABLE_MOVE_EVENT;
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
    }
}
