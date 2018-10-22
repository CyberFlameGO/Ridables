package net.pl3x.bukkit.ridables.entity.projectile;

import net.minecraft.server.v1_13_R2.AxisAlignedBB;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityShulkerBullet;
import net.minecraft.server.v1_13_R2.EnumDirection;
import net.minecraft.server.v1_13_R2.IProjectile;
import net.minecraft.server.v1_13_R2.Material;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.MobEffect;
import net.minecraft.server.v1_13_R2.MobEffects;
import net.minecraft.server.v1_13_R2.MovingObjectPosition;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.entity.RidableShulker;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.entity.EntityPotionEffectEvent;

public class CustomShulkerBullet extends EntityShulkerBullet implements IProjectile, CustomProjectile {
    private final RidableShulker shulker;
    private final EntityPlayer rider;
    private int life;

    public CustomShulkerBullet(World world) {
        super(world);
        this.shulker = null;
        this.rider = null;
    }

    public CustomShulkerBullet(World world, RidableShulker shulker, EntityPlayer rider, Entity target, EnumDirection.EnumAxis dir) {
        super(world, shulker, target, dir);
        this.shulker = shulker;
        this.rider = rider;
    }

    public RidableShulker getRidable() {
        return shulker;
    }

    public Shulker getMob() {
        return shulker == null ? null : (Shulker) shulker.getBukkitEntity();
    }

    public Player getRider() {
        return rider == null ? null : rider.getBukkitEntity();
    }

    public void tick() {
        detectCollisions();

        updatePosition();

        if (!world.a(getBoundingBox(), Material.AIR)) {
            die(); // die if not in air
        }

        if (++life > 100) {
            die();
        }
    }

    private void updatePosition() {
        locX += motX;
        locY += motY;
        locZ += motZ;
        motY -= 0.02D;
        setPosition(locX, locY, locZ);
    }

    private void detectCollisions() {
        double reach = 10.0;

        Vec3D minVec = new Vec3D(locX, locY, locZ);
        Vec3D maxVec = new Vec3D(locX + motX * reach, locY + motY * reach, locZ + motZ * reach);

        MovingObjectPosition mop = world.rayTrace(minVec, maxVec);

        minVec = new Vec3D(locX, locY, locZ);
        maxVec = new Vec3D(locX + motX * reach, locY + motY * reach, locZ + motZ * reach);

        if (mop != null) {
            maxVec = new Vec3D(mop.pos.x, mop.pos.y, mop.pos.z);
        }

        EntityLiving hitEntity = getHitEntity(minVec, maxVec);
        if (hitEntity != null && rider != null) {
            if (RidableShulker.CONFIG.SHOOT_DAMAGE > 0) {
                if (hitEntity.damageEntity(DamageSource.a(this, rider).c(), RidableShulker.CONFIG.SHOOT_DAMAGE)) {
                    a(rider, hitEntity);
                    hitEntity.addEffect(new MobEffect(MobEffects.LEVITATION, 200), EntityPotionEffectEvent.Cause.ATTACK);
                }
            }
            die();
        }
    }

    private EntityLiving getHitEntity(Vec3D vec3d, Vec3D vec3d1) {
        EntityLiving entity = null;
        double d0 = 0.0D;
        for (Entity entity1 : world.getEntities(this, getBoundingBox().b(motX, motY, motZ).g(1.0D))) {
            if (entity1 != shulker && entity1 != rider && entity1 instanceof EntityLiving) {
                AxisAlignedBB axisalignedbb = entity1.getBoundingBox().g(0.5D);
                MovingObjectPosition movingobjectposition = axisalignedbb.b(vec3d, vec3d1);
                if (movingobjectposition != null) {
                    double d1 = vec3d.distanceSquared(movingobjectposition.pos);
                    if (d1 < d0 || d0 == 0.0D) {
                        entity = (EntityLiving) entity1;
                        d0 = d1;
                    }
                }
            }
        }
        return entity;
    }

    public void shoot(double d0, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        motX = d0 = (d0 / (double) f2) * f;
        motY = d1 = (d1 / (double) f2) * f;
        motZ = d2 = (d2 / (double) f2) * f;
        lastYaw = yaw = (float) (MathHelper.c(d0, d2) * 57.2957763671875D);
        lastPitch = pitch = (float) (MathHelper.c(d1, (double) MathHelper.sqrt(d0 * d0 + d2 * d2)) * 57.2957763671875D);
    }
}
