package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class VexConfig extends MobConfig {
    public float SPEED = 1.0F;
    public boolean NO_CLIP = false;
    public boolean RIDABLE_IN_WATER = true;

    public VexConfig() {
        super("vex.yml");
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("speed", SPEED);
            addDefault("no-clip", NO_CLIP);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            save();
        }

        SPEED = (float) getDouble("speed");
        NO_CLIP = getBoolean("no-clip");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
    }
}
