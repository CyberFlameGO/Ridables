package net.pl3x.bukkit.ridables.entity.projectile;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityLargeFireball;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.MovingObjectPosition;
import net.minecraft.server.v1_13_R2.Particles;
import net.minecraft.server.v1_13_R2.ProjectileHelper;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class CustomFireball extends EntityLargeFireball implements CustomProjectile {
    private final RidableEntity ridable;
    private final EntityPlayer rider;
    private final double speed;
    private final double damage;
    private final boolean grief;
    private int ticksAlive;

    public CustomFireball(World world) {
        super(world);
        this.ridable = null;
        this.rider = null;
        this.speed = 1.0F;
        this.damage = 0;
        this.grief = false;
    }

    public CustomFireball(World world, RidableEntity ridable, EntityPlayer rider, double x, double y, double z, double speed, double damage, boolean grief) {
        super(world, rider == null ? (EntityLiving) ridable : rider, x, y, z);
        this.ridable = ridable;
        this.rider = rider;
        this.speed = speed;
        this.damage = damage;
        this.grief = grief;

        setPositionRotation(shooter.locX, shooter.locY, shooter.locZ, shooter.yaw, shooter.pitch);
        setPosition(locX, locY, locZ);

        double d3 = (double) MathHelper.sqrt(x * x + y * y + z * z);
        this.dirX = x / d3 * 0.1D;
        this.dirY = y / d3 * 0.1D;
        this.dirZ = z / d3 * 0.1D;
    }

    @Override
    public RidableEntity getRidable() {
        return ridable;
    }

    @Override
    public Mob getMob() {
        return ridable == null ? null : ((EntityInsentient) ridable).getBukkitMob();
    }

    @Override
    public Player getRider() {
        return rider == null ? null : rider.getBukkitEntity();
    }

    @Override
    public void tick() {
        if (shooter != null && shooter.dead || !world.isLoaded(new BlockPosition(this))) {
            die();
            return;
        }
        setFlag(6, bc());
        W();
        setOnFire(1);
        MovingObjectPosition mop = ProjectileHelper.a(this, true, ++ticksAlive >= 25, shooter);
        if (mop != null && mop.entity != null) {
            if (mop.entity == ridable || mop.entity == rider) {
                mop = null; // dont hit self
            } else if (!CraftEventFactory.callProjectileCollideEvent(this, mop).callEvent()) {
                mop = null;
            }
        }
        if (mop != null) {
            a(mop);
            if (dead) {
                CraftEventFactory.callProjectileHitEvent(this, mop);
            }
        }
        locX += motX * speed;
        locY += motY * speed;
        locZ += motZ * speed;
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
    @Override
    protected void a(MovingObjectPosition mop) {
        if (mop.entity != null) {
            if (damage > 0) {
                mop.entity.damageEntity(DamageSource.fireball(this, shooter), (float) damage);
                a(shooter, mop.entity);
            }
        }
        ExplosionPrimeEvent event = new ExplosionPrimeEvent((Explosive) CraftEntity.getEntity(world.getServer(), this));
        world.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            world.createExplosion(this, locX, locY, locZ, event.getRadius(), event.getFire(), grief);
        }
        die();
    }
}
