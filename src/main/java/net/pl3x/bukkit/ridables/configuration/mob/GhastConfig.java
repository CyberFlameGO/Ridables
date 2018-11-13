package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class GhastConfig extends MobConfig {
    public double BASE_SPEED = 0.7D;
    public double MAX_HEALTH = 10.0D;
    public double AI_FIREBALL_SPEED = 1.0D;
    public double AI_FIREBALL_DAMAGE = 6.0D;
    public double AI_FIREBALL_EXPLOSION_DAMAGE = 17.0D;
    public boolean AI_FIREBALL_EXPLOSION_FIRE = false;
    public boolean AI_FIREBALL_EXPLOSION_GRIEF = true;
    public double AI_FOLLOW_RANGE = 100.0D;
    public double RIDING_SPEED = 1.0D;
    public boolean RIDING_RIDE_IN_WATER = true;
    public boolean RIDING_ENABLE_MOVE_EVENT = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;
    public int RIDING_FIREBALL_COOLDOWN = 20;
    public double RIDING_FIREBALL_SPEED = 1.0D;
    public double RIDING_FIREBALL_DAMAGE = 5.0D;
    public double RIDING_FIREBALL_EXPLOSION_DAMAGE = 10.0D;
    public boolean RIDING_FIREBALL_EXPLOSION_FIRE = false;
    public boolean RIDING_FIREBALL_EXPLOSION_GRIEF = true;

    public GhastConfig() {
        super("ghast.yml");
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ai.fireball.speed", AI_FIREBALL_SPEED);
            addDefault("ai.fireball.damage", AI_FIREBALL_DAMAGE);
            addDefault("ai.fireball.explosion-damage", AI_FIREBALL_EXPLOSION_DAMAGE);
            addDefault("ai.fireball.explosion-fire", AI_FIREBALL_EXPLOSION_FIRE);
            addDefault("ai.fireball.explosion-grief", AI_FIREBALL_EXPLOSION_GRIEF);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            addDefault("riding.fireball.cooldown", RIDING_FIREBALL_COOLDOWN);
            addDefault("riding.fireball.speed", RIDING_FIREBALL_SPEED);
            addDefault("riding.fireball.damage", RIDING_FIREBALL_DAMAGE);
            addDefault("riding.fireball.explosion-damage", RIDING_FIREBALL_EXPLOSION_DAMAGE);
            addDefault("riding.fireball.explosion-fire", RIDING_FIREBALL_EXPLOSION_FIRE);
            addDefault("riding.fireball.explosion-grief", RIDING_FIREBALL_EXPLOSION_GRIEF);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_FIREBALL_SPEED = getDouble("ai.fireball.speed");
        AI_FIREBALL_DAMAGE = getDouble("ai.fireball.damage");
        AI_FIREBALL_EXPLOSION_DAMAGE = getDouble("ai.fireball.explosion-damage");
        AI_FIREBALL_EXPLOSION_FIRE = getBoolean("ai.fireball.explosion-fire");
        AI_FIREBALL_EXPLOSION_GRIEF = getBoolean("ai.fireball.explosion-grief");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        RIDING_SPEED = getDouble("ride-speed");
        RIDING_RIDE_IN_WATER = getBoolean("ride-in-water");
        RIDING_ENABLE_MOVE_EVENT = isSet("riding.enable-move-event") ? getBoolean("riding.enable-move-event") : Config.RIDING_ENABLE_MOVE_EVENT;
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
        RIDING_FIREBALL_COOLDOWN = (int) getDouble("riding.fireball.cooldown");
        RIDING_FIREBALL_SPEED = getDouble("riding.fireball.speed");
        RIDING_FIREBALL_DAMAGE = getDouble("riding.fireball.damage");
        RIDING_FIREBALL_EXPLOSION_DAMAGE = getDouble("riding.fireball.explosion-damage");
        RIDING_FIREBALL_EXPLOSION_FIRE = getBoolean("riding.fireball.explosion-fire");
        RIDING_FIREBALL_EXPLOSION_GRIEF = getBoolean("riding.fireball.explosion-grief");
    }
}
