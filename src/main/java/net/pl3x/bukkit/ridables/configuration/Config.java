package net.pl3x.bukkit.ridables.configuration;

import net.pl3x.bukkit.ridables.Ridables;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class Config {
    public static String LANGUAGE_FILE = "lang-en.yml";

    public static boolean LOGGER_INFO = true;
    public static boolean LOGGER_WARN = true;
    public static boolean LOGGER_ERROR = true;
    public static boolean LOGGER_DEBUG = false;

    public static boolean BAT_ENABLED = false;
    public static boolean BLAZE_ENABLED = false;
    public static boolean CAVE_SPIDER_ENABLED = false;
    public static boolean CHICKEN_ENABLED = false;
    public static boolean COD_ENABLED = false;
    public static boolean COW_ENABLED = false;
    public static boolean CREEPER_ENABLED = false;
    public static boolean DOLPHIN_ENABLED = false;
    public static boolean DONKEY_ENABLED = false;
    public static boolean ENDER_DRAGON_ENABLED = false;
    public static boolean DROWNED_ENABLED = false;
    public static boolean ELDER_GUARDIAN_ENABLED = false;
    public static boolean ENDERMAN_ENABLED = false;
    public static boolean ENDERMITE_ENABLED = false;
    public static boolean EVOKER_ENABLED = false;
    public static boolean GHAST_ENABLED = false;
    public static boolean GIANT_ENABLED = false;
    public static boolean GUARDIAN_ENABLED = false;
    public static boolean HORSE_ENABLED = false;
    public static boolean HUSK_ENABLED = false;
    public static boolean ILLUSIONER_ENABLED = false;
    public static boolean IRON_GOLEM_ENABLED = false;
    public static boolean LLAMA_ENABLED = false;
    public static boolean MAGMA_CUBE_ENABLED = false;
    public static boolean MOOSHROOM_ENABLED = false;
    public static boolean MULE_ENABLED = false;
    public static boolean OCELOT_ENABLED = false;
    public static boolean PARROT_ENABLED = false;
    public static boolean PIG_ENABLED = false;
    public static boolean PHANTOM_ENABLED = false;
    public static boolean POLAR_BEAR_ENABLED = false;
    public static boolean PUFFERFISH_ENABLED = false;
    public static boolean RABBIT_ENABLED = false;
    public static boolean SALMON_ENABLED = false;
    public static boolean SHEEP_ENABLED = false;
    public static boolean SHULKER_ENABLED = false;
    public static boolean SILVERFISH_ENABLED = false;
    public static boolean SKELETON_ENABLED = false;
    public static boolean SKELETON_HORSE_ENABLED = false;
    public static boolean SLIME_ENABLED = false;
    public static boolean SNOW_GOLEM_ENABLED = false;
    public static boolean SPIDER_ENABLED = false;
    public static boolean SQUID_ENABLED = false;
    public static boolean STRAY_ENABLED = false;
    public static boolean TROPICAL_FISH_ENABLED = false;
    public static boolean TURTLE_ENABLED = false;
    public static boolean VEX_ENABLED = false;
    public static boolean VILLAGER_ENABLED = false;
    public static boolean VINDICATOR_ENABLED = false;
    public static boolean WITCH_ENABLED = false;
    public static boolean WITHER_ENABLED = false;
    public static boolean WITHER_SKELETON_ENABLED = false;
    public static boolean WOLF_ENABLED = false;
    public static boolean ZOMBIE_ENABLED = false;
    public static boolean ZOMBIE_HORSE_ENABLED = false;
    public static boolean ZOMBIE_PIGMAN_ENABLED = false;
    public static boolean ZOMBIE_VILLAGER_ENABLED = false;

    private static File mobsDir;

    public static File getMobsDirectory() {
        if (mobsDir == null) {
            mobsDir = new File(Ridables.getInstance().getDataFolder(), "mobs");
            if (!mobsDir.exists()) {
                mobsDir.mkdirs();
            }
        }
        return mobsDir;
    }

    /**
     * Reload the config.yml from disk
     */
    public static void reload() {
        Ridables plugin = Ridables.getInstance();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        LANGUAGE_FILE = config.getString("language-file", "lang-en.yml");

        LOGGER_INFO = config.getBoolean("logger.info", true);
        LOGGER_WARN = config.getBoolean("logger.warn", true);
        LOGGER_ERROR = config.getBoolean("logger.error", true);
        LOGGER_DEBUG = config.getBoolean("logger.debug", false);

        CANCEL_COMMANDS_WHILE_RIDING = config.getBoolean("cancel-commands-while-riding", false);
        REQUIRE_SADDLE = config.getBoolean("saddle-to-mount", false);
        CONSUME_SADDLE = config.getBoolean("consume-saddle", false);
        ALLOW_RIDE_BABIES = config.getBoolean("allow-ride-babies", false);

        FLYING_MAX_Y = (int) config.getDouble("flying-max-y", 256D);

        ConfigurationSection mobs = config.getConfigurationSection("mobs");
        BAT_ENABLED = mobs.getBoolean("bat", false);
        BLAZE_ENABLED = mobs.getBoolean("blaze", false);
        CAVE_SPIDER_ENABLED = mobs.getBoolean("cave_spider", false);
        CHICKEN_ENABLED = mobs.getBoolean("chicken", false);
        COD_ENABLED = mobs.getBoolean("cod", false);
        COW_ENABLED = mobs.getBoolean("cow", false);
        CREEPER_ENABLED = mobs.getBoolean("creeper", false);
        DOLPHIN_ENABLED = mobs.getBoolean("dolphin", false);
        DONKEY_ENABLED = mobs.getBoolean("donkey", false);
        DROWNED_ENABLED = mobs.getBoolean("drowned", false);
        ELDER_GUARDIAN_ENABLED = mobs.getBoolean("elder_guardian", false);
        ENDER_DRAGON_ENABLED = mobs.getBoolean("ender_dragon", false);
        ENDERMAN_ENABLED = mobs.getBoolean("enderman", false);
        ENDERMITE_ENABLED = mobs.getBoolean("endermite", false);
        EVOKER_ENABLED = mobs.getBoolean("evoker", false);
        GHAST_ENABLED = mobs.getBoolean("ghast", false);
        GIANT_ENABLED = mobs.getBoolean("giant", false);
        GUARDIAN_ENABLED = mobs.getBoolean("guardian", false);
        HORSE_ENABLED = mobs.getBoolean("horse", false);
        HUSK_ENABLED = mobs.getBoolean("husk", false);
        ILLUSIONER_ENABLED = mobs.getBoolean("illusioner", false);
        IRON_GOLEM_ENABLED = mobs.getBoolean("iron_golem", false);
        LLAMA_ENABLED = mobs.getBoolean("llama", false);
        MAGMA_CUBE_ENABLED = mobs.getBoolean("magma_cube", false);
        MOOSHROOM_ENABLED = mobs.getBoolean("mooshroom", false);
        MULE_ENABLED = mobs.getBoolean("mule", false);
        OCELOT_ENABLED = mobs.getBoolean("ocelot", false);
        PARROT_ENABLED = mobs.getBoolean("parrot", false);
        PIG_ENABLED = mobs.getBoolean("pig", false);
        PHANTOM_ENABLED = mobs.getBoolean("phantom", false);
        POLAR_BEAR_ENABLED = mobs.getBoolean("polar_bear", false);
        PUFFERFISH_ENABLED = mobs.getBoolean("pufferfish", false);
        RABBIT_ENABLED = mobs.getBoolean("rabbit", false);
        SALMON_ENABLED = mobs.getBoolean("salmon", false);
        SHEEP_ENABLED = mobs.getBoolean("sheep", false);
        SHULKER_ENABLED = mobs.getBoolean("shulker", false);
        SILVERFISH_ENABLED = mobs.getBoolean("silverfish", false);
        SKELETON_ENABLED = mobs.getBoolean("skeleton", false);
        SKELETON_HORSE_ENABLED = mobs.getBoolean("skeleton_horse", false);
        SLIME_ENABLED = mobs.getBoolean("slime", false);
        SNOW_GOLEM_ENABLED = mobs.getBoolean("snow_golem", false);
        SPIDER_ENABLED = mobs.getBoolean("spider", false);
        SQUID_ENABLED = mobs.getBoolean("squid", false);
        STRAY_ENABLED = mobs.getBoolean("stray", false);
        TROPICAL_FISH_ENABLED = mobs.getBoolean("tropical_fish", false);
        TURTLE_ENABLED = mobs.getBoolean("turtle", false);
        VEX_ENABLED = mobs.getBoolean("vex", false);
        VILLAGER_ENABLED = mobs.getBoolean("villager", false);
        VINDICATOR_ENABLED = mobs.getBoolean("vindicator", false);
        WITCH_ENABLED = mobs.getBoolean("witch", false);
        WITHER_ENABLED = mobs.getBoolean("wither", false);
        WITHER_SKELETON_ENABLED = mobs.getBoolean("wither_skeleton", false);
        WOLF_ENABLED = mobs.getBoolean("wolf", false);
        ZOMBIE_ENABLED = mobs.getBoolean("zombie", false);
        ZOMBIE_HORSE_ENABLED = mobs.getBoolean("zombie_horse", false);
        ZOMBIE_PIGMAN_ENABLED = mobs.getBoolean("zombie_pigman", false);
        ZOMBIE_VILLAGER_ENABLED = mobs.getBoolean("zombie_villager", false);
    }
}
