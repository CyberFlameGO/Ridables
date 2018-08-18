package net.pl3x.bukkit.ridables.util;

import net.minecraft.server.v1_13_R1.DataWatcherObject;
import net.minecraft.server.v1_13_R1.EntityCreeper;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EntityPufferFish;
import net.pl3x.bukkit.ridables.entity.EntityRidableCreeper;
import net.pl3x.bukkit.ridables.entity.EntityRidablePufferFish;

import java.lang.reflect.Field;

public class ReflectionUtil {
    private static Field jumping;
    private static Field creeperIgnited;
    private static Field pufferfishBlowupCounter;

    static {
        try {
            jumping = EntityLiving.class.getDeclaredField("bg");
            jumping.setAccessible(true);
            creeperIgnited = EntityCreeper.class.getDeclaredField("c");
            creeperIgnited.setAccessible(true);
            pufferfishBlowupCounter = EntityPufferFish.class.getDeclaredField("c");
            pufferfishBlowupCounter.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if entity has their jump flag toggled on
     * <p>
     * This is true for players when they are pressing the spacebar
     *
     * @param entity Living entity to check
     * @return True if jump flag is toggled on
     */
    public static boolean isJumping(EntityLiving entity) {
        try {
            return jumping.getBoolean(entity);
        } catch (IllegalAccessException ignore) {
            return false;
        }
    }

    /**
     * Set the jump flag for an entity
     *
     * @param entity Entity to set
     */
    public static void setJumping(EntityLiving entity) {
        try {
            jumping.set(entity, false);
        } catch (IllegalAccessException ignore) {
        }
    }

    /**
     * Set ignited state of a ridable creeper
     *
     * @param creeper Ridable creeper
     * @param ignited Ignited state to set
     */
    public static void setCreeperIgnited(EntityRidableCreeper creeper, boolean ignited) {
        try {
            creeper.getDataWatcher().set((DataWatcherObject<Boolean>) creeperIgnited.get(creeper), ignited);
        } catch (IllegalAccessException ignore) {
        }
    }

    /**
     * Get puffer fish blow up count
     *
     * @param fish Ridable puffer fish
     * @return Count
     */
    public static int getPufferfishBlowupCount(EntityRidablePufferFish fish) {
        try {
            return pufferfishBlowupCounter.getInt(fish);
        } catch (IllegalAccessException ignore) {
            return 0;
        }
    }

    /**
     * Set puffer fish blow up count
     *
     * @param fish  Ridable puffer fish
     * @param count New count
     */
    public static void setPufferfishBlowupCount(EntityRidablePufferFish fish, int count) {
        try {
            pufferfishBlowupCounter.set(fish, count);
        } catch (IllegalAccessException ignore) {
        }
    }
}
