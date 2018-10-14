package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class SquidConfig extends MobConfig {
    public float SPEED = 1.0F;
    public int INK_COOLDOWN = 40;

    public SquidConfig() {
        super("squid.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("speed", SPEED);
            addDefault("ink-cooldown", INK_COOLDOWN);
            save();
        }

        SPEED = (float) getDouble("speed");
        INK_COOLDOWN = (int) getDouble("ink-cooldown");
    }
}
