package net.pl3x.bukkit.ridables.entity.ai.goal.turtle;

import com.destroystokyo.paper.event.entity.TurtleLayEggEvent;
import com.destroystokyo.paper.event.entity.TurtleStartDiggingEvent;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.BlockTurtleEgg;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.IWorldReader;
import net.minecraft.server.v1_13_R2.MCUtil;
import net.minecraft.server.v1_13_R2.PathfinderGoalGotoTarget;
import net.minecraft.server.v1_13_R2.SoundCategory;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.pl3x.bukkit.ridables.entity.animal.RidableTurtle;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;
import org.bukkit.entity.Turtle;

public class AITurtleLayEgg extends PathfinderGoalGotoTarget {
    private final RidableTurtle turtle;

    public AITurtleLayEgg(RidableTurtle turtle, double d0) {
        super(turtle, d0, 16);
        this.turtle = turtle;
    }

    // shouldExecute
    @Override
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
    @Override
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

    // tick
    @Override
    public void e() {
        super.e();
        if (turtle.isInWater() || !k()) { // isAboveDestination
            return;
        }
        int diggingTicks = turtle.getDiggingTicks();
        if (diggingTicks < 1) {
            turtle.setDigging(new TurtleStartDiggingEvent((Turtle) turtle.getBukkitEntity(), MCUtil.toLocation(turtle.world, d)).callEvent()); // destinationBlock
        } else if (diggingTicks > 200) {
            int eggCount = turtle.getRandom().nextInt(4) + 1;
            TurtleLayEggEvent layEggEvent = new TurtleLayEggEvent((Turtle) turtle.getBukkitEntity(), MCUtil.toLocation(turtle.world, d.up()), eggCount); // destinationBlock
            eggCount = layEggEvent.callEvent() ? layEggEvent.getEggCount() : 0;
            if (eggCount > 0 && !CraftEventFactory.callEntityChangeBlockEvent(turtle, d.up(), Blocks.TURTLE_EGG.getBlockData().set(BlockTurtleEgg.b, eggCount)).isCancelled()) { // destinationBlock
                turtle.world.a(null, new BlockPosition(turtle), SoundEffects.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3F, 0.9F + turtle.world.random.nextFloat() * 0.2F); // playSound
                turtle.world.setTypeAndData(d.up(), Blocks.TURTLE_EGG.getBlockData().set(BlockTurtleEgg.b, eggCount), 3); // destinationBlock
            }
            turtle.setHasEgg(false);
            turtle.setDigging(false);
            turtle.d(600); // setInLoveTicks
        }
        if (turtle.isDigging()) {
            turtle.setDiggingTicks(++diggingTicks);
        }
    }

    // shouldMoveTo
    @Override
    protected boolean a(IWorldReader world, BlockPosition pos) {
        return world.isEmpty(pos.up()) && world.getType(pos).getBlock() == Blocks.SAND;
    }
}
