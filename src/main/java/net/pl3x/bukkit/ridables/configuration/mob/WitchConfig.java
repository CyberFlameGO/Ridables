package net.pl3x.bukkit.ridables.configuration.mob;

import net.minecraft.server.v1_13_R2.PotionRegistry;
import net.pl3x.bukkit.ridables.configuration.MobConfig;
import net.pl3x.bukkit.ridables.util.Logger;

public class WitchConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float JUMP_POWER = 0.5F;
    public float STEP_HEIGHT = 0.6F;
    public boolean RIDABLE_IN_WATER = true;
    public int SHOOT_COOLDOWN = 40;
    public float SHOOT_SPEED = 1.0F;
    public PotionRegistry SHOOT_POTION_TYPE = PotionRegistry.a("harming");

    public WitchConfig() {
        super("witch.yml");
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
            addDefault("shoot.cooldown", SHOOT_COOLDOWN);
            addDefault("shoot.speed", SHOOT_SPEED);
            addDefault("shoot.potion", SHOOT_POTION_TYPE.b("")); // getNamePrefixed
            save();
        }

        SPEED = (float) getDouble("speed");
        JUMP_POWER = (float) getDouble("jump-power");
        STEP_HEIGHT = (float) getDouble("step-height");
        RIDABLE_IN_WATER = getBoolean("ride-in-water");
        SHOOT_COOLDOWN = (int) getDouble("shoot.cooldown", 40);
        SHOOT_SPEED = (float) getDouble("shoot.speed", 1.0D);

        String potion = getString("shoot.potion", "harming").toLowerCase();
        PotionRegistry potionType = PotionRegistry.a(potion);
        if (potionType != null) {
            SHOOT_POTION_TYPE = potionType;
        } else {
            Logger.error("Unknown potion type for witch.shoot.potion: " + potion);
            Logger.error("Using potion of harming as default");
            SHOOT_POTION_TYPE = PotionRegistry.a("harming");
        }
    }
}
