package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class WitherConfig extends MobConfig {
    public double BASE_SPEED = 0.6D;
    public double MAX_HEALTH = 300.0D;
    public boolean AI_SPAWN_EXPLOSION_ENABLED = true;
    public float AI_SPAWN_EXPLOSION_DAMAGE = 10.0F;
    public float AI_SPAWN_EXPLOSION_RADIUS = 7.0F;
    public boolean AI_SPAWN_EXPLOSION_GRIEF = true;
    public boolean AI_SPAWN_EXPLOSION_FIRE = false;
    public float AI_REGEN_EVERY_SECOND = 1.0F;
    public double AI_FOLLOW_RANGE = 40.0D;
    public double AI_KNOCKBACK_RESISTANCE = 0.0D;
    public double AI_ARMOR = 4.0D;
    public double AI_ARMOR_TOUGHNESS = 0.0D;
    public float AI_SHOOT_SPEED = 1.0F;
    public float AI_SHOOT_DAMAGE = 1.0F;
    public float AI_SHOOT_HEAL_AMOUNT = 1.0F;
    public int AI_SHOOT_EFFECT_DURATION = 10;
    public boolean AI_SHOOT_GRIEF = true;
    public double RIDING_SPEED = 0.6D;
    public int RIDING_FLYING_MAX_Y = 256;
    public int RIDING_SHOOT_COOLDOWN = 20;
    public float RIDING_SHOOT_SPEED = 1.0F;
    public float RIDING_SHOOT_DAMAGE = 1.0F;
    public float RIDING_SHOOT_HEAL_AMOUNT = 1.0F;
    public int RIDING_SHOOT_EFFECT_DURATION = 10;
    public boolean RIDING_SHOOT_GRIEF = true;
    public boolean RIDING_ENABLE_MOVE_EVENT = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public WitherConfig() {
        super("wither.yml");
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ai.spawn.explosion.enabled", AI_SPAWN_EXPLOSION_ENABLED);
            addDefault("ai.spawn.explosion.damage", AI_SPAWN_EXPLOSION_DAMAGE);
            addDefault("ai.spawn.explosion.radius", AI_SPAWN_EXPLOSION_RADIUS);
            addDefault("ai.spawn.explosion.grief", AI_SPAWN_EXPLOSION_GRIEF);
            addDefault("ai.spawn.explosion.fire", AI_SPAWN_EXPLOSION_FIRE);
            addDefault("ai.regen-every-second", AI_REGEN_EVERY_SECOND);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("ai.knockback-resistance", AI_KNOCKBACK_RESISTANCE);
            addDefault("ai.armor", AI_ARMOR);
            addDefault("ai.armor-toughness", AI_ARMOR_TOUGHNESS);
            addDefault("ai.shoot.speed", AI_SHOOT_SPEED);
            addDefault("ai.shoot.damage", AI_SHOOT_DAMAGE);
            addDefault("ai.shoot.heal-amount", AI_SHOOT_HEAL_AMOUNT);
            addDefault("ai.shoot.effect-duration", AI_SHOOT_EFFECT_DURATION);
            addDefault("ai.shoot.grief", AI_SHOOT_GRIEF);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.flying-max-y", RIDING_FLYING_MAX_Y);
            addDefault("riding.shoot.cooldown", RIDING_SHOOT_COOLDOWN);
            addDefault("riding.shoot.speed", RIDING_SHOOT_SPEED);
            addDefault("riding.shoot.damage", RIDING_SHOOT_DAMAGE);
            addDefault("riding.shoot.heal-amount", RIDING_SHOOT_HEAL_AMOUNT);
            addDefault("riding.shoot.effect-duration", RIDING_SHOOT_EFFECT_DURATION);
            addDefault("riding.shoot.grief", RIDING_SHOOT_GRIEF);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_SPAWN_EXPLOSION_ENABLED = getBoolean("ai.spawn.explosion.enabled");
        AI_SPAWN_EXPLOSION_DAMAGE = (float) getDouble("ai.spawn.explosion.damage");
        AI_SPAWN_EXPLOSION_RADIUS = (float) getDouble("ai.spawn.explosion.radius");
        AI_SPAWN_EXPLOSION_GRIEF = getBoolean("ai.spawn.explosion.grief");
        AI_SPAWN_EXPLOSION_FIRE = getBoolean("ai.spawn.explosion.fire");
        AI_REGEN_EVERY_SECOND = (float) getDouble("ai.regen-every-second");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        AI_KNOCKBACK_RESISTANCE = getDouble("ai.knockback-resistance");
        AI_ARMOR = getDouble("ai.armor");
        AI_ARMOR_TOUGHNESS = getDouble("ai.armor-toughness");
        AI_SHOOT_SPEED = (float) getDouble("ai.shoot.speed");
        AI_SHOOT_DAMAGE = (float) getDouble("ai.shoot.damage");
        AI_SHOOT_HEAL_AMOUNT = (float) getDouble("ai.shoot.heal-amount");
        AI_SHOOT_EFFECT_DURATION = (int) getDouble("ai.shoot.effect-duration");
        AI_SHOOT_GRIEF = getBoolean("ai.shoot.grief");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_FLYING_MAX_Y = (int) getDouble("riding.flying-max-y");
        RIDING_SHOOT_COOLDOWN = (int) getDouble("riding.shoot.cooldown");
        RIDING_SHOOT_SPEED = (float) getDouble("riding.shoot.speed");
        RIDING_SHOOT_DAMAGE = (float) getDouble("riding.shoot.damage");
        RIDING_SHOOT_HEAL_AMOUNT = (float) getDouble("riding.shoot.heal-amount");
        RIDING_SHOOT_EFFECT_DURATION = (int) getDouble("riding.shoot.effect-duration");
        RIDING_SHOOT_GRIEF = getBoolean("riding.shoot.grief");
        RIDING_ENABLE_MOVE_EVENT = isSet("riding.enable-move-event") ? getBoolean("riding.enable-move-event") : Config.RIDING_ENABLE_MOVE_EVENT;
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
    }
}
