package net.pl3x.bukkit.ridables.hook;

import com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent;
import com.destroystokyo.paper.event.entity.EndermanEscapeEvent;
import com.destroystokyo.paper.event.entity.SlimeChangeDirectionEvent;
import com.destroystokyo.paper.event.entity.SlimeSwimEvent;
import com.destroystokyo.paper.event.entity.SlimeTargetLivingEntityEvent;
import com.destroystokyo.paper.event.entity.SlimeWanderEvent;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityEnderman;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.MovingObjectPosition;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.entity.RidableSlime;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

public class Paper {
    public static boolean DisableCreeperLingeringEffect(World world) {
        return world.paperConfig.disableCreeperLingeringEffect;
    }

    public static boolean CallProjectileCollideEvent(Entity entity, MovingObjectPosition mop) {
        return CraftEventFactory.callProjectileCollideEvent(entity, mop).isCancelled();
    }

    public static boolean CallEndermanEscapeEvent(EntityEnderman enderman) {
        return new EndermanEscapeEvent((Enderman) enderman.getBukkitEntity(), EndermanEscapeEvent.Reason.STARE).callEvent();
    }

    public static boolean CallEndermanAttackPlayerEvent(EntityEnderman enderman, EntityHuman entityhuman, boolean shouldAttack) {
        EndermanAttackPlayerEvent event = new EndermanAttackPlayerEvent((Enderman) enderman.getBukkitEntity(), (Player) entityhuman.getBukkitEntity());
        event.setCancelled(!shouldAttack);
        return event.callEvent();
    }

    public static boolean CallSlimeSwimEvent(RidableSlime slime) {
        return new SlimeSwimEvent((Slime) slime.getBukkitEntity()).callEvent();
    }

    public static boolean CallSlimeTargetLivingEntity(RidableSlime slime, EntityLiving target) {
        return new SlimeTargetLivingEntityEvent((Slime) slime.getBukkitEntity(), (LivingEntity) target.getBukkitEntity()).callEvent();
    }

    public static float CallSlimeChangeDirectionEvent(RidableSlime slime, float randInt) {
        SlimeChangeDirectionEvent event = new SlimeChangeDirectionEvent((Slime) slime.getBukkitEntity(), randInt);
        return event.callEvent() ? event.getNewYaw() : Float.MIN_VALUE;
    }

    public static boolean CallSlimeWanderEvent(RidableSlime slime) {
        return new SlimeWanderEvent((Slime) slime.getBukkitEntity()).callEvent();
    }
}
