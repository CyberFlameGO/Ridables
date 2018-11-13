package net.pl3x.bukkit.ridables.entity.ai.goal.turtle;

import net.minecraft.server.v1_13_R2.CriterionTriggers;
import net.minecraft.server.v1_13_R2.EntityExperienceOrb;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.PathfinderGoalBreed;
import net.minecraft.server.v1_13_R2.StatisticList;
import net.pl3x.bukkit.ridables.entity.animal.RidableTurtle;

public class AITurtleBreed extends PathfinderGoalBreed {
    private final RidableTurtle turtle;

    public AITurtleBreed(RidableTurtle turtle, double speed) {
        super(turtle, speed);
        this.turtle = turtle;
    }

    // shouldExecute
    @Override
    public boolean a() {
        if (turtle.getRider() != null) {
            return false;
        }
        if (turtle.hasEgg()) {
            return false;
        }
        return super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return turtle.getRider() == null && super.b();
    }

    // spawnBaby
    @Override
    protected void g() {
        EntityPlayer player = turtle.getBreedCause();
        if (player == null && partner.getBreedCause() != null) {
            player = partner.getBreedCause();
        }
        if (player != null) {
            player.a(StatisticList.ANIMALS_BRED); // addStat
            CriterionTriggers.o.a(player, turtle, partner, null); // BRED_ANIMALS.trigger
        }
        turtle.setHasEgg(true);
        turtle.resetLove();
        partner.resetLove();
        if (turtle.world.getGameRules().getBoolean("doMobLoot")) {
            turtle.world.addEntity(new EntityExperienceOrb(turtle.world, turtle.locX, turtle.locY, turtle.locZ, turtle.getRandom().nextInt(7) + 1));
        }
    }
}
