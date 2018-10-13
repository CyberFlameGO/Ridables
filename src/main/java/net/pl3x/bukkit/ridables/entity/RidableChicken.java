package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityChicken;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityItem;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.RecipeItemStack;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIBreed;
import net.pl3x.bukkit.ridables.entity.ai.AIFollowParent;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AIPanic;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AITempt;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDropItemEvent;

public class RidableChicken extends EntityChicken implements RidableEntity {
    public static final RecipeItemStack TEMPTATION_ITEMS = RecipeItemStack.a(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    private int timeUntilNextEgg;

    public RidableChicken(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        calculateNewTimeUntilNextEgg();
    }

    public RidableType getType() {
        return RidableType.CHICKEN;
    }

    protected boolean isTypeNotPersistent() {
        return isChickenJockey();
    }

    // initAI - override vanilla AI
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
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKey("EggLayTime")) {
            timeUntilNextEgg = nbttagcompound.getInt("EggLayTime");
        }
    }

    // writeNBT
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("EggLayTime", timeUntilNextEgg);
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.CHICKEN_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.CHICKEN_JUMP_POWER;
    }

    protected void mobTick() {
        Q = Config.CHICKEN_STEP_HEIGHT;
        super.mobTick();
    }

    // onLivingUpdate
    public void k() {
        bI = 6000; // disable vanilla timeUntilNextEgg tick counter;
        if (getRider() == null || Config.CHICKEN_DROP_EGGS_WHILE_RIDING) {
            timeUntilNextEgg--;
        }
        if (!isBaby() && !isChickenJockey() && timeUntilNextEgg <= 0) {
            Ridables.timings().chickenLayEgg.startTiming();
            a(SoundEffects.ENTITY_CHICKEN_EGG, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
            EntityItem egg = new EntityItem(world, locX, locY, locZ, new ItemStack(Items.EGG));
            egg.n(); // set 10 tick pickup delay
            EntityDropItemEvent event = new EntityDropItemEvent(getBukkitEntity(), (org.bukkit.entity.Item) egg.getBukkitEntity());
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                world.addEntity(egg);
            }
            calculateNewTimeUntilNextEgg();
            Ridables.timings().chickenLayEgg.stopTiming();
        }
        super.k();
    }

    public float getSpeed() {
        return Config.CHICKEN_SPEED;
    }

    // processInteract
    public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        if (passengers.isEmpty() && !entityhuman.isPassenger() && !entityhuman.isSneaking()) {
            return enumhand == EnumHand.MAIN_HAND && tryRide(entityhuman, entityhuman.b(enumhand));
        }
        return passengers.isEmpty() && super.a(entityhuman, enumhand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    public RidableChicken createChild(EntityAgeable entity) {
        return b(entity);
    }

    // createChild (bukkit's weird duplicate method)
    public RidableChicken b(EntityAgeable entity) {
        return new RidableChicken(world);
    }

    private void calculateNewTimeUntilNextEgg() {
        timeUntilNextEgg = random.nextInt((Config.CHICKEN_EGG_DELAY_MAX - Config.CHICKEN_EGG_DELAY_MIN) + 1) + Config.CHICKEN_EGG_DELAY_MIN;
    }
}
