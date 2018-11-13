package net.pl3x.bukkit.ridables.entity.ai.goal.rabbit;

import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.PathfinderGoalMeleeAttack;
import net.pl3x.bukkit.ridables.entity.animal.RidableRabbit;

public class AIKillerRabbitAttack extends PathfinderGoalMeleeAttack {
    private final RidableRabbit rabbit;

    public AIKillerRabbitAttack(RidableRabbit rabbit) {
        super(rabbit, 1.4D, true);
        this.rabbit = rabbit;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return rabbit.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return rabbit.getRider() == null && super.b();
    }

    // getAttackReachSqr
    @Override
    protected double a(EntityLiving target) {
        return (double) (4.0F + target.width);
    }
}
