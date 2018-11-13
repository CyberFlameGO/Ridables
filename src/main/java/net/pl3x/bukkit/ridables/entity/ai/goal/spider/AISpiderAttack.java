package net.pl3x.bukkit.ridables.entity.ai.goal.spider;

import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntitySpider;
import net.minecraft.server.v1_13_R2.PathfinderGoalMeleeAttack;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AISpiderAttack extends PathfinderGoalMeleeAttack {
    private final RidableEntity spider;

    public AISpiderAttack(RidableEntity spider) {
        super((EntitySpider) spider, 1.0D, true);
        this.spider = spider;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return spider.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        if (spider.getRider() != null) {
            return false;
        }
        if (a.az() >= 0.5F && a.getRandom().nextInt(100) == 0) {
            a.setGoalTarget(null);
            return false;
        }
        return super.b();
    }

    // getAttackReach
    @Override
    protected double a(EntityLiving target) {
        return (double) (4.0F + target.width);
    }
}
