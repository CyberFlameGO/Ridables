package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class CreeperConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float JUMP_POWER = 0.5F;
    public float STEP_HEIGHT = 0.6F;
    public boolean RIDABLE_IN_WATER = true;
    public float EXPLOSION_DAMAGE = 5.0F;
    public int EXPLOSION_RADIUS = 3;
    public boolean EXPLOSION_GRIEF = true;

    public CreeperConfig() {
        super("creeper.yml");
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
            addDefault("explosion.damage", EXPLOSION_DAMAGE);
            addDefault("explosion.radius", EXPLOSION_RADIUS);
            addDefault("explosion.grief", EXPLOSION_GRIEF);
            save();
        }

        SPEED = (float) getDouble("speed");
        JUMP_POWER = (float) getDouble("jump-power");
        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        EXPLOSION_DAMAGE = (float) getDouble("explosion.damage");
        EXPLOSION_RADIUS = (int) getDouble("explosion.radius");
        EXPLOSION_GRIEF = getBoolean("explosion.grief");
    }
}
