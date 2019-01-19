package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.BiomeBase;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.ChatMessage;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityMonster;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityRabbit;
import net.minecraft.server.v1_13_R2.EntityWolf;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import net.minecraft.server.v1_13_R2.PathEntity;
import net.minecraft.server.v1_13_R2.RecipeItemStack;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.SystemUtils;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.RabbitConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIBreed;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AITempt;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.rabbit.AIKillerRabbitAttack;
import net.pl3x.bukkit.ridables.entity.ai.goal.rabbit.AIRabbitAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.rabbit.AIRabbitEatCarrots;
import net.pl3x.bukkit.ridables.entity.ai.goal.rabbit.AIRabbitPanic;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class RidableRabbit extends EntityRabbit implements RidableEntity {
    public static final RabbitConfig CONFIG = new RabbitConfig();
    public static final RecipeItemStack TEMPTATION_ITEMS = RecipeItemStack.a(Items.CARROT, Items.GOLDEN_CARROT, Blocks.DANDELION);

    private static Field carrotTicks;
    private static Field killerBunny;
    private static Field rabbitType;

    static {
        try {
            carrotTicks = EntityRabbit.class.getDeclaredField("bJ");
            carrotTicks.setAccessible(true);
            killerBunny = EntityRabbit.class.getDeclaredField("bD");
            killerBunny.setAccessible(true);
            rabbitType = EntityRabbit.class.getDeclaredField("bC");
            rabbitType.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    private boolean wasOnGround;

    public RidableRabbit(World world) {
        super(world);
        moveController = new RabbitWASDController(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.RABBIT;
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
    @Override
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cG() {
        if (getRider() == null) {
            if (positionChanged || moveController.b() && moveController.e() > locY + 0.5D) { // isUpdating getY
                return CONFIG.AI_JUMP_POWER; // (collided with block, or jumping to higher block)
            }
            PathEntity path = navigation.m(); // getPath
            if (path != null && path.e() < path.d()) { // getCurrentPathIndex getCurrentPathLength
                if (path.a(this).y > locY + 0.5D) { // getPosition
                    return CONFIG.AI_JUMP_POWER; // (jumping to higher block)
                }
            }
            return moveController.c() <= 0.6D ? 0.2F : 0.3F; // getSpeed (hopping around on level ground)
        }
        if (bj < 0) {
            r(bj * 2F);
        }
        return CONFIG.RIDING_JUMP_POWER;
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

    private MinecraftKey getKillerBunnyKey() {
        try {
            return (MinecraftKey) killerBunny.get(this);
        } catch (IllegalAccessException ignore) {
        }
        return new MinecraftKey("killer_bunny");
    }

    @Override
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
        if (hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
            if (!CONFIG.RIDING_BABIES && isBaby()) {
                return false; // do not ride babies
            }
            if (!CONFIG.RIDING_RIDE_KILLER_BUNNY && getRabbitType() == 99) {
                return false; // do not ride killer bunny
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
        return super.removePassenger(passenger, notCancellable);
    }

    @Override
    public RidableRabbit createChild(EntityAgeable entity) {
        int type;
        if (random.nextInt(20) == 0) {
            type = getRandomRabbitType(); // 5% new rabbit type
        } else {
            // 50/50 rabbit type from a parent
            if (entity instanceof EntityRabbit && random.nextBoolean()) {
                type = ((EntityRabbit) entity).getRabbitType();
            } else {
                type = getRabbitType();
            }
        }
        RidableRabbit baby = new RidableRabbit(world);
        baby.setRabbitType(type);
        return baby;
    }

    private int getRandomRabbitType() {
        if (CONFIG.AI_KILLER_CHANCE > 0D && CONFIG.AI_KILLER_CHANCE > random.nextDouble()) {
            return 99; // killer rabbit type
        }
        BiomeBase biome = world.getBiome(new BlockPosition(this));
        if (biome.p() == BiomeBase.Geography.DESERT) { // getCategory
            return 4; // 100% chance to be type 4 in desert (gold fur)
        }
        int chance = random.nextInt(100);
        if (biome.c() == BiomeBase.Precipitation.SNOW) { // getPrecipitation
            if (chance < 80) {
                return 1; // 80% chance to be type 1 in snow (white fur)
            } else {
                return 3; // 20% chance to be type 3 in snow (black and white fur)
            }
        }
        if (chance < 50) {
            return 0; // 50% chance to be type 0 (brown fur)
        }
        if (chance < 90) {
            return 5; // 40% chance to be type 5 (salt and pepper fur)
        } else {
            return 2; // 10% chance to be type 2 (black fur)
        }
    }

    @Override
    public void setRabbitType(int type) {
        if (type == 99) {
            getAttributeInstance(GenericAttributes.h).setValue(CONFIG.AI_KILLER_ARMOR); // ARMOR
            goalSelector.a(4, new AIKillerRabbitAttack(this));
            targetSelector.a(1, new AIHurtByTarget(this, false));
            targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, true));
            targetSelector.a(2, new AIAttackNearest<>(this, EntityWolf.class, true));
            if (!hasCustomName()) {
                setCustomName(new ChatMessage(SystemUtils.a("entity", getKillerBunnyKey())));
            }
        }
        if (!hasCustomName() && CONFIG.AI_TOAST_CHANCE > 0D && CONFIG.AI_TOAST_CHANCE > random.nextDouble()) {
            setCustomName(new ChatMessage("Toast"));
        }
        try {
            datawatcher.set((DataWatcherObject<Integer>) rabbitType.get(this), type);
        } catch (IllegalAccessException ignore) {
        }
    }

    // attackEntityAsMob
    @Override
    public boolean B(Entity entity) {
        if (getRabbitType() == 99) {
            a(SoundEffects.ENTITY_RABBIT_ATTACK, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F); // playSound
            return entity.damageEntity(DamageSource.mobAttack(this), CONFIG.AI_KILLER_DAMAGE);
        } else {
            return entity.damageEntity(DamageSource.mobAttack(this), CONFIG.AI_MELEE_DAMAGE);
        }
    }

    static class RabbitWASDController extends ControllerWASD {
        private final RidableRabbit rabbit;
        private double nextJumpSpeed;

        public RabbitWASDController(RidableRabbit rabbit) {
            super(rabbit);
            this.rabbit = rabbit;
        }

        @Override
        public void tick() {
            if (rabbit.onGround && !rabbit.bg && !((EntityRabbit.ControllerJumpRabbit) rabbit.h).c()) { // isJumping jumpHelper getIsJumping
                rabbit.c(0.0D); // setMovementSpeed
            } else if (b()) { // isUpdating
                rabbit.c(nextJumpSpeed); // setMovementSpeed
            }
            super_tick();
        }

        @Override
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
