package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class EvokerConfig extends MobConfig {
    public double BASE_SPEED = 0.5D;
    public double RIDE_SPEED = 1.0D;
    public double MAX_HEALTH = 24.0D;
    public float JUMP_POWER = 0.5F;
    public float STEP_HEIGHT = 0.6F;
    public boolean RIDABLE_IN_WATER = true;
    public int RIDER_FANGS_COOLDOWN = 40;
    public float RIDER_FANGS_DAMAGE = 6.0F;
    public float AI_FANGS_DAMAGE = 6.0F;
    public double AI_FOLLOW_RANGE = 12.0D;

    public EvokerConfig() {
        super("evoker.yml");
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
            addDefault("rider.fangs.cooldown", RIDER_FANGS_COOLDOWN);
            addDefault("rider.fangs.damage", RIDER_FANGS_DAMAGE);
            addDefault("ai.fangs.damage", AI_FANGS_DAMAGE);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        RIDE_SPEED = getDouble("ride-speed");
        MAX_HEALTH = getDouble("max-health");
        JUMP_POWER = (float) getDouble("jump-power");
        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        RIDER_FANGS_COOLDOWN = (int) getDouble("rider.fangs.cooldown");
        RIDER_FANGS_DAMAGE = (float) getDouble("rider.fangs.damage");
        AI_FANGS_DAMAGE = (float) getDouble("ai.fangs.damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
    }
}
