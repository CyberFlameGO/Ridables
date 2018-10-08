package net.pl3x.bukkit.ridables.entity.ai.fish.pufferfish;

import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EnumMonsterType;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.RidablePufferFish;

import java.util.List;
import java.util.function.Predicate;

public class AIPuffUp extends PathfinderGoal {
    public static final Predicate<EntityLiving> ENEMY_MATCHER = (entityliving) -> {
        if (entityliving == null) {
            return false;
        }
        if (!(entityliving instanceof EntityHuman) || !((EntityHuman) entityliving).isSpectator() && !((EntityHuman) entityliving).u()) {
            return false;
        }
        return entityliving.getMonsterType() != EnumMonsterType.e;
    };
    private final RidablePufferFish pufferfish;

    public AIPuffUp(RidablePufferFish pufferfish) {
        this.pufferfish = pufferfish;
    }

    // shouldExecute
    public boolean a() {
        if (pufferfish.getRider() != null) {
            return false;
        }
        List list = pufferfish.world.a(EntityLiving.class, pufferfish.getBoundingBox().g(2.0D), ENEMY_MATCHER);
        return !list.isEmpty();
    }

    // shouldContinueExecuting
    public boolean b() {
        if (pufferfish.getRider() != null) {
            return false;
        }
        List list = pufferfish.world.a(EntityLiving.class, pufferfish.getBoundingBox().g(2.0D), ENEMY_MATCHER);
        return !list.isEmpty();
    }

    // startExecuting
    public void c() {
        pufferfish.setPuffTimer(1);
        pufferfish.setDeflateTimer(0);
    }

    // resetTask
    public void d() {
        pufferfish.setPuffTimer(0);
    }
}
