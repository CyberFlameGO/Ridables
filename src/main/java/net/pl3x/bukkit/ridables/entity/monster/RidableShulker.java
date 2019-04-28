package net.pl3x.bukkit.ridables.entity.monster;

import net.minecraft.server.v1_14_R1.AxisAlignedBB;
import net.minecraft.server.v1_14_R1.ControllerLook;
import net.minecraft.server.v1_14_R1.Entity;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.EntityShulker;
import net.minecraft.server.v1_14_R1.EntityShulkerBullet;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EnumDifficulty;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.IMonster;
import net.minecraft.server.v1_14_R1.PathfinderGoal;
import net.minecraft.server.v1_14_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_14_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_14_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_14_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_14_R1.SoundEffects;
import net.minecraft.server.v1_14_R1.World;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.configuration.mob.ShulkerConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.projectile.CustomShulkerBullet;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.util.Vector;

import java.util.EnumSet;

public class RidableShulker extends EntityShulker implements RidableEntity {
    private static ShulkerConfig config;

    private final ControllerWASD controllerWASD;

    private boolean isOpen = true;
    private int shootCooldown = 0;
    private int spacebarCooldown = 0;

    public RidableShulker(EntityTypes<? extends EntityShulker> entitytypes, World world) {
        super(entitytypes, world);
        moveController = controllerWASD = new ControllerWASD(this);
        lookController = new ControllerLook(this);

        if (config == null) {
            config = getConfig();
        }
    }

    @Override
    public RidableType getType() {
        return RidableType.SHULKER;
    }

    @Override
    public ControllerWASD getController() {
        return controllerWASD;
    }

    @Override
    public ShulkerConfig getConfig() {
        return (ShulkerConfig) getType().getConfig();
    }

    @Override
    public double getRidingSpeed() {
        return config.RIDING_SPEED;
    }

    @Override
    protected void initPathfinder() {
        goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        PathfinderGoal aiAttack = new PathfinderGoal() {
            private EntityLiving target;
            private int attackTime;

            public boolean a() { // shouldExecute
                return getRider() == null && (target = getGoalTarget()) != null && target.isAlive() && world.getDifficulty() != EnumDifficulty.PEACEFUL;
            }

            public void c() { // startExecuting
                attackTime = 20;
                updateArmorModifier(100);
            }

            public void d() { // resetTask
                updateArmorModifier(0);
            }

            public void e() { // tick
                if (world.getDifficulty() != EnumDifficulty.PEACEFUL) {
                    --attackTime;
                    getControllerLook().a(target, 180.0F, 180.0F);
                    if (h(target) < 400.0D) {
                        if (attackTime <= 0) {
                            attackTime = 20 + random.nextInt(10) * 20 / 2;
                            world.addEntity(new EntityShulkerBullet(world, RidableShulker.this, target, dV().k()));
                            RidableShulker.this.a(SoundEffects.ENTITY_SHULKER_SHOOT, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                        }
                    } else {
                        setGoalTarget(null);
                    }
                    super.e();
                }
            }
        };
        aiAttack.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
        goalSelector.a(4, aiAttack);
        goalSelector.a(7, new PathfinderGoal() { // AIPeek
            private int peekTime;

            public boolean a() { // shouldExecute
                return getRider() == null && getGoalTarget() == null && random.nextInt(40) == 0;
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && getGoalTarget() == null && peekTime > 0;
            }

            public void c() { // startExecuting
                peekTime = 20 * (1 + random.nextInt(3));
                updateArmorModifier(30);
            }

            public void d() { // resetTask
                if (getGoalTarget() == null) {
                    updateArmorModifier(0);
                }
            }

            public void e() { // tick
                --peekTime;
            }
        });
        goalSelector.a(8, new PathfinderGoalRandomLookaround(this) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        targetSelector.a(1, new PathfinderGoalHurtByTarget(this) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        }.a(new Class[0]));
        targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true) {
            public boolean a() { // shouldExecute
                return getRider() == null && world.getDifficulty() != EnumDifficulty.PEACEFUL && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }

            protected AxisAlignedBB a(double d0) { // getTargetableArea
                switch (dV().k()) { // getAttachmentFace getAxis
                    case X:
                        return getBoundingBox().grow(4.0D, d0, d0);
                    case Z:
                        return getBoundingBox().grow(d0, d0, 4.0D);
                    default:
                        return getBoundingBox().grow(d0, 4.0D, d0);
                }
            }
        }); // AIAttackNearest
        targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<EntityLiving>(this, EntityLiving.class, 10, true, false,
                (entityliving) -> entityliving instanceof IMonster) {
            public boolean a() { // shouldExecute
                return getRider() == null && getScoreboardTeam() != null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }

            protected AxisAlignedBB a(double d0) { // getTargetableArea
                switch (dV().k()) { // getAttachmentFace getAxis
                    case X:
                        return getBoundingBox().grow(4.0D, d0, d0);
                    case Z:
                        return getBoundingBox().grow(d0, d0, 4.0D);
                    default:
                        return getBoundingBox().grow(d0, 4.0D, d0);
                }
            }
        }); // AIDefenseAttack
    }

    // canBeRiddenInWater
    @Override
    public boolean be() {
        return config.RIDING_RIDE_IN_WATER;
    }

    // tryTeleportToNewPosition
    @Override
    protected boolean l() {
        return getRider() != null || super.l();
    }

    @Override
    protected void mobTick() {
        if (spacebarCooldown > 0) {
            spacebarCooldown--;
        }
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        if (getRider() != null) {
            updatePeek();
        }
        super.mobTick();
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (super.a(entityhuman, hand)) {
            return true; // handled by vanilla action
        }
        if (hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
            return tryRide(entityhuman, config.RIDING_SADDLE_REQUIRE, config.RIDING_SADDLE_CONSUME);
        }
        return false;
    }

    @Override
    public boolean onSpacebar() {
        if (spacebarCooldown == 0) {
            spacebarCooldown = 20;
            setOpen(!isOpen());
            return true;
        }
        return false;
    }

    @Override
    public boolean onClick(org.bukkit.entity.Entity entity, EnumHand hand) {
        handleClick();
        return true;
    }

    @Override
    public boolean onClick(Block block, BlockFace blockFace, EnumHand hand) {
        handleClick();
        return true;
    }

    @Override
    public boolean onClick(EnumHand hand) {
        handleClick();
        return true;
    }

    private void handleClick() {
        if (shootCooldown == 0 && isOpen()) {
            EntityPlayer rider = getRider();
            if (rider != null) {
                shoot(rider);
            }
        }
    }

    private void updatePeek() {
        byte peekTick = (byte) (isOpen ? config.RIDING_PEEK_HEIGHT : 0);
        if (getPeekTick() != peekTick) {
            updateArmorModifier(peekTick);
        }
    }

    public int getPeekTick() {
        return dX();
    }

    public void updateArmorModifier(int modifier) {
        a(modifier); // updateArmorModifier
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean shoot(EntityPlayer rider) {
        shootCooldown = config.RIDING_SHOOT_COOLDOWN;

        if (rider == null) {
            return false;
        }

        CraftPlayer player = (CraftPlayer) ((Entity) rider).getBukkitEntity();
        if (!player.hasPermission("ridables.shoot.shulker")) {
            Lang.send(player, Lang.SHOOT_NO_PERMISSION);
            return false;
        }

        Vector target = player.getEyeLocation().getDirection().normalize().multiply(25);

        CustomShulkerBullet bullet = new CustomShulkerBullet(world, this, rider, null, dV().k()); // getAttachmentFacing getAxis
        bullet.shoot(target.getX(), target.getY(), target.getZ(), config.RIDING_SHOOT_SPEED, 5.0F);
        world.addEntity(bullet);

        a(SoundEffects.ENTITY_SHULKER_SHOOT, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        return true;
    }
}
