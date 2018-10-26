package net.pl3x.bukkit.ridables.entity.ai.goal.vindicator;

import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.PathfinderGoalNearestAttackableTarget;
import net.pl3x.bukkit.ridables.entity.monster.RidableVindicator;

public class AIVindicatorJohnnyAttack extends PathfinderGoalNearestAttackableTarget<EntityLiving> {
    private final RidableVindicator vindicator;

    public AIVindicatorJohnnyAttack(RidableVindicator vindicator) {
        super(vindicator, EntityLiving.class, 0, true, true, RidableVindicator.JOHNNY_SELECTOR);
        this.vindicator = vindicator;
    }

    // shouldExecute
    public boolean a() {
        return vindicator.getRider() == null && vindicator.isJohnnyMode() && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return vindicator.getRider() == null && super.b();
    }
}
