package net.pl3x.bukkit.ridables.entity.ai.evoker;

import net.minecraft.server.v1_13_R2.EntityIllagerWizard;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.RidableEvoker;

public class AIEvokerCastingSpell extends PathfinderGoal {
    private RidableEvoker evoker;

    public AIEvokerCastingSpell(RidableEvoker evoker) {
        this.evoker = evoker;
        a(3); // setMutexBits
    }

    // shouldExecute
    public boolean a() {
        return evoker.getRider() == null && evoker.getSpellTicks() > 0;
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }

    // startExecuting
    public void c() {
        super.c();
        evoker.getNavigation().q(); // clearPath
    }

    // resetTask
    public void d() {
        super.d();
        evoker.setSpell(EntityIllagerWizard.Spell.NONE);
    }

    // tick
    public void e() {
        if (evoker.getGoalTarget() != null) {
            evoker.getControllerLook().a(evoker.getGoalTarget(), (float) evoker.getHorizontalFaceSpeed(), (float) evoker.getVerticalFaceSpeed());
        } else if (evoker.getWololoTarget() != null) {
            evoker.getControllerLook().a(evoker.getWololoTarget(), (float) evoker.getHorizontalFaceSpeed(), (float) evoker.getVerticalFaceSpeed());
        }
    }
}
