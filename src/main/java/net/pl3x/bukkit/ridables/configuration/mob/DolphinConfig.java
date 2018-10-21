package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class DolphinConfig extends MobConfig {
    public double BASE_SPEED = 1.2D;
    public double RIDE_SPEED = 1.0D;
    public double MAX_HEALTH = 10.0D;
    public boolean BOUNCE = true;
    public boolean BUBBLES = true;
    public String SPACEBAR_MODE = "shoot";
    public double AI_ATTACK_DAMAGE = 3.0D;
    public double AI_FOLLOW_RANGE = 16.0D;
    public int SHOOT_COOLDOWN = 10;
    public float SHOOT_SPEED = 1.0F;
    public float SHOOT_DAMAGE = 2.0F;
    public int DASH_COOLDOWN = 10;
    public float DASH_BOOST = 1.5F;
    public int DASH_DURATION = 20;

    public DolphinConfig() {
        super("dolphin.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("ride-speed", RIDE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("bounce", BOUNCE);
            addDefault("bubbles", BUBBLES);
            addDefault("spacebar-mode", SPACEBAR_MODE);
            addDefault("ai.attack-damage", AI_ATTACK_DAMAGE);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("shoot.cooldown", SHOOT_COOLDOWN);
            addDefault("shoot.speed", SHOOT_SPEED);
            addDefault("shoot.damage", SHOOT_DAMAGE);
            addDefault("dash.cooldown", DASH_COOLDOWN);
            addDefault("dash.boost", DASH_BOOST);
            addDefault("dash.duration", DASH_DURATION);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        RIDE_SPEED = getDouble("ride-speed");
        MAX_HEALTH = getDouble("max-health");
        BOUNCE = getBoolean("bounce");
        BUBBLES = getBoolean("bubbles");
        SPACEBAR_MODE = getString("spacebar-mode");
        AI_ATTACK_DAMAGE = getDouble("ai.attack-damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        SHOOT_COOLDOWN = (int) getDouble("shoot.cooldown");
        SHOOT_SPEED = (float) getDouble("shoot.speed");
        SHOOT_DAMAGE = (float) getDouble("shoot.damage");
        DASH_COOLDOWN = (int) getDouble("dash.cooldown");
        DASH_BOOST = (float) getDouble("dash.boost");
        DASH_DURATION = (int) getDouble("dash.duration");
    }
}
