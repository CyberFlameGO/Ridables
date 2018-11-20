package net.pl3x.bukkit.ridables.entity.ai.goal.silverfish;

import net.minecraft.server.v1_13_R2.Block;
import net.minecraft.server.v1_13_R2.BlockMonsterEggs;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.monster.RidableSilverfish;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;

public class AISilverfishWakeOthers extends PathfinderGoal {
    private final RidableSilverfish silverfish;
    private int lookForFriends;

    public AISilverfishWakeOthers(RidableSilverfish silverfish) {
        this.silverfish = silverfish;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return silverfish.getRider() == null && lookForFriends > 0;
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return a();
    }

    // tick
    @Override
    public void e() {
        --lookForFriends;
        if (lookForFriends > 0) {
            return;
        }
        BlockPosition originPos = new BlockPosition(silverfish);
        for (int y = 0; y <= 5 && y >= -5; y = (y <= 0 ? 1 : 0) - y) {
            for (int x = 0; x <= 10 && x >= -10; x = (x <= 0 ? 1 : 0) - x) {
                for (int z = 0; z <= 10 && z >= -10; z = (z <= 0 ? 1 : 0) - z) {
                    BlockPosition pos = originPos.a(x, y, z);
                    Block block = silverfish.world.getType(pos).getBlock();
                    if (block instanceof BlockMonsterEggs) {
                        if (CraftEventFactory.callEntityChangeBlockEvent(silverfish, pos, Blocks.AIR.getBlockData()).isCancelled()) {
                            continue;
                        }
                        if (silverfish.world.getGameRules().getBoolean("mobGriefing")) {
                            silverfish.world.setAir(pos, true);
                        } else {
                            silverfish.world.setTypeAndData(pos, ((BlockMonsterEggs) block).d().getBlockData(), 3); // getMimickedBlock
                        }
                        if (silverfish.getRandom().nextBoolean()) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public void notifyHurt() {
        if (lookForFriends == 0) {
            lookForFriends = 20;
        }
    }
}
