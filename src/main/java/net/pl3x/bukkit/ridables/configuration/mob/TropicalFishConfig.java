package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class TropicalFishConfig extends MobConfig {
    public double BASE_SPEED = 0.7D;
    public double MAX_HEALTH = 3.0D;
    public double AI_FOLLOW_RANGE = 16.0D;
    public double AI_PANIC_SPEED = 1.25D;
    public float AI_AVOID_PLAYER_DISTANCE = 8.0F;
    public double AI_AVOID_PLAYER_SPEED_FAR = 1.6D;
    public double AI_AVOID_PLAYER_SPEED_NEAR = 1.4D;
    public boolean AI_FOLLOW_SCHOOL = true;
    public double RIDING_SPEED = 0.7D;
    public boolean RIDING_ENABLE_MOVE_EVENT = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public TropicalFishConfig() {
        super("tropical_fish.yml");
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
            addDefault("ai.panic-speed", AI_PANIC_SPEED);
            addDefault("ai.avoid-player.distance", AI_AVOID_PLAYER_DISTANCE);
            addDefault("ai.avoid-player.speed.far", AI_AVOID_PLAYER_SPEED_FAR);
            addDefault("ai.avoid-player.speed.near", AI_AVOID_PLAYER_SPEED_NEAR);
            addDefault("ai.follow-school", AI_FOLLOW_SCHOOL);
            addDefault("riding.speed", RIDING_SPEED);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        AI_PANIC_SPEED = getDouble("ai.panic-speed");
        AI_AVOID_PLAYER_DISTANCE = (float) getDouble("ai.avoid-player.distance");
        AI_AVOID_PLAYER_SPEED_FAR = getDouble("ai.avoid-player.speed.far");
        AI_AVOID_PLAYER_SPEED_NEAR = getDouble("ai.avoid-player.speed.near");
        AI_FOLLOW_SCHOOL = getBoolean("ai.follow-school");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_ENABLE_MOVE_EVENT = isSet("riding.enable-move-event") ? getBoolean("riding.enable-move-event") : Config.RIDING_ENABLE_MOVE_EVENT;
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
    }
}
