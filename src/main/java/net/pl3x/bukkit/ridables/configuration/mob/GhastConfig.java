package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class GhastConfig extends MobConfig {
    public double BASE_SPEED = 0.7D;
    public double RIDE_SPEED = 1.0D;
    public double MAX_HEALTH = 10.0D;
    public boolean RIDABLE_IN_WATER = true;
    public double AI_FIREBALL_SPEED = 1.0D;
    public double AI_FIREBALL_DAMAGE = 6.0D;
    public double AI_FIREBALL_EXPLOSION_DAMAGE = 17.0D;
    public boolean AI_FIREBALL_GRIEF = true;
    public double AI_FOLLOW_RANGE = 100.0D;
    public int SHOOT_COOLDOWN = 20;
    public double SHOOT_FIREBALL_SPEED = 1.0D;
    public double SHOOT_FIREBALL_DAMAGE = 5.0D;
    public double SHOOT_FIREBALL_EXPLOSION_DAMAGE = 10.0D;
    public boolean SHOOT_FIREBALL_GRIEF = true;

    public GhastConfig() {
        super("ghast.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("ride-speed", RIDE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            addDefault("ai.fireball-speed", AI_FIREBALL_SPEED);
            addDefault("ai.fireball-damage", AI_FIREBALL_DAMAGE);
            addDefault("ai.fireball-explosion-damage", AI_FIREBALL_EXPLOSION_DAMAGE);
            addDefault("ai.fireball-grief", AI_FIREBALL_GRIEF);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("shoot.cooldown", SHOOT_COOLDOWN);
            addDefault("shoot.fireball-speed", SHOOT_FIREBALL_SPEED);
            addDefault("shoot.fireball-damage", SHOOT_FIREBALL_DAMAGE);
            addDefault("shoot.fireball-explosion-damage", SHOOT_FIREBALL_EXPLOSION_DAMAGE);
            addDefault("shoot.fireball-grief", SHOOT_FIREBALL_GRIEF);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        RIDE_SPEED = getDouble("ride-speed");
        MAX_HEALTH = getDouble("max-health");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        AI_FIREBALL_SPEED = getDouble("ai.fireball-speed");
        AI_FIREBALL_DAMAGE = getDouble("ai.fireball-damage");
        AI_FIREBALL_EXPLOSION_DAMAGE = getDouble("ai.fireball-explosion-damage");
        AI_FIREBALL_GRIEF = getBoolean("ai.fireball-grief");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        SHOOT_COOLDOWN = (int) getDouble("shoot.cooldown");
        SHOOT_FIREBALL_SPEED = getDouble("shoot.fireball-speed");
        SHOOT_FIREBALL_DAMAGE = getDouble("shoot.fireball-damage");
        SHOOT_FIREBALL_EXPLOSION_DAMAGE = getDouble("shoot.fireball-explosion-damage");
        SHOOT_FIREBALL_GRIEF = getBoolean("shoot.fireball-grief");
    }
}
