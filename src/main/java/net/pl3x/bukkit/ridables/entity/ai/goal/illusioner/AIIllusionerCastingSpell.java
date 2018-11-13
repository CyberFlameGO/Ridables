package net.pl3x.bukkit.ridables.entity.ai.goal.illusioner;

import net.minecraft.server.v1_13_R2.EntityIllagerWizard;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.monster.RidableIllusioner;

public class AIIllusionerCastingSpell extends PathfinderGoal {
    private RidableIllusioner illusioner;

    public AIIllusionerCastingSpell(RidableIllusioner illusioner) {
        this.illusioner = illusioner;
        a(3); // setMutexBits
    }

    // shouldExecute
    @Override
    public boolean a() {
        return illusioner.getRider() == null && illusioner.getSpellTicks() > 0;
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
        illusioner.getNavigation().q(); // clearPath
    }

    // resetTask
    @Override
    public void d() {
        super.d();
        illusioner.setSpell(EntityIllagerWizard.Spell.NONE);
    }

    // tick
    @Override
    public void e() {
        if (illusioner.getGoalTarget() != null) {
            illusioner.getControllerLook().a(illusioner.getGoalTarget(), (float) illusioner.L(), (float) illusioner.K());
        }
    }
}
