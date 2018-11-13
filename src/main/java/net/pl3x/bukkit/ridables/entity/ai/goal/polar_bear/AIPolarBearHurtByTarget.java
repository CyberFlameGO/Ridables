package net.pl3x.bukkit.ridables.entity.ai.goal.polar_bear;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPolarBear;
import net.minecraft.server.v1_13_R2.PathfinderGoalHurtByTarget;
import net.pl3x.bukkit.ridables.entity.animal.RidablePolarBear;

public class AIPolarBearHurtByTarget extends PathfinderGoalHurtByTarget {
    private final RidablePolarBear bear;

    public AIPolarBearHurtByTarget(RidablePolarBear bear) {
        super(bear, false);
        this.bear = bear;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return bear.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return bear.getRider() == null && super.b();
    }

    // startExecuting
    @Override
    public void c() {
        super.c();
        if (bear.isBaby()) {
            g(); // alertOthers
            d(); // resetTask
        }
    }

    // setEntityAttackTarget
    @Override
    protected void a(EntityCreature entity, EntityLiving target) {
        if (entity instanceof EntityPolarBear && !entity.isBaby()) {
            super.a(entity, target);
        }
    }
}
