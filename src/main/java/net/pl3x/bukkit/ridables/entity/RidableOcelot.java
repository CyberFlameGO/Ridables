package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityChicken;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityOcelot;
import net.minecraft.server.v1_13_R2.EntityTurtle;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.PathfinderGoalTempt;
import net.minecraft.server.v1_13_R2.RecipeItemStack;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.OcelotConfig;
import net.pl3x.bukkit.ridables.entity.ai.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.AIBreed;
import net.pl3x.bukkit.ridables.entity.ai.AIFollowOwner;
import net.pl3x.bukkit.ridables.entity.ai.AILeapAtTarget;
import net.pl3x.bukkit.ridables.entity.ai.AISit;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AITargetNonTamed;
import net.pl3x.bukkit.ridables.entity.ai.AITempt;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.ocelot.AIOcelotAttack;
import net.pl3x.bukkit.ridables.entity.ai.ocelot.AIOcelotSitOnBlock;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

import java.lang.reflect.Field;

public class RidableOcelot extends EntityOcelot implements RidableEntity {
    public static final OcelotConfig CONFIG = new OcelotConfig();
    public static final RecipeItemStack TEMPTATION_ITEMS = RecipeItemStack.a(Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH);

    private static Field aiTempt;

    static {
        try {
            aiTempt = EntityOcelot.class.getDeclaredField("bK");
            aiTempt.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    private AIAvoidTarget<EntityHuman> aiAvoidEntity;

    public RidableOcelot(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.OCELOT;
    }

    // initAI - override vanilla AI
    protected void n() {
        PathfinderGoalTempt goalTempt = new AITempt(this, 0.6D, true, TEMPTATION_ITEMS);
        try {
            aiTempt.set(this, goalTempt);
        } catch (IllegalAccessException ignore) {
        }

        goalSit = new AISit(this);

        goalSelector.a(1, new AISwim(this));
        goalSelector.a(2, goalSit);
        goalSelector.a(3, goalTempt);
        goalSelector.a(5, new AIFollowOwner(this, 1.0D, 10.0F, 5.0F));
        goalSelector.a(6, new AIOcelotSitOnBlock(this, 0.8D));
        goalSelector.a(7, new AILeapAtTarget(this, 0.3F));
        goalSelector.a(8, new AIOcelotAttack(this));
        goalSelector.a(9, new AIBreed(this, 0.8D, EntityChicken.class));
        goalSelector.a(10, new AIWanderAvoidWater(this, 0.8D, 0.00001F));
        goalSelector.a(11, new AIWatchClosest(this, EntityHuman.class, 10.0F));
        targetSelector.a(1, new AITargetNonTamed<>(this, EntityChicken.class, false, null));
        targetSelector.a(1, new AITargetNonTamed<>(this, EntityTurtle.class, false, EntityTurtle.bC));
    }

    // setupTamedAI
    protected void dz() {
        if (aiAvoidEntity == null) {
            aiAvoidEntity = new AIAvoidTarget<>(this, EntityHuman.class, 16.0F, 0.8D, 1.33D);
        }
        goalSelector.a(aiAvoidEntity); // removeTask
        if (!isTamed()) {
            goalSelector.a(4, aiAvoidEntity); // addTask
        }
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return CONFIG.JUMP_POWER;
    }

    public void mobTick() {
        Q = CONFIG.STEP_HEIGHT;
        super.mobTick();
    }

    // processInteract
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    public RidableOcelot createChild(EntityAgeable entity) {
        return b(entity);
    }

    // createChild (bukkit's weird duplicate method)
    public RidableOcelot b(EntityAgeable entity) {
        RidableOcelot baby = new RidableOcelot(world);
        if (isTamed()) {
            baby.setOwnerUUID(getOwnerUUID());
            baby.setTamed(true);
            baby.setCatType(getCatType());
        }
        return baby;
    }
}
