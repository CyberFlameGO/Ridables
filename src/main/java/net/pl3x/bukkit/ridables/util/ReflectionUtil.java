package net.pl3x.bukkit.ridables.util;

import net.minecraft.server.v1_13_R1.EntityLiving;

import java.lang.reflect.Field;

public class ReflectionUtil {
    private static Field jumping;

    static {
        try {
            jumping = EntityLiving.class.getDeclaredField("bg");
            jumping.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static boolean isJumping(EntityLiving entity) {
        try {
            return jumping.getBoolean(entity);
        } catch (IllegalAccessException ignore) {
            return false;
        }
    }
}
