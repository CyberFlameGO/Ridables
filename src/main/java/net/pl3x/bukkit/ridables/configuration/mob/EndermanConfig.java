package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class EndermanConfig extends MobConfig {
    public double BASE_SPEED = 0.3D;
    public double RIDE_SPEED = 1.0D;
    public double MAX_HEALTH = 40.0D;
    public float JUMP_POWER = 0.5F;
    public float STEP_HEIGHT = 1.0F;
    public boolean RIDABLE_IN_WATER = true;
    public boolean DAMAGE_WHEN_WET = true;
    public boolean EJECT_WHEN_WET = true;
    public boolean TELEPORT_WHEN_DAMAGED = true;
    public double AI_ATTACK_DAMAGE = 7.0D;
    public double AI_FOLLOW_RANGE = 64.0D;

    public EndermanConfig() {
        super("enderman.yml");
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
            addDefault("damage-when-wet", DAMAGE_WHEN_WET);
            addDefault("eject-when-wet", EJECT_WHEN_WET);
            addDefault("teleport-when-damaged", TELEPORT_WHEN_DAMAGED);
            addDefault("ai.attack-damage", AI_ATTACK_DAMAGE);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        RIDE_SPEED = getDouble("ride-speed");
        MAX_HEALTH = getDouble("max-health");
        JUMP_POWER = (float) getDouble("jump-power");
        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        DAMAGE_WHEN_WET = getBoolean("damage-when-wet");
        EJECT_WHEN_WET = getBoolean("eject-when-wet");
        TELEPORT_WHEN_DAMAGED = getBoolean("teleport-when-damaged");
        AI_ATTACK_DAMAGE = getDouble("ai.attack-damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
    }
}
