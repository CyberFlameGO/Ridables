package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class SlimeConfig extends MobConfig {
    public float SPEED = 1.0F;
    public boolean RIDABLE_IN_WATER = true;

    public SlimeConfig() {
        super("slime.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("speed", SPEED);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            save();
        }

        SPEED = (float) getDouble("speed");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
    }
}