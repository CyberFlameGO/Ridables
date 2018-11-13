package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class PufferfishConfig extends MobConfig {
    public double BASE_SPEED = 0.7F;
    public double MAX_HEALTH = 3.0D;
    public double AI_FOLLOW_RANGE = 16.0D;
    public double AI_PANIC_SPEED = 1.25D;
    public float AI_AVOID_PLAYER_DISTANCE = 8.0F;
    public double AI_AVOID_PLAYER_SPEED_FAR = 1.6D;
    public double AI_AVOID_PLAYER_SPEED_NEAR = 1.4D;
    public double AI_PUFF_UP_RADIUS = 2.0D;
    public float AI_DAMAGE_HALF_PUFF = 2;
    public int AI_POISON_DURATION_HALF_PUFF = 60;
    public int AI_POISON_AMPLIFIER_HALF_PUFF = 0;
    public float AI_DAMAGE_FULL_PUFF = 3;
    public int AI_POISON_DURATION_FULL_PUFF = 120;
    public int AI_POISON_AMPLIFIER_FULL_PUFF = 0;
    public double RIDING_SPEED = 1.0D;
    public boolean RIDING_ENABLE_MOVE_EVENT = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;
    public float RIDING_DAMAGE_HALF_PUFF = 2;
    public int RIDING_POISON_DURATION_HALF_PUFF = 60;
    public int RIDING_POISON_AMPLIFIER_HALF_PUFF = 0;
    public float RIDING_DAMAGE_FULL_PUFF = 3;
    public int RIDING_POISON_DURATION_FULL_PUFF = 120;
    public int RIDING_POISON_AMPLIFIER_FULL_PUFF = 0;

    public PufferfishConfig() {
        super("pufferfish.yml");
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
            addDefault("ai.puff-up-radius", AI_PUFF_UP_RADIUS);
            addDefault("ai.half-puff.damage", AI_DAMAGE_HALF_PUFF);
            addDefault("ai.half-puff.poison.duration", AI_POISON_DURATION_HALF_PUFF);
            addDefault("ai.half-puff.poison.amplifier", AI_POISON_AMPLIFIER_HALF_PUFF);
            addDefault("ai.full-puff.damage", AI_DAMAGE_FULL_PUFF);
            addDefault("ai.full-puff.poison.duration", AI_POISON_DURATION_FULL_PUFF);
            addDefault("ai.full-puff.poison.amplifier", AI_POISON_AMPLIFIER_FULL_PUFF);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.half-puff.damage", RIDING_DAMAGE_HALF_PUFF);
            addDefault("riding.half-puff.poison.duration", RIDING_POISON_DURATION_HALF_PUFF);
            addDefault("riding.half-puff.poison.amplifier", RIDING_POISON_AMPLIFIER_HALF_PUFF);
            addDefault("riding.full-puff.damage", RIDING_DAMAGE_FULL_PUFF);
            addDefault("riding.full-puff.poison.duration", RIDING_POISON_DURATION_FULL_PUFF);
            addDefault("riding.full-puff.poison.amplifier", RIDING_POISON_AMPLIFIER_FULL_PUFF);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        AI_PANIC_SPEED = getDouble("ai.panic-speed");
        AI_AVOID_PLAYER_DISTANCE = (float) getDouble("ai.avoid-player.distance");
        AI_AVOID_PLAYER_SPEED_FAR = getDouble("ai.avoid-player.speed.far");
        AI_AVOID_PLAYER_SPEED_NEAR = getDouble("ai.avoid-player.speed.near");
        AI_PUFF_UP_RADIUS = getDouble("ai.puff-up-radius");
        AI_DAMAGE_HALF_PUFF = (float) getDouble("ai.half-puff.damage");
        AI_POISON_DURATION_HALF_PUFF = getInt("ai.half-puff.poison.duration");
        AI_POISON_AMPLIFIER_HALF_PUFF = getInt("ai.half-puff.poison.amplifier");
        AI_DAMAGE_FULL_PUFF = (float) getDouble("ai.full-puff.damage");
        AI_POISON_DURATION_FULL_PUFF = getInt("ai.full-puff.poison.duration");
        AI_POISON_AMPLIFIER_FULL_PUFF = getInt("ai.full-puff.poison.amplifier");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_ENABLE_MOVE_EVENT = isSet("riding.enable-move-event") ? getBoolean("riding.enable-move-event") : Config.RIDING_ENABLE_MOVE_EVENT;
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
        RIDING_DAMAGE_HALF_PUFF = (float) getDouble("riding.half-puff.damage");
        RIDING_POISON_DURATION_HALF_PUFF = getInt("riding.half-puff.poison.duration");
        RIDING_POISON_AMPLIFIER_HALF_PUFF = getInt("riding.half-puff.poison.amplifier");
        RIDING_DAMAGE_FULL_PUFF = (float) getDouble("riding.full-puff.damage");
        RIDING_POISON_DURATION_FULL_PUFF = getInt("riding.full-puff.poison.duration");
        RIDING_POISON_AMPLIFIER_FULL_PUFF = getInt("riding.full-puff.poison.amplifier");
    }
}
