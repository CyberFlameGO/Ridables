package net.pl3x.bukkit.ridables.entity.projectile;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityLargeFireball;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.MovingObjectPosition;
import net.minecraft.server.v1_13_R2.Particles;
import net.minecraft.server.v1_13_R2.ProjectileHelper;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.data.ServerType;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class EntityCustomFireball extends EntityLargeFireball {
    private final EntityLiving ridable;
    private final EntityPlayer rider;
    private int f;

    public EntityCustomFireball(World world) {
        super(world);
        this.ridable = null;
        this.rider = null;
    }

    public EntityCustomFireball(World world, EntityLiving ridable, EntityPlayer rider, double x, double y, double z) {
        super(world, rider, x, y, z);
        this.ridable = ridable;
        this.rider = rider;

        setPositionRotation(ridable.locX, ridable.locY, ridable.locZ, ridable.yaw, ridable.pitch);
        setPosition(locX, locY, locZ);

        double d3 = (double) MathHelper.sqrt(x * x + y * y + z * z);
        this.dirX = x / d3 * 0.1D;
        this.dirY = y / d3 * 0.1D;
        this.dirZ = z / d3 * 0.1D;
    }

    public LivingEntity getEntity() {
        return (LivingEntity) ((Entity) ridable).getBukkitEntity();
    }

    public Player getRider() {
        return (Player) ((Entity) rider).getBukkitEntity();
    }

    public void tick() {
        if (!world.isClientSide && (shooter != null && shooter.dead || !world.isLoaded(new BlockPosition(this)))) {
            die();
            return;
        }
        setFlag(6, bc());
        W();
        setOnFire(1);
        MovingObjectPosition mop = ProjectileHelper.a(this, true, ++f >= 25, shooter);
        if (mop != null && mop.entity != null) {
            if (mop.entity == ridable || mop.entity == rider) {
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
        locX += motX * Config.GHAST_SHOOT_SPEED;
        locY += motY * Config.GHAST_SHOOT_SPEED;
        locZ += motZ * Config.GHAST_SHOOT_SPEED;
        ProjectileHelper.a(this, 0.2F);
        double f = (double) k();
        if (isInWater()) {
            for (int i = 0; i < 4; ++i) {
                world.addParticle(Particles.e, locX - motX * 0.25D, locY - motY * 0.25D, locZ - motZ * 0.25D, motX, motY, motZ);
            }
            f = 0.8D;
        }
        motX += dirX;
        motY += dirY;
        motZ += dirZ;
        motX *= f;
        motY *= f;
        motZ *= f;
        world.addParticle(i(), locX, locY + 0.5D, locZ, 0.0D, 0.0D, 0.0D);
        setPosition(locX, locY, locZ);
    }

    // onImpact
    protected void a(MovingObjectPosition mop) {
        if (mop.entity != null) {
            if (Config.GHAST_SHOOT_DAMAGE > 0) {
                mop.entity.damageEntity(DamageSource.fireball(this, shooter), Config.GHAST_SHOOT_DAMAGE);
                a(shooter, mop.entity);
            }
        }
        ExplosionPrimeEvent event = new ExplosionPrimeEvent((Explosive) CraftEntity.getEntity(world.getServer(), this));
        world.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            world.createExplosion(this, locX, locY, locZ, event.getRadius(), event.getFire(), Config.GHAST_SHOOT_GRIEF);
        }
        die();
    }
}
