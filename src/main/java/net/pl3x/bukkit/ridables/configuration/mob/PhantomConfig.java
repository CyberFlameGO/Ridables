package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class PhantomConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float GRAVITY = 0.04F;
    public boolean RIDABLE_IN_WATER = true;
    public boolean BURN_IN_SUNLIGHT = true;
    public boolean ATTACK_IN_DAYLIGHT = true;
    public float SHOOT_DAMAGE = 1.0F;

    public PhantomConfig() {
        super("phantom.yml");
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("speed", SPEED);
            addDefault("gravity", GRAVITY);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            addDefault("burn-in-sunlight", BURN_IN_SUNLIGHT);
            addDefault("attack-in-daylight", ATTACK_IN_DAYLIGHT);
            addDefault("shoot-damage", SHOOT_DAMAGE);
            save();
        }

        SPEED = (float) getDouble("speed");
        GRAVITY = (float) getDouble("gravity");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        BURN_IN_SUNLIGHT = getBoolean("burn-in-sunlight");
        ATTACK_IN_DAYLIGHT = getBoolean("attack-in-daylight");
        SHOOT_DAMAGE = (float) getDouble("shoot-damage");
    }
}
