package net.pl3x.bukkit.ridables.entity.projectile;

import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.EntityEvokerFangs;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.entity.RidableEvoker;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;

public class CustomEvokerFangs extends EntityEvokerFangs {
    private static Field a;
    private static Field b;
    private static Field c;

    static {
        try {
            a = EntityEvokerFangs.class.getDeclaredField("a");
            a.setAccessible(true);
            b = EntityEvokerFangs.class.getDeclaredField("b");
            b.setAccessible(true);
            c = EntityEvokerFangs.class.getDeclaredField("c");
            c.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    private final RidableEvoker evoker;
    private final EntityPlayer rider;

    public CustomEvokerFangs(World world) {
        super(world);
        this.evoker = null;
        this.rider = null;
    }

    public CustomEvokerFangs(World world, double x, double y, double z, float rotationYaw, int warmupDelayTicks, RidableEvoker evoker, EntityPlayer rider) {
        super(world, x, y, z, rotationYaw, warmupDelayTicks, rider);
        this.evoker = evoker;
        this.rider = rider;
    }

    public Evoker getEvoker() {
        return (Evoker) evoker.getBukkitEntity();
    }

    public Player getRider() {
        return rider.getBukkitEntity();
    }

    public void tick() {
        setFlag(6, this.bc());
        W();
        int warmupDelayTicks = getWarmupDelayTicks() - 1;
        setWarmupDelayTicks(warmupDelayTicks);
        if (warmupDelayTicks < 0) { // warmupDelayTicks
            if (warmupDelayTicks == -8) {
                List<EntityLiving> list = world.a(EntityLiving.class, getBoundingBox().grow(0.2D, 0.0D, 0.2D));
                for (EntityLiving entity : list) {
                    damage(entity); // damage
                }
            }
            if (!hasSentSpikeEvent()) { // sentSpikeEvent
                world.broadcastEntityEffect(this, (byte) 4);
                setSentSpikeEvent();
            }
            int lifeTicks = getLifeTicks() - 1;
            setLifeTicks(lifeTicks);
            if (lifeTicks < 0) { // lifeTicks
                die();
            }
        }
    }

    private void damage(EntityLiving target) {
        if (target == rider || target == evoker) {
            return; // do not hit self
        }
        EntityLiving owner = getOwner();
        if (target.isAlive() && !target.bl() && target != owner) {
            if (owner == null) {
                CraftEventFactory.entityDamage = this;
                target.damageEntity(DamageSource.MAGIC, RidableEvoker.CONFIG.RIDER_FANGS_DAMAGE);
                CraftEventFactory.entityDamage = null;
            } else {
                if (owner.r(target)) {
                    return;
                }
                target.damageEntity(DamageSource.c(this, owner), RidableEvoker.CONFIG.RIDER_FANGS_DAMAGE);
            }
        }
    }

    private int getWarmupDelayTicks() {
        try {
            return a.getInt(this);
        } catch (IllegalAccessException ignore) {
        }
        return 0;
    }

    private void setWarmupDelayTicks(int ticks) {
        try {
            a.setInt(this, ticks);
        } catch (IllegalAccessException ignore) {
        }
    }

    private boolean hasSentSpikeEvent() {
        try {
            return b.getBoolean(this);
        } catch (IllegalAccessException ignore) {
        }
        return false;
    }

    private void setSentSpikeEvent() {
        try {
            b.setBoolean(this, true);
        } catch (IllegalAccessException ignore) {
        }
    }

    private int getLifeTicks() {
        try {
            return c.getInt(this);
        } catch (IllegalAccessException ignore) {
        }
        return 0;
    }

    private void setLifeTicks(int ticks) {
        try {
            c.setInt(this, ticks);
        } catch (IllegalAccessException ignore) {
        }
    }
}
