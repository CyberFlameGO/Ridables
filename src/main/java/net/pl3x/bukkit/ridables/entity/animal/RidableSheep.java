package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntitySheep;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.RecipeItemStack;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.SheepConfig;
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
import net.pl3x.bukkit.ridables.entity.ai.goal.sheep.AISheepEatGrass;

import java.lang.reflect.Field;

public class RidableSheep extends EntitySheep implements RidableEntity {
    public static final SheepConfig CONFIG = new SheepConfig();
    public static final RecipeItemStack TEMPTATION_ITEMS = RecipeItemStack.a(Items.WHEAT);

    private static Field eatGrassGoal;

    static {
        try {
            eatGrassGoal = EntitySheep.class.getDeclaredField("bI");
            eatGrassGoal.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    public RidableSheep(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.SHEEP;
    }

    // initAI - override vanilla AI
    protected void n() {
        AISheepEatGrass eatGrass = new AISheepEatGrass(this);

        try {
            eatGrassGoal.set(this, eatGrass);
        } catch (IllegalAccessException ignore) {
        }

        goalSelector.a(0, new AISwim(this));
        goalSelector.a(1, new AIPanic(this, 1.25D));
        goalSelector.a(2, new AIBreed(this, 1.0D, EntitySheep.class));
        goalSelector.a(3, new AITempt(this, 1.1D, false, TEMPTATION_ITEMS));
        goalSelector.a(4, new AIFollowParent(this, 1.1D));
        goalSelector.a(5, eatGrass);
        goalSelector.a(6, new AIWanderAvoidWater(this, 1.0D));
        goalSelector.a(7, new AIWatchClosest(this, EntityHuman.class, 6.0F));
        goalSelector.a(8, new AILookIdle(this));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return getRider() == null ? super.cG() : CONFIG.JUMP_POWER;
    }

    protected void mobTick() {
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

    public RidableSheep createChild(EntityAgeable entity) {
        return b(entity);
    }

    // createChild (bukkit's weird duplicate method)
    public RidableSheep b(EntityAgeable entity) {
        return new RidableSheep(world);
    }
}