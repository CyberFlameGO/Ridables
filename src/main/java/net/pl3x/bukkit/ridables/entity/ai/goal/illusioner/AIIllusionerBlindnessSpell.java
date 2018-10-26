package net.pl3x.bukkit.ridables.entity.ai.goal.illusioner;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.EntityIllagerWizard;
import net.minecraft.server.v1_13_R2.EnumDifficulty;
import net.minecraft.server.v1_13_R2.MobEffect;
import net.minecraft.server.v1_13_R2.MobEffects;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.pl3x.bukkit.ridables.entity.monster.RidableIllusioner;

public class AIIllusionerBlindnessSpell extends PathfinderGoal {
    private RidableIllusioner illusioner;
    private int spellWarmup;
    private int spellCooldown;
    private int lastTargetId;

    public AIIllusionerBlindnessSpell(RidableIllusioner illusioner) {
        this.illusioner = illusioner;
    }

    // shouldExecute
    public boolean a() {
        if (illusioner.getRider() != null) {
            return false;
        }
        if (illusioner.getGoalTarget() == null) {
            return false;
        }
        if (illusioner.dA()) {
            return false;
        }
        if (illusioner.ticksLived < spellCooldown) {
            return false;
        }
        if (illusioner.getGoalTarget().getId() == lastTargetId) {
            return false;
        }
        return illusioner.world.getDamageScaler(new BlockPosition(illusioner)).a((float) EnumDifficulty.NORMAL.ordinal());
    }

    // shouldContinueExecuting
    public boolean b() {
        return illusioner.getGoalTarget() != null && spellWarmup > 0;
    }

    // startExecuting
    public void c() {
        spellWarmup = 20;
        illusioner.setSpellTicks(20);
        spellCooldown = illusioner.ticksLived + 180;
        illusioner.a(SoundEffects.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1.0F, 1.0F);
        illusioner.setSpell(EntityIllagerWizard.Spell.BLINDNESS);
        lastTargetId = illusioner.getGoalTarget().getId();
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
        illusioner.getGoalTarget().addEffect(new MobEffect(MobEffects.BLINDNESS, 400));
    }
}
