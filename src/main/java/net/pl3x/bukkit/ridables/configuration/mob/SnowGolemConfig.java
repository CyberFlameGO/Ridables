package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class SnowGolemConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float JUMP_POWER = 0.5F;
    public float STEP_HEIGHT = 0.6F;
    public boolean RIDABLE_IN_WATER = true;
    public boolean DAMAGE_WHEN_HOT = true;
    public boolean DAMAGE_WHEN_WET = true;
    public boolean LEAVE_SNOW_TRAIL = true;

    public SnowGolemConfig() {
        super("snow_golem.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("speed", SPEED);
            addDefault("jump-power", JUMP_POWER);
            addDefault("step-height", STEP_HEIGHT);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            addDefault("damage-when-hot", DAMAGE_WHEN_HOT);
            addDefault("damage-when-wet", DAMAGE_WHEN_WET);
            addDefault("leave-snow-trail", LEAVE_SNOW_TRAIL);
            save();
        }

        SPEED = (float) getDouble("speed");
        JUMP_POWER = (float) getDouble("jump-power");
        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        DAMAGE_WHEN_HOT = getBoolean("damage-when-hot");
        DAMAGE_WHEN_WET = getBoolean("damage-when-wet");
        LEAVE_SNOW_TRAIL = getBoolean("leave-snow-trail");
    }
}
