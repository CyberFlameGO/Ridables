package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class BlazeConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float VERTICAL = 0.5F;
    public float GRAVITY = 0.04F;
    public boolean RIDABLE_IN_WATER = true;
    public int SHOOT_COOLDOWN = 20;
    public float SHOOT_SPEED = 1.0F;
    public float SHOOT_DAMAGE = 5.0F;
    public boolean SHOOT_GRIEF = true;

    public BlazeConfig() {
        super("blaze.yml");
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
            addDefault("shoot.cooldown", SHOOT_COOLDOWN);
            addDefault("shoot.speed", SHOOT_SPEED);
            addDefault("shoot.damage", SHOOT_DAMAGE);
            addDefault("shoot.grief", SHOOT_GRIEF);
            save();
        }

        SPEED = (float) getDouble("speed");
        VERTICAL = (float) getDouble("vertical");
        GRAVITY = (float) getDouble("gravity");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        SHOOT_COOLDOWN = (int) getDouble("shoot.cooldown");
        SHOOT_SPEED = (float) getDouble("shoot.speed");
        SHOOT_DAMAGE = (float) getDouble("shoot.damage");
        SHOOT_GRIEF = getBoolean("shoot.grief");
    }
}
