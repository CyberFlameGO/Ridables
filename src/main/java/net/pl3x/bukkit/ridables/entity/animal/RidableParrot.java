package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityParrot;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.ParrotConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASDFlyingWithSpacebar;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIPanic;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISit;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.parrot.AIParrotFollowEntity;
import net.pl3x.bukkit.ridables.entity.ai.goal.parrot.AIParrotFollowOwner;
import net.pl3x.bukkit.ridables.entity.ai.goal.parrot.AIParrotLandOnOwnersShoulder;
import net.pl3x.bukkit.ridables.entity.ai.goal.parrot.AIParrotWanderAvoidWater;

public class RidableParrot extends EntityParrot implements RidableEntity {
    public static final ParrotConfig CONFIG = new ParrotConfig();

    public RidableParrot(World world) {
        super(world);
        moveController = new ParrotWASDController(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.PARROT;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSit = new AISit(this);

        goalSelector.a(0, new AIPanic(this, 1.25D));
        goalSelector.a(0, new AISwim(this));
        goalSelector.a(1, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(2, goalSit);
        goalSelector.a(2, new AIParrotFollowOwner(this, 1.0D, 5.0F, 1.0F));
        goalSelector.a(2, new AIParrotWanderAvoidWater(this, 1.0D));
        goalSelector.a(3, new AIParrotLandOnOwnersShoulder(this));
        goalSelector.a(3, new AIParrotFollowEntity(this, 1.0D, 3.0F, 7.0F));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    protected void mobTick() {
        if (getRider() != null) {
            motY += bi > 0 ? 0.07F * CONFIG.VERTICAL : 0.04704F - CONFIG.GRAVITY;
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

    public RidableParrot createChild(EntityAgeable entity) {
        return null;
    }

    public class ParrotWASDController extends ControllerWASDFlyingWithSpacebar {
        private final RidableParrot parrot;

        public ParrotWASDController(RidableParrot parrot) {
            super(parrot);
            this.parrot = parrot;
        }

        public void tick() {
            if (h == Operation.MOVE_TO) {
                h = Operation.WAIT;
                parrot.setNoGravity(true);
                double x = b - parrot.locX;
                double y = c - parrot.locY;
                double z = d - parrot.locZ;
                if (x * x + y * y + z * z < 2.5D) {
                    parrot.s(0.0F); // setMoveVertical
                    parrot.r(0.0F); // setMoveForward
                    return;
                }
                parrot.yaw = a(parrot.yaw, (float) (MathHelper.c(z, x) * (double) (180F / (float) Math.PI)) - 90.0F, 10.0F); // limitAngle
                parrot.o((float) (e * parrot.getAttributeInstance(parrot.onGround ? GenericAttributes.MOVEMENT_SPEED : GenericAttributes.e).getValue()));
                parrot.pitch = a(parrot.pitch, (float) (-(MathHelper.c(y, (double) MathHelper.sqrt(x * x + z * z)) * (double) (180F / (float) Math.PI))), 10.0F); // limitAngle
                parrot.s(y > 0.0D ? parrot.cK() : -parrot.cK()); // setMoveVertical getAIMoveSpeed
            } else {
                parrot.setNoGravity(false);
                parrot.s(0.0F); // setMoveVertical
                parrot.r(0.0F); // setMoveForward
            }
        }
    }
}