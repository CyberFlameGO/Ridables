package net.pl3x.bukkit.ridables.entity.ai.goal.shulker;

import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.monster.RidableShulker;

public class AIShulkerPeek extends PathfinderGoal {
    private final RidableShulker shulker;
    private int peekTime;

    public AIShulkerPeek(RidableShulker shulker) {
        this.shulker = shulker;
    }

    // shouldExecute
    public boolean a() {
        if (shulker.getRider() != null) {
            return false;
        }
        if (shulker.getGoalTarget() != null) {
            return false;
        }
        return shulker.getRandom().nextInt(40) == 0;
    }

    // shouldContinueExecuting
    public boolean b() {
        if (shulker.getRider() != null) {
            return false;
        }
        if (shulker.getGoalTarget() != null) {
            return false;
        }
        return peekTime > 0;
    }

    // startExecuting
    public void c() {
        peekTime = 20 * (1 + shulker.getRandom().nextInt(3));
        shulker.a(30); // updateArmorModifier
    }

    // resetTask
    public void d() {
        if (shulker.getGoalTarget() == null) {
            shulker.a(0); // updateArmorModifier
        }
    }

    // tick
    public void e() {
        --peekTime;
    }
}
