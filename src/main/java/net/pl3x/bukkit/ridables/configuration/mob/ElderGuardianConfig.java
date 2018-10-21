package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class ElderGuardianConfig extends MobConfig {
    public double BASE_SPEED = 0.3D;
    public double RIDE_SPEED = 1.0D;
    public double MAX_HEALTH = 80.0D;
    public double AI_ATTACK_DAMAGE = 8.0D;
    public double AI_FOLLOW_RANGE = 16.0D;

    public ElderGuardianConfig() {
        super("elder_guardian.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;
            addDefault("base-speed", BASE_SPEED);
            addDefault("ride-speed", RIDE_SPEED);
            addDefault("max-health", MAX_HEALTH);
            addDefault("ai.attack-damage", AI_ATTACK_DAMAGE);
            addDefault("ai.follow-range", AI_FOLLOW_RANGE);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        RIDE_SPEED = getDouble("ride-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_ATTACK_DAMAGE = getDouble("ai.attack-damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
    }
}
