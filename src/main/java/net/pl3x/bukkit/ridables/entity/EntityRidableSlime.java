package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.ControllerLook;
import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntityIronGolem;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EntitySlime;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.MobEffects;
import net.minecraft.server.v1_13_R1.PathfinderGoal;
import net.minecraft.server.v1_13_R1.PathfinderGoalNearestAttackableTargetInsentient;
import net.minecraft.server.v1_13_R1.PathfinderGoalTargetNearestPlayer;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.BlankLookController;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class EntityRidableSlime extends EntitySlime implements RidableEntity {
    private ControllerWASD controllerWASD;
    private ControllerLook defaultLookController;
    private BlankLookController blankLookController;

    public EntityRidableSlime(World world) {
        super(world);
        moveController = new SlimeController(this);
        controllerWASD = new ControllerWASD(this);
        defaultLookController = lookController;
        blankLookController = new BlankLookController(this);
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return false;
    }

    public boolean aY() {
        return true;
    }

    protected void mobTick() {
        EntityPlayer rider = getRider();
        if (rider != null) {
            super.setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
        }
        super.mobTick();
    }

    public void setRotation(float newYaw, float newPitch) {
        setYawPitch(lastYaw = yaw = newYaw, pitch = newPitch * 0.5F);
        aS = aQ = yaw;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.SLIME_SPEED;
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
        if (lookController != defaultLookController) {
            lookController = defaultLookController;
        }
    }

    public void useWASDController() {
        if (lookController != blankLookController) {
            lookController = blankLookController;
        }
    }

    public boolean onSpacebar() {
        return false;
    }

    public void setGoalTarget(@Nullable EntityLiving entityliving) {
        setGoalTarget(entityliving, EntityTargetEvent.TargetReason.UNKNOWN, true);
    }

    public boolean setGoalTarget(EntityLiving entityliving, EntityTargetEvent.TargetReason reason, boolean fireEvent) {
        return getRider() != null && super.setGoalTarget(entityliving, reason, fireEvent);
    }

    protected void n() {
        this.goalSelector.a(1, new PathfinderGoalSlimeRandomJump(this));
        this.goalSelector.a(2, new PathfinderGoalSlimeNearestPlayer(this));
        this.goalSelector.a(3, new PathfinderGoalSlimeRandomDirection(this));
        this.goalSelector.a(5, new PathfinderGoalSlimeIdle(this));
        this.targetSelector.a(1, new PathfinderGoalTargetNearestPlayer(this));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTargetInsentient(this, EntityIronGolem.class));
    }

    static class PathfinderGoalSlimeIdle extends PathfinderGoal {
        private final EntityRidableSlime slime;

        public PathfinderGoalSlimeIdle(EntityRidableSlime entityslime) {
            slime = entityslime;
            a(5);
        }

        public boolean a() {
            return true;
        }

        public void e() {
            ((SlimeController) slime.getControllerMove()).a(1.0D);
        }
    }

    static class PathfinderGoalSlimeRandomJump extends PathfinderGoal {
        private final EntityRidableSlime slime;

        public PathfinderGoalSlimeRandomJump(EntityRidableSlime entityslime) {
            slime = entityslime;
            a(5);
            entityslime.getNavigation().d(true);
        }

        public boolean a() {
            return slime.isInWater() || slime.ax();
        }

        public void e() {
            if (slime.getRandom().nextFloat() < 0.8F) {
                slime.getControllerJump().a();
            }
            ((SlimeController) slime.getControllerMove()).a(1.2D);
        }
    }

    static class PathfinderGoalSlimeRandomDirection extends PathfinderGoal {
        private final EntityRidableSlime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public PathfinderGoalSlimeRandomDirection(EntityRidableSlime entityslime) {
            slime = entityslime;
            a(2);
        }

        public boolean a() {
            return slime.getGoalTarget() == null && (slime.onGround || slime.isInWater() || slime.ax() || slime.hasEffect(MobEffects.LEVITATION));
        }

        public void e() {
            if (--nextRandomizeTime <= 0) {
                nextRandomizeTime = 40 + slime.getRandom().nextInt(60);
                chosenDegrees = (float) slime.getRandom().nextInt(360);
            }
            ((SlimeController) slime.getControllerMove()).a(chosenDegrees, false);
        }
    }

    static class PathfinderGoalSlimeNearestPlayer extends PathfinderGoal {
        private final EntityRidableSlime slime;
        private int growTimer;

        public PathfinderGoalSlimeNearestPlayer(EntityRidableSlime entityslime) {
            slime = entityslime;
            a(2);
        }

        public boolean a() {
            EntityLiving entityliving = slime.getGoalTarget();
            if (entityliving == null) {
                return false;
            } else if (!entityliving.isAlive()) {
                return false;
            } else {
                return !(entityliving instanceof EntityHuman) || !((EntityHuman) entityliving).abilities.isInvulnerable;
            }
        }

        public void c() {
            growTimer = 300;
            super.c();
        }

        public boolean b() {
            EntityLiving entityliving = slime.getGoalTarget();
            if (entityliving == null) {
                return false;
            } else if (!entityliving.isAlive()) {
                return false;
            } else if (entityliving instanceof EntityHuman && ((EntityHuman) entityliving).abilities.canInstantlyBuild) {
                return false;
            } else {
                return --growTimer > 0;
            }
        }

        public void e() {
            slime.a(slime.getGoalTarget(), 10.0F, 10.0F);
            ((SlimeController) slime.getControllerMove()).a(slime.yaw, slime.du());
        }
    }

    static class SlimeController extends ControllerMove {
        private float yRot;
        private int jumpDelay;
        private final EntityRidableSlime slime;
        private boolean isAggressive;

        public SlimeController(EntityRidableSlime entityslime) {
            super(entityslime);
            this.slime = entityslime;
            this.yRot = 180.0F * entityslime.yaw / 3.1415927F;
        }

        public void a(float yRot, boolean aggressive) {
            this.yRot = yRot;
            this.isAggressive = aggressive;
        }

        public void a(double speed) {
            e = speed;
            h = ControllerMove.Operation.MOVE_TO;
        }

        public void a() {
            EntityPlayer rider = slime.getRider();
            if (rider != null) {
                slime.controllerWASD.a();
                if (slime.bj != 0 || slime.bh != 0) {
                    if (jumpDelay > 10) {
                        jumpDelay = 6;
                    }
                } else {
                    jumpDelay = 20;
                }
            } else {
                a.yaw = a(a.yaw, yRot, 90.0F);
                a.aS = a.yaw;
                a.aQ = a.yaw;
            }
            if (rider == null && h != ControllerMove.Operation.MOVE_TO) {
                a.r(0.0F);
            } else {
                h = ControllerMove.Operation.WAIT;
                if (a.onGround) {
                    if (rider != null) {
                        if (slime.bj != 0 || slime.bh != 0) {
                            a.o((float) (e * slime.getSpeed()));
                        }
                    }
                    if (jumpDelay-- <= 0) {
                        jumpDelay = slime.ds();
                        if (isAggressive) {
                            jumpDelay /= 3;
                        }
                        slime.getControllerJump().a();
                        if (slime.dA()) {
                            slime.a(slime.dx(), slime.cD(), ((slime.getRandom().nextFloat() - slime.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                        }
                    } else {
                        slime.bh = 0.0F;
                        slime.bj = 0.0F;
                        a.o(0.0F);
                    }
                } else {
                    a.o((float) (e * (rider != null ? slime.getSpeed() : slime.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue())));
                }
            }
        }
    }
}
