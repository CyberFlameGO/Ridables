package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class DrownedConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float JUMP_POWER = 0.5F;
    public float STEP_HEIGHT = 0.6F;
    public boolean RIDABLE_IN_WATER = true;
    public int SHOOT_COOLDOWN = 20;
    public float SHOOT_SPEED = 1.0F;
    public float SHOOT_DAMAGE = 8.0F;
    public boolean SHOOT_CHANNELING = true;
    public boolean SHOOT_REQUIRE_TRIDENT = false;

    public DrownedConfig() {
        super("drowned.yml");
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
            addDefault("shoot.cooldown", SHOOT_COOLDOWN);
            addDefault("shoot.speed", SHOOT_SPEED);
            addDefault("shoot.damage", SHOOT_DAMAGE);
            addDefault("shoot.channeling", SHOOT_CHANNELING);
            addDefault("shoot.require-trident", SHOOT_REQUIRE_TRIDENT);
            save();
        }

        SPEED = (float) getDouble("speed");
        JUMP_POWER = (float) getDouble("jump-power");
        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        SHOOT_COOLDOWN = (int) getDouble("shoot.cooldown");
        SHOOT_SPEED = (float) getDouble("shoot.speed");
        SHOOT_DAMAGE = (float) getDouble("shoot.damage");
        SHOOT_CHANNELING = getBoolean("shoot.channeling");
        SHOOT_REQUIRE_TRIDENT = getBoolean("shoot.require-trident");
    }
}
