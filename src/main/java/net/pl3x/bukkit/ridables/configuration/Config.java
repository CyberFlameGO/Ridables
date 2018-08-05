package net.pl3x.bukkit.ridables.configuration;

import net.pl3x.bukkit.ridables.Ridables;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public static String LANGUAGE_FILE = "lang-en.yml";

    public static boolean CHECK_FOR_UPDATES = true;

    public static boolean UNMOUNT_ON_TELEPORT = true;
    public static boolean CANCEL_COMMANDS_WHILE_RIDING = false;

    public static boolean CHICKEN_ENABLED = true;
    public static float CHICKEN_SPEED = 1.0F;
    public static float CHICKEN_JUMP_POWER = 0.5F;

    public static boolean COW_ENABLED = true;
    public static float COW_SPEED = 1.0F;
    public static float COW_JUMP_POWER = 0.5F;

    public static boolean DOLPHIN_ENABLED = true;
    public static float DOLPHIN_SPEED = 1.0F;
    public static boolean DOLPHIN_BOUNCE = true;
    public static boolean DOLPHIN_BUBBLES = true;
    public static String DOLPHIN_SPACEBAR_MODE = "shoot";
    public static int DOLPHIN_SHOOT_COOLDOWN = 10;
    public static float DOLPHIN_SHOOT_SPEED = 1.0F;
    public static float DOLPHIN_SHOOT_DAMAGE = 5.0F;
    public static int DOLPHIN_DASH_COOLDOWN = 10;
    public static float DOLPHIN_DASH_BOOST = 1.5F;
    public static int DOLPHIN_DASH_DURATION = 20;

    public static boolean DRAGON_ENABLED = false;
    public static float DRAGON_SPEED = 1.0F;
    public static int DRAGON_MAX_Y = 256;

    public static boolean LLAMA_ENABLED = true;
    public static float LLAMA_SPEED = 1.0F;
    public static float LLAMA_JUMP_POWER = 0.5F;
    public static boolean LLAMA_CARAVAN = true;

    public static boolean MOOSHROOM_ENABLED = true;
    public static float MOOSHROOM_SPEED = 1.0F;
    public static float MOOSHROOM_JUMP_POWER = 0.5F;

    public static boolean OCELOT_ENABLED = true;
    public static float OCELOT_SPEED = 1.0F;
    public static float OCELOT_JUMP_POWER = 0.5F;

    public static boolean PHANTOM_ENABLED = true;
    public static float PHANTOM_SPEED = 1.0F;
    public static double PHANTOM_GRAVITY = 0.05D;
    public static boolean PHANTOM_FALL_DAMAGE = true;
    public static boolean PHANTOM_BURN_IN_SUNLIGHT = true;
    public static int PHANTOM_MAX_Y = 256;

    public static boolean POLAR_BEAR_ENABLED = true;
    public static float POLAR_BEAR_SPEED = 1.0F;
    public static float POLAR_BEAR_JUMP_POWER = 0.5F;
    public static float POLAR_BEAR_STEP_HEIGHT = 1.0F;
    public static boolean POLAR_BEAR_STAND = true;

    public static boolean SHEEP_ENABLED = true;
    public static float SHEEP_SPEED = 1.0F;
    public static float SHEEP_JUMP_POWER = 0.5F;

    public static boolean TURTLE_ENABLED = true;
    public static float TURTLE_SPEED_LAND = 1.0F;
    public static float TURTLE_SPEED_WATER = 1.0F;

    public static boolean WOLF_ENABLED = true;
    public static float WOLF_SPEED = 1.0F;
    public static float WOLF_JUMP_POWER = 0.5F;

    public static void reload() {
        Ridables plugin = Ridables.getPlugin(Ridables.class);
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        LANGUAGE_FILE = config.getString("language-file", "lang-en.yml");

        CHECK_FOR_UPDATES = config.getBoolean("update-checker", true);

        UNMOUNT_ON_TELEPORT = config.getBoolean("unmount-on-teleport", true);
        CANCEL_COMMANDS_WHILE_RIDING = config.getBoolean("cancel-commands-while-riding", false);

        CHICKEN_ENABLED = config.getBoolean("chicken.enabled", true);
        CHICKEN_SPEED = (float) config.getDouble("chicken.speed", 1.0D);
        CHICKEN_JUMP_POWER = (float) config.getDouble("chicken.jump-power", 0.5D);

        COW_ENABLED = config.getBoolean("cow.enabled", true);
        COW_SPEED = (float) config.getDouble("cow.speed", 1.0D);
        COW_JUMP_POWER = (float) config.getDouble("cow.jump-power", 0.5D);

        DOLPHIN_ENABLED = config.getBoolean("dolphin.enabled", true);
        DOLPHIN_SPEED = (float) config.getDouble("dolphin.speed", 1.0D);
        DOLPHIN_BOUNCE = config.getBoolean("dolphin.bounce", true);
        DOLPHIN_BUBBLES = config.getBoolean("dolphin.bubbles", true);
        DOLPHIN_SPACEBAR_MODE = config.getString("dolphin.spacebar", "shoot");
        DOLPHIN_SHOOT_COOLDOWN = (int) config.getDouble("dolphin.shoot.cooldown", 10);
        DOLPHIN_SHOOT_SPEED = (float) config.getDouble("dolphin.shoot.speed", 1.0D);
        DOLPHIN_SHOOT_DAMAGE = (float) config.getDouble("dolphin.shoot.damage", 5.0D);
        DOLPHIN_DASH_COOLDOWN = (int) config.getDouble("dolphin.dash.cooldown", 100);
        DOLPHIN_DASH_BOOST = (float) config.getDouble("dolphin.dash.boost", 1.5D);
        DOLPHIN_DASH_DURATION = (int) config.getDouble("dolphin.dash.duration", 20);

        DRAGON_ENABLED = config.getBoolean("dragon.enabled", false);
        DRAGON_SPEED = (float) config.getDouble("dragon.speed", 1.0D);
        DRAGON_MAX_Y = (int) config.getDouble("dragon.max-y", 256);

        LLAMA_ENABLED = config.getBoolean("llama.enabled", true);
        LLAMA_SPEED = (float) config.getDouble("llama.speed", 1.0D);
        LLAMA_JUMP_POWER = (float) config.getDouble("llama.jump-power", 0.5D);
        LLAMA_CARAVAN = config.getBoolean("llama.caravan", true);

        MOOSHROOM_ENABLED = config.getBoolean("mooshroom.enabled", true);
        MOOSHROOM_SPEED = (float) config.getDouble("mooshroom.speed", 1.0D);
        MOOSHROOM_JUMP_POWER = (float) config.getDouble("mooshroom.jump-power", 0.5D);

        OCELOT_ENABLED = config.getBoolean("ocelot.enabled", true);
        OCELOT_SPEED = (float) config.getDouble("ocelot.speed", 1.0D);
        OCELOT_JUMP_POWER = (float) config.getDouble("ocelot.jump-power", 0.5D);

        PHANTOM_ENABLED = config.getBoolean("phantom.enabled", true);
        PHANTOM_SPEED = (float) config.getDouble("phantom.speed", 1.0D);
        PHANTOM_GRAVITY = config.getDouble("phantom.gravity", 0.05D);
        PHANTOM_FALL_DAMAGE = config.getBoolean("phantom.fall-damage", true);
        PHANTOM_BURN_IN_SUNLIGHT = config.getBoolean("phantom.burn-in-sun", true);
        PHANTOM_MAX_Y = (int) config.getDouble("phantom.max-y", 256);

        POLAR_BEAR_ENABLED = config.getBoolean("polar-bear.enabled", true);
        POLAR_BEAR_SPEED = (float) config.getDouble("polar-bear.speed", 1.0D);
        POLAR_BEAR_JUMP_POWER = (float) config.getDouble("polar-bear.jump-power", 0.5D);
        POLAR_BEAR_STEP_HEIGHT = (float) config.getDouble("polar-bear.step-height", 1.0D);
        POLAR_BEAR_STAND = config.getBoolean("polar-bear.stand", true);

        SHEEP_ENABLED = config.getBoolean("sheep.enabled", true);
        SHEEP_SPEED = (float) config.getDouble("sheep.speed", 1.0D);
        SHEEP_JUMP_POWER = (float) config.getDouble("sheep.jump-power", 0.5D);

        TURTLE_ENABLED = config.getBoolean("turtle.enabled", true);
        TURTLE_SPEED_LAND = (float) config.getDouble("turtle.speed.on-land", 1.0D);
        TURTLE_SPEED_WATER = (float) config.getDouble("turtle.speed.in-water", 1.0D);

        WOLF_ENABLED = config.getBoolean("wolf.enabled", true);
        WOLF_SPEED = (float) config.getDouble("wolf.speed", 1.0D);
        WOLF_JUMP_POWER = (float) config.getDouble("wolf.jump-power", 0.5D);
    }
}
