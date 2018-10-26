package net.pl3x.bukkit.ridables.entity.ai.goal.silverfish;

import net.minecraft.server.v1_13_R2.BlockMonsterEggs;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.EnumDirection;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomStroll;
import net.pl3x.bukkit.ridables.entity.monster.RidableSilverfish;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;

public class AISilverfishHideInBlock extends PathfinderGoalRandomStroll {
    private final RidableSilverfish silverfish;
    private EnumDirection facing;
    private boolean doMerge;

    public AISilverfishHideInBlock(RidableSilverfish silverfish) {
        super(silverfish, 1.0D, 10);
        this.silverfish = silverfish;
    }

    // shouldExecute
    public boolean a() {
        if (silverfish.getRider() != null) {
            return false;
        }
        if (silverfish.getGoalTarget() != null) {
            return false;
        }
        if (!silverfish.getNavigation().p()) { // noPath
            return false;
        }
        if (silverfish.world.getGameRules().getBoolean("mobGriefing") && silverfish.getRandom().nextInt(10) == 0) {
            facing = EnumDirection.a(silverfish.getRandom()); // random
            if (BlockMonsterEggs.k(silverfish.world.getType((new BlockPosition(silverfish.locX, silverfish.locY + 0.5D, silverfish.locZ)).shift(facing)))) { // canContainSilverfish
                doMerge = true;
                return true;
            }
        }
        doMerge = false;
        return super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return !doMerge && silverfish.getRider() == null && super.b();
    }

    // startExecuting
    public void c() {
        if (!doMerge) {
            super.c();
            return;
        }
        BlockPosition pos = (new BlockPosition(silverfish.locX, silverfish.locY + 0.5D, silverfish.locZ)).shift(facing);
        IBlockData state = silverfish.world.getType(pos);
        if (!BlockMonsterEggs.k(state)) { // canContainSilverfish
            return;
        }
        IBlockData infestedState = BlockMonsterEggs.f(state.getBlock()); // infest
        if (CraftEventFactory.callEntityChangeBlockEvent(silverfish, pos, infestedState).isCancelled()) {
            return;
        }
        silverfish.world.setTypeAndData(pos, infestedState, 3);
        silverfish.doSpawnEffect();
        silverfish.die();
    }
}
