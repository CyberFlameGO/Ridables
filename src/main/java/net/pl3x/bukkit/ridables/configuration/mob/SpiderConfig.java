package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class SpiderConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float JUMP_POWER = 0.5F;
    public boolean RIDABLE_IN_WATER = true;
    public boolean CLIMB_WALLS = true;
    public float CLIMB_SPEED = 1.0F;

    public SpiderConfig() {
        super("spider.yml");
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("speed", SPEED);
            addDefault("jump-power", JUMP_POWER);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            addDefault("climb-walls", CLIMB_WALLS);
            addDefault("climb-speed", CLIMB_SPEED);
            save();
        }

        SPEED = (float) getDouble("speed");
        JUMP_POWER = (float) getDouble("jump-power");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        CLIMB_WALLS = getBoolean("climb-walls");
        CLIMB_SPEED = (float) getDouble("climb-speed");
    }
}
