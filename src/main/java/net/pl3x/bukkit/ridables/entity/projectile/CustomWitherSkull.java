package net.pl3x.bukkit.ridables.entity.projectile;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityWitherSkull;
import net.minecraft.server.v1_13_R2.MobEffect;
import net.minecraft.server.v1_13_R2.MobEffects;
import net.minecraft.server.v1_13_R2.MovingObjectPosition;
import net.minecraft.server.v1_13_R2.Particles;
import net.minecraft.server.v1_13_R2.ProjectileHelper;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.entity.RidableWither;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class CustomWitherSkull extends EntityWitherSkull implements CustomProjectile {
    private RidableWither wither;
    private EntityPlayer rider;
    private int f;

    public CustomWitherSkull(World world) {
        super(world);
    }

    public CustomWitherSkull(World world, RidableWither wither, EntityPlayer rider, double x, double y, double z) {
        super(world, rider, x, y, z);
        this.wither = wither;
        this.rider = rider;
    }

    public RidableWither getRidable() {
        return wither;
    }

    public Wither getMob() {
        return wither == null ? null : (Wither) wither.getBukkitEntity();
    }

    public Player getRider() {
        return rider == null ? null : rider.getBukkitEntity();
    }

    public void tick() {
        if (shooter != null && shooter.dead || !world.isLoaded(new BlockPosition(this))) {
            die();
            return;
        }
        setFlag(6, this.bc());
        W();
        MovingObjectPosition mop = ProjectileHelper.a(this, true, ++f >= 25, shooter);
        if (mop != null && mop.entity != null) {
            if (mop.entity == wither || mop.entity == rider) {
                mop = null; // dont hit self
            } else if (CraftEventFactory.callProjectileCollideEvent(this, mop).isCancelled()) {
                mop = null;
            }
        }
        if (mop != null) {
            a(mop);
            if (dead) {
                CraftEventFactory.callProjectileHitEvent(this, mop);
            }
        }

        locX += motX * RidableWither.CONFIG.SHOOT_SPEED;
        locY += motY * RidableWither.CONFIG.SHOOT_SPEED;
        locZ += motZ * RidableWither.CONFIG.SHOOT_SPEED;
        ProjectileHelper.a(this, 0.2F);
        float f = k();
        if (isInWater()) {
            for (int i = 0; i < 4; ++i) {
                world.addParticle(Particles.e, locX - motX * 0.25D, locY - motY * 0.25D, locZ - motZ * 0.25D, motX, motY, motZ);
            }
            f = 0.8F;
        }
        motX += dirX;
        motY += dirY;
        motZ += dirZ;
        motX *= (double) f;
        motY *= (double) f;
        motZ *= (double) f;
        world.addParticle(i(), locX, locY + 0.5D, locZ, 0.0D, 0.0D, 0.0D);
        setPosition(locX, locY, locZ);
    }

    protected void a(MovingObjectPosition mop) {
        if (mop.entity != null && RidableWither.CONFIG.SHOOT_DAMAGE > 0) {
            boolean didDamage;
            if (shooter != null) {
                didDamage = mop.entity.damageEntity(DamageSource.projectile(this, shooter), RidableWither.CONFIG.SHOOT_DAMAGE);
                if (didDamage) {
                    if (mop.entity.isAlive()) {
                        a(shooter, mop.entity);
                    }
                    shooter.heal(RidableWither.CONFIG.SHOOT_HEAL_AMOUNT, EntityRegainHealthEvent.RegainReason.WITHER);
                }
            } else {
                didDamage = mop.entity.damageEntity(DamageSource.MAGIC, RidableWither.CONFIG.SHOOT_DAMAGE);
            }
            if (didDamage && mop.entity instanceof EntityLiving) {
                if (RidableWither.CONFIG.SHOOT_EFFECT_DURATION > 0) {
                    ((EntityLiving) mop.entity).addEffect(new MobEffect(MobEffects.WITHER, 20 * RidableWither.CONFIG.SHOOT_EFFECT_DURATION, 1), EntityPotionEffectEvent.Cause.ATTACK);
                }
            }
        }
        ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(), 1.0F, false);
        world.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            world.createExplosion(this, locX, locY, locZ, event.getRadius(), event.getFire(), RidableWither.CONFIG.SHOOT_GRIEF);
        }
        die();
    }
}
