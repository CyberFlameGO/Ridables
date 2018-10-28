package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class DolphinConfig extends MobConfig {
    public double BASE_SPEED = 1.2D;
    public double MAX_HEALTH = 10.0D;
    public double AI_ATTACK_DAMAGE = 3.0D;
    public double AI_FOLLOW_RANGE = 16.0D;
    public double RIDING_SPEED = 1.0D;
    public boolean RIDING_BOUNCE = true;
    public boolean RIDING_BUBBLES = true;
    public String RIDING_SPACEBAR_MODE = "shoot";
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;
    public int RIDING_SHOOT_COOLDOWN = 10;
    public float RIDING_SHOOT_SPEED = 1.0F;
    public float RIDING_SHOOT_DAMAGE = 2.0F;
    public int RIDING_DASH_COOLDOWN = 20;
    public float RIDING_DASH_BOOST = 1.5F;
    public int RIDING_DASH_DURATION = 20;

    public DolphinConfig() {
        super("dolphin.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ai.attack-damage", AI_ATTACK_DAMAGE);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.bounce", RIDING_BOUNCE);
            addDefault("riding.bubbles", RIDING_BUBBLES);
            addDefault("riding.spacebar-mode", RIDING_SPACEBAR_MODE);
            addDefault("riding.saddle.require", RIDING_SADDLE_REQUIRE);
            addDefault("riding.saddle.consume", RIDING_SADDLE_CONSUME);
            addDefault("riding.shoot.cooldown", RIDING_SHOOT_COOLDOWN);
            addDefault("riding.shoot.speed", RIDING_SHOOT_SPEED);
            addDefault("riding.shoot.damage", RIDING_SHOOT_DAMAGE);
            addDefault("riding.dash.cooldown", RIDING_DASH_COOLDOWN);
            addDefault("riding.dash.boost", RIDING_DASH_BOOST);
            addDefault("riding.dash.duration", RIDING_DASH_DURATION);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_ATTACK_DAMAGE = getDouble("ai.attack-damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_BOUNCE = getBoolean("riding.bounce");
        RIDING_BUBBLES = getBoolean("riding.bubbles");
        RIDING_SPACEBAR_MODE = getString("riding.spacebar-mode");
        RIDING_SADDLE_REQUIRE = getBoolean("riding.saddle.require");
        RIDING_SADDLE_CONSUME = getBoolean("riding.saddle.consume");
        RIDING_SHOOT_COOLDOWN = (int) getDouble("riding.shoot.cooldown");
        RIDING_SHOOT_SPEED = (float) getDouble("riding.shoot.speed");
        RIDING_SHOOT_DAMAGE = (float) getDouble("riding.shoot.damage");
        RIDING_DASH_COOLDOWN = (int) getDouble("riding.dash.cooldown");
        RIDING_DASH_BOOST = (float) getDouble("riding.dash.boost");
        RIDING_DASH_DURATION = (int) getDouble("riding.dash.duration");
    }
}
