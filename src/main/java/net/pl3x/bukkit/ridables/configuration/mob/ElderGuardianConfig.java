package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class ElderGuardianConfig extends MobConfig {
    public double BASE_SPEED = 0.3D;
    public double MAX_HEALTH = 80.0D;
    public double AI_ATTACK_DAMAGE = 8.0D;
    public double AI_FOLLOW_RANGE = 16.0D;
    public double RIDING_SPEED = 1.0D;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public ElderGuardianConfig() {
        super("elder_guardian.yml");
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
            addDefault("riding.saddle.require", RIDING_SADDLE_REQUIRE);
            addDefault("riding.saddle.consume", RIDING_SADDLE_CONSUME);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_ATTACK_DAMAGE = getDouble("ai.attack-damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_SADDLE_REQUIRE = getBoolean("riding.saddle.require");
        RIDING_SADDLE_CONSUME = getBoolean("riding.saddle.consume");
    }
}
