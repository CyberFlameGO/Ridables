package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.ControllerMove;
import net.minecraft.server.v1_13_R2.CriterionTriggers;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityTurtle;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.turtle.AITurtleBreed;
import net.pl3x.bukkit.ridables.entity.ai.turtle.AITurtleGoHome;
import net.pl3x.bukkit.ridables.entity.ai.turtle.AITurtleGoToWater;
import net.pl3x.bukkit.ridables.entity.ai.turtle.AITurtleLayEgg;
import net.pl3x.bukkit.ridables.entity.ai.turtle.AITurtlePanic;
import net.pl3x.bukkit.ridables.entity.ai.turtle.AITurtleTempt;
import net.pl3x.bukkit.ridables.entity.ai.turtle.AITurtleTravel;
import net.pl3x.bukkit.ridables.entity.ai.turtle.AITurtleWander;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class RidableTurtle extends EntityTurtle implements RidableEntity {
    private static Field homePosition;
    private static Field hasEgg;
    private static Field digging;
    private static Field travelPosition;
    private static Field goingHome;
    private static Field travelling;
    private static Field diggingTicks;

    static {
        try {
            homePosition = EntityTurtle.class.getDeclaredField("bD");
            homePosition.setAccessible(true);
            hasEgg = EntityTurtle.class.getDeclaredField("bE");
            hasEgg.setAccessible(true);
            digging = EntityTurtle.class.getDeclaredField("bG");
            digging.setAccessible(true);
            travelPosition = EntityTurtle.class.getDeclaredField("bH");
            travelPosition.setAccessible(true);
            goingHome = EntityTurtle.class.getDeclaredField("bI");
            goingHome.setAccessible(true);
            travelling = EntityTurtle.class.getDeclaredField("bJ");
            travelling.setAccessible(true);
            diggingTicks = EntityTurtle.class.getDeclaredField("bK");
            diggingTicks.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    private DataWatcherObject<BlockPosition> HOME_POSITION;
    private DataWatcherObject<Boolean> HAS_EGG;
    private DataWatcherObject<Boolean> DIGGING;
    private DataWatcherObject<BlockPosition> TRAVEL_POSITION;
    private DataWatcherObject<Boolean> GOING_HOME;
    private DataWatcherObject<Boolean> TRAVELLING;

    public RidableTurtle(World world) {
        super(world);

        try {
            HOME_POSITION = (DataWatcherObject<BlockPosition>) homePosition.get(this);
            HAS_EGG = (DataWatcherObject<Boolean>) hasEgg.get(this);
            DIGGING = (DataWatcherObject<Boolean>) digging.get(this);
            TRAVEL_POSITION = (DataWatcherObject<BlockPosition>) travelPosition.get(this);
            GOING_HOME = (DataWatcherObject<Boolean>) goingHome.get(this);
            TRAVELLING = (DataWatcherObject<Boolean>) travelling.get(this);
        } catch (IllegalAccessException ignore) {
        }

        moveController = new TurtleWASDController(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.TURTLE;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(0, new AITurtlePanic(this, 1.2D));
        goalSelector.a(1, new AITurtleBreed(this, 1.0D));
        goalSelector.a(1, new AITurtleLayEgg(this, 1.0D));
        goalSelector.a(2, new AITurtleTempt(this, 1.1D, Blocks.SEAGRASS.getItem()));
        goalSelector.a(3, new AITurtleGoToWater(this, 1.0D));
        goalSelector.a(4, new AITurtleGoHome(this, 1.0D));
        goalSelector.a(7, new AITurtleTravel(this, 1.0D));
        goalSelector.a(8, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(9, new AITurtleWander(this, 1.0D, 100));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return true;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.TURTLE_JUMP_POWER;
    }

    public BlockPosition getHome() {
        return datawatcher.get(HOME_POSITION);
    }

    public void setHome(BlockPosition pos) {
        datawatcher.set(HOME_POSITION, pos);
    }

    public boolean hasEgg() {
        return datawatcher.get(HAS_EGG);
    }

    public void setHasEgg(boolean hasEgg) {
        datawatcher.set(HAS_EGG, hasEgg);
    }

    public boolean isDigging() {
        return datawatcher.get(DIGGING);
    }

    public void setDigging(boolean digging) {
        setDiggingTicks(digging ? 1 : 0);
        datawatcher.set(DIGGING, digging);
    }

    public BlockPosition getTravelPos() {
        return datawatcher.get(TRAVEL_POSITION);
    }

    public void setTravelPos(BlockPosition pos) {
        datawatcher.set(TRAVEL_POSITION, pos);
    }

    public boolean isGoingHome() {
        return datawatcher.get(GOING_HOME);
    }

    public void setGoingHome(boolean goingHome) {
        datawatcher.set(GOING_HOME, goingHome);
    }

    public boolean isTravelling() {
        return datawatcher.get(TRAVELLING);
    }

    public void setTravelling(boolean travelling) {
        datawatcher.set(TRAVELLING, travelling);
    }

    public int getDiggingTicks() {
        try {
            return diggingTicks.getInt(this);
        } catch (IllegalAccessException ignore) {
        }
        return 0;
    }

    public void setDiggingTicks(int ticks) {
        try {
            diggingTicks.setInt(this, ticks);
        } catch (IllegalAccessException ignore) {
        }
    }

    protected void mobTick() {
        Q = Config.TURTLE_STEP_HEIGHT;
        if (isInWater() && getRider() != null) {
            motY += 0.005D;
        }
        super.mobTick();
    }

    public float getSpeed() {
        return isInWater() ? Config.TURTLE_SPEED_WATER : Config.TURTLE_SPEED_LAND;
    }

    // processInteract
    public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        if (itemstack.getItem() == Items.WATER_BUCKET && isAlive() && hasCollectPerm((Player) entityhuman.getBukkitEntity())) {
            a(SoundEffects.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
            itemstack.subtract(1);
            ItemStack bucket = CraftItemStack.asNMSCopy(Bucket.TURTLE.getItemStack());
            // TODO set custom name
            CriterionTriggers.j.a((EntityPlayer) entityhuman, bucket);
            if (itemstack.isEmpty()) {
                entityhuman.a(enumhand, bucket);
            } else if (!entityhuman.inventory.pickup(bucket)) {
                entityhuman.drop(bucket, false);
            }
            die();
            return true;
        }
        if (passengers.isEmpty() && !entityhuman.isPassenger() && !entityhuman.isSneaking()) {
            return enumhand == EnumHand.MAIN_HAND && tryRide(entityhuman, itemstack);
        }
        return passengers.isEmpty() && super.a(entityhuman, enumhand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    public RidableTurtle createChild(EntityAgeable entity) {
        return new RidableTurtle(world);
    }

    static class TurtleWASDController extends ControllerWASD {
        private final RidableTurtle turtle;

        public TurtleWASDController(RidableTurtle turtle) {
            super(turtle);
            this.turtle = turtle;
        }

        public void tick(EntityPlayer rider) {
            updateSpeed();
            super.tick(rider);
        }

        public void tick() {
            updateSpeed();
            if (h == ControllerMove.Operation.MOVE_TO && !turtle.getNavigation().p()) { // noPath
                double x = b - turtle.locX;
                double y = c - turtle.locY;
                double z = d - turtle.locZ;
                y /= (double) MathHelper.sqrt(x * x + y * y + z * z);
                turtle.aQ = turtle.yaw = a(turtle.yaw, (float) (MathHelper.c(z, x) * (double) (180F / (float) Math.PI)) - 90.0F, 90.0F); // limitAngle
                float speed = (float) (e * turtle.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
                turtle.o(turtle.cK() + (speed - turtle.cK()) * 0.125F); // setAIMoveSpeed getAIMoveSpeed
                turtle.motY += (double) turtle.cK() * y * 0.1D; // getAIMoveSpeed
            } else {
                turtle.o(0.0F); // setAIMoveSpeed
            }
        }

        private void updateSpeed() {
            if (turtle.isInWater()) {
                turtle.motY += 0.005D;
                if (turtle.c(turtle.getHome()) > 256.0D) { // getDistanceSq
                    turtle.o(Math.max(turtle.cK() / 2.0F, 0.08F)); // setAIMoveSpeed
                }
                if (turtle.isBaby()) {
                    turtle.o(Math.max(turtle.cK() / 3.0F, 0.06F)); // setAIMoveSpeed
                }
            } else if (turtle.onGround) {
                turtle.o(Math.max(turtle.cK() / 2.0F, 0.06F)); // setAIMoveSpeed
            }
        }
    }
}
