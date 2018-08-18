package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.Block;
import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.Blocks;
import net.minecraft.server.v1_13_R1.ControllerLook;
import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.DamageSource;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityEnderman;
import net.minecraft.server.v1_13_R1.EntityEndermite;
import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EnumHand;
import net.minecraft.server.v1_13_R1.FluidCollisionOption;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.IBlockData;
import net.minecraft.server.v1_13_R1.IWorldReader;
import net.minecraft.server.v1_13_R1.MathHelper;
import net.minecraft.server.v1_13_R1.MovingObjectPosition;
import net.minecraft.server.v1_13_R1.PathfinderGoal;
import net.minecraft.server.v1_13_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_13_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_13_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_13_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_13_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_13_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_13_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_13_R1.TagsBlock;
import net.minecraft.server.v1_13_R1.Vec3D;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.BlankLookController;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_13_R1.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class EntityRidableEnderman extends EntityEnderman implements RidableEntity {
    private ControllerMove aiController;
    private ControllerWASD wasdController;
    private ControllerLook defaultLookController;
    private BlankLookController blankLookController;
    private boolean skipTP;

    public EntityRidableEnderman(World world) {
        super(world);
        aiController = moveController;
        wasdController = new ControllerWASD(this);
        defaultLookController = lookController;
        blankLookController = new BlankLookController(this);
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return false;
    }

    public boolean aY() {
        return !Config.ENDERMAN_EJECT_WHEN_WET;
    }

    protected void mobTick() {
        EntityPlayer rider = getRider();
        if (rider != null) {
            super.setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
            if (ap()) { // isWet
                if (Config.ENDERMAN_EJECT_WHEN_WET) {
                    ejectPassengers();
                }
                if (Config.ENDERMAN_DAMAGE_WHEN_WET) {
                    damageEntity(DamageSource.DROWN, 1.0F);
                }
            }
            return;
        }
        super.mobTick();
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return super.cG() * getJumpPower() * 2.2F;
    }

    public void setRotation(float newYaw, float newPitch) {
        setYawPitch(lastYaw = yaw = newYaw, pitch = newPitch * 0.5F);
        aS = aQ = yaw;
    }

    public float getJumpPower() {
        return Config.ENDERMAN_JUMP_POWER * 1.5F;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.ENDERMAN_SPEED;
    }

    public EntityPlayer getRider() {
        if (passengers != null && !passengers.isEmpty()) {
            Entity entity = passengers.get(0);
            if (entity instanceof EntityPlayer) {
                return (EntityPlayer) entity;
            }
        }
        return null;
    }

    public void useAIController() {
        if (moveController != aiController) {
            moveController = aiController;
            lookController = defaultLookController;
        }
    }

    public void useWASDController() {
        if (moveController != wasdController) {
            moveController = wasdController;
            lookController = blankLookController;
        }
    }

    public boolean onClick(org.bukkit.block.Block block, BlockFace blockFace, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            return false; // ignore left clicks
        }
        EntityPlayer rider = getRider();
        if (rider == null || !rider.getBukkitEntity().hasPermission("allow.special.enderman")) {
            return false;
        }
        if (getCarried() == null) {
            BlockPosition blockposition = new BlockPosition(block.getX(), block.getY(), block.getZ());
            setCarried(world.getType(blockposition));
            world.setAir(blockposition);
        } else {
            block = block.getRelative(blockFace);
            BlockPosition blockposition = new BlockPosition(block.getX(), block.getY(), block.getZ());
            world.setTypeAndData(blockposition, getCarried(), 3);
            setCarried(null);
        }
        return true;
    }

    public void setGoalTarget(@Nullable EntityLiving entityliving) {
        setGoalTarget(entityliving, EntityTargetEvent.TargetReason.UNKNOWN, true);
    }

    public boolean setGoalTarget(EntityLiving entityliving, EntityTargetEvent.TargetReason reason, boolean fireEvent) {
        return getRider() != null && super.setGoalTarget(entityliving, reason, fireEvent);
    }

    // randomlyTeleport
    protected boolean dA() {
        return skipTP || super.dA();
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        skipTP = !Config.ENDERMAN_TELEPORT_WHEN_DAMAGED;
        boolean result = super.damageEntity(damagesource, f);
        skipTP = false;
        return result;
    }

    protected void n() {
        goalSelector.a(0, new PathfinderGoalFloat(this));
        goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.0D, false));
        goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D, 0.0F));
        goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        goalSelector.a(10, new PathfinderGoalEndermanPlaceBlock(this));
        goalSelector.a(11, new PathfinderGoalEndermanPickupBlock(this));
        targetSelector.a(1, new PathfinderGoalPlayerWhoLookedAtTarget(this));
        targetSelector.a(2, new PathfinderGoalHurtByTarget(this, false));
        targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, EntityEndermite.class, 10, true, false, (Predicate<EntityEndermite>) EntityEndermite::l));
    }

    static class PathfinderGoalEndermanPickupBlock extends PathfinderGoal {
        private final EntityRidableEnderman enderman;

        public PathfinderGoalEndermanPickupBlock(EntityRidableEnderman enderman) {
            this.enderman = enderman;
        }

        public boolean a() {
            if (enderman.getRider() != null) {
                return false;
            } else if (enderman.getCarried() != null) {
                return false;
            } else if (!enderman.world.getGameRules().getBoolean("mobGriefing")) {
                return false;
            } else {
                return enderman.getRandom().nextInt(20) == 0;
            }
        }

        public void e() {
            int x = MathHelper.floor(enderman.locX - 2.0D + enderman.random.nextDouble() * 4.0D);
            int y = MathHelper.floor(enderman.locY + enderman.random.nextDouble() * 3.0D);
            int z = MathHelper.floor(enderman.locZ - 2.0D + enderman.random.nextDouble() * 4.0D);
            BlockPosition blockposition = new BlockPosition(x, y, z);
            IBlockData iblockdata = enderman.world.getType(blockposition);
            Block block = iblockdata.getBlock();
            MovingObjectPosition movingobjectposition = enderman.world.rayTrace(new Vec3D((double) ((float) MathHelper.floor(enderman.locX) + 0.5F), (double) ((float) y + 0.5F), (double) ((float) MathHelper.floor(enderman.locZ) + 0.5F)), new Vec3D((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F)), FluidCollisionOption.NEVER, true, false);
            boolean flag = movingobjectposition != null && movingobjectposition.a().equals(blockposition);
            if (block.a(TagsBlock.G) && flag) {
                if (!CraftEventFactory.callEntityChangeBlockEvent(enderman, blockposition, Blocks.AIR.getBlockData()).isCancelled()) {
                    enderman.setCarried(iblockdata);
                    enderman.world.setAir(blockposition);
                }
            }
        }
    }

    static class PathfinderGoalEndermanPlaceBlock extends PathfinderGoal {
        private final EntityRidableEnderman enderman;

        public PathfinderGoalEndermanPlaceBlock(EntityRidableEnderman enderman) {
            this.enderman = enderman;
        }

        public boolean a() {
            if (enderman.getRider() != null) {
                return false;
            } else if (enderman.getCarried() == null) {
                return false;
            } else if (!enderman.world.getGameRules().getBoolean("mobGriefing")) {
                return false;
            } else {
                return enderman.getRandom().nextInt(2000) == 0;
            }
        }

        public void e() {
            int x = MathHelper.floor(enderman.locX - 1.0D + enderman.random.nextDouble() * 2.0D);
            int y = MathHelper.floor(enderman.locY + enderman.random.nextDouble() * 2.0D);
            int z = MathHelper.floor(enderman.locZ - 1.0D + enderman.random.nextDouble() * 2.0D);
            BlockPosition blockposition = new BlockPosition(x, y, z);
            IBlockData iblockdata = enderman.world.getType(blockposition);
            IBlockData iblockdata1 = enderman.world.getType(blockposition.down());
            IBlockData iblockdata2 = enderman.getCarried();
            if (iblockdata2 != null && a(enderman.world, blockposition, iblockdata2, iblockdata, iblockdata1)) {
                if (!CraftEventFactory.callEntityChangeBlockEvent(enderman, blockposition, iblockdata2).isCancelled()) {
                    enderman.world.setTypeAndData(blockposition, iblockdata2, 3);
                    enderman.setCarried(null);
                }
            }

        }

        private boolean a(IWorldReader iworldreader, BlockPosition blockposition, IBlockData iblockdata, IBlockData iblockdata1, IBlockData iblockdata2) {
            return iblockdata1.isAir() && !iblockdata2.isAir() && iblockdata2.g() && iblockdata.canPlace(iworldreader, blockposition);
        }
    }

    static class PathfinderGoalPlayerWhoLookedAtTarget extends PathfinderGoalNearestAttackableTarget<EntityHuman> {
        private final EntityRidableEnderman enderman;

        private EntityHuman player;
        private int aggroTime;
        private int teleportTime;

        public PathfinderGoalPlayerWhoLookedAtTarget(EntityRidableEnderman enderman) {
            super(enderman, EntityHuman.class, false);
            this.enderman = enderman;
        }

        public boolean a() {
            if (enderman.getRider() != null) {
                return false;
            }
            double d0 = i();
            player = enderman.world.a(enderman.locX, enderman.locY, enderman.locZ, d0, d0, null, player -> player != null && shouldAttack(player));
            return player != null;
        }

        public void c() {
            aggroTime = 5;
            teleportTime = 0;
        }

        public void d() {
            player = null;
            super.d();
        }

        public boolean b() {
            if (enderman.getRider() != null) {
                return false;
            }
            if (player != null) {
                if (!shouldAttack(player)) {
                    return false;
                } else {
                    enderman.a(player, 10.0F, 10.0F);
                    return true;
                }
            } else {
                return d != null && d.isAlive() || super.b();
            }
        }

        public void e() {
            if (player != null) {
                if (--aggroTime <= 0) {
                    d = player;
                    player = null;
                    super.c();
                }
            } else {
                if (d != null) {
                    if (shouldAttack(d)) {
                        if (d.h(enderman) < 16.0D && tryEscape()) {
                            enderman.dA();
                        }
                        teleportTime = 0;
                    } else if (d.h(enderman) > 256.0D && teleportTime++ >= 30 && enderman.a((Entity) d)) {
                        teleportTime = 0;
                    }
                }
                super.e();
            }
        }

        private boolean tryEscape() {
            try {
                return new com.destroystokyo.paper.event.entity.EndermanEscapeEvent(
                        (org.bukkit.craftbukkit.v1_13_R1.entity.CraftEnderman) enderman.getBukkitEntity(),
                        com.destroystokyo.paper.event.entity.EndermanEscapeEvent.Reason.STARE)
                        .callEvent();
            } catch (NoClassDefFoundError ignore) {
                return true;
            }
        }

        private boolean shouldAttack(EntityHuman entityhuman) {
            boolean shouldAttack = shouldAttack_real(entityhuman);
            try {
                com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent event =
                        new com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent(
                                (org.bukkit.entity.Enderman) enderman.getBukkitEntity(),
                                (org.bukkit.entity.Player) entityhuman.getBukkitEntity());
                event.setCancelled(!shouldAttack);
                return event.callEvent();
            } catch (NoClassDefFoundError ignore) {
            }
            return shouldAttack;
        }

        private boolean shouldAttack_real(EntityHuman entityhuman) {
            net.minecraft.server.v1_13_R1.ItemStack itemstack = entityhuman.inventory.armor.get(3);
            if (itemstack.getItem() == Blocks.CARVED_PUMPKIN.getItem()) {
                return false;
            } else {
                Vec3D vec3d = entityhuman.f(1.0F).a();
                Vec3D vec3d1 = new Vec3D(enderman.locX - entityhuman.locX, enderman.getBoundingBox().b + (double) enderman.getHeadHeight() - (entityhuman.locY + (double) entityhuman.getHeadHeight()), enderman.locZ - entityhuman.locZ);
                double d0 = vec3d1.b();
                vec3d1 = vec3d1.a();
                double d1 = vec3d.b(vec3d1);
                return d1 > 1.0D - 0.025D / d0 && entityhuman.hasLineOfSight(enderman);
            }
        }
    }
}
