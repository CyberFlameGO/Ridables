package net.pl3x.bukkit.ridables.util;

import net.pl3x.bukkit.ridables.configuration.Config;

public class Logger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_MAGENTA = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void debug(String str) {
        if (Config.DEBUG_MODE) {
            log(ANSI_CYAN + "[" + ANSI_GREEN + "DEBUG" + ANSI_CYAN + "]" + ANSI_MAGENTA + " " + str);
        }
    }

    public static void info(String str) {
        log(ANSI_YELLOW + str);
    }

    public static void error(String str) {
        log(ANSI_RED + str);
    }

    private static void log(String str) {
        System.out.println(ANSI_YELLOW + "[" + ANSI_CYAN + "Ridables" + ANSI_YELLOW + "]" + ANSI_RESET + " " + str + ANSI_RESET);
    }
}
