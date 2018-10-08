package net.pl3x.bukkit.ridables.util;

import net.minecraft.server.v1_13_R2.BlockPosition;
import org.bukkit.Location;

public class Utils {
    public static BlockPosition toBlockPosition(Location loc) {
        return new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }
}
