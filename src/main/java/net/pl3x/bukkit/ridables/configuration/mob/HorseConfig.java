package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class HorseConfig extends MobConfig {
    public float STEP_HEIGHT = 1.0F;
    public boolean RIDABLE_IN_WATER = true;

    public HorseConfig() {
        super("horse.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("step-height", STEP_HEIGHT);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            save();
        }

        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
    }
}
