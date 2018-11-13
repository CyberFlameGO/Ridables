package net.pl3x.bukkit.ridables.entity.ai.goal;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.Navigation;
import net.minecraft.server.v1_13_R2.PathEntity;
import net.minecraft.server.v1_13_R2.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_13_R2.RandomPositionGenerator;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.minecraft.server.v1_13_R2.Village;
import net.minecraft.server.v1_13_R2.VillageDoor;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

import java.util.List;

public class AIMoveThroughVillage extends PathfinderGoalMoveThroughVillage {
    private final RidableEntity ridable;
    private final EntityCreature entity;
    private final double speed;
    private PathEntity path;
    private VillageDoor doorInfo;
    private final boolean isNocturnal;
    private final List<VillageDoor> doorList = Lists.newArrayList();

    public AIMoveThroughVillage(RidableEntity ridable, double speed, boolean isNocturnal) {
        super((EntityCreature) ridable, speed, isNocturnal);
        this.ridable = ridable;
        this.entity = (EntityCreature) ridable;
        this.speed = speed;
        this.isNocturnal = isNocturnal;
        a(1); // setMutexBits
        if (!(entity.getNavigation() instanceof Navigation)) {
            throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
        }
    }

    // shouldExecute
    @Override
    public boolean a() {
        if (ridable.getRider() != null) {
            return false;
        }
        if (isNocturnal && entity.world.L()) { // isDayTime
            return false;
        }
        Village village = entity.world.af().getClosestVillage(new BlockPosition(entity), 0);
        if (village == null) {
            return false;
        }
        if (doorList.size() > 15) {
            doorList.remove(0);
        }
        doorInfo = findNearestDoor(village);
        if (doorInfo == null) {
            return false;
        }
        Navigation navigation = (Navigation) entity.getNavigation();
        boolean entersDoor = navigation.g(); // getEntersDoor
        navigation.a(false); // setBreakDoors
        path = navigation.b(doorInfo.d()); // getPathToPos
        navigation.a(entersDoor); // setBreakDoors
        if (path != null) {
            return true;
        }
        Vec3D vec = RandomPositionGenerator.a(entity, 10, 7, new Vec3D((double) doorInfo.d().getX(), (double) doorInfo.d().getY(), (double) doorInfo.d().getZ())); // findRandomTargetBlockTowards
        if (vec == null) {
            return false;
        }
        navigation.a(false); // setBreakDoors
        path = entity.getNavigation().a(vec.x, vec.y, vec.z); // getPathToXYZ
        navigation.a(entersDoor); // setBreakDoors
        return path != null;
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        if (ridable.getRider() != null) {
            return false;
        }
        if (entity.getNavigation().p()) { // noPath
            return false;
        }
        float radius = entity.width + 4.0F;
        return entity.c(doorInfo.d()) > (double) (radius * radius); // getDistanceSq getDoorBlockPos
    }

    // startExecuting
    @Override
    public void c() {
        entity.getNavigation().a(path, speed); // setPath
    }

    // resetTask
    @Override
    public void d() {
        if (entity.getNavigation().p() || entity.c(doorInfo.d()) < 16.0D) { // noPath getDistanceSq getDoorBlockPos
            doorList.add(doorInfo);
        }
    }

    // tick
    @Override
    public void e() {
    }

    private VillageDoor findNearestDoor(Village village) {
        VillageDoor villageDoor = null;
        int x = MathHelper.floor(entity.locX);
        int y = MathHelper.floor(entity.locY);
        int z = MathHelper.floor(entity.locZ);
        int curDistance = Integer.MAX_VALUE;
        for (VillageDoor door : village.f()) { // getVillageDoorInfo
            int distance = door.b(x, y, z); // getDistanceSq
            if (distance < curDistance && !doesDoorListContain(door)) {
                villageDoor = door;
                curDistance = distance;
            }
        }
        return villageDoor;
    }

    private boolean doesDoorListContain(VillageDoor villageDoor) {
        for (VillageDoor door : doorList) {
            if (villageDoor.d().equals(door.d())) { // getDoorBlockPos
                return true;
            }
        }
        return false;
    }
}
