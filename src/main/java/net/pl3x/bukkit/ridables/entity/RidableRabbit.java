package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.BiomeBase;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityMonster;
import net.minecraft.server.v1_13_R2.EntityOcelot;
import net.minecraft.server.v1_13_R2.EntityRabbit;
import net.minecraft.server.v1_13_R2.EntityWolf;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.RecipeItemStack;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.RabbitConfig;
import net.pl3x.bukkit.ridables.entity.ai.AIBreed;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AITempt;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.rabbit.AIRabbitAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.rabbit.AIRabbitEatCarrots;
import net.pl3x.bukkit.ridables.entity.ai.rabbit.AIRabbitPanic;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

import java.lang.reflect.Field;

public class RidableRabbit extends EntityRabbit implements RidableEntity {
    public static final RabbitConfig CONFIG = new RabbitConfig();
    public static final RecipeItemStack TEMPTATION_ITEMS = RecipeItemStack.a(Items.CARROT, Items.GOLDEN_CARROT, Blocks.DANDELION);

    private static Field carrotTicks;

    static {
        try {
            carrotTicks = EntityOcelot.class.getDeclaredField("bJ");
            carrotTicks.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    private boolean wasOnGround;

    public RidableRabbit(World world) {
        super(world);
        moveController = new RabbitWASDController(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.RABBIT;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(1, new AISwim(this));
        goalSelector.a(1, new AIRabbitPanic(this, 2.2D));
        goalSelector.a(2, new AIBreed(this, 0.8D, EntityRabbit.class));
        goalSelector.a(3, new AITempt(this, 1.0D, false, TEMPTATION_ITEMS));
        goalSelector.a(4, new AIRabbitAvoidTarget<>(this, EntityHuman.class, 8.0F, 2.2D, 2.2D));
        goalSelector.a(4, new AIRabbitAvoidTarget<>(this, EntityWolf.class, 10.0F, 2.2D, 2.2D));
        goalSelector.a(4, new AIRabbitAvoidTarget<>(this, EntityMonster.class, 4.0F, 2.2D, 2.2D));
        goalSelector.a(5, new AIRabbitEatCarrots(this));
        goalSelector.a(6, new AIWanderAvoidWater(this, 0.6D));
        goalSelector.a(11, new AIWatchClosest(this, EntityHuman.class, 10.0F));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        if (getRider() == null) {
            return super.cG();
        }
        if (bj < 0) {
            r(bj * 2F);
        }
        return CONFIG.JUMP_POWER;
    }

    public boolean isCarrotEaten() {
        try {
            return carrotTicks.getInt(this) == 0;
        } catch (IllegalAccessException ignore) {
        }
        return false;
    }

    public void setCarrotTicks(int ticks) {
        try {
            carrotTicks.setInt(this, ticks);
        } catch (IllegalAccessException ignore) {
        }
    }

    public void mobTick() {
        if (getRider() != null) {
            handleJumping();
            return;
        }
        super.mobTick();
    }

    private void handleJumping() {
        if (onGround) {
            ControllerJumpRabbit jumpHelper = (ControllerJumpRabbit) h;
            if (!wasOnGround) {
                o(false); // setJumping
                jumpHelper.a(false); // setCanJump
            }
            if (!jumpHelper.c()) { // getIsJumping
                if (moveController.b()) { // isUpdating
                    dy(); // startJumping
                }
            } else if (!jumpHelper.d()) { // canJump
                jumpHelper.a(true); // setCanJump
            }
        }
        wasOnGround = onGround;
    }

    // processInteract
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    public RidableRabbit createChild(EntityAgeable entity) {
        return b(entity);
    }

    // createChild (bukkit's weird duplicate method)
    public RidableRabbit b(EntityAgeable entity) {
        RidableRabbit baby = new RidableRabbit(world);
        int type;
        if (random.nextInt(20) == 0) {
            type = getRandomRabbitType();
        } else {
            if (entity instanceof EntityRabbit && random.nextBoolean()) {
                type = ((EntityRabbit) entity).getRabbitType();
            } else {
                type = getRabbitType();
            }
        }

        baby.setRabbitType(type);
        return baby;
    }

    private int getRandomRabbitType() {
        if (CONFIG.KILLER_CHANCE > 0D && CONFIG.KILLER_CHANCE > random.nextDouble()) {
            return 99;
        }
        BiomeBase biome = world.getBiome(new BlockPosition(this));
        if (biome.p() == BiomeBase.Geography.DESERT) { // getCategory
            return 4;
        }
        int type = random.nextInt(100);
        if (biome.c() == BiomeBase.Precipitation.SNOW) { // getPrecipitation
            return (type < 80 ? 1 : 3);
        }
        return type < 50 ? 0 : (type < 90 ? 5 : 2);
    }

    static class RabbitWASDController extends ControllerWASD {
        private final RidableRabbit rabbit;
        private double nextJumpSpeed;

        public RabbitWASDController(RidableRabbit rabbit) {
            super(rabbit);
            this.rabbit = rabbit;
        }

        public void tick() {
            if (rabbit.onGround && !rabbit.bg && !((EntityRabbit.ControllerJumpRabbit) rabbit.h).c()) { // isJumping jumpHelper getIsJumping
                rabbit.c(0.0D); // setMovementSpeed
            } else if (b()) { // isUpdating
                rabbit.c(nextJumpSpeed); // setMovementSpeed
            }
            super_tick();
        }

        public void a(double x, double y, double z, double speed) {
            if (rabbit.isInWater()) {
                speed = 1.5D;
            }
            super.a(x, y, z, speed);
            if (speed > 0.0D) {
                nextJumpSpeed = speed;
            }
        }
    }
}
