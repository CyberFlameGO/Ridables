package net.pl3x.bukkit.ridables.entity.ai.goal.zombie.drowned;

import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.PathfinderGoalArrowAttack;
import net.pl3x.bukkit.ridables.entity.monster.zombie.RidableDrowned;

public class AIDrownedTridentAttack extends PathfinderGoalArrowAttack {
    private final RidableDrowned drowned;

    public AIDrownedTridentAttack(RidableDrowned drowned, double speed, int maxAttackTime, float maxAttackDistance) {
        super(drowned, speed, maxAttackTime, maxAttackDistance);
        this.drowned = drowned;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return drowned.getRider() == null && super.a() && drowned.getItemInMainHand().getItem() == Items.TRIDENT;
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return drowned.getRider() == null && super.b();
    }

    // startExecuting
    @Override
    public void c() {
        super.c();
        drowned.s(true); // setArmsRaised
    }

    // resetTask
    @Override
    public void d() {
        super.d();
        drowned.s(false); // setArmsRaised
    }
}
