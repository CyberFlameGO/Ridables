package net.pl3x.bukkit.ridables.entity.ai.guardian;

import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EnumDifficulty;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.RidableElderGuardian;
import net.pl3x.bukkit.ridables.entity.RidableGuardian;

public class AIGuardianAttack extends PathfinderGoal {
    private final RidableGuardian guardian;
    private int timer;
    private final boolean isElder;

    public AIGuardianAttack(RidableGuardian guardian) {
        this.guardian = guardian;
        this.isElder = guardian instanceof RidableElderGuardian;
        a(3); // setMutexBits
    }

    // shouldExecute
    public boolean a() {
        if (guardian.getRider() != null) {
            return false;
        }
        EntityLiving target = guardian.getGoalTarget();
        return target != null && target.isAlive();
    }

    // shouldContinueExecuting
    public boolean b() {
        return a() && (isElder || guardian.h(guardian.getGoalTarget()) > 9.0D);
    }

    // startExecuting
    public void c() {
        timer = -10;
        guardian.getNavigation().q(); // clearPath
        guardian.getControllerLook().a(guardian.getGoalTarget(), 90.0F, 90.0F);
        guardian.impulse = true; // isAirBorne
    }

    // resetTask
    public void d() {
        guardian.setTargetedEntity(0);
        guardian.setGoalTarget(null);
        guardian.goalRandomStroll.i(); // makeUpdate
    }

    // tick
    public void e() {
        EntityLiving target = guardian.getGoalTarget();
        guardian.getNavigation().q(); // clearPath
        guardian.getControllerLook().a(target, 90.0F, 90.0F); // setLookPositionWithEntity
        if (!guardian.hasLineOfSight(target)) {
            guardian.setGoalTarget(null);
            return;
        }
        ++timer;
        if (timer == 0) {
            guardian.setTargetedEntity(target.getId());
            guardian.world.broadcastEntityEffect(guardian, (byte) 21);
        } else if (timer >= guardian.l()) { // getAttackDuration
            float damage = 1.0F;
            if (guardian.world.getDifficulty() == EnumDifficulty.HARD) {
                damage += 2.0F;
            }
            if (isElder) {
                damage += 2.0F;
            }
            target.damageEntity(DamageSource.c(guardian, guardian), damage);
            target.damageEntity(DamageSource.mobAttack(guardian), (float) guardian.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue());
            guardian.setGoalTarget(null);
        }
    }
}
