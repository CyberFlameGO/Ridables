package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class DolphinConfig extends MobConfig {
    public float SPEED = 1.0F;
    public boolean BOUNCE = true;
    public boolean BUBBLES = true;
    public String SPACEBAR_MODE = "shoot";
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
            addDefault("speed", SPEED);
            addDefault("bounce", BOUNCE);
            addDefault("bubbles", BUBBLES);
            addDefault("spacebar-mode", SPACEBAR_MODE);
            addDefault("shoot.cooldown", SHOOT_COOLDOWN);
            addDefault("shoot.speed", SHOOT_SPEED);
            addDefault("shoot.damage", SHOOT_DAMAGE);
            addDefault("dash.cooldown", DASH_COOLDOWN);
            addDefault("dash.boost", DASH_BOOST);
            addDefault("dash.duration", DASH_DURATION);
            save();
        }

        SPEED = (float) getDouble("speed");
        BOUNCE = getBoolean("bounce");
        BUBBLES = getBoolean("bubbles");
        SPACEBAR_MODE = getString("spacebar-mode");
        SHOOT_COOLDOWN = (int) getDouble("shoot.cooldown");
        SHOOT_SPEED = (float) getDouble("shoot.speed");
        SHOOT_DAMAGE = (float) getDouble("shoot.damage");
        DASH_COOLDOWN = (int) getDouble("dash.cooldown");
        DASH_BOOST = (float) getDouble("dash.boost");
        DASH_DURATION = (int) getDouble("dash.duration");
    }
}
