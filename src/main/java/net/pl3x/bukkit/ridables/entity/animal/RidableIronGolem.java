package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityCreeper;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.IMonster;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.IronGolemConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveThroughVillage;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveTowardsTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.iron_golem.AIIronGolemDefendVillage;
import net.pl3x.bukkit.ridables.entity.ai.goal.iron_golem.AIIronGolemOfferFlower;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class RidableIronGolem extends EntityIronGolem implements RidableEntity {
    public static final IronGolemConfig CONFIG = new IronGolemConfig();

    public RidableIronGolem(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.IRON_GOLEM;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(1, new AIAttackMelee(this, 1.0D, true));
        goalSelector.a(2, new AIMoveTowardsTarget(this, 0.9D, 32.0F));
        goalSelector.a(3, new AIMoveThroughVillage(this, 0.6D, true));
        goalSelector.a(4, new AIMoveTowardsRestriction(this, 1.0D));
        goalSelector.a(5, new AIIronGolemOfferFlower(this));
        goalSelector.a(6, new AIWanderAvoidWater(this, 0.6D));
        goalSelector.a(7, new AIWatchClosest(this, EntityHuman.class, 6.0F));
        goalSelector.a(8, new AILookIdle(this));
        targetSelector.a(1, new AIIronGolemDefendVillage(this));
        targetSelector.a(2, new AIHurtByTarget(this, false));
        targetSelector.a(3, new AIAttackNearest<>(this, EntityInsentient.class, 10, false, true,
                target -> target != null && IMonster.e.test(target) && !(target instanceof EntityCreeper)));
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

    public boolean onClick(org.bukkit.entity.Entity entity, EnumHand hand) {
        handleClick(hand);
        return false;
    }

    public boolean onClick(Block block, BlockFace blockFace, EnumHand hand) {
        handleClick(hand);
        return false;
    }

    public boolean onClick(EnumHand hand) {
        handleClick(hand);
        return false;
    }

    private void handleClick(EnumHand hand) {
        if (hand == EnumHand.OFF_HAND) {
            a(dz() == 0); // toggle rose on right click
        } else {
            if (dz() > 0) {
                a(false); // remove rose
            }
            world.broadcastEntityEffect(this, (byte) 4);
        }
    }
}
