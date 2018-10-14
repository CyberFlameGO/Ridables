package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class ShulkerConfig extends MobConfig {
    public int PEEK_HEIGHT = 33;
    public int SHOOT_COOLDOWN = 10;
    public float SHOOT_SPEED = 1.0F;
    public float SHOOT_DAMAGE = 4.0F;

    public ShulkerConfig() {
        super("shulker.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("peek-height", PEEK_HEIGHT);
            addDefault("shoot.cooldown", SHOOT_COOLDOWN);
            addDefault("shoot.speed", SHOOT_SPEED);
            addDefault("shoot.damage", SHOOT_DAMAGE);
            save();
        }

        PEEK_HEIGHT = (int) getDouble("peek-height");
        SHOOT_COOLDOWN = (int) getDouble("shoot.cooldown");
        SHOOT_SPEED = (float) getDouble("shoot.speed");
        SHOOT_DAMAGE = (float) getDouble("shoot.damage");
    }
}
