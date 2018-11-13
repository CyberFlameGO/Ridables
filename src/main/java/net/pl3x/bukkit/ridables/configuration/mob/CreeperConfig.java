package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class CreeperConfig extends MobConfig {
    public double BASE_SPEED = 0.25D;
    public double MAX_HEALTH = 20.0D;
    public float AI_JUMP_POWER = 0.42F;
    public float AI_STEP_HEIGHT = 0.6F;
    public double AI_FOLLOW_RANGE = 16.0D;
    public double AI_EXPLOSION_DAMAGE = 5.0D;
    public int AI_EXPLOSION_RADIUS = 3;
    public boolean AI_EXPLOSION_GRIEF = true;
    public boolean AI_EXPLOSION_FIRE = false;
    public boolean AI_EXPLOSION_LINGERING_CLOUD = true;
    public double RIDING_SPEED = 1.0D;
    public float RIDING_JUMP_POWER = 0.5F;
    public float RIDING_STEP_HEIGHT = 0.6F;
    public boolean RIDING_RIDE_IN_WATER = true;
    public boolean RIDING_ENABLE_MOVE_EVENT = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;
    public double RIDING_EXPLOSION_DAMAGE = 5.0D;
    public int RIDING_EXPLOSION_RADIUS = 3;
    public boolean RIDING_EXPLOSION_GRIEF = true;
    public boolean RIDING_EXPLOSION_FIRE = false;
    public boolean RIDING_EXPLOSION_LINGERING_CLOUD = false;

    public CreeperConfig() {
        super("creeper.yml");
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ai.jump-power", AI_JUMP_POWER);
            addDefault("ai.step-height", AI_STEP_HEIGHT);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("ai.explosion.damage", AI_EXPLOSION_DAMAGE);
            addDefault("ai.explosion.radius", AI_EXPLOSION_RADIUS);
            addDefault("ai.explosion.grief", AI_EXPLOSION_GRIEF);
            addDefault("ai.explosion.fire", AI_EXPLOSION_FIRE);
            addDefault("ai.explosion.lingering-cloud", AI_EXPLOSION_LINGERING_CLOUD);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.jump-power", RIDING_JUMP_POWER);
            addDefault("riding.step-height", RIDING_STEP_HEIGHT);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            addDefault("riding.explosion.damage", RIDING_EXPLOSION_DAMAGE);
            addDefault("riding.explosion.radius", RIDING_EXPLOSION_RADIUS);
            addDefault("riding.explosion.grief", RIDING_EXPLOSION_GRIEF);
            addDefault("riding.explosion.fire", RIDING_EXPLOSION_FIRE);
            addDefault("riding.explosion.lingering-cloud", RIDING_EXPLOSION_LINGERING_CLOUD);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_JUMP_POWER = (float) getDouble("ai.jump-power");
        AI_STEP_HEIGHT = (float) getDouble("ai.step-height");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        AI_EXPLOSION_DAMAGE = getDouble("ai.explosion.damage");
        AI_EXPLOSION_RADIUS = (int) getDouble("ai.explosion.radius");
        AI_EXPLOSION_GRIEF = getBoolean("ai.explosion.grief");
        AI_EXPLOSION_FIRE = getBoolean("ai.explosion.fire");
        AI_EXPLOSION_LINGERING_CLOUD = getBoolean("ai.explosion.lingering-cloud");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_JUMP_POWER = (float) getDouble("riding.jump-power");
        RIDING_STEP_HEIGHT = (float) getDouble("riding.step-height");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
        RIDING_ENABLE_MOVE_EVENT = isSet("riding.enable-move-event") ? getBoolean("riding.enable-move-event") : Config.RIDING_ENABLE_MOVE_EVENT;
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
        RIDING_EXPLOSION_DAMAGE = getDouble("riding.explosion.damage");
        RIDING_EXPLOSION_RADIUS = (int) getDouble("riding.explosion.radius");
        RIDING_EXPLOSION_GRIEF = getBoolean("riding.explosion.grief");
        RIDING_EXPLOSION_FIRE = getBoolean("riding.explosion.fire");
        RIDING_EXPLOSION_LINGERING_CLOUD = getBoolean("riding.explosion.lingering-cloud");
    }
}
