package net.pl3x.bukkit.ridables.entity;

import io.papermc.lib.PaperLib;
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
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.MovingObjectPosition;
import net.minecraft.server.v1_13_R2.TagsBlock;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.EndermanConfig;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.enderman.AIEndermanFindPlayer;
import net.pl3x.bukkit.ridables.entity.ai.enderman.AIEndermanPlaceBlock;
import net.pl3x.bukkit.ridables.entity.ai.enderman.AIEndermanTakeBlock;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import net.pl3x.bukkit.ridables.hook.Paper;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;

public class RidableEnderman extends EntityEnderman implements RidableEntity {
    public static final EndermanConfig CONFIG = new EndermanConfig();

    private boolean skipTP;

    public RidableEnderman(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.ENDERMAN;
    }

    // initAI - override vanilla AI
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
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER && !CONFIG.EJECT_WHEN_WET;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return CONFIG.JUMP_POWER;
    }

    // randomlyTeleport
    public boolean dz() {
        return skipTP || super.dz();
    }

    public boolean teleportToEntity(Entity entity) {
        return super.a(entity);
    }

    protected void mobTick() {
        Q = CONFIG.STEP_HEIGHT;
        if (ap()) { // isWet
            if (CONFIG.EJECT_WHEN_WET && getRider() != null) {
                ejectPassengers();
            }
            if (CONFIG.DAMAGE_WHEN_WET) {
                damageEntity(DamageSource.DROWN, 1.0F);
            }
        }
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

    public boolean onClick(org.bukkit.block.Block block, BlockFace blockFace, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            return false; // ignore left clicks
        }

        EntityPlayer rider = getRider();
        if (rider == null || !hasSpecialPerm(rider.getBukkitEntity())) {
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

    public boolean damageEntity(DamageSource damagesource, float f) {
        skipTP = !CONFIG.TELEPORT_WHEN_DAMAGED;
        boolean result = super.damageEntity(damagesource, f);
        skipTP = false;
        return result;
    }

    public boolean shouldAttack(EntityHuman player) {
        boolean shouldAttack = shouldAttack_real(player);
        if (PaperLib.isPaper()) {
            return Paper.CallEndermanAttackPlayerEvent(this, player, shouldAttack);
        }
        return shouldAttack;
    }

    private boolean shouldAttack_real(EntityHuman player) {
        ItemStack itemstack = player.inventory.armor.get(3);
        if (itemstack.getItem() == Blocks.CARVED_PUMPKIN.getItem()) {
            return false;
        } else {
            Vec3D vec3d = player.f(1.0F).a();
            Vec3D vec3d1 = new Vec3D(locX - player.locX, getBoundingBox().b + (double) getHeadHeight() - (player.locY + (double) player.getHeadHeight()), locZ - player.locZ);
            double d0 = vec3d1.b();
            vec3d1 = vec3d1.a();
            double d1 = vec3d.b(vec3d1);
            return d1 > 1.0D - 0.025D / d0 && player.hasLineOfSight(this);
        }
    }
}
