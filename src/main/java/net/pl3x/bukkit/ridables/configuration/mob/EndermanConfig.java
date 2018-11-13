package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class EndermanConfig extends MobConfig {
    public double BASE_SPEED = 0.3D;
    public double MAX_HEALTH = 40.0D;
    public double AI_MELEE_DAMAGE = 7.0D;
    public double AI_FOLLOW_RANGE = 64.0D;
    public float AI_JUMP_POWER = 0.5F;
    public float AI_STEP_HEIGHT = 1.0F;
    public float AI_DAMAGE_WHEN_WET = 1.0F;
    public boolean AI_TELEPORT_WHEN_DAMAGED = true;
    public double RIDING_SPEED = 1.0D;
    public float RIDING_JUMP_POWER = 0.5F;
    public float RIDING_STEP_HEIGHT = 1.0F;
    public boolean RIDING_RIDE_IN_WATER = true;
    public float RIDING_DAMAGE_WHEN_WET = 1.0F;
    public boolean RIDING_EJECT_WHEN_WET = true;
    public boolean RIDING_TELEPORT_WHEN_DAMAGED = true;
    public boolean RIDING_ENABLE_MOVE_EVENT = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public EndermanConfig() {
        super("enderman.yml");
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ai.melee-damage", AI_MELEE_DAMAGE);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("ai.jump-power", AI_JUMP_POWER);
            addDefault("ai.step-height", AI_STEP_HEIGHT);
            addDefault("ai.damage-when-wet", AI_DAMAGE_WHEN_WET);
            addDefault("ai.teleport-when-damaged", AI_TELEPORT_WHEN_DAMAGED);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.jump-power", RIDING_JUMP_POWER);
            addDefault("riding.step-height", RIDING_STEP_HEIGHT);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            addDefault("riding.damage-when-wet", RIDING_DAMAGE_WHEN_WET);
            addDefault("riding.eject-when-wet", RIDING_EJECT_WHEN_WET);
            addDefault("riding.teleport-when-damaged", RIDING_TELEPORT_WHEN_DAMAGED);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_MELEE_DAMAGE = getDouble("ai.melee-damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        AI_JUMP_POWER = (float) getDouble("ai.jump-power");
        AI_STEP_HEIGHT = (float) getDouble("ai.step-height");
        AI_DAMAGE_WHEN_WET = (float) getDouble("ai.damage-when-wet");
        AI_TELEPORT_WHEN_DAMAGED = getBoolean("ai.teleport-when-damaged");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_JUMP_POWER = (float) getDouble("riding.jump-power");
        RIDING_STEP_HEIGHT = (float) getDouble("riding.step-height");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
        RIDING_DAMAGE_WHEN_WET = (float) getDouble("riding.damage-when-wet");
        RIDING_EJECT_WHEN_WET = getBoolean("riding.eject-when-wet");
        RIDING_TELEPORT_WHEN_DAMAGED = getBoolean("riding.teleport-when-damaged");
        RIDING_ENABLE_MOVE_EVENT = isSet("riding.enable-move-event") ? getBoolean("riding.enable-move-event") : Config.RIDING_ENABLE_MOVE_EVENT;
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
    }
}
