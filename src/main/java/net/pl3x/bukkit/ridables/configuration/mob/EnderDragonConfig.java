package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class EnderDragonConfig extends MobConfig {
    public float SPEED = 1.0F;

    public EnderDragonConfig() {
        super("ender_dragon.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("speed", SPEED);
            save();
        }

        SPEED = (float) getDouble("speed");
    }
}
