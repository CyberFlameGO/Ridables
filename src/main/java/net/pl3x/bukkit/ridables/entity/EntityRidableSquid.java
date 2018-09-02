package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntitySquid;
import net.minecraft.server.v1_13_R2.Fluid;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.Particles;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.TagsFluid;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class EntityRidableSquid extends EntitySquid implements RidableEntity {
    private EntityPlayer rider;

    public EntityRidableSquid(World world) {
        super(world);
    }

    public RidableType getType() {
        return RidableType.SQUID;
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return false;
    }

    // canBeRiddenInWater
    public boolean aY() {
        return true;
    }

    protected void mobTick() {
        updateRider();
    }

    public void setRotation(float newYaw, float newPitch) {
    }

    public float getJumpPower() {
        return 0;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.SQUID_SPEED;
    }

    public EntityPlayer getRider() {
        return rider;
    }

    public EntityPlayer updateRider() {
        if (passengers == null || passengers.isEmpty()) {
            rider = null;
        } else {
            Entity entity = passengers.get(0);
            rider = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
        }
        return rider;
    }

    public void useAIController() {
    }

    public void useWASDController() {
    }

    // initEntityAI
    protected void n() {
        goalSelector.a(0, new AIMoveRandom(this));
        goalSelector.a(1, new AIFlee(this));
    }

    class AIFlee extends PathfinderGoal {
        private final EntityRidableSquid squid;
        private int ticks;

        AIFlee(EntityRidableSquid squid) {
            this.squid = squid;
        }

        public boolean a() {
            EntityLiving target = squid.getLastDamager();
            return squid.rider == null && squid.isInWater() && target != null && squid.h(target) < 100.0D;
        }

        public void c() {
            ticks = 0;
        }

        public void e() {
            if (rider != null) {
                return;
            }
            ++ticks;
            EntityLiving target = squid.getLastDamager();
            if (target == null) {
                return;
            }
            Vec3D vec = new Vec3D(squid.locX - target.locX, squid.locY - target.locY, squid.locZ - target.locZ);
            IBlockData blockState = squid.world.getType(new BlockPosition(squid.locX + vec.x, squid.locY + vec.y, squid.locZ + vec.z));
            Fluid fluidState = squid.world.b(new BlockPosition(squid.locX + vec.x, squid.locY + vec.y, squid.locZ + vec.z));
            if (fluidState.a(TagsFluid.WATER) || blockState.isAir()) { // is water or air
                double d0 = vec.b(); // vec.length
                if (d0 > 0.0D) {
                    vec.a(); // vec.normalize
                    float f = 3.0F;
                    if (d0 > 5.0D) {
                        f = (float) ((double) f - (d0 - 5.0D) / 5.0D);
                    }
                    if (f > 0.0F) {
                        vec = vec.a((double) f); // vec.scale
                    }
                }
                if (blockState.isAir()) {
                    vec = vec.a(0.0D, vec.y, 0.0D); // vec.subtract
                }
                squid.c((float) vec.x / 20.0F, (float) vec.y / 20.0F, (float) vec.z / 20.0F); // setMovementVector
            }
            if (ticks % 10 == 5) {
                squid.world.addParticle(Particles.e, squid.locX, squid.locY, squid.locZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    class AIMoveRandom extends PathfinderGoal {
        private final EntityRidableSquid squid;

        AIMoveRandom(EntityRidableSquid squid) {
            this.squid = squid;
        }

        // shouldExecute
        public boolean a() {
            return true;
        }

        // updateTask
        public void e() {
            if (squid.rider == null) {
                int i = squid.cj(); // getIdleTime
                if (i > 100) {
                    squid.c(0.0F, 0.0F, 0.0F); // setMovementVector
                } else if (squid.getRandom().nextInt(50) == 0 || !squid.inWater || !squid.l()) { // !hasMovementVector
                    float f = squid.getRandom().nextFloat() * ((float) Math.PI * 2F);
                    float x = MathHelper.cos(f) * 0.2F;
                    float y = -0.1F + squid.getRandom().nextFloat() * 0.2F;
                    float z = MathHelper.sin(f) * 0.2F;
                    squid.c(x, y, z); // setMovementVector
                }
            } else {
                float forward = rider.bj;
                float strafe = rider.bh;
                float speed = getSpeed() * 5;
                if (forward < 0) {
                    speed *= -0.5;
                }
                Vector target = ((CraftPlayer) ((Entity) rider).getBukkitEntity()).getEyeLocation()
                        .subtract(new Vector(0, 2, 0)).getDirection().normalize().multiply(speed);
                if (strafe != 0) {
                    if (forward == 0) {
                        rotateVectorAroundY(target, strafe > 0 ? -90 : 90);
                        target.setY(0);
                    } else {
                        if (forward < 0) {
                            rotateVectorAroundY(target, strafe > 0 ? 45 : -45);
                        } else {
                            rotateVectorAroundY(target, strafe > 0 ? -45 : 45);
                        }
                    }
                }
                if (forward != 0 || strafe != 0) {
                    Vec3D vec = new Vec3D(target.getX(), target.getY(), target.getZ());
                    squid.c((float) vec.x / 20.0F, (float) vec.y / 20.0F, (float) vec.z / 20.0F); // setMovementVector
                } else {
                    squid.c(0.0F, 0.0F, 0.0F); // setMovementVector
                }
            }
        }

        private void rotateVectorAroundY(Vector vector, double degrees) {
            double rad = Math.toRadians(degrees);
            double cos = Math.cos(rad);
            double sine = Math.sin(rad);
            double x = vector.getX();
            double z = vector.getZ();
            vector.setX(cos * x - sine * z);
            vector.setZ(sine * x + cos * z);
        }
    }
}
