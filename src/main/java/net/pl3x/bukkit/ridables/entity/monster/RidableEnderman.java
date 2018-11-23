package net.pl3x.bukkit.ridables.entity.monster;

import com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent;
import net.minecraft.server.v1_13_R2.Block;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityEnderman;
import net.minecraft.server.v1_13_R2.EntityEndermite;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.FluidCollisionOption;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.MovingObjectPosition;
import net.minecraft.server.v1_13_R2.TagsBlock;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.EndermanConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.enderman.AIEndermanFindPlayer;
import net.pl3x.bukkit.ridables.entity.ai.goal.enderman.AIEndermanPlaceBlock;
import net.pl3x.bukkit.ridables.entity.ai.goal.enderman.AIEndermanTakeBlock;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;

public class RidableEnderman extends EntityEnderman implements RidableEntity {
    public static final EndermanConfig CONFIG = new EndermanConfig();

    private boolean skipTP;

    public RidableEnderman(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.ENDERMAN;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !hasCustomName() && !isLeashed();
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
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_MELEE_DAMAGE);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        goalSelector.a(0, new AISwim(this));
        goalSelector.a(2, new AIAttackMelee(this, 1.0D, false));
        goalSelector.a(7, new AIWanderAvoidWater(this, 1.0D, 0.0F));
        goalSelector.a(8, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(8, new AILookIdle(this));
        goalSelector.a(10, new AIEndermanPlaceBlock(this));
        goalSelector.a(11, new AIEndermanTakeBlock(this));
        targetSelector.a(1, new AIEndermanFindPlayer(this));
        targetSelector.a(2, new AIHurtByTarget(this, false));
        targetSelector.a(3, new AIAttackNearest<>(this, EntityEndermite.class, 10, true, false, EntityEndermite::l));
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER && !CONFIG.RIDING_EJECT_WHEN_WET;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cG() {
        return getRider() == null ? CONFIG.AI_JUMP_POWER : CONFIG.RIDING_JUMP_POWER;
    }

    // randomlyTeleport
    @Override
    public boolean dz() {
        return skipTP || super.dz();
    }

    public boolean teleportToEntity(Entity entity) {
        return super.a(entity);
    }

    @Override
    protected void mobTick() {
        boolean hasRider = getRider() != null;
        Q = hasRider ? CONFIG.RIDING_STEP_HEIGHT : CONFIG.AI_STEP_HEIGHT;
        if (ap()) { // isWet
            if (CONFIG.RIDING_EJECT_WHEN_WET && getRider() != null) {
                ejectPassengers();
            }
            if (hasRider) {
                if (CONFIG.RIDING_DAMAGE_WHEN_WET > 0F) {
                    damageEntity(DamageSource.DROWN, CONFIG.RIDING_DAMAGE_WHEN_WET);
                }
            } else {
                if (CONFIG.AI_DAMAGE_WHEN_WET > 0F) {
                    damageEntity(DamageSource.DROWN, CONFIG.AI_DAMAGE_WHEN_WET);
                }
            }
        }
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
        if (super.a(entityhuman, hand)) {
            return true; // handled by vanilla action
        }
        if (hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
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
    public boolean onClick(org.bukkit.block.Block block, BlockFace blockFace, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            return false; // ignore left clicks
        }

        EntityPlayer rider = getRider();
        if (rider == null || !rider.getBukkitEntity().hasPermission("allow.special.enderman")) {
            return false;
        }

        if (getCarried() == null) {
            return tryTakeBlock(block.getX(), block.getY(), block.getZ());
        }

        block = block.getRelative(blockFace);
        return tryPlaceBlock(block.getX(), block.getY(), block.getZ());
    }

    public boolean tryTakeBlock(int x, int y, int z) {
        BlockPosition pos = new BlockPosition(x, y, z);
        IBlockData state = world.getType(pos);
        if (!state.getBlock().a(TagsBlock.ENDERMAN_HOLDABLE)) {
            return false; // not a holdable block
        }
        MovingObjectPosition rayTrace = world.rayTrace(
                new Vec3D(MathHelper.floor(locX) + 0.5F, y + 0.5F, MathHelper.floor(locZ) + 0.5F),
                new Vec3D(x + 0.5F, y + 0.5F, z + 0.5F),
                FluidCollisionOption.NEVER, true, false);
        if (rayTrace == null) {
            return false; // no target block in range (shouldn't happen?)
        }
        if (rayTrace.a().equals(pos)) {
            return false; // block in the way
        }
        if (CraftEventFactory.callEntityChangeBlockEvent(this, pos, Blocks.AIR.getBlockData()).isCancelled()) {
            return false; // plugin cancelled
        }
        world.setAir(pos);
        setCarried(Block.b(state, world, pos));
        return true;
    }

    public boolean tryPlaceBlock(int x, int y, int z) {
        IBlockData carried = getCarried();
        if (carried == null) {
            return false; // not carrying a block
        }
        BlockPosition pos = new BlockPosition(x, y, z);
        if (!world.getType(pos).isAir()) {
            return false; // cannot place in non-air block
        }
        IBlockData stateDown = world.getType(pos.down());
        if (stateDown.isAir() || !stateDown.g()) {
            return false; // cannot place on air or non-full cube
        }
        IBlockData newState = Block.b(carried, world, pos); // getValidBlockForPosition
        if (newState == null) {
            return false; // no valid blockstate for position
        }
        if (!newState.canPlace(world, pos)) {
            return false; // cannot place this block here
        }
        if (CraftEventFactory.callEntityChangeBlockEvent(this, pos, newState).isCancelled()) {
            return false; // plugin cancelled
        }
        world.setTypeAndData(pos, newState, 3);
        setCarried(null);
        return true;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        skipTP = getRider() == null ? !CONFIG.AI_TELEPORT_WHEN_DAMAGED : !CONFIG.RIDING_TELEPORT_WHEN_DAMAGED;
        boolean result = super.damageEntity(damagesource, f);
        skipTP = false;
        return result;
    }

    public boolean shouldAttack(EntityHuman player) {
        boolean shouldAttack = shouldAttack_real(player);
        EndermanAttackPlayerEvent event = new EndermanAttackPlayerEvent((Enderman) getBukkitEntity(), (Player) player.getBukkitEntity());
        event.setCancelled(!shouldAttack);
        return event.callEvent();
    }

    private boolean shouldAttack_real(EntityHuman player) {
        if (player.inventory.armor.get(3).getItem() == Blocks.CARVED_PUMPKIN.getItem()) {
            return false;
        }
        Vec3D direction = new Vec3D(locX - player.locX, getBoundingBox().minY + getHeadHeight() - (player.locY + player.getHeadHeight()), locZ - player.locZ);
        return player.f(1.0F).a().b(direction.a()) > 1.0D - 0.025D / direction.b() && player.hasLineOfSight(this); // getLook normalize dotProduct normalize length
    }
}
