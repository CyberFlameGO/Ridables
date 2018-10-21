package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityEvoker;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityVex;
import net.minecraft.server.v1_13_R2.EntityVillager;
import net.minecraft.server.v1_13_R2.EntityVindicator;
import net.minecraft.server.v1_13_R2.EntityZombie;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.VillagerConfig;
import net.pl3x.bukkit.ridables.entity.ai.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.AIMoveIndoors;
import net.pl3x.bukkit.ridables.entity.ai.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.AIOpenDoor;
import net.pl3x.bukkit.ridables.entity.ai.AIRestrictOpenDoor;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosestWithoutMoving;
import net.pl3x.bukkit.ridables.entity.ai.villager.AIVillagerBreed;
import net.pl3x.bukkit.ridables.entity.ai.villager.AIVillagerInteract;
import net.pl3x.bukkit.ridables.entity.ai.villager.AIVillagerLookAtTradingPlayer;
import net.pl3x.bukkit.ridables.entity.ai.villager.AIVillagerTakeFlower;
import net.pl3x.bukkit.ridables.entity.ai.villager.AIVillagerTradeWithPlayer;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableVillager extends EntityVillager implements RidableEntity {
    public static final VillagerConfig CONFIG = new VillagerConfig();

    public RidableVillager(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.VILLAGER;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(0, new AISwim(this));
        goalSelector.a(1, new AIAvoidTarget<>(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        goalSelector.a(1, new AIAvoidTarget<>(this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
        goalSelector.a(1, new AIAvoidTarget<>(this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
        goalSelector.a(1, new AIAvoidTarget<>(this, EntityVex.class, 8.0F, 0.6D, 0.6D));
        goalSelector.a(1, new AIVillagerTradeWithPlayer(this));
        goalSelector.a(1, new AIVillagerLookAtTradingPlayer(this));
        goalSelector.a(2, new AIMoveIndoors(this));
        goalSelector.a(3, new AIRestrictOpenDoor(this));
        goalSelector.a(4, new AIOpenDoor(this, true));
        goalSelector.a(5, new AIMoveTowardsRestriction(this, 0.6D));
        goalSelector.a(6, new AIVillagerBreed(this));
        goalSelector.a(7, new AIVillagerTakeFlower(this));
        goalSelector.a(9, new AIWatchClosestWithoutMoving(this, EntityHuman.class, 3.0F, 1.0F));
        goalSelector.a(9, new AIVillagerInteract(this));
        goalSelector.a(9, new AIWanderAvoidWater(this, 0.6D));
        goalSelector.a(10, new AIWatchClosest(this, EntityInsentient.class, 8.0F));
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

    public RidableVillager createChild(EntityAgeable entity) {
        return b(entity);
    }

    // createChild (bukkit's weird duplicate method)
    public RidableVillager b(EntityAgeable entity) {
        return new RidableVillager(world);
    }
}
