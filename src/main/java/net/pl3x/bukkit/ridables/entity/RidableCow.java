package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityCow;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.RecipeItemStack;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIFollowParent;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AIMate;
import net.pl3x.bukkit.ridables.entity.ai.AIPanic;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AITempt;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableCow extends EntityCow implements RidableEntity {
    public RidableCow(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.COW;
    }

    public float getSpeed() {
        return Config.COW_SPEED;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
        goalSelector.a(0, new AISwim(this));
        goalSelector.a(1, new AIPanic(this, 2.0D));
        goalSelector.a(2, new AIMate(this, 1.0D, EntityCow.class));
        goalSelector.a(3, new AITempt(this, 1.25D, false, RecipeItemStack.a(Items.WHEAT)));
        goalSelector.a(4, new AIFollowParent(this, 1.25D));
        goalSelector.a(5, new AIWanderAvoidWater(this, 1.0D));
        goalSelector.a(6, new AIWatchClosest(this, EntityHuman.class, 6.0F));
        goalSelector.a(7, new AILookIdle(this));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.COW_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.COW_JUMP_POWER;
    }

    protected void mobTick() {
        Q = Config.COW_STEP_HEIGHT;
        super.mobTick();
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
}
