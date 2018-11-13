package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class BlazeConfig extends MobConfig {
    public double BASE_SPEED = 0.23D;
    public double MAX_HEALTH = 20.0D;
    public double AI_MELEE_DAMAGE = 6.0D;
    public double AI_FOLLOW_RANGE = 48.0D;
    public double AI_SHOOT_SPEED = 1.0D;
    public double AI_SHOOT_IMPACT_DAMAGE = 6.0D;
    public double AI_SHOOT_EXPLOSION_DAMAGE = 17.0D;
    public boolean AI_SHOOT_EXPLOSION_FIRE = true;
    public boolean AI_SHOOT_GRIEF = true;
    public double RIDING_SPEED = 1.0D;
    public double RIDING_VERTICAL = 0.5D;
    public double RIDING_GRAVITY = 0.04D;
    public boolean RIDING_RIDE_IN_WATER = true;
    public int RIDING_FLYING_MAX_Y = 256;
    public boolean RIDING_ENABLE_MOVE_EVENT = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;
    public int RIDING_SHOOT_COOLDOWN = 20;
    public double RIDING_SHOOT_SPEED = 1.0D;
    public double RIDING_SHOOT_IMPACT_DAMAGE = 5.0D;
    public double RIDING_SHOOT_EXPLOSION_DAMAGE = 10.0D;
    public boolean RIDING_SHOOT_EXPLOSION_FIRE = false;
    public boolean RIDING_SHOOT_GRIEF = true;

    public BlazeConfig() {
        super("blaze.yml");
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ai.melee-damage", AI_MELEE_DAMAGE);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("ai.shoot.speed", AI_SHOOT_SPEED);
            addDefault("ai.shoot.impact-damage", AI_SHOOT_IMPACT_DAMAGE);
            addDefault("ai.shoot.explosion-damage", AI_SHOOT_EXPLOSION_DAMAGE);
            addDefault("ai.shoot.explosion-fire", AI_SHOOT_EXPLOSION_FIRE);
            addDefault("ai.shoot.grief", AI_SHOOT_GRIEF);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.vertical", RIDING_VERTICAL);
            addDefault("riding.gravity", RIDING_GRAVITY);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            addDefault("riding.flying-max-y", RIDING_FLYING_MAX_Y);
            addDefault("riding.shoot.cooldown", RIDING_SHOOT_COOLDOWN);
            addDefault("riding.shoot.speed", RIDING_SHOOT_SPEED);
            addDefault("riding.shoot.impact-damage", RIDING_SHOOT_IMPACT_DAMAGE);
            addDefault("riding.shoot.explosion-damage", RIDING_SHOOT_EXPLOSION_DAMAGE);
            addDefault("riding.shoot.explosion-fire", RIDING_SHOOT_EXPLOSION_FIRE);
            addDefault("riding.shoot.grief", RIDING_SHOOT_GRIEF);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_MELEE_DAMAGE = getDouble("ai.melee-damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        AI_SHOOT_SPEED = getDouble("ai.shoot.speed");
        AI_SHOOT_IMPACT_DAMAGE = getDouble("ai.shoot.impact-damage");
        AI_SHOOT_EXPLOSION_DAMAGE = getDouble("ai.shoot.explosion-damage");
        AI_SHOOT_EXPLOSION_FIRE = getBoolean("ai.shoot.explosion-fire");
        AI_SHOOT_GRIEF = getBoolean("ai.shoot.grief");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_VERTICAL = getDouble("riding.vertical");
        RIDING_GRAVITY = getDouble("riding.gravity");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
        RIDING_FLYING_MAX_Y = (int) getDouble("riding.flying-max-y");
        RIDING_ENABLE_MOVE_EVENT = isSet("riding.enable-move-event") ? getBoolean("riding.enable-move-event") : Config.RIDING_ENABLE_MOVE_EVENT;
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
        RIDING_SHOOT_COOLDOWN = (int) getDouble("riding.shoot.cooldown");
        RIDING_SHOOT_SPEED = getDouble("riding.shoot.speed");
        RIDING_SHOOT_IMPACT_DAMAGE = getDouble("riding.shoot.impact-damage");
        RIDING_SHOOT_EXPLOSION_DAMAGE = getDouble("riding.shoot.explosion-damage");
        RIDING_SHOOT_EXPLOSION_FIRE = getBoolean("riding.shoot.explosion-fire");
        RIDING_SHOOT_GRIEF = getBoolean("riding.shoot.grief");
    }
}
