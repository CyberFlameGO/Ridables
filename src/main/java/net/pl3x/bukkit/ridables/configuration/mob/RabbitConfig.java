package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class RabbitConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float JUMP_POWER = 0.5F;
    public boolean RIDABLE_IN_WATER = true;
    public double KILLER_CHANCE = 0.0D;

    public RabbitConfig() {
        super("rabbit.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("speed", SPEED);
            addDefault("jump-power", JUMP_POWER);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            addDefault("spawn-killer-bunny-chance", KILLER_CHANCE);
            save();
        }

        SPEED = (float) getDouble("speed");
        JUMP_POWER = (float) getDouble("jump-power");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        KILLER_CHANCE = (float) getDouble("spawn-killer-bunny-chance");
    }
}
