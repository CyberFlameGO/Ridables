package net.pl3x.bukkit.ridables.data;

import org.bukkit.Bukkit;

public enum ServerType {
    /**
     * CraftBukkit Server (or unknown)
     */
    CRAFTBUKKIT(Bukkit.getName()),
    /**
     * Spigot Server
     */
    SPIGOT(Bukkit.getVersion().startsWith("git-Spigot") ? "Spigot" : Bukkit.getName()),
    /**
     * Paper Server
     */
    PAPER(Bukkit.getName());

    public final String name;

    ServerType(String name) {
        this.name = name;
    }
}
