package net.pl3x.bukkit.ridables.entity.ai.horse.llama;

import net.minecraft.server.v1_13_R2.EntityWolf;
import net.minecraft.server.v1_13_R2.PathfinderGoalNearestAttackableTarget;
import net.pl3x.bukkit.ridables.entity.RidableLlama;

public class AILlamaDefendTarget extends PathfinderGoalNearestAttackableTarget<EntityWolf> {
    private final RidableLlama llama;

    public AILlamaDefendTarget(RidableLlama llama) {
        super(llama, EntityWolf.class, 16, false, true, null);
        this.llama = llama;
    }

    // shouldExecute
    public boolean a() {
        if (llama.getRider() == null && super.a() && d != null && !d.isTamed()) { // targetEntity
            return true;
        }
        llama.setGoalTarget(null);
        return false;
    }

    // shouldContinueExecuting
    public boolean b() {
        return llama.getRider() == null && super.b();
    }

    // getTargetDistance
    protected double i() {
        return super.i() * 0.25D;
    }
}
