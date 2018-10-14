package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class WitherConfig extends MobConfig {
    public float SPEED = 1.0F;
    public boolean RIDABLE_IN_WATER = true;
    public int SHOOT_COOLDOWN = 20;
    public float SHOOT_SPEED = 1.0F;
    public float SHOOT_DAMAGE = 1.0F;
    public float SHOOT_HEAL_AMOUNT = 1.0F;
    public int SHOOT_EFFECT_DURATION = 10;
    public boolean SHOOT_GRIEF = true;

    public WitherConfig() {
        super("wither.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("speed", SPEED);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            addDefault("shoot.cooldown", SHOOT_COOLDOWN);
            addDefault("shoot.speed", SHOOT_SPEED);
            addDefault("shoot.damage", SHOOT_DAMAGE);
            addDefault("shoot.heal-amount", SHOOT_HEAL_AMOUNT);
            addDefault("shoot.effect-duration", SHOOT_EFFECT_DURATION);
            addDefault("shoot.grief", SHOOT_GRIEF);
            save();
        }

        SPEED = (float) getDouble("speed");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        SHOOT_COOLDOWN = (int) getDouble("shoot.cooldown");
        SHOOT_SPEED = (float) getDouble("shoot.speed");
        SHOOT_DAMAGE = (float) getDouble("shoot.damage");
        SHOOT_HEAL_AMOUNT = (float) getDouble("shoot.heal-amount");
        SHOOT_EFFECT_DURATION = (int) getDouble("shoot.effect-duration");
        SHOOT_GRIEF = getBoolean("shoot.grief");
    }
}
