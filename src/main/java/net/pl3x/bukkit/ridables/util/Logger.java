package net.pl3x.bukkit.ridables.util;

import net.pl3x.bukkit.ridables.configuration.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {
    /**
     * Logs a debug message (if debug mode is enabled)
     *
     * @param str String to log
     */
    public static void debug(String str) {
        if (Config.LOGGER_DEBUG) {
            log("&3[&aDEBUG&3]&d " + str);
        }
    }

    /**
     * Logs an informative message
     *
     * @param str String to log
     */
    public static void info(String str) {
        if (Config.LOGGER_INFO) {
            log("&e" + str);
        }
    }

    /**
     * Logs a warning message
     *
     * @param str String to log
     */
    public static void warn(String str) {
        if (Config.LOGGER_WARN) {
            log("&6" + str);
        }
    }

    /**
     * Logs an error message
     *
     * @param str String to log
     */
    public static void error(String str) {
        if (Config.LOGGER_ERROR) {
            log("&c" + str);
        }
    }

    private static void log(String str) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&3Ridables&e]&r " + str + "&r"));
    }
}
