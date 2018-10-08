package net.pl3x.bukkit.ridables.entity.ai.dolphin;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.PathMode;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.RandomPositionGenerator;
import net.minecraft.server.v1_13_R2.TagsFluid;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.entity.RidableDolphin;

public class AIDolphinSwimToTreasure extends PathfinderGoal {
    private final RidableDolphin dolphin;
    private boolean foundTreasure;

    public AIDolphinSwimToTreasure(RidableDolphin dolphin) {
        this.dolphin = dolphin;
        a(3); // setMutexBits
    }

    // isInterruptible
    public boolean f() {
        return dolphin.getRider() != null;
    }

    // shouldExecute
    public boolean a() {
        return dolphin.getRider() == null && dolphin.dy() && dolphin.getAirTicks() >= 100;
    }

    // shouldContinueExecuting
    public boolean b() {
        if (dolphin.getRider() != null) {
            return false;
        }
        BlockPosition pos = dolphin.l(); // getTreasurePos
        return dolphin.c(new BlockPosition((double) pos.getX(), dolphin.locY, (double) pos.getZ())) > 16.0D && !foundTreasure && dolphin.getAirTicks() >= 100;
    }

    // startExecuting
    public void c() {
        foundTreasure = false;
        dolphin.getNavigation().q();
        World world = dolphin.world;
        BlockPosition pos = new BlockPosition(dolphin);
        String s = (double) world.random.nextFloat() >= 0.5D ? "Ocean_Ruin" : "Shipwreck";
        BlockPosition pos1 = world.a(s, pos, 50, false); // findNearestStructure
        if (pos1 == null) {
            BlockPosition pos2 = world.a(s.equals("Ocean_Ruin") ? "Shipwreck" : "Ocean_Ruin", pos, 50, false); // findNearestStructure
            if (pos2 == null) {
                foundTreasure = true;
                return;
            }
            dolphin.g(pos2); // setTreasurePos
        } else {
            dolphin.g(pos1); // setTreasurePos
        }
        world.broadcastEntityEffect(dolphin, (byte) 38);
    }

    // resetTask
    public void d() {
        BlockPosition pos = dolphin.l(); // getTreasurePos
        if (dolphin.c(new BlockPosition((double) pos.getX(), dolphin.locY, (double) pos.getZ())) <= 16.0D || foundTreasure) {
            dolphin.a(false); // setGotFish
        }
    }

    // tick
    public void e() {
        BlockPosition pos = dolphin.l(); // getTreasurePos
        if (dolphin.nearTargetPos() || dolphin.getNavigation().p()) {
            Vec3D vec3d = RandomPositionGenerator.a(dolphin, 16, 1, new Vec3D((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()), (double) ((float) Math.PI / 8F));
            if (vec3d == null) {
                vec3d = RandomPositionGenerator.a(dolphin, 8, 4, new Vec3D((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()));
            }
            if (vec3d != null) {
                BlockPosition pos1 = new BlockPosition(vec3d);
                if (!dolphin.world.b(pos1).a(TagsFluid.WATER) || !dolphin.world.getType(pos1).a(dolphin.world, pos1, PathMode.WATER)) {
                    vec3d = RandomPositionGenerator.a(dolphin, 8, 5, new Vec3D((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()));
                }
            }
            if (vec3d == null) {
                foundTreasure = true;
                return;
            }
            dolphin.getControllerLook().a(vec3d.x, vec3d.y, vec3d.z, (float) (dolphin.L() + 20), (float) dolphin.K());
            dolphin.getNavigation().a(vec3d.x, vec3d.y, vec3d.z, 1.3D);
            if (dolphin.world.random.nextInt(80) == 0) {
                dolphin.world.broadcastEntityEffect(dolphin, (byte) 38);
            }
        }
    }
}
