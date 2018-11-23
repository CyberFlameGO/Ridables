package net.pl3x.bukkit.ridables.entity.npc;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityEvoker;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityVex;
import net.minecraft.server.v1_13_R2.EntityVillager;
import net.minecraft.server.v1_13_R2.EntityVindicator;
import net.minecraft.server.v1_13_R2.EntityZombie;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.VillagerConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveIndoors;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIOpenDoor;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIRestrictOpenDoor;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosestWithoutMoving;
import net.pl3x.bukkit.ridables.entity.ai.goal.villager.AIVillagerBreed;
import net.pl3x.bukkit.ridables.entity.ai.goal.villager.AIVillagerInteract;
import net.pl3x.bukkit.ridables.entity.ai.goal.villager.AIVillagerLookAtTradingPlayer;
import net.pl3x.bukkit.ridables.entity.ai.goal.villager.AIVillagerTakeFlower;
import net.pl3x.bukkit.ridables.entity.ai.goal.villager.AIVillagerTradeWithPlayer;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.entity.Player;

public class RidableVillager extends EntityVillager implements RidableEntity {
    public static final VillagerConfig CONFIG = new VillagerConfig();

    public RidableVillager(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.VILLAGER;
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
        super.a(strafe, vertical, forward);
        checkMove();
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
            if (!CONFIG.RIDING_BABIES && isBaby()) {
                return false; // do not ride babies
            }
            return tryRide(entityhuman, CONFIG.RIDING_SADDLE_REQUIRE, CONFIG.RIDING_SADDLE_CONSUME);
        }
        return super.a(entityhuman, hand); // handle vanilla actions last because of trade menu
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
}
