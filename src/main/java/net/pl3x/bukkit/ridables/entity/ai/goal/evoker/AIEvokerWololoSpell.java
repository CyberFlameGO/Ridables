package net.pl3x.bukkit.ridables.entity.ai.goal.evoker;

import net.minecraft.server.v1_13_R2.EntityIllagerWizard;
import net.minecraft.server.v1_13_R2.EntitySheep;
import net.minecraft.server.v1_13_R2.EnumColor;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.pl3x.bukkit.ridables.entity.monster.RidableEvoker;

import java.util.List;
import java.util.function.Predicate;

public class AIEvokerWololoSpell extends PathfinderGoal {
    private RidableEvoker evoker;
    private int spellWarmup;
    private int spellCooldown;

    private final Predicate<EntitySheep> wololoSelector = (sheep) -> sheep.getColor() == EnumColor.BLUE;

    public AIEvokerWololoSpell(RidableEvoker evoker) {
        this.evoker = evoker;
    }

    // shouldExecute
    @Override
    public boolean a() {
        if (evoker.getRider() != null) {
            return false;
        }
        if (evoker.getGoalTarget() != null) {
            return false;
        }
        if (evoker.dA()) { // isSpellCasting
            return false;
        }
        if (evoker.ticksLived < spellCooldown) {
            return false;
        }
        if (!evoker.world.getGameRules().getBoolean("mobGriefing")) {
            return false;
        }
        List<EntitySheep> list = evoker.world.a(EntitySheep.class, evoker.getBoundingBox().grow(16.0D, 4.0D, 16.0D), wololoSelector);
        if (list.isEmpty()) {
            return false;
        }
        evoker.setWololoTarget(list.get(evoker.getRandom().nextInt(list.size())));
        return true;
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return evoker.getRider() == null && evoker.getWololoTarget() != null && spellWarmup > 0;
    }

    // startExecuting
    @Override
    public void c() {
        spellWarmup = 40;
        evoker.setSpellTicks(60);
        spellCooldown = evoker.ticksLived + 140;
        evoker.a(SoundEffects.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.0F);
        evoker.setSpell(EntityIllagerWizard.Spell.WOLOLO);
    }

    // resetTask
    @Override
    public void d() {
        super.d();
        evoker.setWololoTarget(null);
    }

    // tick
    @Override
    public void e() {
        --spellWarmup;
        if (spellWarmup == 0) {
            castSpell();
            evoker.a(SoundEffects.ENTITY_EVOKER_CAST_SPELL, 1.0F, 1.0F);
        }
    }

    private void castSpell() {
        EntitySheep sheep = evoker.getWololoTarget();
        if (sheep != null && sheep.isAlive()) {
            sheep.setColor(EnumColor.RED);
        }
    }
}
