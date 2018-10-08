package net.pl3x.bukkit.ridables.hook;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class Spigot {
    public static boolean CallEntityMountEvent(Entity vehicle, Entity passenger) {
        org.spigotmc.event.entity.EntityMountEvent event = new org.spigotmc.event.entity.EntityMountEvent(passenger, vehicle);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static boolean CallEntityDismountEvent(Entity vehicle, Entity passenger) {
        org.spigotmc.event.entity.EntityDismountEvent event = new org.spigotmc.event.entity.EntityDismountEvent(passenger, vehicle);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }
}
