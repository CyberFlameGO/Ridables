package net.pl3x.bukkit.ridables.entity.ai.evoker;

import net.minecraft.server.v1_13_R2.EntityIllagerWizard;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.SoundEffect;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.pl3x.bukkit.ridables.entity.RidableEvoker;

public class AIEvokerFangsSpell extends PathfinderGoal {
    private RidableEvoker evoker;
    private int spellWarmup;
    private int spellCooldown;

    public AIEvokerFangsSpell(RidableEvoker evoker) {
        this.evoker = evoker;
    }

    // shouldExecute
    public boolean a() {
        if (evoker.getRider() != null) {
            return false;
        }
        if (evoker.getGoalTarget() == null) {
            return false;
        }
        if (evoker.dA()) {
            return false;
        }
        return evoker.ticksLived >= spellCooldown;
    }

    // shouldContinueExecuting
    public boolean b() {
        return evoker.getRider() == null && evoker.getGoalTarget() != null && spellWarmup > 0;
    }

    // startExecuting
    public void c() {
        spellWarmup = 20;
        evoker.setSpellTicks(40);
        spellCooldown = evoker.ticksLived + 100;
        SoundEffect soundeffect = SoundEffects.ENTITY_EVOKER_PREPARE_ATTACK;
        if (soundeffect != null) {
            evoker.a(soundeffect, 1.0F, 1.0F);
        }
        evoker.setSpell(EntityIllagerWizard.Spell.FANGS);
    }

    // tick
    public void e() {
        --spellWarmup;
        if (spellWarmup == 0) {
            castSpell();
            evoker.a(SoundEffects.ENTITY_EVOKER_CAST_SPELL, 1.0F, 1.0F);
        }
    }

    private void castSpell() {
        EntityLiving target = evoker.getGoalTarget();
        if (target != null) {
            evoker.castFangs(null, target.locX, target.locZ,
                    Math.min(target.locY, evoker.locY),
                    Math.max(target.locY, evoker.locY) + 1.0D,
                    evoker.h(target) < 9.0D); // getDistanceSq
        }
    }
}
