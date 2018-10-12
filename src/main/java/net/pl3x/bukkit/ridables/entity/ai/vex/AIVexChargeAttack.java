package net.pl3x.bukkit.ridables.entity.ai.vex;

import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.pl3x.bukkit.ridables.entity.RidableVex;

public class AIVexChargeAttack extends PathfinderGoal {
    private final RidableVex vex;

    public AIVexChargeAttack(RidableVex vex) {
        this.vex = vex;
        a(1); // setMutexBits
    }

    // shouldExecute
    public boolean a() {
        if (vex.getRider() != null) {
            return false;
        }
        if (vex.getGoalTarget() == null) {
            return false;
        }
        if (vex.getControllerMove().b()) { // isUpdating
            return false;
        }
        if (vex.getRandom().nextInt(7) != 0) {
            return false;
        }
        return vex.h(vex.getGoalTarget()) > 4.0D; // getDistanceSq
    }

    // shouldContinueExecuting
    public boolean b() {
        if (vex.getRider() != null) {
            return false;
        }
        if (!vex.getControllerMove().b()) { // isUpdating
            return false;
        }
        if (!vex.dA()) { // isCharging
            return false;
        }
        if (vex.getGoalTarget() == null) {
            return false;
        }
        return vex.getGoalTarget().isAlive();
    }

    // startExecuting
    public void c() {
        Vec3D eye = vex.getGoalTarget().i(1.0F); // getEyePosition
        vex.getControllerMove().a(eye.x, eye.y, eye.z, 1.0D); // setMoveTo
        vex.a(true); // setCharging
        vex.a(SoundEffects.ENTITY_VEX_CHARGE, 1.0F, 1.0F); // playSound
    }

    // resetTask
    public void d() {
        vex.a(false); // setCharging
    }

    // tick
    public void e() {
        EntityLiving target = vex.getGoalTarget();
        if (vex.getBoundingBox().c(target.getBoundingBox())) { // intersects
            vex.B(target); // attackEntityAsMob
            vex.a(false); // setCharging
            return;
        }
        if (vex.h(target) < 9.0D) { // getDistanceSq
            Vec3D eye = target.i(1.0F); // getEyePosition
            vex.getControllerMove().a(eye.x, eye.y, eye.z, 1.0D); // setMoveTo
        }
    }
}
