package net.pl3x.bukkit.ridables.entity.ai.skeleton;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.EntitySkeletonAbstract;
import net.minecraft.server.v1_13_R2.PathfinderGoalMeleeAttack;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AISkeletonMeleeAttack extends PathfinderGoalMeleeAttack {
    private final RidableEntity ridable;
    private final EntitySkeletonAbstract skeleton;

    public AISkeletonMeleeAttack(RidableEntity ridable, double speed, boolean useLongMemory) {
        super((EntityCreature) ridable, speed, useLongMemory);
        this.ridable = ridable;
        this.skeleton = (EntitySkeletonAbstract) ridable;
    }

    // shouldExecute
    public boolean a() {
        return ridable.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return ridable.getRider() == null && super.b();
    }

    // startExecuting
    public void c() {
        super.c();
        skeleton.s(true); // setSwingingArms
    }

    // resetTask
    public void d() {
        super.d();
        skeleton.s(false); // setSwingingArms
    }
}
