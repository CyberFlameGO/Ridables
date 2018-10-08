package net.pl3x.bukkit.ridables.hook;

import net.pl3x.bukkit.ridables.util.Logger;

public class PlugMan {
    public static void configurePlugMan() {
        Logger.warn("PlugMan is detected to be installed!");
        com.rylinaux.plugman.PlugMan.getInstance().getIgnoredPlugins().add("Ridables");
        Logger.warn("Forcing PlugMan to ignore Ridables");
    }
}
