package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class CodConfig extends MobConfig {
    public double BASE_SPEED = 0.7D;
    public double MAX_HEALTH = 3.0D;
    public double AI_FOLLOW_RANGE = 16.0D;
    public double RIDING_SPEED = 1.0D;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public CodConfig() {
        super("cod.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.saddle.require", RIDING_SADDLE_REQUIRE);
            addDefault("riding.saddle.consume", RIDING_SADDLE_CONSUME);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_SADDLE_REQUIRE = getBoolean("riding.saddle.require");
        RIDING_SADDLE_CONSUME = getBoolean("riding.saddle.consume");
    }
}
