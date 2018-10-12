package net.pl3x.bukkit.ridables.entity.ai.rabbit;

import net.minecraft.server.v1_13_R2.Block;
import net.minecraft.server.v1_13_R2.BlockCarrots;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.IWorldReader;
import net.minecraft.server.v1_13_R2.PathfinderGoalGotoTarget;
import net.pl3x.bukkit.ridables.entity.RidableRabbit;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;

public class AIRabbitEatCarrots extends PathfinderGoalGotoTarget {
    private final RidableRabbit rabbit;
    private boolean wantsToRaid;
    private boolean canRaid;

    public AIRabbitEatCarrots(RidableRabbit rabbit) {
        super(rabbit, 0.7D, 16);
        this.rabbit = rabbit;
    }

    // shouldExecute
    public boolean a() {
        if (rabbit.getRider() != null) {
            return false;
        }
        if (b <= 0) { // runDelay
            if (!rabbit.world.getGameRules().getBoolean("mobGriefing")) {
                return false;
            }
            canRaid = false;
            wantsToRaid = rabbit.isCarrotEaten();
            wantsToRaid = true;
        }
        return super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return canRaid && rabbit.getRider() == null && super.b();
    }

    // tick
    public void e() {
        super.e();
        rabbit.getControllerLook().a((double) d.getX() + 0.5D, (double) (d.getY() + 1), (double) d.getZ() + 0.5D, 10.0F, (float) rabbit.K()); // setLookPosition destinationBlock getVerticalFaceSpeed
        if (!k()) { // isAboveDestination
            return;
        }
        if (canRaid) {
            BlockPosition pos = d.up(); // destinationBlock
            IBlockData state = rabbit.world.getType(pos);
            if (state.getBlock() instanceof BlockCarrots) {
                Integer age = state.get(BlockCarrots.AGE);
                if (age == 0) {
                    if (CraftEventFactory.callEntityChangeBlockEvent(rabbit, pos, Blocks.AIR.getBlockData()).isCancelled()) {
                        return;
                    }
                    rabbit.world.setTypeAndData(pos, Blocks.AIR.getBlockData(), 2);
                    rabbit.world.setAir(pos, true);
                } else {
                    if (CraftEventFactory.callEntityChangeBlockEvent(rabbit, pos, state.set(BlockCarrots.AGE, age - 1)).isCancelled()) {
                        return;
                    }
                    rabbit.world.setTypeAndData(pos, state.set(BlockCarrots.AGE, age - 1), 2);
                    rabbit.world.triggerEffect(2001, pos, Block.getCombinedId(state));
                }
                rabbit.setCarrotTicks(40);
            }
        }
        canRaid = false;
        b = 10; // runDelay
    }

    // shouldMoveTo
    protected boolean a(IWorldReader world, BlockPosition pos) {
        if (wantsToRaid && !canRaid) {
            Block block = world.getType(pos).getBlock();
            if (block == Blocks.FARMLAND) {
                IBlockData state = world.getType(pos.up());
                block = state.getBlock();
                if (block instanceof BlockCarrots && ((BlockCarrots) block).w(state)) { // isMaxAge
                    canRaid = true;
                    return true;
                }
            }
        }
        return false;
    }
}
