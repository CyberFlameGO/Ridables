package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class EnderDragonConfig extends MobConfig {
    public double BASE_SPEED = 0.7D;
    public double MAX_HEALTH = 200.0D;
    public float AI_MELEE_DAMAGE = 10.0F;
    public double AI_FOLLOW_RANGE = 16.0D;
    public double AI_KNOCKBACK_RESISTANCE = 0.0D;
    public double AI_ARMOR = 0.0D;
    public double AI_ARMOR_TOUGHNESS = 0.0D;
    public double RIDING_SPEED = 0.7D;
    public int RIDING_FLYING_MAX_Y = 256;
    public boolean RIDING_ENABLE_MOVE_EVENT = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public EnderDragonConfig() {
        super("ender_dragon.yml");
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
            addDefault("ai.knockback-resistance", AI_KNOCKBACK_RESISTANCE);
            addDefault("ai.armor", AI_ARMOR);
            addDefault("ai.armor-toughness", AI_ARMOR_TOUGHNESS);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.flying-max-y", RIDING_FLYING_MAX_Y);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_MELEE_DAMAGE = (float) getDouble("ai.melee-damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        AI_KNOCKBACK_RESISTANCE = getDouble("ai.knockback-resistance");
        AI_ARMOR = getDouble("ai.armor");
        AI_ARMOR_TOUGHNESS = getDouble("ai.armor-toughness");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_FLYING_MAX_Y = (int) getDouble("riding.flying-max-y");
        RIDING_ENABLE_MOVE_EVENT = isSet("riding.enable-move-event") ? getBoolean("riding.enable-move-event") : Config.RIDING_ENABLE_MOVE_EVENT;
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
    }
}
