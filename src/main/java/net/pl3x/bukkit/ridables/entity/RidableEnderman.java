package net.pl3x.bukkit.ridables.entity;

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
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AIMeleeAttack;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.enderman.AIEndermanFindPlayer;
import net.pl3x.bukkit.ridables.entity.ai.enderman.AIEndermanPlaceBlock;
import net.pl3x.bukkit.ridables.entity.ai.enderman.AIEndermanTakeBlock;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import net.pl3x.bukkit.ridables.util.PaperOnly;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;

public class RidableEnderman extends EntityEnderman implements RidableEntity {
    private boolean skipTP;

    public RidableEnderman(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.ENDERMAN;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
        goalSelector.a(0, new AISwim(this));
        goalSelector.a(2, new AIMeleeAttack(this, 1.0D, false));
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
        return Config.ENDERMAN_RIDABLE_IN_WATER && !Config.ENDERMAN_EJECT_WHEN_WET;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.ENDERMAN_JUMP_POWER;
    }

    // randomlyTeleport
    public boolean dz() {
        return skipTP || super.dz();
    }

    public boolean teleportToEntity(Entity entity) {
        return super.a(entity);
    }

    protected void mobTick() {
        Q = Config.ENDERMAN_STEP_HEIGHT;
        if (ap()) { // isWet
            if (Config.ENDERMAN_EJECT_WHEN_WET && getRider() != null) {
                ejectPassengers();
            }
            if (Config.ENDERMAN_DAMAGE_WHEN_WET) {
                damageEntity(DamageSource.DROWN, 1.0F);
            }
        }
        super.mobTick();
    }

    public float getSpeed() {
        return Config.ENDERMAN_SPEED;
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

    public boolean onClick(org.bukkit.block.Block block, BlockFace blockFace, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            return false; // ignore left clicks
        }

        EntityPlayer rider = getRider();
        if (rider == null || !hasSpecialPerm(rider.getBukkitEntity())) {
            return false;
        }

        if (getCarried() == null) {
            return pickUpBlock(block.getX(), block.getY(), block.getZ());
        }

        block = block.getRelative(blockFace);
        return placeBlock(block.getX(), block.getY(), block.getZ());
    }

    public boolean pickUpBlock(int x, int y, int z) {
        BlockPosition pos = new BlockPosition(x, y, z);
        IBlockData state = world.getType(pos);
        Block block = state.getBlock();
        MovingObjectPosition movingobjectposition = world.rayTrace(new Vec3D((double) ((float) MathHelper.floor(locX) + 0.5F), (double) ((float) y + 0.5F), (double) ((float) MathHelper.floor(locZ) + 0.5F)), new Vec3D((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F)), FluidCollisionOption.NEVER, true, false);
        boolean flag = movingobjectposition != null && movingobjectposition.a().equals(pos);
        if (block.a(TagsBlock.ENDERMAN_HOLDABLE) && flag) {
            if (!CraftEventFactory.callEntityChangeBlockEvent(this, pos, Blocks.AIR.getBlockData()).isCancelled()) {
                world.setAir(pos);
                setCarried(Block.b(state, world, pos));
                return true;
            }
        }
        return false;
    }

    public boolean placeBlock(int x, int y, int z) {
        BlockPosition pos = new BlockPosition(x, y, z);
        IBlockData state = world.getType(pos);
        IBlockData state1 = world.getType(pos.down());
        IBlockData state2 = Block.b(getCarried(), world, pos);
        if (state2 != null && state.isAir() && !state1.isAir() && state1.g() && state2.canPlace(world, pos)) {
            if (!CraftEventFactory.callEntityChangeBlockEvent(this, pos, state2).isCancelled()) {
                world.setTypeAndData(pos, state2, 3);
                setCarried(null);
                return true;
            }
        }
        return false;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        skipTP = !Config.ENDERMAN_TELEPORT_WHEN_DAMAGED;
        boolean result = super.damageEntity(damagesource, f);
        skipTP = false;
        return result;
    }

    public boolean shouldAttack(EntityHuman player) {
        boolean shouldAttack = shouldAttack_real(player);
        if (Ridables.isPaper()) {
            return PaperOnly.CallEndermanAttackPlayerEvent(this, player, shouldAttack);
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
