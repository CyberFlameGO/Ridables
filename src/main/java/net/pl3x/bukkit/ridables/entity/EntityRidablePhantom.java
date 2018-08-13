package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.DamageSource;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityPhantom;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EnumHand;
import net.minecraft.server.v1_13_R1.EnumMoveType;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.IBlockData;
import net.minecraft.server.v1_13_R1.MathHelper;
import net.minecraft.server.v1_13_R1.SoundEffectType;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDFlying;
import net.pl3x.bukkit.ridables.entity.projectile.EntityPhantomFlames;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class EntityRidablePhantom extends EntityPhantom implements RidableEntity {
    private ControllerMove aiController;
    private ControllerWASDFlying wasdController;

    public EntityRidablePhantom(World world) {
        super(world);
        aiController = moveController;
        wasdController = new ControllerWASDFlying(this);
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return false;
    }

    public boolean aY() {
        return false; // eject passengers when in water
    }

    protected void mobTick() {
        EntityPlayer rider = getRider();
        if (rider != null) {
            setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
            if (rider.bj == 0) {
                motY -= Config.PHANTOM_GRAVITY;
            } else {
                fallDistance = 0;
            }
        } else {
            fallDistance = 0;
        }
        super.mobTick();
    }

    public void setRotation(float newYaw, float newPitch) {
        setYawPitch(lastYaw = yaw = newYaw, pitch = -newPitch * 0.75F);
        aS = aQ = yaw;
    }

    public float getJumpPower() {
        return 0;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.PHANTOM_SPEED * (onGround ? 1 : 3F);
    }

    public EntityPlayer getRider() {
        if (passengers != null && !passengers.isEmpty()) {
            Entity entity = passengers.get(0);
            if (entity instanceof EntityPlayer) {
                return (EntityPlayer) entity;
            }
        }
        return null;
    }

    public void useAIController() {
        if (moveController != aiController) {
            moveController = aiController;
        }
    }

    public void useWASDController() {
        if (moveController != wasdController) {
            moveController = wasdController;
        }
    }

    public boolean onSpacebar() {
        EntityPlayer rider = getRider();
        if (rider != null && rider.getBukkitEntity().hasPermission("allow.shoot.phantom")) {
            shoot(getRider());
        }
        return false;
    }

    public boolean onClick(org.bukkit.entity.Entity entity, EnumHand hand) {
        return false;
    }

    public boolean onClick(Block block, EnumHand hand) {
        return false;
    }

    public boolean onClick(EnumHand hand) {
        return false;
    }

    public boolean shoot(EntityPlayer rider) {
        Location loc = ((LivingEntity) getBukkitEntity()).getEyeLocation();
        loc.setPitch(-loc.getPitch());
        Vector target = loc.getDirection().normalize().multiply(100).add(loc.toVector());

        EntityPhantomFlames flames = new EntityPhantomFlames(world, this, rider);
        flames.shoot(target.getX() - locX, target.getY() - locY, target.getZ() - locZ, 1.0F, 5.0F);
        world.addEntity(flames);
        return true;
    }

    // onLivingUpdate
    public void k() {
        super.k();
        if (!Config.PHANTOM_BURN_IN_SUNLIGHT) {
            extinguish(); // dont burn in sunlight
        }
    }

    // updateFallState
    protected void a(double d0, boolean flag, IBlockData iblockdata, BlockPosition blockposition) {
        if (flag) {
            if (Config.PHANTOM_FALL_DAMAGE && fallDistance > 0.0F) {
                iblockdata.getBlock().fallOn(world, blockposition, this, fallDistance);
            }
            fallDistance = 0.0F;
        } else if (d0 < 0.0D) {
            fallDistance = (float) ((double) fallDistance - (d0 / 2));
        }
    }

    // fall
    public void c(float f, float f1) {
        super.c(f, f1);
        int i = (int) (MathHelper.f((f - 3.0F) * f1) * -(motY));
        if (i > 0) {
            if (!damageEntity(DamageSource.FALL, (float) i)) {
                return;
            }
            a(n(i), 1.0F, 1.0F);
            int j = MathHelper.floor(locX);
            int k = MathHelper.floor(locY - 0.2D);
            int l = MathHelper.floor(locZ);
            IBlockData iblockdata = world.getType(new BlockPosition(j, k, l));
            if (!iblockdata.isAir()) {
                SoundEffectType soundeffecttype = iblockdata.getBlock().getStepSound();
                a(soundeffecttype.g(), soundeffecttype.a() * 0.5F, soundeffecttype.b() * 0.75F);
            }
        }
    }

    public void a(float strafe, float vertical, float forward) {
        if (getRider() == null) {
            super.a(strafe, vertical, forward);
            return;
        }

        a(strafe, vertical, forward, 0.02F * getSpeed());
        move(EnumMoveType.PLAYER, motX, motY, motZ);

        motX *= 0.91F;
        motY *= 0.91F;
        motZ *= 0.91F;

        aI = aJ;
        double d0 = locX - lastX;
        double d1 = locZ - lastZ;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        aJ += (f - aJ) * 0.4F;
        aK += aJ;
    }
}
