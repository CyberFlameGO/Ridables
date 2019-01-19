package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.EnchantmentManager;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPig;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.EnumMoveType;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.MobEffect;
import net.minecraft.server.v1_13_R2.MobEffects;
import net.minecraft.server.v1_13_R2.RecipeItemStack;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.PigConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIBreed;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIFollowParent;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIPanic;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AITempt;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.util.Const;

import java.lang.reflect.Field;

public class RidablePig extends EntityPig implements RidableEntity {
    public static final PigConfig CONFIG = new PigConfig();
    public static final RecipeItemStack TEMPTATION_ITEMS = RecipeItemStack.a(Items.CARROT, Items.POTATO, Items.BEETROOT, Items.CARROT_ON_A_STICK);

    private static Field boosting;
    private static Field boostTime;
    private static Field totalBoostTime;

    static {
        try {
            boosting = EntityPig.class.getDeclaredField("bG");
            boosting.setAccessible(true);
            boostTime = EntityPig.class.getDeclaredField("bH");
            boostTime.setAccessible(true);
            totalBoostTime = EntityPig.class.getDeclaredField("bI");
            totalBoostTime.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    public RidablePig(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.PIG;
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
        goalSelector.a(0, new AISwim(this));
        goalSelector.a(1, new AIPanic(this, 1.25D));
        goalSelector.a(3, new AIBreed(this, 1.0D, EntityPig.class));
        goalSelector.a(4, new AITempt(this, 1.2D, false, TEMPTATION_ITEMS));
        goalSelector.a(5, new AIFollowParent(this, 1.1D));
        goalSelector.a(6, new AIWanderAvoidWater(this, 1.0D));
        goalSelector.a(7, new AIWatchClosest(this, EntityHuman.class, 6.0F));
        goalSelector.a(8, new AILookIdle(this));
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cG() {
        return getRider() == null ? CONFIG.AI_JUMP_POWER : CONFIG.RIDING_JUMP_POWER;
    }

    @Override
    protected void mobTick() {
        Q = getRider() == null ? CONFIG.AI_STEP_HEIGHT : CONFIG.RIDING_STEP_HEIGHT;
        super.mobTick();
    }

    // travel
    @Override
    public void a(float strafe, float vertical, float forward) {
        if (isVehicle() && dh()) { // isBeingRidden canBeSteered
            //Q = 1.0F; // disable vanilla's step-height change
            aU = cK() * 0.1F; // jumpMovementFactor
            boolean boosting = isBoosting();
            int boostTime = incrementBoostTime();
            int totalBoostTime = getTotalBoostTime();
            if (boosting && boostTime > totalBoostTime) {
                disableBoosting();
            }
            if (bT()) { // canPassengerSteer
                float speed = (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * 0.225F;
                if (boosting) {
                    speed += speed * 1.15F * MathHelper.sin((float) boostTime / (float) totalBoostTime * Const.PI_FLOAT);
                }
                o(speed);
                super_a(0.0F, 0.0F, 1.0F);
            } else {
                motX = 0.0D;
                motY = 0.0D;
                motZ = 0.0D;
            }
        } else {
            //Q = 0.5F; // disable vanilla's step-height change
            aU = 0.02F; // jumpMovementFactor
            super_a(strafe, vertical, forward);
        }
        checkMove();
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (CONFIG.GET_SADDLE_BACK && entityhuman.isSneaking() && hasSaddle() && passengers.isEmpty()) {
            ItemStack item = entityhuman.b(hand);
            if (item.isEmpty() || item.getItem() == Items.SADDLE) {
                setSaddle(false);
                if (!entityhuman.abilities.canInstantlyBuild || item.getItem() != Items.SADDLE) {
                    ItemStack saddle = new ItemStack(Items.SADDLE);
                    if (!entityhuman.inventory.pickup(saddle)) {
                        entityhuman.drop(saddle, false);
                    }
                }
                return true; // handled
            }
        }
        if (super.a(entityhuman, hand)) {
            return true; // handled by vanilla action
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
        return super.removePassenger(passenger, notCancellable);
    }

    private boolean isBoosting() {
        try {
            return boosting.getBoolean(this);
        } catch (IllegalAccessException ignore) {
        }
        return false;
    }

    private void disableBoosting() {
        try {
            boosting.setBoolean(this, false);
        } catch (IllegalAccessException ignore) {
        }
    }

    private int incrementBoostTime() {
        try {
            boostTime.setInt(this, boostTime.getInt(this) + 1);
        } catch (IllegalAccessException ignore) {
        }
        return 0;
    }

    private int getTotalBoostTime() {
        try {
            totalBoostTime.getInt(this);
        } catch (IllegalAccessException ignore) {
        }
        return 0;
    }

    // modified travel method from EntityLiving
    private void super_a(float strafe, float vertical, float forward) {
        double gravity = 0.08D;
        if (motY <= 0.0D && hasEffect(MobEffects.SLOW_FALLING)) {
            gravity = 0.01D;
            fallDistance = 0.0F;
        }
        if (isInWater()) {
            double oldY = locY;
            float speedSlowed = isSprinting() ? 0.9F : cJ();
            float speed = 0.02F;
            float depthStrider = (float) EnchantmentManager.e(this);
            if (depthStrider > 3.0F) {
                depthStrider = 3.0F;
            }
            if (!onGround) {
                depthStrider *= 0.5F;
            }
            if (depthStrider > 0.0F) {
                speedSlowed += (0.54600006F - speedSlowed) * depthStrider / 3.0F;
                speed += (cK() - speed) * depthStrider / 3.0F;
            }
            if (hasEffect(MobEffects.DOLPHINS_GRACE)) {
                speedSlowed = 0.96F;
            }
            a(strafe, vertical, forward, speed);
            move(EnumMoveType.SELF, motX, motY, motZ);
            motX *= (double) speedSlowed;
            motY *= 0.8D;
            motZ *= (double) speedSlowed;
            if (!isNoGravity() && !isSprinting()) {
                if (motY <= 0.0D && Math.abs(motY - 0.005D) >= 0.003D && Math.abs(motY - gravity / 16.0D) < 0.003D) {
                    motY = -0.003D;
                } else {
                    motY -= gravity / 16.0D;
                }
            }
            if (positionChanged && c(motX, motY + 0.6D - locY + oldY, motZ)) {
                motY = 0.3D;
            }
        } else if (ax()) { // isInLava()
            double oldY = locY;
            a(strafe, vertical, forward, 0.02F);
            move(EnumMoveType.SELF, motX, motY, motZ);
            motX *= 0.5D;
            motY *= 0.5D;
            motZ *= 0.5D;
            if (!isNoGravity()) {
                motY -= gravity / 4.0D;
            }
            if (positionChanged && c(motX, motY + 0.6D - locY + oldY, motZ)) {
                motY = 0.3D;
            }
        } else {
            try (BlockPosition.b pos = BlockPosition.b.d(locX, getBoundingBox().minY - 1.0D, locZ)) {
                float friction = onGround ? world.getType(pos).getBlock().n() * 0.91F : 0.91F;
                float speed = onGround ? cK() * (0.16277137F / (friction * friction * friction)) : aU;
                a(strafe, vertical, forward, speed);
                friction = onGround ? world.getType(pos.setValues(locX, getBoundingBox().minY - 1.0D, locZ)).getBlock().n() * 0.91F : 0.91F;
                if (z_()) { // isOnLadder
                    motX = MathHelper.a(motX, -0.15D, 0.15D);
                    motZ = MathHelper.a(motZ, -0.15D, 0.15D);
                    fallDistance = 0.0F;
                    if (motY < -0.15D) {
                        motY = -0.15D;
                    }
                }
                move(EnumMoveType.SELF, motX, motY, motZ);
                if (positionChanged && z_()) { // isOnLadder
                    motY = 0.2D;
                }
                MobEffect levitation = getEffect(MobEffects.LEVITATION);
                if (levitation != null) {
                    motY += (0.05D * (double) (levitation.getAmplifier() + 1) - motY) * 0.2D;
                    fallDistance = 0.0F;
                } else {
                    pos.setValues(locX, 0.0D, locZ); // setPos
                    if (world.isClientSide && (!world.isLoaded(pos) || !world.getChunkAtWorldCoords(pos).y())) {
                        motY = locY > 0.0D ? -0.1D : 0.0D;
                    } else if (!isNoGravity()) {
                        motY -= gravity;
                    }
                }
                motY *= 0.98D;
                motX *= (double) friction;
                motZ *= (double) friction;
            }
        }
        aI = aJ;
        double x = locX - lastX;
        double z = locZ - lastZ;
        float distance = MathHelper.sqrt(x * x + z * z) * 4.0F;
        if (distance > 1.0F) {
            distance = 1.0F;
        }
        aJ += (distance - aJ) * 0.4F;
        aK += aJ;
    }
}
