package net.pl3x.bukkit.ridables.data;

public enum ServerType {
    /**
     * CraftBukkit Server (or unknown)
     */
    CRAFTBUKKIT("CraftBukkit"),
    /**
     * Spigot Server
     */
    SPIGOT("Spigot"),
    /**
     * Paper Server
     */
    PAPER("Paper");

    public final String name;

    ServerType(String name) {
        this.name = name;
    }
}
