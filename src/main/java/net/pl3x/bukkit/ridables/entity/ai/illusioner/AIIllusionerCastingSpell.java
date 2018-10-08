package net.pl3x.bukkit.ridables.entity.ai.illusioner;

import net.minecraft.server.v1_13_R2.EntityIllagerWizard;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.RidableIllusioner;

public class AIIllusionerCastingSpell extends PathfinderGoal {
    private RidableIllusioner illusioner;

    public AIIllusionerCastingSpell(RidableIllusioner illusioner) {
        this.illusioner = illusioner;
        a(3); // setMutexBits
    }

    // shouldExecute
    public boolean a() {
        return illusioner.getRider() == null && illusioner.getSpellTicks() > 0;
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }

    // startExecuting
    public void c() {
        super.c();
        illusioner.getNavigation().q(); // clearPath
    }

    // resetTask
    public void d() {
        super.d();
        illusioner.setSpell(EntityIllagerWizard.Spell.NONE);
    }

    // tick
    public void e() {
        if (illusioner.getGoalTarget() != null) {
            illusioner.getControllerLook().a(illusioner.getGoalTarget(), (float) illusioner.L(), (float) illusioner.K());
        }
    }
}
