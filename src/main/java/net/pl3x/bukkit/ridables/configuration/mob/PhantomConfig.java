package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;

public class PhantomConfig extends MobConfig {
    public double BASE_SPEED = 0.7D;
    public double MAX_HEALTH = 20.0D;
    public double AI_MELEE_DAMAGE = 6.0D;
    public double AI_FOLLOW_RANGE = 16.0D;
    public boolean AI_BURN_IN_SUNLIGHT = true;
    public boolean AI_ATTACK_IN_SUNLIGHT = true;
    public boolean AI_ENDER_CRYSTALS_ORBIT = false;
    public int AI_ENDER_CRYSTALS_BEAM_TICKS = 60;
    public int AI_ENDER_CRYSTALS_IDLE_COOLDOWN = 60;
    public float AI_ENDER_CRYSTALS_DAMAGE = 1.0F;
    public int AI_ENDER_CRYSTALS_DAMAGE_COOLDOWN = 20;
    public double RIDING_SPEED = 0.7D;
    public float RIDING_GRAVITY = 0.04F;
    public boolean RIDING_RIDE_IN_WATER = true;
    public boolean RIDING_BURN_IN_SUNLIGHT = true;
    public int RIDING_FLYING_MAX_Y = 256;
    public float RIDING_SHOOT_DAMAGE = 1.0F;
    public boolean RIDING_ENABLE_MOVE_EVENT = false;
    public boolean RIDING_SADDLE_REQUIRE = false;
    public boolean RIDING_SADDLE_CONSUME = false;

    public PhantomConfig() {
        super("phantom.yml");
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
            addDefault("ai.burn-in-sunlight", AI_BURN_IN_SUNLIGHT);
            addDefault("ai.attack-in-sunlight", AI_ATTACK_IN_SUNLIGHT);
            addDefault("ai.ender-crystals.orbit", AI_ENDER_CRYSTALS_ORBIT);
            addDefault("ai.ender-crystals.beam-ticks", AI_ENDER_CRYSTALS_BEAM_TICKS);
            addDefault("ai.ender-crystals.idle-cooldown", AI_ENDER_CRYSTALS_IDLE_COOLDOWN);
            addDefault("ai.ender-crystals.damage", AI_ENDER_CRYSTALS_DAMAGE);
            addDefault("ai.ender-crystals.damage-cooldown", AI_ENDER_CRYSTALS_DAMAGE_COOLDOWN);
            addDefault("riding.speed", RIDING_SPEED);
            addDefault("riding.gravity", RIDING_GRAVITY);
            addDefault("riding.ride-in-water", RIDING_RIDE_IN_WATER);
            addDefault("riding.burn-in-sunlight", RIDING_BURN_IN_SUNLIGHT);
            addDefault("riding.flying-max-y", RIDING_FLYING_MAX_Y);
            addDefault("riding.shoot.damage", RIDING_SHOOT_DAMAGE);
            save();
        }

        BASE_SPEED = getDouble("base-speed");
        MAX_HEALTH = getDouble("max-health");
        AI_MELEE_DAMAGE = getDouble("ai.melee-damage");
        AI_FOLLOW_RANGE = getDouble("ai.follow-range");
        AI_BURN_IN_SUNLIGHT = getBoolean("ai.burn-in-sunlight");
        AI_ATTACK_IN_SUNLIGHT = getBoolean("ai.attack-in-sunlight");
        AI_ENDER_CRYSTALS_ORBIT = getBoolean("ai.ender-crystals.orbit");
        AI_ENDER_CRYSTALS_BEAM_TICKS = (int) getDouble("ai.ender-crystals.beam-ticks");
        AI_ENDER_CRYSTALS_IDLE_COOLDOWN = (int) getDouble("ai.ender-crystals.idle-cooldown");
        AI_ENDER_CRYSTALS_DAMAGE = (float) getDouble("ai.ender-crystals.damage");
        AI_ENDER_CRYSTALS_DAMAGE_COOLDOWN = (int) getDouble("ai.ender-crystals.damage-cooldown");
        RIDING_SPEED = getDouble("riding.speed");
        RIDING_GRAVITY = (float) getDouble("riding.gravity");
        RIDING_RIDE_IN_WATER = getBoolean("riding.ride-in-water");
        RIDING_BURN_IN_SUNLIGHT = getBoolean("riding.burn-in-sunlight");
        RIDING_FLYING_MAX_Y = (int) getDouble("riding.flying-max-y");
        RIDING_SHOOT_DAMAGE = (float) getDouble("riding.shoot.damage");
        RIDING_ENABLE_MOVE_EVENT = isSet("riding.enable-move-event") ? getBoolean("riding.enable-move-event") : Config.RIDING_ENABLE_MOVE_EVENT;
        RIDING_SADDLE_REQUIRE = isSet("riding.saddle.require") ? getBoolean("riding.saddle.require") : Config.RIDING_SADDLE_REQUIRE;
        RIDING_SADDLE_CONSUME = isSet("riding.saddle.consume") ? getBoolean("riding.saddle.consume") : Config.RIDING_SADDLE_CONSUME;
    }
}
