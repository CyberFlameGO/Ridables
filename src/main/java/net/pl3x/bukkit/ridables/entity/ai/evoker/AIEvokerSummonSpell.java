package net.pl3x.bukkit.ridables.entity.ai.evoker;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.EntityIllagerWizard;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.EntityVex;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.pl3x.bukkit.ridables.entity.RidableEvoker;

public class AIEvokerSummonSpell extends PathfinderGoal {
    private RidableEvoker evoker;
    private int spellWarmup;
    private int spellCooldown;

    public AIEvokerSummonSpell(RidableEvoker evoker) {
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
        if (evoker.ticksLived < spellCooldown) {
            return false;
        }
        return evoker.getRandom().nextInt(8) + 1 > evoker.world.a(EntityVex.class, evoker.getBoundingBox().g(16.0D)).size();
    }

    // shouldContinueExecuting
    public boolean b() {
        return evoker.getRider() == null && evoker.getGoalTarget() != null && spellWarmup > 0;
    }

    // startExecuting
    public void c() {
        spellWarmup = 20;
        evoker.setSpellTicks(100);
        spellCooldown = evoker.ticksLived + 340;
        evoker.a(SoundEffects.ENTITY_EVOKER_PREPARE_SUMMON, 1.0F, 1.0F);
        evoker.setSpell(EntityIllagerWizard.Spell.SUMMON_VEX);
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
        for (int i = 0; i < 3; ++i) {
            BlockPosition pos = (new BlockPosition(evoker)).a(-2 + evoker.getRandom().nextInt(5), 1, -2 + evoker.getRandom().nextInt(5));
            EntityVex vex = EntityTypes.VEX.a(evoker.world);
            vex.setPositionRotation(pos, 0.0F, 0.0F);
            vex.prepare(evoker.world.getDamageScaler(pos), null, null);
            vex.a(evoker); // setOwner
            vex.g(pos); // setBoundingOrigin
            vex.a(20 * (30 + evoker.getRandom().nextInt(90))); // setLimitedLife
            evoker.world.addEntity(vex);
        }
    }
}
