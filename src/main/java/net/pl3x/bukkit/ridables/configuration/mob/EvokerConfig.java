package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class EvokerConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float JUMP_POWER = 0.5F;
    public float STEP_HEIGHT = 0.6F;
    public boolean RIDABLE_IN_WATER = true;
    public int FANGS_COOLDOWN = 40;
    public float FANGS_DAMAGE = 6.0F;

    public EvokerConfig() {
        super("evoker.yml");
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
            addDefault("fangs-cooldown", FANGS_COOLDOWN);
            addDefault("fangs-damage", FANGS_DAMAGE);
            save();
        }

        SPEED = (float) getDouble("speed");
        JUMP_POWER = (float) getDouble("jump-power");
        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        FANGS_COOLDOWN = (int) getDouble("fangs-cooldown");
        FANGS_DAMAGE = (float) getDouble("fangs-damage");
    }
}
