package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.ControllerMove;
import net.minecraft.server.v1_13_R2.CriterionTriggers;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityTurtle;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.configuration.mob.TurtleConfig;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.turtle.AITurtleBreed;
import net.pl3x.bukkit.ridables.entity.ai.goal.turtle.AITurtleGoHome;
import net.pl3x.bukkit.ridables.entity.ai.goal.turtle.AITurtleGoToWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.turtle.AITurtleLayEgg;
import net.pl3x.bukkit.ridables.entity.ai.goal.turtle.AITurtlePanic;
import net.pl3x.bukkit.ridables.entity.ai.goal.turtle.AITurtleTempt;
import net.pl3x.bukkit.ridables.entity.ai.goal.turtle.AITurtleTravel;
import net.pl3x.bukkit.ridables.entity.ai.goal.turtle.AITurtleWander;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import net.pl3x.bukkit.ridables.event.RidableSpacebarEvent;
import net.pl3x.bukkit.ridables.util.Const;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class RidableTurtle extends EntityTurtle implements RidableEntity {
    public static final TurtleConfig CONFIG = new TurtleConfig();

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

    @Override
    public RidableType getType() {
        return RidableType.TURTLE;
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED); // registerAttribute
        reloadAttributes();
    }

    @Override
    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
    @Override
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
    @Override
    public boolean aY() {
        return true;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cG() {
        return getRider() == null ? CONFIG.AI_JUMP_POWER : CONFIG.RIDING_JUMP_POWER;
    }

    @Override
    public BlockPosition getHome() {
        return datawatcher.get(HOME_POSITION);
    }

    @Override
    public void setHome(BlockPosition pos) {
        datawatcher.set(HOME_POSITION, pos);
    }

    @Override
    public boolean hasEgg() {
        return datawatcher.get(HAS_EGG);
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        datawatcher.set(HAS_EGG, hasEgg);
    }

    @Override
    public boolean isDigging() {
        return datawatcher.get(DIGGING);
    }

    @Override
    public void setDigging(boolean digging) {
        setDiggingTicks(digging ? 1 : 0);
        datawatcher.set(DIGGING, digging);
    }

    @Override
    public BlockPosition getTravelPos() {
        return datawatcher.get(TRAVEL_POSITION);
    }

    @Override
    public void setTravelPos(BlockPosition pos) {
        datawatcher.set(TRAVEL_POSITION, pos);
    }

    @Override
    public boolean isGoingHome() {
        return datawatcher.get(GOING_HOME);
    }

    @Override
    public void setGoingHome(boolean goingHome) {
        datawatcher.set(GOING_HOME, goingHome);
    }

    @Override
    public boolean isTravelling() {
        return datawatcher.get(TRAVELLING);
    }

    @Override
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

    @Override
    protected void mobTick() {
        Q = getRider() == null ? CONFIG.AI_STEP_HEIGHT : CONFIG.RIDING_STEP_HEIGHT;
        if (isInWater() && getRider() != null) {
            motY += 0.005D;
        }
        super.mobTick();
    }

    // travel
    @Override
    public void a(float strafe, float vertical, float forward) {
        super.a(strafe, vertical, forward);
        checkMove();
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (super.a(entityhuman, hand)) {
            return true; // handled by vanilla action
        }
        if (collectInWaterBucket(entityhuman, hand)) {
            return true; // handled
        }
        if (hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
            if (!CONFIG.RIDING_BABIES && isBaby()) {
                return false; // do not ride babies
            }
            return tryRide(entityhuman, CONFIG.RIDING_SADDLE_REQUIRE, CONFIG.RIDING_SADDLE_CONSUME);
        }
        return false;
    }

    @Override
    public boolean removePassenger(Entity passenger, boolean notCancellable) {
        if (passenger instanceof EntityPlayer && !passengers.isEmpty() && passenger == passengers.get(0)) {
            if (!new RidableDismountEvent(this, (Player) passenger.getBukkitEntity(), notCancellable).callEvent() && !notCancellable) {
                return false; // cancelled
            }
        }
        if (super.removePassenger(passenger, notCancellable)) {
            passenger.locY += 0.5; // dont let rider get stuck in a block
            return true;
        }
        return false;
    }

    static class TurtleWASDController extends ControllerWASD {
        private final RidableTurtle turtle;

        public TurtleWASDController(RidableTurtle turtle) {
            super(turtle);
            this.turtle = turtle;
        }

        @Override
        public void tick(EntityPlayer rider) {
            //updateSpeed();
            if (turtle.isInWater()) {
                float forward = rider.bj;
                float strafe = rider.bh;
                float vertical = -(rider.pitch / 90);

                if (forward < 0.0F) {
                    forward *= 0.25F;
                    vertical = -vertical * 0.1F;
                    strafe *= 0.25F;
                } else if (forward == 0) {
                    vertical = 0F;
                }

                if (isJumping(rider)) {
                    RidableSpacebarEvent event = new RidableSpacebarEvent(ridable);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled() && !event.isHandled()) {
                        ridable.onSpacebar();
                    }
                }

                float speed = (float) (e = ((EntityInsentient) ridable).getAttributeInstance(RidableType.RIDING_SPEED).getValue());

                a.o(speed * 0.1F);
                a.s(vertical * 1.5F * speed);
                a.t(strafe * speed);
                a.r(forward * speed);

                f = a.bj;
                g = a.bh;
            } else {
                float forward = rider.bj * 0.5F;
                float strafe = rider.bh * 0.25F;
                if (forward <= 0.0F) {
                    forward *= 0.5F;
                }

                float yaw = rider.yaw;
                if (strafe != 0) {
                    if (forward == 0) {
                        yaw += strafe > 0 ? -90 : 90;
                        forward = Math.abs(strafe * 2);
                    } else {
                        yaw += strafe > 0 ? -30 : 30;
                        strafe /= 2;
                        if (forward < 0) {
                            yaw += strafe > 0 ? -110 : 110;
                            forward *= -1;
                        }
                    }
                } else if (forward < 0) {
                    yaw -= 180;
                    forward *= -1;
                }
                ((LookController) a.getControllerLook()).setOffsets(yaw - rider.yaw, 0);

                if (isJumping(rider)) {
                    RidableSpacebarEvent event = new RidableSpacebarEvent(ridable);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled() && !event.isHandled() && !ridable.onSpacebar() && a.onGround) {
                        a.getControllerJump().a();
                    }
                }

                e = ((EntityInsentient) ridable).getAttributeInstance(RidableType.RIDING_SPEED).getValue() * 0.1F;

                a.o((float) e); // speed
                a.r(forward);

                f = a.bj; // forward
                g = a.bh; // strafe
            }
        }

        @Override
        public void tick() {
            updateSpeed();
            if (h == ControllerMove.Operation.MOVE_TO && !turtle.getNavigation().p()) { // noPath
                double x = b - turtle.locX;
                double y = c - turtle.locY;
                double z = d - turtle.locZ;
                y /= (double) MathHelper.sqrt(x * x + y * y + z * z);
                turtle.aQ = turtle.yaw = a(turtle.yaw, (float) (MathHelper.c(z, x) * Const.RAD2DEG) - 90.0F, 90.0F); // limitAngle
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
