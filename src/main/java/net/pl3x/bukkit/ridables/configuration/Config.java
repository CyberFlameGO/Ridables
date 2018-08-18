package net.pl3x.bukkit.ridables.configuration;

import net.pl3x.bukkit.ridables.Ridables;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public static String LANGUAGE_FILE = "lang-en.yml";
    public static boolean DEBUG_MODE = false;

    public static boolean CHECK_FOR_UPDATES = true;

    public static boolean USE_NEW_WASD_YAW_CALCULATIONS = true;
    public static boolean UNMOUNT_ON_TELEPORT = true;
    public static boolean CANCEL_COMMANDS_WHILE_RIDING = false;
    public static boolean REQUIRE_SADDLE = false;
    public static boolean CONSUME_SADDLE = false;

    public static int FLYING_MAX_Y = 256;

    public static boolean BAT_ENABLED = false;
    public static float BAT_SPEED = 1.0F;
    public static float BAT_VERTICAL = 1.0F;
    public static float BAT_GRAVITY = 0.04F;

    public static boolean CAVE_SPIDER_ENABLED = false;
    public static float CAVE_SPIDER_SPEED = 1.0F;
    public static float CAVE_SPIDER_JUMP_POWER = 0.5F;
    public static boolean CAVE_SPIDER_CLIMB_WALLS = true;
    public static float CAVE_SPIDER_CLIMB_SPEED = 1.0F;

    public static boolean CHICKEN_ENABLED = false;
    public static float CHICKEN_SPEED = 1.0F;
    public static float CHICKEN_JUMP_POWER = 0.5F;

    public static boolean COW_ENABLED = false;
    public static float COW_SPEED = 1.0F;
    public static float COW_JUMP_POWER = 0.5F;

    public static boolean CREEPER_ENABLED = false;
    public static float CREEPER_SPEED = 1.0F;
    public static float CREEPER_JUMP_POWER = 0.5F;
    public static float CREEPER_EXPLOSION_DAMAGE = 5.0F;
    public static boolean CREEPER_EXPLOSION_GRIEF = true;

    public static boolean DOLPHIN_ENABLED = false;
    public static float DOLPHIN_SPEED = 1.0F;
    public static boolean DOLPHIN_BOUNCE = true;
    public static boolean DOLPHIN_BUBBLES = true;
    public static String DOLPHIN_SPACEBAR_MODE = "shoot";
    public static int DOLPHIN_SHOOT_COOLDOWN = 10;
    public static float DOLPHIN_SHOOT_SPEED = 1.0F;
    public static float DOLPHIN_SHOOT_DAMAGE = 2.0F;
    public static int DOLPHIN_DASH_COOLDOWN = 10;
    public static float DOLPHIN_DASH_BOOST = 1.5F;
    public static int DOLPHIN_DASH_DURATION = 20;

    public static boolean DRAGON_ENABLED = false;
    public static float DRAGON_SPEED = 1.0F;

    public static boolean DROWNED_ENABLED = false;
    public static float DROWNED_SPEED = 1.0F;
    public static float DROWNED_JUMP_POWER = 0.5F;

    public static boolean ENDERMAN_ENABLED = false;
    public static float ENDERMAN_SPEED = 1.0F;
    public static float ENDERMAN_JUMP_POWER = 0.5F;
    public static boolean ENDERMAN_DAMAGE_WHEN_WET = true;
    public static boolean ENDERMAN_EJECT_WHEN_WET = true;
    public static boolean ENDERMAN_TELEPORT_WHEN_DAMAGED = true;

    public static boolean ENDERMITE_ENABLED = false;
    public static float ENDERMITE_SPEED = 1.0F;
    public static float ENDERMITE_JUMP_POWER = 0.5F;

    public static boolean EVOKER_ENABLED = false;
    public static float EVOKER_SPEED = 1.0F;
    public static float EVOKER_JUMP_POWER = 0.5F;

    public static boolean GHAST_ENABLED = false;
    public static float GHAST_SPEED = 1.0F;
    public static int GHAST_SHOOT_COOLDOWN = 20;
    public static float GHAST_SHOOT_SPEED = 1.0F;
    public static float GHAST_SHOOT_DAMAGE = 5.0F;
    public static boolean GHAST_SHOOT_GRIEF = true;

    public static boolean GIANT_ENABLED = false;
    public static float GIANT_SPEED = 1.0F;
    public static float GIANT_JUMP_POWER = 0.5F;

    public static boolean HUSK_ENABLED = false;
    public static float HUSK_SPEED = 1.0F;
    public static float HUSK_JUMP_POWER = 0.5F;

    public static boolean ILLUSIONER_ENABLED = false;
    public static float ILLUSIONER_SPEED = 1.0F;
    public static float ILLUSIONER_JUMP_POWER = 0.5F;

    public static boolean IRON_GOLEM_ENABLED = false;
    public static float IRON_GOLEM_SPEED = 1.0F;
    public static float IRON_GOLEM_JUMP_POWER = 0.5F;

    public static boolean LLAMA_ENABLED = false;
    public static float LLAMA_SPEED = 1.0F;
    public static float LLAMA_JUMP_POWER = 0.5F;
    public static boolean LLAMA_CARAVAN = true;

    public static boolean MOOSHROOM_ENABLED = false;
    public static float MOOSHROOM_SPEED = 1.0F;
    public static float MOOSHROOM_JUMP_POWER = 0.5F;

    public static boolean OCELOT_ENABLED = false;
    public static float OCELOT_SPEED = 1.0F;
    public static float OCELOT_JUMP_POWER = 0.5F;

    public static boolean PARROT_ENABLED = false;
    public static float PARROT_SPEED = 1.0F;
    public static float PARROT_VERTICAL = 1.0F;
    public static float PARROT_GRAVITY = 0.04F;

    public static boolean PIG_ENABLED = false;
    public static float PIG_SPEED = 1.0F;
    public static float PIG_JUMP_POWER = 0.5F;
    public static boolean PIG_SADDLE_BACK = true;

    public static boolean PHANTOM_ENABLED = false;
    public static float PHANTOM_SPEED = 1.0F;
    public static double PHANTOM_GRAVITY = 0.05D;
    public static boolean PHANTOM_FALL_DAMAGE = true;
    public static boolean PHANTOM_BURN_IN_SUNLIGHT = true;
    public static float PHANTOM_SHOOT_DAMAGE = 1.0F;

    public static boolean POLAR_BEAR_ENABLED = false;
    public static float POLAR_BEAR_SPEED = 1.0F;
    public static float POLAR_BEAR_JUMP_POWER = 0.5F;
    public static float POLAR_BEAR_STEP_HEIGHT = 1.0F;
    public static boolean POLAR_BEAR_STAND = true;

    public static boolean RABBIT_ENABLED = false;
    public static float RABBIT_SPEED = 1.0F;
    public static float RABBIT_JUMP_POWER = 0.5F;

    public static boolean SHEEP_ENABLED = false;
    public static float SHEEP_SPEED = 1.0F;
    public static float SHEEP_JUMP_POWER = 0.5F;

    public static boolean SILVERFISH_ENABLED = false;
    public static float SILVERFISH_SPEED = 1.0F;
    public static float SILVERFISH_JUMP_POWER = 0.5F;

    public static boolean SKELETON_ENABLED = false;
    public static float SKELETON_SPEED = 1.0F;
    public static float SKELETON_JUMP_POWER = 0.5F;

    public static boolean SKELETON_HORSE_ENABLED = false;

    public static boolean SNOWMAN_ENABLED = false;
    public static float SNOWMAN_SPEED = 1.0F;
    public static float SNOWMAN_JUMP_POWER = 0.5F;
    public static boolean SNOWMAN_DAMAGE_WHEN_HOT = true;
    public static boolean SNOWMAN_DAMAGE_WHEN_WET = true;
    public static boolean SNOWMAN_LEAVE_SNOW_TRAIL = true;

    public static boolean SPIDER_ENABLED = false;
    public static float SPIDER_SPEED = 1.0F;
    public static float SPIDER_JUMP_POWER = 0.5F;
    public static boolean SPIDER_CLIMB_WALLS = true;
    public static float SPIDER_CLIMB_SPEED = 1.0F;

    public static boolean STRAY_ENABLED = false;
    public static float STRAY_SPEED = 1.0F;
    public static float STRAY_JUMP_POWER = 0.5F;

    public static boolean TURTLE_ENABLED = false;
    public static float TURTLE_SPEED_LAND = 1.0F;
    public static float TURTLE_SPEED_WATER = 1.0F;

    public static boolean VILLAGER_ENABLED = false;
    public static float VILLAGER_SPEED = 1.0F;
    public static float VILLAGER_JUMP_POWER = 0.5F;

    public static boolean VINDICATOR_ENABLED = false;
    public static float VINDICATOR_SPEED = 1.0F;
    public static float VINDICATOR_JUMP_POWER = 0.5F;

    public static boolean WITCH_ENABLED = false;
    public static float WITCH_SPEED = 1.0F;
    public static float WITCH_JUMP_POWER = 0.5F;

    public static boolean WITHER_SKELETON_ENABLED = false;
    public static float WITHER_SKELETON_SPEED = 1.0F;
    public static float WITHER_SKELETON_JUMP_POWER = 0.5F;

    public static boolean WOLF_ENABLED = false;
    public static float WOLF_SPEED = 1.0F;
    public static float WOLF_JUMP_POWER = 0.5F;

    public static boolean ZOMBIE_ENABLED = false;
    public static float ZOMBIE_SPEED = 1.0F;
    public static float ZOMBIE_JUMP_POWER = 0.5F;

    public static boolean ZOMBIE_HORSE_ENABLED = false;

    public static boolean ZOMBIE_PIGMAN_ENABLED = false;
    public static float ZOMBIE_PIGMAN_SPEED = 1.0F;
    public static float ZOMBIE_PIGMAN_JUMP_POWER = 0.5F;

    public static boolean ZOMBIE_VILLAGER_ENABLED = false;
    public static float ZOMBIE_VILLAGER_SPEED = 1.0F;
    public static float ZOMBIE_VILLAGER_JUMP_POWER = 0.5F;

    /**
     * Reload the config.yml from disk
     */
    public static void reload() {
        Ridables plugin = Ridables.getInstance();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        LANGUAGE_FILE = config.getString("language-file", "lang-en.yml");
        DEBUG_MODE = config.getBoolean("debug-mode", false);

        CHECK_FOR_UPDATES = config.getBoolean("update-checker", true);

        USE_NEW_WASD_YAW_CALCULATIONS = config.getBoolean("use-new-wasd-yaw-calculations", true);
        UNMOUNT_ON_TELEPORT = config.getBoolean("unmount-on-teleport", true);
        CANCEL_COMMANDS_WHILE_RIDING = config.getBoolean("cancel-commands-while-riding", false);
        REQUIRE_SADDLE = config.getBoolean("saddle-to-mount", false);
        CONSUME_SADDLE = config.getBoolean("consume-saddle", false);

        FLYING_MAX_Y = (int) config.getDouble("flying-max-y", 256D);

        BAT_ENABLED = config.getBoolean("bat.enabled", false);
        BAT_SPEED = (float) config.getDouble("bat.speed", 1.0D);
        BAT_VERTICAL = (float) config.getDouble("bat.vertical", 1.0D);
        BAT_GRAVITY = (float) config.getDouble("bat.gravity", 0.04D);

        CAVE_SPIDER_ENABLED = config.getBoolean("cave-spider.enabled", false);
        CAVE_SPIDER_SPEED = (float) config.getDouble("cave-spider.speed", 1.0D);
        CAVE_SPIDER_JUMP_POWER = (float) config.getDouble("cave-spider.jump-power", 0.5D);
        CAVE_SPIDER_CLIMB_WALLS = config.getBoolean("cave-spider.climb-walls", true);
        CAVE_SPIDER_CLIMB_SPEED = (float) config.getDouble("cave-spider.climb-speed", 1.0D);

        CHICKEN_ENABLED = config.getBoolean("chicken.enabled", false);
        CHICKEN_SPEED = (float) config.getDouble("chicken.speed", 1.0D);
        CHICKEN_JUMP_POWER = (float) config.getDouble("chicken.jump-power", 0.5D);

        COW_ENABLED = config.getBoolean("cow.enabled", false);
        COW_SPEED = (float) config.getDouble("cow.speed", 1.0D);
        COW_JUMP_POWER = (float) config.getDouble("cow.jump-power", 0.5D);

        CREEPER_ENABLED = config.getBoolean("creeper.enabled", false);
        CREEPER_SPEED = (float) config.getDouble("creeper.speed", 1.0D);
        CREEPER_JUMP_POWER = (float) config.getDouble("creeper.jump-power", 0.5D);
        CREEPER_EXPLOSION_DAMAGE = (float) config.getDouble("creeper.explosion.damage", 5.0D);
        CREEPER_EXPLOSION_GRIEF = config.getBoolean("creeper.explosion.grief", true);

        DOLPHIN_ENABLED = config.getBoolean("dolphin.enabled", false);
        DOLPHIN_SPEED = (float) config.getDouble("dolphin.speed", 1.0D);
        DOLPHIN_BOUNCE = config.getBoolean("dolphin.bounce", true);
        DOLPHIN_BUBBLES = config.getBoolean("dolphin.bubbles", true);
        DOLPHIN_SPACEBAR_MODE = config.getString("dolphin.spacebar", "shoot");
        DOLPHIN_SHOOT_COOLDOWN = (int) config.getDouble("dolphin.shoot.cooldown", 10);
        DOLPHIN_SHOOT_SPEED = (float) config.getDouble("dolphin.shoot.speed", 1.0D);
        DOLPHIN_SHOOT_DAMAGE = (float) config.getDouble("dolphin.shoot.damage", 2.0D);
        DOLPHIN_DASH_COOLDOWN = (int) config.getDouble("dolphin.dash.cooldown", 100);
        DOLPHIN_DASH_BOOST = (float) config.getDouble("dolphin.dash.boost", 1.5D);
        DOLPHIN_DASH_DURATION = (int) config.getDouble("dolphin.dash.duration", 20);

        DRAGON_ENABLED = config.getBoolean("dragon.enabled", false);
        DRAGON_SPEED = (float) config.getDouble("dragon.speed", 1.0D);

        DROWNED_ENABLED = config.getBoolean("drowned.enabled", false);
        DROWNED_SPEED = (float) config.getDouble("drowned.speed", 1.0D);
        DROWNED_JUMP_POWER = (float) config.getDouble("drowned.jump-power", 0.5D);

        ENDERMAN_ENABLED = config.getBoolean("enderman.enabled", false);
        ENDERMAN_SPEED = (float) config.getDouble("enderman.speed", 1.0D);
        ENDERMAN_JUMP_POWER = (float) config.getDouble("enderman.jump-power", 0.5D);
        ENDERMAN_DAMAGE_WHEN_WET = config.getBoolean("enderman.when-wet.damage", true);
        ENDERMAN_EJECT_WHEN_WET = config.getBoolean("enderman.when-wet.eject", true);
        ENDERMAN_TELEPORT_WHEN_DAMAGED = config.getBoolean("enderman.teleport-on-damage", true);

        ENDERMITE_ENABLED = config.getBoolean("endermite.enabled", false);
        ENDERMITE_SPEED = (float) config.getDouble("endermite.speed", 1.0D);
        ENDERMITE_JUMP_POWER = (float) config.getDouble("endermite.jump-power", 0.5D);

        EVOKER_ENABLED = config.getBoolean("evoker.enabled", false);
        EVOKER_SPEED = (float) config.getDouble("evoker.speed", 1.0D);
        EVOKER_JUMP_POWER = (float) config.getDouble("evoker.jump-power", 0.5D);

        GHAST_ENABLED = config.getBoolean("ghast.enabled", false);
        GHAST_SPEED = (float) config.getDouble("ghast.speed", 1.0D);
        GHAST_SHOOT_COOLDOWN = (int) config.getDouble("ghast.shoot.cooldown", 20);
        GHAST_SHOOT_SPEED = (float) config.getDouble("ghast.shoot.speed", 1.0D);
        GHAST_SHOOT_DAMAGE = (float) config.getDouble("ghast.shoot.damage", 5.0D);
        GHAST_SHOOT_GRIEF = config.getBoolean("ghast.shoot.grief", true);

        GIANT_ENABLED = config.getBoolean("giant.enabled", false);
        GIANT_SPEED = (float) config.getDouble("giant.speed", 1.0D);
        GIANT_JUMP_POWER = (float) config.getDouble("giant.jump-power", 0.5D);

        HUSK_ENABLED = config.getBoolean("husk.enabled", false);
        HUSK_SPEED = (float) config.getDouble("husk.speed", 1.0D);
        HUSK_JUMP_POWER = (float) config.getDouble("husk.jump-power", 0.5D);

        ILLUSIONER_ENABLED = config.getBoolean("illusioner.enabled", false);
        ILLUSIONER_SPEED = (float) config.getDouble("illusioner.speed", 1.0D);
        ILLUSIONER_JUMP_POWER = (float) config.getDouble("illusioner.jump-power", 0.5D);

        IRON_GOLEM_ENABLED = config.getBoolean("iron-golem.enabled", false);
        IRON_GOLEM_SPEED = (float) config.getDouble("iron-golem.speed", 1.0D);
        IRON_GOLEM_JUMP_POWER = (float) config.getDouble("iron-golem.jump-power", 0.5D);

        LLAMA_ENABLED = config.getBoolean("llama.enabled", false);
        LLAMA_SPEED = (float) config.getDouble("llama.speed", 1.0D);
        LLAMA_JUMP_POWER = (float) config.getDouble("llama.jump-power", 0.5D);
        LLAMA_CARAVAN = config.getBoolean("llama.caravan", true);

        MOOSHROOM_ENABLED = config.getBoolean("mooshroom.enabled", false);
        MOOSHROOM_SPEED = (float) config.getDouble("mooshroom.speed", 1.0D);
        MOOSHROOM_JUMP_POWER = (float) config.getDouble("mooshroom.jump-power", 0.5D);

        OCELOT_ENABLED = config.getBoolean("ocelot.enabled", false);
        OCELOT_SPEED = (float) config.getDouble("ocelot.speed", 1.0D);
        OCELOT_JUMP_POWER = (float) config.getDouble("ocelot.jump-power", 0.5D);

        PARROT_ENABLED = config.getBoolean("parrot.enabled", false);
        PARROT_SPEED = (float) config.getDouble("parrot.speed", 1.0D);
        PARROT_VERTICAL = (float) config.getDouble("parrot.vertical", 1.0D);
        PARROT_GRAVITY = (float) config.getDouble("parrot.gravity", 0.04D);

        PIG_ENABLED = config.getBoolean("pig.enabled", false);
        PIG_SPEED = (float) config.getDouble("pig.speed", 1.0D);
        PIG_JUMP_POWER = (float) config.getDouble("pig.jump-power", 0.5D);
        PIG_SADDLE_BACK = config.getBoolean("pig.saddle-back", true);

        PHANTOM_ENABLED = config.getBoolean("phantom.enabled", false);
        PHANTOM_SPEED = (float) config.getDouble("phantom.speed", 1.0D);
        PHANTOM_GRAVITY = config.getDouble("phantom.gravity", 0.05D);
        PHANTOM_FALL_DAMAGE = config.getBoolean("phantom.fall-damage", true);
        PHANTOM_BURN_IN_SUNLIGHT = config.getBoolean("phantom.burn-in-sun", true);
        PHANTOM_SHOOT_DAMAGE = (float) config.getDouble("phantom.shoot-damage", 1.0D);

        POLAR_BEAR_ENABLED = config.getBoolean("polar-bear.enabled", false);
        POLAR_BEAR_SPEED = (float) config.getDouble("polar-bear.speed", 1.0D);
        POLAR_BEAR_JUMP_POWER = (float) config.getDouble("polar-bear.jump-power", 0.5D);
        POLAR_BEAR_STEP_HEIGHT = (float) config.getDouble("polar-bear.step-height", 1.0D);
        POLAR_BEAR_STAND = config.getBoolean("polar-bear.stand", true);

        RABBIT_ENABLED = config.getBoolean("rabbit.enabled", false);
        RABBIT_SPEED = (float) config.getDouble("rabbit.speed", 1.0D);
        RABBIT_JUMP_POWER = (float) config.getDouble("rabbit.jump-power", 0.5D);

        SHEEP_ENABLED = config.getBoolean("sheep.enabled", false);
        SHEEP_SPEED = (float) config.getDouble("sheep.speed", 1.0D);
        SHEEP_JUMP_POWER = (float) config.getDouble("sheep.jump-power", 0.5D);

        SILVERFISH_ENABLED = config.getBoolean("silverfish.enabled", false);
        SILVERFISH_SPEED = (float) config.getDouble("silverfish.speed", 1.0D);
        SILVERFISH_JUMP_POWER = (float) config.getDouble("silverfish.jump-power", 0.5D);

        SKELETON_ENABLED = config.getBoolean("skeleton.enabled", false);
        SKELETON_SPEED = (float) config.getDouble("skeleton.speed", 1.0D);
        SKELETON_JUMP_POWER = (float) config.getDouble("skeleton.jump-power", 0.5D);

        SKELETON_HORSE_ENABLED = config.getBoolean("skeleton-horse.enabled", false);

        SNOWMAN_ENABLED = config.getBoolean("snowman.enabled", false);
        SNOWMAN_SPEED = (float) config.getDouble("snowman.speed", 1.0D);
        SNOWMAN_JUMP_POWER = (float) config.getDouble("snowman.jump-power", 0.5D);
        SNOWMAN_DAMAGE_WHEN_HOT = config.getBoolean("snowman.damage.when-hot", true);
        SNOWMAN_DAMAGE_WHEN_WET = config.getBoolean("snowman.damage.when-wet", true);
        SNOWMAN_LEAVE_SNOW_TRAIL = config.getBoolean("snowman.snow-trail", true);

        SPIDER_ENABLED = config.getBoolean("spider.enabled", false);
        SPIDER_SPEED = (float) config.getDouble("spider.speed", 1.0D);
        SPIDER_JUMP_POWER = (float) config.getDouble("spider.jump-power", 0.5D);
        SPIDER_CLIMB_WALLS = config.getBoolean("spider.climb-walls", true);
        SPIDER_CLIMB_SPEED = (float) config.getDouble("spider.climb-speed", 1.0D);

        STRAY_ENABLED = config.getBoolean("stray.enabled", false);
        STRAY_SPEED = (float) config.getDouble("stray.speed", 1.0D);
        STRAY_JUMP_POWER = (float) config.getDouble("stray.jump-power", 0.5D);

        TURTLE_ENABLED = config.getBoolean("turtle.enabled", false);
        TURTLE_SPEED_LAND = (float) config.getDouble("turtle.speed.on-land", 1.0D);
        TURTLE_SPEED_WATER = (float) config.getDouble("turtle.speed.in-water", 1.0D);

        VILLAGER_ENABLED = config.getBoolean("villager.enabled", false);
        VILLAGER_SPEED = (float) config.getDouble("villager.speed", 1.0D);
        VILLAGER_JUMP_POWER = (float) config.getDouble("villager.jump-power", 0.5D);

        VINDICATOR_ENABLED = config.getBoolean("vindicator.enabled", false);
        VINDICATOR_SPEED = (float) config.getDouble("vindicator.speed", 1.0D);
        VINDICATOR_JUMP_POWER = (float) config.getDouble("vindicator.jump-power", 0.5D);

        WITCH_ENABLED = config.getBoolean("witch.enabled", false);
        WITCH_SPEED = (float) config.getDouble("witch.speed", 1.0D);
        WITCH_JUMP_POWER = (float) config.getDouble("witch.jump-power", 0.5D);

        WITHER_SKELETON_ENABLED = config.getBoolean("wither-skeleton.enabled", false);
        WITHER_SKELETON_SPEED = (float) config.getDouble("wither-skeleton.speed", 1.0D);
        WITHER_SKELETON_JUMP_POWER = (float) config.getDouble("wither-skeleton.jump-power", 0.5D);

        WOLF_ENABLED = config.getBoolean("wolf.enabled", false);
        WOLF_SPEED = (float) config.getDouble("wolf.speed", 1.0D);
        WOLF_JUMP_POWER = (float) config.getDouble("wolf.jump-power", 0.5D);

        ZOMBIE_ENABLED = config.getBoolean("zombie.enabled", false);
        ZOMBIE_SPEED = (float) config.getDouble("zombie.speed", 1.0D);
        ZOMBIE_JUMP_POWER = (float) config.getDouble("zombie.jump-power", 0.5D);

        ZOMBIE_HORSE_ENABLED = config.getBoolean("zombie-horse.enabled", false);

        ZOMBIE_PIGMAN_ENABLED = config.getBoolean("zombie-pigman.enabled", false);
        ZOMBIE_PIGMAN_SPEED = (float) config.getDouble("zombie-pigman.speed", 1.0D);
        ZOMBIE_PIGMAN_JUMP_POWER = (float) config.getDouble("zombie-pigman.jump-power", 0.5D);

        ZOMBIE_VILLAGER_ENABLED = config.getBoolean("zombie-villager.enabled", false);
        ZOMBIE_VILLAGER_SPEED = (float) config.getDouble("zombie-villager.speed", 1.0D);
        ZOMBIE_VILLAGER_JUMP_POWER = (float) config.getDouble("zombie-villager.jump-power", 0.5D);
    }
}
