package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class EnderDragonConfig extends MobConfig {
    public double BASE_SPEED = 0.7D;
    public double MAX_HEALTH = 200.0D;
    public double RIDING_SPEED = 1.0D;
    public int RIDING_FLYING_MAX_Y = 256;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public EnderDragonConfig() {
        super("ender_dragon.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.flying-max-y", RIDING_FLYING_MAX_Y);
            addDefault("riding.saddle.require", RIDING_SADDLE_REQUIRE);
            addDefault("riding.saddle.consume", RIDING_SADDLE_CONSUME);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_FLYING_MAX_Y = (int) getDouble("riding.flying-max-y");
        RIDING_SADDLE_REQUIRE = getBoolean("riding.saddle.require");
        RIDING_SADDLE_CONSUME = getBoolean("riding.saddle.consume");
    }
}
