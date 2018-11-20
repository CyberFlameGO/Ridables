package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class ParrotConfig extends MobConfig {
    public double BASE_SPEED = 0.4D;
    public double MAX_HEALTH = 6.0D;
    public double AI_FOLLOW_RANGE = 16.0D;
    public double RIDING_SPEED = 0.4D;
    public double RIDING_VERTICAL = 1.0F;
    public double RIDING_GRAVITY = 0.04F;
    public int RIDING_FLYING_MAX_Y = 256;
    public boolean RIDING_RIDE_IN_WATER = false;
    public boolean RIDING_BABIES = false;
    public boolean RIDING_ONLY_OWNER_CAN_RIDE = true;
    public boolean RIDING_ENABLE_MOVE_EVENT = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public ParrotConfig() {
        super("parrot.yml");
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
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.vertical", RIDING_VERTICAL);
            addDefault("riding.gravity", RIDING_GRAVITY);
            addDefault("riding.flying-max-y", RIDING_FLYING_MAX_Y);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            addDefault("riding.ride-babies", RIDING_BABIES);
            addDefault("riding.only-owner-can-ride", RIDING_ONLY_OWNER_CAN_RIDE);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_VERTICAL = getDouble("riding.vertical");
        RIDING_GRAVITY = getDouble("riding.gravity");
        RIDING_FLYING_MAX_Y = (int) getDouble("riding.flying-max-y");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
        RIDING_BABIES = getBoolean("riding.ride-babies");
        RIDING_ONLY_OWNER_CAN_RIDE = getBoolean("riding.only-owner-can-ride");
        RIDING_ENABLE_MOVE_EVENT = isSet("riding.enable-move-event") ? getBoolean("riding.enable-move-event") : Config.RIDING_ENABLE_MOVE_EVENT;
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
    }
}
