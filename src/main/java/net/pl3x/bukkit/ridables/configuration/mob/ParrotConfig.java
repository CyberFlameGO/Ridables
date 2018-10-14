package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class ParrotConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float VERTICAL = 1.0F;
    public float GRAVITY = 0.04F;
    public boolean RIDABLE_IN_WATER = true;

    public ParrotConfig() {
        super("parrot.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("speed", SPEED);
            addDefault("vertical", VERTICAL);
            addDefault("gravity", GRAVITY);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            save();
        }

        SPEED = (float) getDouble("speed");
        VERTICAL = (float) getDouble("vertical");
        GRAVITY = (float) getDouble("gravity");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
    }
}
