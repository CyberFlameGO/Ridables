package net.pl3x.bukkit.ridables.entity.ai.goal.evoker;

import net.minecraft.server.v1_13_R2.EntityIllagerWizard;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.monster.RidableEvoker;

public class AIEvokerCastingSpell extends PathfinderGoal {
    private RidableEvoker evoker;

    public AIEvokerCastingSpell(RidableEvoker evoker) {
        this.evoker = evoker;
        a(3); // setMutexBits
    }

    // shouldExecute
    @Override
    public boolean a() {
        return evoker.getRider() == null && evoker.getSpellTicks() > 0;
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return a();
    }

    // startExecuting
    @Override
    public void c() {
        super.c();
        evoker.getNavigation().q(); // clearPath
    }

    // resetTask
    @Override
    public void d() {
        super.d();
        evoker.setSpell(EntityIllagerWizard.Spell.NONE);
    }

    // tick
    @Override
    public void e() {
        if (evoker.getGoalTarget() != null) {
            evoker.getControllerLook().a(evoker.getGoalTarget(), (float) evoker.getHorizontalFaceSpeed(), (float) evoker.getVerticalFaceSpeed());
        } else if (evoker.getWololoTarget() != null) {
            evoker.getControllerLook().a(evoker.getWololoTarget(), (float) evoker.getHorizontalFaceSpeed(), (float) evoker.getVerticalFaceSpeed());
        }
    }
}
