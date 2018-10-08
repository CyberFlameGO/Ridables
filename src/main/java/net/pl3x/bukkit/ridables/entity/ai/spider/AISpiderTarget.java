package net.pl3x.bukkit.ridables.entity.ai.spider;

import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntitySpider;
import net.minecraft.server.v1_13_R2.PathfinderGoalNearestAttackableTarget;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AISpiderTarget<T extends EntityLiving> extends PathfinderGoalNearestAttackableTarget<T> {
    private final RidableEntity spider;

    public AISpiderTarget(RidableEntity spider, Class<T> targetClass) {
        super((EntitySpider) spider, targetClass, true);
        this.spider = spider;
    }

    // shouldExecute
    public boolean a() {
        return spider.getRider() == null && e.az() < 0.5F && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return spider.getRider() == null && super.b();
    }
}
