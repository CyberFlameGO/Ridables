package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;
import net.pl3x.bukkit.ridables.util.Logger;

public class ChickenConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float JUMP_POWER = 0.5F;
    public float STEP_HEIGHT = 0.6F;
    public boolean RIDABLE_IN_WATER = true;
    public boolean DROP_EGGS_WHILE_RIDING = false;
    public int EGG_DELAY_MIN = 6000;
    public int EGG_DELAY_MAX = 12000;

    public ChickenConfig() {
        super("chicken.yml");
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
            addDefault("drop-eggs", DROP_EGGS_WHILE_RIDING);
            addDefault("egg-delay.min", EGG_DELAY_MIN);
            addDefault("egg-delay.max", EGG_DELAY_MAX);
            save();
        }

        SPEED = (float) getDouble("speed");
        JUMP_POWER = (float) getDouble("jump-power");
        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        DROP_EGGS_WHILE_RIDING = getBoolean("drop-eggs");
        EGG_DELAY_MIN = (int) getDouble("egg-delay.min");
        EGG_DELAY_MAX = (int) getDouble("egg-delay.max");

        if (EGG_DELAY_MIN > EGG_DELAY_MAX) {
            Logger.error("Chicken egg delay min and max values are backwards");
            Logger.error("Swapping values in memory");
            EGG_DELAY_MIN = EGG_DELAY_MIN ^ EGG_DELAY_MAX;
            EGG_DELAY_MAX = EGG_DELAY_MIN ^ EGG_DELAY_MAX;
            EGG_DELAY_MIN = EGG_DELAY_MIN ^ EGG_DELAY_MAX;
        }
    }
}
