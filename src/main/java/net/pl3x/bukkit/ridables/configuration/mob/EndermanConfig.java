package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class EndermanConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float JUMP_POWER = 0.5F;
    public float STEP_HEIGHT = 1.0F;
    public boolean RIDABLE_IN_WATER = true;
    public boolean DAMAGE_WHEN_WET = true;
    public boolean EJECT_WHEN_WET = true;
    public boolean TELEPORT_WHEN_DAMAGED = true;

    public EndermanConfig() {
        super("enderman.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("speed", SPEED);
            addDefault("jump-power", JUMP_POWER);
            addDefault("step-height", STEP_HEIGHT);
            addDefault("ride-in-water", RIDABLE_IN_WATER);
            addDefault("damage-when-wet", DAMAGE_WHEN_WET);
            addDefault("eject-when-wet", EJECT_WHEN_WET);
            addDefault("teleport-when-damaged", TELEPORT_WHEN_DAMAGED);
            save();
        }

        SPEED = (float) getDouble("speed");
        JUMP_POWER = (float) getDouble("jump-power");
        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        DAMAGE_WHEN_WET = getBoolean("damage-when-wet");
        EJECT_WHEN_WET = getBoolean("eject-when-wet");
        TELEPORT_WHEN_DAMAGED = getBoolean("teleport-when-damaged");
    }
}
