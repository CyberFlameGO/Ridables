package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityWither;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.EnumMonsterType;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_13_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_13_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_13_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDFlying;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import net.pl3x.bukkit.ridables.entity.projectile.CustomWitherSkull;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;

import java.util.function.Predicate;

public class RidableWither extends EntityWither implements RidableEntity {
    private static final Predicate<Entity> NOT_UNDEAD = (entity) -> entity instanceof EntityLiving &&
            ((EntityLiving) entity).getMonsterType() != EnumMonsterType.UNDEAD && ((EntityLiving) entity).df();

    private int shootCooldown = 0;

    public RidableWither(World world) {
        super(world);
        moveController = new ControllerWASDFlying(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.WITHER;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
        goalSelector.a(0, new DoNothing());
        goalSelector.a(2, new PathfinderGoalArrowAttack(this, 1.0D, 40, 20.0F));
        goalSelector.a(5, new PathfinderGoalRandomStrollLand(this, 1.0D));
        goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
        targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
        targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityInsentient.class, 0, false, false, NOT_UNDEAD));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.WITHER_RIDABLE_IN_WATER;
    }

    protected void mobTick() {
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        super.mobTick();
    }

    public float getSpeed() {
        return Config.WITHER_SPEED;
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

    public boolean onSpacebar() {
        return shoot(getRider(), new int[]{0, 1, 2});
    }

    public boolean onClick(org.bukkit.entity.Entity entity, EnumHand hand) {
        return handleClick(hand);
    }

    public boolean onClick(Block block, BlockFace blockFace, EnumHand hand) {
        return handleClick(hand);
    }

    public boolean onClick(EnumHand hand) {
        return handleClick(hand);
    }

    private boolean handleClick(EnumHand hand) {
        return shoot(getRider(), hand == EnumHand.MAIN_HAND ? new int[]{1} : new int[]{2});
    }

    public boolean shoot(EntityPlayer rider, int[] heads) {
        if (shootCooldown > 0) {
            return false;
        }

        shootCooldown = Config.WITHER_SHOOT_COOLDOWN;
        if (rider == null) {
            return false;
        }

        CraftPlayer player = (CraftPlayer) ((Entity) rider).getBukkitEntity();
        if (!hasShootPerm(player)) {
            Lang.send(player, Lang.SHOOT_NO_PERMISSION);
            return false;
        }

        Location loc = player.getTargetBlock(null, 120).getLocation();
        for (int head : heads) {
            shoot(head, loc.getX(), loc.getY(), loc.getZ(), rider);
        }

        return true;
    }

    public void shoot(int head, double x, double y, double z, EntityPlayer shooter) {
        world.a(null, 1024, new BlockPosition(this), 0);
        double headX = getHeadX(head);
        double headY = getHeadY(head);
        double headZ = getHeadZ(head);
        CustomWitherSkull skull = new CustomWitherSkull(world, this, shooter, x - headX, y - headY, z - headZ);
        skull.locY = headY;
        skull.locX = headX;
        skull.locZ = headZ;
        world.addEntity(skull);
    }

    public double getHeadX(int i) {
        return i <= 0 ? locX : locX + (double) MathHelper.cos((aQ + (float) (180 * (i - 1))) * 0.017453292F) * 1.3D;
    }

    public double getHeadY(int i) {
        return i <= 0 ? locY + 3.0D : locY + 2.2D;
    }

    public double getHeadZ(int i) {
        return i <= 0 ? locZ : locZ + (double) MathHelper.sin((aQ + (float) (180 * (i - 1))) * 0.017453292F) * 1.3D;
    }

    public int p(int i) {
        return getRider() != null ? 0 : super.p(i);
    }

    public void a(int i, int j) {
        if (getRider() == null) {
            super.a(i, j);
        }
    }

    private class DoNothing extends PathfinderGoal {
        DoNothing() {
            a(7);
        }

        public boolean a() {
            return dz() > 0;
        }
    }
}
