package net.pl3x.bukkit.ridables.entity.ai.goal.squid;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.animal.RidableSquid;
import net.pl3x.bukkit.ridables.event.RidableSpacebarEvent;
import net.pl3x.bukkit.ridables.util.Const;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.util.Vector;

public class AISquidMove extends PathfinderGoal {
    private final RidableSquid squid;

    public AISquidMove(RidableSquid squid) {
        this.squid = squid;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return true;
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return a();
    }

    // tick
    @Override
    public void e() {
        EntityPlayer rider = squid.getRider();
        if (rider == null) {
            if (squid.cj() > 100) { // getIdleTime
                squid.c(0.0F, 0.0F, 0.0F); // setMovementVector
            } else if (squid.getRandom().nextInt(50) == 0 || !squid.inWater || !squid.l()) { // hasMovementVector
                float randYaw = squid.getRandom().nextFloat() * (Const.PI_FLOAT * 2F);
                squid.c(MathHelper.cos(randYaw) * 0.2F, -0.1F + squid.getRandom().nextFloat() * 0.2F, MathHelper.sin(randYaw) * 0.2F); // setMovementVector
            }
        } else {
            if (ControllerWASD.isJumping(rider)) {
                RidableSpacebarEvent spacebarEvent = new RidableSpacebarEvent(squid);
                if (spacebarEvent.callEvent() && !spacebarEvent.isHandled()) {
                    squid.onSpacebar();
                }
            }
            float forward = rider.bj;
            float strafe = rider.bh;
            float speed = (float) squid.getAttributeInstance(RidableType.RIDING_SPEED).getValue() * 5;
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
