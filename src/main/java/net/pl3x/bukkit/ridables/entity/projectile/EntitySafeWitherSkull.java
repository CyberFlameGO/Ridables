package net.pl3x.bukkit.ridables.entity.projectile;

import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.DamageSource;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EntityWitherSkull;
import net.minecraft.server.v1_13_R1.MobEffect;
import net.minecraft.server.v1_13_R1.MobEffects;
import net.minecraft.server.v1_13_R1.MovingObjectPosition;
import net.minecraft.server.v1_13_R1.Particles;
import net.minecraft.server.v1_13_R1.ProjectileHelper;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.data.ServerType;
import net.pl3x.bukkit.ridables.entity.EntityRidableWither;
import org.bukkit.craftbukkit.v1_13_R1.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class EntitySafeWitherSkull extends EntityWitherSkull {
    private EntityRidableWither wither;
    private EntityPlayer player;
    private int f;

    public EntitySafeWitherSkull(World world) {
        super(world);
    }

    public EntitySafeWitherSkull(World world, EntityRidableWither wither, EntityPlayer player, double x, double y, double z) {
        super(world, player, x, y, z);
        this.wither = wither;
        this.player = player;
    }

    public Wither getWither() {
        return (Wither) wither.getBukkitEntity();
    }

    public Player getRider() {
        return player.getBukkitEntity();
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
            if (mop.entity == wither || mop.entity == player) {
                mop = null; // dont hit self
            } else if (Ridables.getInstance().getServerType() == ServerType.PAPER &&
                    CraftEventFactory.callProjectileCollideEvent(this, mop).isCancelled()) {
                mop = null;
            }
        }
        if (mop != null) {
            a(mop);
            if (dead) {
                CraftEventFactory.callProjectileHitEvent(this, mop);
            }
        }

        locX += motX * Config.WITHER_SHOOT_SPEED;
        locY += motY * Config.WITHER_SHOOT_SPEED;
        locZ += motZ * Config.WITHER_SHOOT_SPEED;
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
        if (mop.entity != null && Config.WITHER_SHOOT_DAMAGE > 0) {
            boolean didDamage;
            if (shooter != null) {
                didDamage = mop.entity.damageEntity(DamageSource.projectile(this, shooter), Config.WITHER_SHOOT_DAMAGE);
                if (didDamage) {
                    if (mop.entity.isAlive()) {
                        a(shooter, mop.entity);
                    }
                    shooter.heal(Config.WITHER_SHOOT_HEAL_AMOUNT, EntityRegainHealthEvent.RegainReason.WITHER);
                }
            } else {
                didDamage = mop.entity.damageEntity(DamageSource.MAGIC, Config.WITHER_SHOOT_DAMAGE);
            }
            if (didDamage && mop.entity instanceof EntityLiving) {
                if (Config.WITHER_SHOOT_EFFECT_DURATION > 0) {
                    ((EntityLiving) mop.entity).addEffect(new MobEffect(MobEffects.WITHER, 20 * Config.WITHER_SHOOT_EFFECT_DURATION, 1), EntityPotionEffectEvent.Cause.ATTACK);
                }
            }
        }
        ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(), 1.0F, false);
        world.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            world.createExplosion(this, locX, locY, locZ, event.getRadius(), event.getFire(), Config.WITHER_SHOOT_GRIEF);
        }
        die();
    }
}
