package net.pl3x.bukkit.ridables.entity.ai.illusioner;

import net.minecraft.server.v1_13_R2.EntityIllagerWizard;
import net.minecraft.server.v1_13_R2.MobEffect;
import net.minecraft.server.v1_13_R2.MobEffects;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.pl3x.bukkit.ridables.entity.RidableIllusioner;

public class AIIllusionerMirrorSpell extends PathfinderGoal {
    private RidableIllusioner illusioner;
    private int spellWarmup;
    private int spellCooldown;

    public AIIllusionerMirrorSpell(RidableIllusioner illusioner) {
        this.illusioner = illusioner;
    }

    // shouldExecute
    public boolean a() {
        if (illusioner.getGoalTarget() == null) {
            return false;
        }
        if (illusioner.dA()) {
            return false;
        }
        if (illusioner.ticksLived < spellCooldown) {
            return false;
        }
        return !illusioner.hasEffect(MobEffects.INVISIBILITY);
    }

    // shouldContinueExecuting
    public boolean b() {
        return illusioner.getRider() == null && illusioner.getGoalTarget() != null && spellWarmup > 0;
    }

    // startExecuting
    public void c() {
        spellWarmup = 20;
        illusioner.setSpellTicks(20);
        spellCooldown = illusioner.ticksLived + 340;
        illusioner.a(SoundEffects.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1.0F, 1.0F);
        illusioner.setSpell(EntityIllagerWizard.Spell.DISAPPEAR);
    }

    // tick
    public void e() {
        --spellWarmup;
        if (spellWarmup == 0) {
            castSpell();
            illusioner.a(SoundEffects.ENTITY_ILLUSIONER_CAST_SPELL, 1.0F, 1.0F);
        }
    }

    private void castSpell() {
        illusioner.addEffect(new MobEffect(MobEffects.INVISIBILITY, 1200));
    }
}
