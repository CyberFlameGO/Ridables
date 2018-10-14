package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class TurtleConfig extends MobConfig {
    public float SPEED_LAND = 1.0F;
    public float SPEED_WATER = 1.0F;
    public float JUMP_POWER = 0.5F;
    public float STEP_HEIGHT = 0.6F;
    public boolean RIDABLE_IN_WATER = true;

    public TurtleConfig() {
        super("turtle.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("speed-on-land", SPEED_LAND);
            addDefault("speed-in-water", SPEED_WATER);
            addDefault("jump-power", JUMP_POWER);
            addDefault("step-height", STEP_HEIGHT);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            save();
        }

        SPEED_LAND = (float) getDouble("speed-on-land");
        SPEED_WATER = (float) getDouble("speed-in-water");
        JUMP_POWER = (float) getDouble("jump-power");
        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
    }
}
