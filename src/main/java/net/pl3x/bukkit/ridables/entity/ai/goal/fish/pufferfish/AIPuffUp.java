package net.pl3x.bukkit.ridables.entity.ai.goal.fish.pufferfish;

import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.animal.fish.RidablePufferFish;

import java.util.List;

public class AIPuffUp extends PathfinderGoal {
    private final RidablePufferFish pufferfish;

    public AIPuffUp(RidablePufferFish pufferfish) {
        this.pufferfish = pufferfish;
    }

    // shouldExecute
    @Override
    public boolean a() {
        if (pufferfish.getRider() != null) {
            return false;
        }
        List list = pufferfish.world.a(EntityLiving.class,
                pufferfish.getBoundingBox().g(RidablePufferFish.CONFIG.AI_PUFF_UP_RADIUS), // grow
                RidablePufferFish.ENEMY_MATCHER);
        return !list.isEmpty();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        if (pufferfish.getRider() != null) {
            return false;
        }
        List list = pufferfish.world.a(EntityLiving.class,
                pufferfish.getBoundingBox().g(RidablePufferFish.CONFIG.AI_PUFF_UP_RADIUS), // grow
                RidablePufferFish.ENEMY_MATCHER);
        return !list.isEmpty();
    }

    // startExecuting
    @Override
    public void c() {
        pufferfish.setPuffTimer(1);
        pufferfish.setDeflateTimer(0);
    }

    // resetTask
    @Override
    public void d() {
        pufferfish.setPuffTimer(0);
    }
}
