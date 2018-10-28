package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityChicken;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityItem;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.RecipeItemStack;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.ChickenConfig;
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
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDropItemEvent;

public class RidableChicken extends EntityChicken implements RidableEntity {
    public static final ChickenConfig CONFIG = new ChickenConfig();
    public static final RecipeItemStack TEMPTATION_ITEMS = RecipeItemStack.a(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);

    private int timeUntilNextEgg;

    public RidableChicken(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        calculateNewTimeUntilNextEgg();
    }

    @Override
    public RidableType getType() {
        return RidableType.CHICKEN;
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
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        goalSelector.a(0, new AISwim(this));
        goalSelector.a(1, new AIPanic(this, 1.4D));
        goalSelector.a(2, new AIBreed(this, 1.0D, EntityChicken.class));
        goalSelector.a(3, new AITempt(this, 1.0D, false, TEMPTATION_ITEMS));
        goalSelector.a(4, new AIFollowParent(this, 1.1D));
        goalSelector.a(5, new AIWanderAvoidWater(this, 1.0D));
        goalSelector.a(6, new AIWatchClosest(this, EntityHuman.class, 6.0F));
        goalSelector.a(7, new AILookIdle(this));
    }

    // readNBT
    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKey("EggLayTime")) {
            timeUntilNextEgg = nbttagcompound.getInt("EggLayTime");
        }
    }

    // writeNBT
    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("EggLayTime", timeUntilNextEgg);
    }

    @Override
    public boolean isTypeNotPersistent() {
        return isChickenJockey();
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

    // onLivingUpdate
    @Override
    public void k() {
        bI = 6000; // disable vanilla timeUntilNextEgg tick counter;
        if (getRider() == null || CONFIG.RIDING_DROP_EGGS) {
            timeUntilNextEgg--;
        }
        if (!isBaby() && !isChickenJockey() && timeUntilNextEgg <= 0) {
            a(SoundEffects.ENTITY_CHICKEN_EGG, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F); // playSound
            EntityItem egg = new EntityItem(world, locX, locY, locZ, new ItemStack(Items.EGG));
            egg.n(); // setDefaultPickupDelay
            EntityDropItemEvent event = new EntityDropItemEvent(getBukkitEntity(), (org.bukkit.entity.Item) egg.getBukkitEntity());
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                world.addEntity(egg);
            }
            calculateNewTimeUntilNextEgg();
        }
        super.k();
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
            return tryRide(entityhuman, CONFIG.RIDING_SADDLE_REQUIRE, CONFIG.RIDING_SADDLE_CONSUME);
        }
        return false;
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        return (!(passenger instanceof Player) || passengers.isEmpty() || !passenger.equals(passengers.get(0))
                || new RidableDismountEvent(this, (Player) passenger).callEvent()) && super.removePassenger(passenger);
    }

    private void calculateNewTimeUntilNextEgg() {
        timeUntilNextEgg = random.nextInt((CONFIG.RIDING_DROP_EGGS_DELAY_MAX - CONFIG.RIDING_DROP_EGGS_DELAY_MIN) + 1) + CONFIG.RIDING_DROP_EGGS_DELAY_MIN;
    }
}
