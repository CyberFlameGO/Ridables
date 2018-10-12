package net.pl3x.bukkit.ridables.entity.ai.turtle;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.BlockTurtleEgg;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.IWorldReader;
import net.minecraft.server.v1_13_R2.PathfinderGoalGotoTarget;
import net.minecraft.server.v1_13_R2.SoundCategory;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.entity.RidableTurtle;
import net.pl3x.bukkit.ridables.hook.Paper;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;

public class AITurtleLayEgg extends PathfinderGoalGotoTarget {
    private final RidableTurtle turtle;

    public AITurtleLayEgg(RidableTurtle turtle, double d0) {
        super(turtle, d0, 16);
        this.turtle = turtle;
    }

    // shouldExecute
    public boolean a() {
        if (turtle.getRider() != null) {
            return false;
        }
        if (!turtle.hasEgg()) {
            return false;
        }
        if (turtle.c(turtle.getHome()) >= 81) { // getDistanceSq
            return false;
        }
        return super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        if (turtle.getRider() != null) {
            return false;
        }
        if (!turtle.hasEgg()) {
            return false;
        }
        if (turtle.c(turtle.getHome()) >= 81) { // getDistanceSq
            return false;
        }
        return super.b();
    }

    public void e() {
        super.e();
        BlockPosition pos = new BlockPosition(turtle);
        if (turtle.isInWater() || k()) {
            return;
        }
        int diggingTicks = turtle.getDiggingTicks();
        if (diggingTicks < 1) {
            turtle.setDigging(true);
        } else if (diggingTicks > 200) {
            int count = turtle.getRandom().nextInt(4) + 1;
            if (Ridables.isPaper()) {
                count = Paper.CallTurtleLayEggEvent(turtle, d.up(), count);
            }
            if (count > 0) {
                if (!CraftEventFactory.callEntityChangeBlockEvent(turtle, d.up(), Blocks.TURTLE_EGG.getBlockData().set(BlockTurtleEgg.b, count)).isCancelled()) {
                    turtle.world.a(null, pos, SoundEffects.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3F, 0.9F + turtle.world.random.nextFloat() * 0.2F);
                    turtle.world.setTypeAndData(d.up(), Blocks.TURTLE_EGG.getBlockData().set(BlockTurtleEgg.b, count), 3);
                }
            }
            turtle.setHasEgg(false);
            turtle.setDigging(false);
            turtle.d(600);
        }
        if (turtle.isDigging()) {
            turtle.setDiggingTicks(++diggingTicks);
        }
    }

    // shouldMoveTo
    protected boolean a(IWorldReader world, BlockPosition pos) {
        if (!world.isEmpty(pos.up())) {
            return false;
        }
        return world.getType(pos).getBlock() == Blocks.SAND;
    }
}
