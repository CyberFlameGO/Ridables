package net.pl3x.bukkit.ridables.entity.ai.goal.turtle;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.Item;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.animal.RidableTurtle;

import java.util.Set;

public class AITurtleTempt extends PathfinderGoal {
    private final RidableTurtle turtle;
    private final double speed;
    private EntityHuman target;
    private int timer;
    private final Set<Item> temptationItems;

    public AITurtleTempt(RidableTurtle turtle, double d0, Item item) {
        this.turtle = turtle;
        this.speed = d0;
        this.temptationItems = Sets.newHashSet(item);
        a(3); // setMutexBits
    }

    // shouldExecute
    @Override
    public boolean a() {
        if (timer > 0) {
            --timer;
            return false;
        }
        if (turtle.getRider() != null) {
            return false;
        }
        target = turtle.world.findNearbyPlayer(turtle, 10.0D);
        if (target == null) {
            return false;
        }
        return isTemptationItem(target.getItemInMainHand()) || isTemptationItem(target.getItemInOffHand());
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return a();
    }

    // resetTask
    @Override
    public void d() {
        target = null;
        turtle.getNavigation().q(); // clearPath
        timer = 100;
    }

    // tick
    @Override
    public void e() {
        turtle.getControllerLook().a(target, turtle.L() + 20, turtle.K()); // setLookPositionWithEntity getHorizontalFaceSpeed getVerticalFaceSpeed
        if (turtle.h(target) < 6.25D) { // getDistanceSq
            turtle.getNavigation().q(); // clearPath
        } else {
            turtle.getNavigation().a(target, speed); // tryMoveToEntityLiving
        }
    }

    private boolean isTemptationItem(ItemStack itemstack) {
        return temptationItems.contains(itemstack.getItem());
    }
}
