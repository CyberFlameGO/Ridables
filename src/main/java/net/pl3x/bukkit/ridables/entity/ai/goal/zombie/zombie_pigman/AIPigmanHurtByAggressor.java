package net.pl3x.bukkit.ridables.entity.ai.goal.zombie.zombie_pigman;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPigZombie;
import net.minecraft.server.v1_13_R2.PathfinderGoalHurtByTarget;
import net.pl3x.bukkit.ridables.entity.monster.zombie.RidableZombiePigman;

public class AIPigmanHurtByAggressor extends PathfinderGoalHurtByTarget {
    private final RidableZombiePigman pigman;

    public AIPigmanHurtByAggressor(RidableZombiePigman pigman) {
        super(pigman, true);
        this.pigman = pigman;
    }

    // setEntityAttackTarget
    @Override
    protected void a(EntityCreature entitycreature, EntityLiving entityliving) {
        super.a(entitycreature, entityliving);
        if (entitycreature instanceof EntityPigZombie) {
            pigman.becomeAngryAt(entityliving);
        }
    }

    // shouldExecute
    @Override
    public boolean a() {
        return pigman.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return pigman.getRider() == null && super.b();
    }
}
