package net.pl3x.bukkit.ridables.configuration;

import net.pl3x.bukkit.ridables.Ridables;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
    public static String LANGUAGE_FILE = "lang-en.yml";
    public static boolean DEBUG_MODE = false;

    public static boolean CHECK_FOR_UPDATES = true;

    public static boolean USE_NEW_WASD_YAW_CALCULATIONS = true;
    public static boolean CANCEL_COMMANDS_WHILE_RIDING = false;
    public static boolean REQUIRE_SADDLE = false;
    public static boolean CONSUME_SADDLE = false;
    public static boolean ALLOW_RIDE_BABIES = false;

    public static int FLYING_MAX_Y = 256;

    public static boolean BAT_ENABLED = false;
    public static float BAT_SPEED = 1.0F;
    public static float BAT_VERTICAL = 1.0F;
    public static float BAT_GRAVITY = 0.04F;

    public static boolean BLAZE_ENABLED = false;
    public static float BLAZE_SPEED = 1.0F;
    public static float BLAZE_VERTICAL = 0.5F;
    public static float BLAZE_GRAVITY = 0.04F;
    public static int BLAZE_SHOOT_COOLDOWN = 20;
    public static float BLAZE_SHOOT_SPEED = 1.0F;
    public static float BLAZE_SHOOT_DAMAGE = 5.0F;
    public static boolean BLAZE_SHOOT_GRIEF = true;

    public static boolean CAVE_SPIDER_ENABLED = false;
    public static float CAVE_SPIDER_SPEED = 1.0F;
    public static float CAVE_SPIDER_JUMP_POWER = 0.5F;
    public static boolean CAVE_SPIDER_CLIMB_WALLS = true;
    public static float CAVE_SPIDER_CLIMB_SPEED = 1.0F;

    public static boolean CHICKEN_ENABLED = false;
    public static float CHICKEN_SPEED = 1.0F;
    public static float CHICKEN_JUMP_POWER = 0.5F;
    public static boolean CHICKEN_DROP_EGGS_WHILE_RIDING = false;

    public static boolean COD_ENABLED = false;
    public static float COD_SPEED = 1.0F;

    public static boolean COW_ENABLED = false;
    public static float COW_SPEED = 1.0F;
    public static float COW_JUMP_POWER = 0.5F;

    public static boolean CREEPER_ENABLED = false;
    public static float CREEPER_SPEED = 1.0F;
    public static float CREEPER_JUMP_POWER = 0.5F;
    public static float CREEPER_EXPLOSION_DAMAGE = 5.0F;
    public static int CREEPER_EXPLOSION_RADIUS = 3;
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
    public static int DROWNED_SHOOT_COOLDOWN = 20;
    public static float DROWNED_SHOOT_SPEED = 1.0F;
    public static float DROWNED_SHOOT_DAMAGE = 8.0F;
    public static boolean DROWNED_SHOOT_CHANNELING = true;
    public static boolean DROWNED_SHOOT_REQUIRE_TRIDENT = false;

    public static boolean ELDER_GUARDIAN_ENABLED = false;
    public static float ELDER_GUARDIAN_SPEED = 1.0F;

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
    public static int EVOKER_SPELL_COOLDOWN = 40;
    public static float EVOKER_SPELL_DAMAGE = 6.0F;

    public static boolean GHAST_ENABLED = false;
    public static float GHAST_SPEED = 1.0F;
    public static int GHAST_SHOOT_COOLDOWN = 20;
    public static float GHAST_SHOOT_SPEED = 1.0F;
    public static float GHAST_SHOOT_DAMAGE = 5.0F;
    public static boolean GHAST_SHOOT_GRIEF = true;

    public static boolean GIANT_ENABLED = false;
    public static float GIANT_SPEED = 1.0F;
    public static float GIANT_JUMP_POWER = 0.5F;
    public static boolean GIANT_AI_ENABLED = true;
    public static float GIANT_MAX_HEALTH = 100F;
    public static float GIANT_AI_SPEED = 0.5F;
    public static float GIANT_FOLLOW_RANGE = 32.0F;
    public static boolean GIANT_HOSTILE = true;
    public static float GIANT_ATTACK_DAMAGE = 5.0F;
    public static boolean GIANT_SPAWN_NATURALLY = false;
    public static int GIANT_SPAWN_LIGHT_LEVEL = 8;
    public static int GIANT_SPAWN_WEIGHT = 10;
    public static int GIANT_SPAWN_MIN_GROUP = 1;
    public static int GIANT_SPAWN_MAX_GROUP = 1;
    public static List<Integer> GIANT_SPAWN_BIOMES = new ArrayList<>(Arrays.asList(
            1, 2, 3, 4, 5, 6, 17, 18, 19, 20, 27, 28, 32, 33, 34, 35, 36, 37, 38, 39,
            129, 130, 131, 132, 133, 134, 156, 157, 160, 161, 162, 163, 164, 165, 166, 167));

    public static boolean GUARDIAN_ENABLED = false;
    public static float GUARDIAN_SPEED = 1.0F;

    public static boolean HUSK_ENABLED = false;
    public static float HUSK_SPEED = 1.0F;
    public static float HUSK_JUMP_POWER = 0.5F;

    public static boolean ILLUSIONER_ENABLED = false;
    public static float ILLUSIONER_SPEED = 1.0F;
    public static float ILLUSIONER_JUMP_POWER = 0.5F;
    public static boolean ILLUSIONER_SPAWN_NATURALLY = false;
    public static int ILLUSIONER_SPAWN_LIGHT_LEVEL = 8;
    public static int ILLUSIONER_SPAWN_WEIGHT = 5;
    public static int ILLUSIONER_SPAWN_MIN_GROUP = 1;
    public static int ILLUSIONER_SPAWN_MAX_GROUP = 1;
    public static List<Integer> ILLUSIONER_SPAWN_BIOMES = new ArrayList<>(Arrays.asList(
            1, 2, 3, 4, 5, 6, 17, 18, 19, 20, 27, 28, 32, 33, 34, 35, 36, 37, 38, 39,
            129, 130, 131, 132, 133, 134, 156, 157, 160, 161, 162, 163, 164, 165, 166, 167));

    public static boolean IRON_GOLEM_ENABLED = false;
    public static float IRON_GOLEM_SPEED = 1.0F;
    public static float IRON_GOLEM_JUMP_POWER = 0.5F;

    public static boolean LLAMA_ENABLED = false;
    public static float LLAMA_SPEED = 1.0F;
    public static float LLAMA_JUMP_POWER = 0.5F;
    public static boolean LLAMA_CARAVAN = true;

    public static boolean MAGMA_CUBE_ENABLED = false;
    public static float MAGMA_CUBE_SPEED = 1.0F;

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

    public static boolean PUFFERFISH_ENABLED = false;
    public static float PUFFERFISH_SPEED = 1.0F;

    public static boolean RABBIT_ENABLED = false;
    public static float RABBIT_SPEED = 1.0F;
    public static float RABBIT_JUMP_POWER = 0.5F;

    public static boolean SALMON_ENABLED = false;
    public static float SALMON_SPEED = 1.0F;

    public static boolean SHEEP_ENABLED = false;
    public static float SHEEP_SPEED = 1.0F;
    public static float SHEEP_JUMP_POWER = 0.5F;

    public static boolean SHULKER_ENABLED = false;
    public static int SHULKER_PEEK_HEIGHT = 33;
    public static int SHULKER_SHOOT_COOLDOWN = 10;
    public static float SHULKER_SHOOT_SPEED = 1.0F;
    public static float SHULKER_SHOOT_DAMAGE = 4.0F;

    public static boolean SILVERFISH_ENABLED = false;
    public static float SILVERFISH_SPEED = 1.0F;
    public static float SILVERFISH_JUMP_POWER = 0.5F;

    public static boolean SKELETON_ENABLED = false;
    public static float SKELETON_SPEED = 1.0F;
    public static float SKELETON_JUMP_POWER = 0.5F;

    public static boolean SKELETON_HORSE_ENABLED = false;

    public static boolean SLIME_ENABLED = false;
    public static float SLIME_SPEED = 1.0F;

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

    public static boolean SQUID_ENABLED = false;
    public static float SQUID_SPEED = 1.0F;

    public static boolean STRAY_ENABLED = false;
    public static float STRAY_SPEED = 1.0F;
    public static float STRAY_JUMP_POWER = 0.5F;

    public static boolean TROPICAL_FISH_ENABLED = false;
    public static float TROPICAL_FISH_SPEED = 1.0F;

    public static boolean TURTLE_ENABLED = false;
    public static float TURTLE_SPEED_LAND = 1.0F;
    public static float TURTLE_SPEED_WATER = 1.0F;

    public static boolean VEX_ENABLED = false;
    public static float VEX_SPEED = 1.0F;
    public static boolean VEX_NOCLIP = false;

    public static boolean VILLAGER_ENABLED = false;
    public static float VILLAGER_SPEED = 1.0F;
    public static float VILLAGER_JUMP_POWER = 0.5F;

    public static boolean VINDICATOR_ENABLED = false;
    public static float VINDICATOR_SPEED = 1.0F;
    public static float VINDICATOR_JUMP_POWER = 0.5F;

    public static boolean WITCH_ENABLED = false;
    public static float WITCH_SPEED = 1.0F;
    public static float WITCH_JUMP_POWER = 0.5F;

    public static boolean WITHER_ENABLED = false;
    public static float WITHER_SPEED = 1.0F;
    public static int WITHER_SHOOT_COOLDOWN = 20;
    public static float WITHER_SHOOT_SPEED = 1.0F;
    public static float WITHER_SHOOT_DAMAGE = 1.0F;
    public static float WITHER_SHOOT_HEAL_AMOUNT = 1.0F;
    public static int WITHER_SHOOT_EFFECT_DURATION = 10;
    public static boolean WITHER_SHOOT_GRIEF = true;

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
        CANCEL_COMMANDS_WHILE_RIDING = config.getBoolean("cancel-commands-while-riding", false);
        REQUIRE_SADDLE = config.getBoolean("saddle-to-mount", false);
        CONSUME_SADDLE = config.getBoolean("consume-saddle", false);
        ALLOW_RIDE_BABIES = config.getBoolean("allow-ride-babies", false);

        FLYING_MAX_Y = (int) config.getDouble("flying-max-y", 256D);

        BAT_ENABLED = config.getBoolean("bat.enabled", false);
        BAT_SPEED = (float) config.getDouble("bat.speed", 1.0D);
        BAT_VERTICAL = (float) config.getDouble("bat.vertical", 1.0D);
        BAT_GRAVITY = (float) config.getDouble("bat.gravity", 0.04D);

        BLAZE_ENABLED = config.getBoolean("blaze.enabled", false);
        BLAZE_SPEED = (float) config.getDouble("blaze.speed", 1.0D);
        BLAZE_VERTICAL = (float) config.getDouble("blaze.vertical", 0.5D);
        BLAZE_GRAVITY = (float) config.getDouble("blaze.gravity", 0.04D);
        BLAZE_SHOOT_COOLDOWN = (int) config.getDouble("blaze.shoot.cooldown", 20);
        BLAZE_SHOOT_SPEED = (float) config.getDouble("blaze.shoot.speed", 1.0D);
        BLAZE_SHOOT_DAMAGE = (float) config.getDouble("blaze.shoot.damage", 5.0D);
        BLAZE_SHOOT_GRIEF = config.getBoolean("blaze.shoot.grief", true);

        CAVE_SPIDER_ENABLED = config.getBoolean("cave_spider.enabled", false);
        CAVE_SPIDER_SPEED = (float) config.getDouble("cave_spider.speed", 1.0D);
        CAVE_SPIDER_JUMP_POWER = (float) config.getDouble("cave_spider.jump-power", 0.5D);
        CAVE_SPIDER_CLIMB_WALLS = config.getBoolean("cave_spider.climb-walls", true);
        CAVE_SPIDER_CLIMB_SPEED = (float) config.getDouble("cave_spider.climb-speed", 1.0D);

        CHICKEN_ENABLED = config.getBoolean("chicken.enabled", false);
        CHICKEN_SPEED = (float) config.getDouble("chicken.speed", 1.0D);
        CHICKEN_JUMP_POWER = (float) config.getDouble("chicken.jump-power", 0.5D);
        CHICKEN_DROP_EGGS_WHILE_RIDING = config.getBoolean("chicken.drop-eggs", false);

        COD_ENABLED = config.getBoolean("cod.enabled", false);
        COD_SPEED = (float) config.getDouble("cod.speed", 1.0D);

        COW_ENABLED = config.getBoolean("cow.enabled", false);
        COW_SPEED = (float) config.getDouble("cow.speed", 1.0D);
        COW_JUMP_POWER = (float) config.getDouble("cow.jump-power", 0.5D);

        CREEPER_ENABLED = config.getBoolean("creeper.enabled", false);
        CREEPER_SPEED = (float) config.getDouble("creeper.speed", 1.0D);
        CREEPER_JUMP_POWER = (float) config.getDouble("creeper.jump-power", 0.5D);
        CREEPER_EXPLOSION_DAMAGE = (float) config.getDouble("creeper.explosion.damage", 5.0D);
        CREEPER_EXPLOSION_RADIUS = (int) config.getDouble("creeper.explosion.radius", 3.0D);
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
        DROWNED_SHOOT_COOLDOWN = (int) config.getDouble("drowned.shoot.cooldown", 20);
        DROWNED_SHOOT_SPEED = (float) config.getDouble("drowned.shoot.speed", 1.0D);
        DROWNED_SHOOT_DAMAGE = (float) config.getDouble("drowned.shoot.damage", 8.0D);
        DROWNED_SHOOT_CHANNELING = config.getBoolean("drowned.shoot.channeling", true);
        DROWNED_SHOOT_REQUIRE_TRIDENT = config.getBoolean("drowned.shoot.require-trident", false);

        ELDER_GUARDIAN_ENABLED = config.getBoolean("elder_guardian.enabled", false);
        ELDER_GUARDIAN_SPEED = (float) config.getDouble("elder_guardian.speed", 1.0D);

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
        EVOKER_SPELL_COOLDOWN = (int) config.getDouble("evoker.spell.cooldown", 40);
        EVOKER_SPELL_DAMAGE = (float) config.getDouble("evoker.spell.damage", 6.0D);

        GHAST_ENABLED = config.getBoolean("ghast.enabled", false);
        GHAST_SPEED = (float) config.getDouble("ghast.speed", 1.0D);
        GHAST_SHOOT_COOLDOWN = (int) config.getDouble("ghast.shoot.cooldown", 20);
        GHAST_SHOOT_SPEED = (float) config.getDouble("ghast.shoot.speed", 1.0D);
        GHAST_SHOOT_DAMAGE = (float) config.getDouble("ghast.shoot.damage", 5.0D);
        GHAST_SHOOT_GRIEF = config.getBoolean("ghast.shoot.grief", true);

        GIANT_ENABLED = config.getBoolean("giant.enabled", false);
        GIANT_SPEED = (float) config.getDouble("giant.speed", 1.0D);
        GIANT_JUMP_POWER = (float) config.getDouble("giant.jump-power", 0.5D);
        GIANT_AI_ENABLED = config.getBoolean("giant.ai.enabled", true);
        GIANT_MAX_HEALTH = (float) config.getDouble("giant.ai.health", 100D);
        GIANT_AI_SPEED = (float) config.getDouble("giant.ai.speed", 0.3D);
        GIANT_FOLLOW_RANGE = (float) config.getDouble("giant.ai.follow", 32.0D);
        GIANT_HOSTILE = config.getBoolean("giant.ai.hostile", true);
        GIANT_ATTACK_DAMAGE = (float) config.getDouble("giant.ai.attack", 5.0D);
        GIANT_SPAWN_NATURALLY = config.getBoolean("giant.spawn.natural", false);
        GIANT_SPAWN_LIGHT_LEVEL = (int) config.getDouble("giant.spawn.max-light", 8);
        GIANT_SPAWN_WEIGHT = (int) config.getDouble("giant.spawn.weight", 10);
        GIANT_SPAWN_MIN_GROUP = (int) config.getDouble("giant.spawn.min-group", 1);
        GIANT_SPAWN_MAX_GROUP = (int) config.getDouble("giant.spawn.max-group", 1);
        GIANT_SPAWN_BIOMES = config.getIntegerList("giant.spawn.biomes");
        if (GIANT_SPAWN_BIOMES == null) {
            GIANT_SPAWN_BIOMES = new ArrayList<>(Arrays.asList(
                    1, 2, 3, 4, 5, 6, 17, 18, 19, 20, 27, 28, 32, 33, 34, 35, 36, 37, 38, 39,
                    129, 130, 131, 132, 133, 134, 156, 157, 160, 161, 162, 163, 164, 165, 166, 167));
        }

        GUARDIAN_ENABLED = config.getBoolean("guardian.enabled", false);
        GUARDIAN_SPEED = (float) config.getDouble("guardian.speed", 1.0D);

        HUSK_ENABLED = config.getBoolean("husk.enabled", false);
        HUSK_SPEED = (float) config.getDouble("husk.speed", 1.0D);
        HUSK_JUMP_POWER = (float) config.getDouble("husk.jump-power", 0.5D);

        ILLUSIONER_ENABLED = config.getBoolean("illusioner.enabled", false);
        ILLUSIONER_SPEED = (float) config.getDouble("illusioner.speed", 1.0D);
        ILLUSIONER_JUMP_POWER = (float) config.getDouble("illusioner.jump-power", 0.5D);
        ILLUSIONER_SPAWN_NATURALLY = config.getBoolean("illusioner.spawn.natural", false);
        ILLUSIONER_SPAWN_LIGHT_LEVEL = (int) config.getDouble("illusioner.spawn.max-light", 8);
        ILLUSIONER_SPAWN_WEIGHT = (int) config.getDouble("illusioner.spawn.weight", 5);
        ILLUSIONER_SPAWN_MIN_GROUP = (int) config.getDouble("illusioner.spawn.min-group", 1);
        ILLUSIONER_SPAWN_MAX_GROUP = (int) config.getDouble("illusioner.spawn.max-group", 1);
        ILLUSIONER_SPAWN_BIOMES = config.getIntegerList("illusioner.spawn.biomes");
        if (ILLUSIONER_SPAWN_BIOMES == null) {
            ILLUSIONER_SPAWN_BIOMES = new ArrayList<>(Arrays.asList(
                    1, 2, 3, 4, 5, 6, 17, 18, 19, 20, 27, 28, 32, 33, 34, 35, 36, 37, 38, 39,
                    129, 130, 131, 132, 133, 134, 156, 157, 160, 161, 162, 163, 164, 165, 166, 167));
        }

        IRON_GOLEM_ENABLED = config.getBoolean("iron_golem.enabled", false);
        IRON_GOLEM_SPEED = (float) config.getDouble("iron_golem.speed", 1.0D);
        IRON_GOLEM_JUMP_POWER = (float) config.getDouble("iron_golem.jump-power", 0.5D);

        LLAMA_ENABLED = config.getBoolean("llama.enabled", false);
        LLAMA_SPEED = (float) config.getDouble("llama.speed", 1.0D);
        LLAMA_JUMP_POWER = (float) config.getDouble("llama.jump-power", 0.5D);
        LLAMA_CARAVAN = config.getBoolean("llama.caravan", true);

        MAGMA_CUBE_ENABLED = config.getBoolean("magma_cube.enabled", false);
        MAGMA_CUBE_SPEED = (float) config.getDouble("magma_cube.speed", 1.0D);

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

        POLAR_BEAR_ENABLED = config.getBoolean("polar_bear.enabled", false);
        POLAR_BEAR_SPEED = (float) config.getDouble("polar_bear.speed", 1.0D);
        POLAR_BEAR_JUMP_POWER = (float) config.getDouble("polar_bear.jump-power", 0.5D);
        POLAR_BEAR_STEP_HEIGHT = (float) config.getDouble("polar_bear.step-height", 1.0D);
        POLAR_BEAR_STAND = config.getBoolean("polar_bear.stand", true);

        PUFFERFISH_ENABLED = config.getBoolean("pufferfish.enabled", false);
        PUFFERFISH_SPEED = (float) config.getDouble("pufferfish.speed", 1.0D);

        RABBIT_ENABLED = config.getBoolean("rabbit.enabled", false);
        RABBIT_SPEED = (float) config.getDouble("rabbit.speed", 1.0D);
        RABBIT_JUMP_POWER = (float) config.getDouble("rabbit.jump-power", 0.5D);

        SALMON_ENABLED = config.getBoolean("salmon.enabled", false);
        SALMON_SPEED = (float) config.getDouble("salmon.speed", 1.0D);

        SHEEP_ENABLED = config.getBoolean("sheep.enabled", false);
        SHEEP_SPEED = (float) config.getDouble("sheep.speed", 1.0D);
        SHEEP_JUMP_POWER = (float) config.getDouble("sheep.jump-power", 0.5D);

        SHULKER_ENABLED = config.getBoolean("shulker.enabled", false);
        SHULKER_PEEK_HEIGHT = (int) config.getDouble("shulker.peek-height", 33);
        SHULKER_SHOOT_COOLDOWN = (int) config.getDouble("shulker.shoot.cooldown", 10);
        SHULKER_SHOOT_SPEED = (float) config.getDouble("shulker.shoot.speed", 1.0D);
        SHULKER_SHOOT_DAMAGE = (float) config.getDouble("shulker.shoot.damage", 4.0D);

        SILVERFISH_ENABLED = config.getBoolean("silverfish.enabled", false);
        SILVERFISH_SPEED = (float) config.getDouble("silverfish.speed", 1.0D);
        SILVERFISH_JUMP_POWER = (float) config.getDouble("silverfish.jump-power", 0.5D);

        SKELETON_ENABLED = config.getBoolean("skeleton.enabled", false);
        SKELETON_SPEED = (float) config.getDouble("skeleton.speed", 1.0D);
        SKELETON_JUMP_POWER = (float) config.getDouble("skeleton.jump-power", 0.5D);

        SKELETON_HORSE_ENABLED = config.getBoolean("skeleton_horse.enabled", false);

        SLIME_ENABLED = config.getBoolean("slime.enabled", false);
        SLIME_SPEED = (float) config.getDouble("slime.speed", 1.0D);

        SNOWMAN_ENABLED = config.getBoolean("snow_golem.enabled", false);
        SNOWMAN_SPEED = (float) config.getDouble("snow_golem.speed", 1.0D);
        SNOWMAN_JUMP_POWER = (float) config.getDouble("snow_golem.jump-power", 0.5D);
        SNOWMAN_DAMAGE_WHEN_HOT = config.getBoolean("snow_golem.damage.when-hot", true);
        SNOWMAN_DAMAGE_WHEN_WET = config.getBoolean("snow_golem.damage.when-wet", true);
        SNOWMAN_LEAVE_SNOW_TRAIL = config.getBoolean("snow_golem.snow-trail", true);

        SPIDER_ENABLED = config.getBoolean("spider.enabled", false);
        SPIDER_SPEED = (float) config.getDouble("spider.speed", 1.0D);
        SPIDER_JUMP_POWER = (float) config.getDouble("spider.jump-power", 0.5D);
        SPIDER_CLIMB_WALLS = config.getBoolean("spider.climb-walls", true);
        SPIDER_CLIMB_SPEED = (float) config.getDouble("spider.climb-speed", 1.0D);

        SQUID_ENABLED = config.getBoolean("squid.enabled", false);
        SQUID_SPEED = (float) config.getDouble("squid.speed", 1.0D);

        STRAY_ENABLED = config.getBoolean("stray.enabled", false);
        STRAY_SPEED = (float) config.getDouble("stray.speed", 1.0D);
        STRAY_JUMP_POWER = (float) config.getDouble("stray.jump-power", 0.5D);

        TROPICAL_FISH_ENABLED = config.getBoolean("tropical_fish.enabled", false);
        TROPICAL_FISH_SPEED = (float) config.getDouble("tropical_fish.speed", 1.0D);

        TURTLE_ENABLED = config.getBoolean("turtle.enabled", false);
        TURTLE_SPEED_LAND = (float) config.getDouble("turtle.speed.on-land", 1.0D);
        TURTLE_SPEED_WATER = (float) config.getDouble("turtle.speed.in-water", 1.0D);

        VEX_ENABLED = config.getBoolean("vex.enabled", false);
        VEX_SPEED = (float) config.getDouble("vex.speed", 1.0D);
        VEX_NOCLIP = config.getBoolean("vex.noclip", false);

        VILLAGER_ENABLED = config.getBoolean("villager.enabled", false);
        VILLAGER_SPEED = (float) config.getDouble("villager.speed", 1.0D);
        VILLAGER_JUMP_POWER = (float) config.getDouble("villager.jump-power", 0.5D);

        VINDICATOR_ENABLED = config.getBoolean("vindicator.enabled", false);
        VINDICATOR_SPEED = (float) config.getDouble("vindicator.speed", 1.0D);
        VINDICATOR_JUMP_POWER = (float) config.getDouble("vindicator.jump-power", 0.5D);

        WITCH_ENABLED = config.getBoolean("witch.enabled", false);
        WITCH_SPEED = (float) config.getDouble("witch.speed", 1.0D);
        WITCH_JUMP_POWER = (float) config.getDouble("witch.jump-power", 0.5D);

        WITHER_ENABLED = config.getBoolean("wither.enabled", false);
        WITHER_SPEED = (float) config.getDouble("wither.speed", 1.0D);
        WITHER_SHOOT_COOLDOWN = (int) config.getDouble("wither.shoot.cooldown", 20);
        WITHER_SHOOT_SPEED = (float) config.getDouble("wither.shoot.speed", 1.0D);
        WITHER_SHOOT_DAMAGE = (float) config.getDouble("wither.shoot.damage", 1.0D);
        WITHER_SHOOT_HEAL_AMOUNT = (float) config.getDouble("wither.shoot.heal", 1.0D);
        WITHER_SHOOT_EFFECT_DURATION = (int) config.getDouble("wither.shoot.effect-duration", 10);
        WITHER_SHOOT_GRIEF = config.getBoolean("wither.shoot.grief", true);

        WITHER_SKELETON_ENABLED = config.getBoolean("wither_skeleton.enabled", false);
        WITHER_SKELETON_SPEED = (float) config.getDouble("wither_skeleton.speed", 1.0D);
        WITHER_SKELETON_JUMP_POWER = (float) config.getDouble("wither_skeleton.jump-power", 0.5D);

        WOLF_ENABLED = config.getBoolean("wolf.enabled", false);
        WOLF_SPEED = (float) config.getDouble("wolf.speed", 1.0D);
        WOLF_JUMP_POWER = (float) config.getDouble("wolf.jump-power", 0.5D);

        ZOMBIE_ENABLED = config.getBoolean("zombie.enabled", false);
        ZOMBIE_SPEED = (float) config.getDouble("zombie.speed", 1.0D);
        ZOMBIE_JUMP_POWER = (float) config.getDouble("zombie.jump-power", 0.5D);

        ZOMBIE_HORSE_ENABLED = config.getBoolean("zombie_horse.enabled", false);

        ZOMBIE_PIGMAN_ENABLED = config.getBoolean("zombie_pigman.enabled", false);
        ZOMBIE_PIGMAN_SPEED = (float) config.getDouble("zombie_pigman.speed", 1.0D);
        ZOMBIE_PIGMAN_JUMP_POWER = (float) config.getDouble("zombie_pigman.jump-power", 0.5D);

        ZOMBIE_VILLAGER_ENABLED = config.getBoolean("zombie_villager.enabled", false);
        ZOMBIE_VILLAGER_SPEED = (float) config.getDouble("zombie_villager.speed", 1.0D);
        ZOMBIE_VILLAGER_JUMP_POWER = (float) config.getDouble("zombie_villager.jump-power", 0.5D);
    }
}
