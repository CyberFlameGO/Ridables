package net.pl3x.bukkit.ridables.entity.ai.slime;

import net.minecraft.server.v1_13_R2.MobEffects;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.entity.RidableSlime;
import net.pl3x.bukkit.ridables.util.PaperOnly;

public class AISlimeFaceRandom extends PathfinderGoal {
    private final RidableSlime slime;
    private float chosenYaw;
    private int timer;

    public AISlimeFaceRandom(RidableSlime slime) {
        this.slime = slime;
        a(2); // setMutexBits
    }

    // shouldExecute
    public boolean a() {
        if (slime.getRider() != null) {
            return false;
        }
        if (Ridables.isPaper()) {
            if (!slime.canWander()) {
                return false;
            }
        }
        return slime.getGoalTarget() == null && (slime.onGround || slime.isInWater() || slime.ax() || slime.hasEffect(MobEffects.LEVITATION));
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }

    // tick
    public void e() {
        if (--timer <= 0) {
            timer = 40 + slime.getRandom().nextInt(60);
            if (Ridables.isPaper()) {
                if (!slime.canWander()) {
                    return;
                }
                float newYaw = PaperOnly.CallSlimeChangeDirectionEvent(slime, (float) this.slime.getRandom().nextInt(360));
                if (newYaw == Float.MIN_VALUE) {
                    return;
                }
                chosenYaw = newYaw;
            }
        }
        ((RidableSlime.SlimeWASDController) slime.getControllerMove()).setDirection(chosenYaw, false);
    }
}
