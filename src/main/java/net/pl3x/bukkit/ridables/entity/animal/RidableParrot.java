package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.Entity;
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
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.entity.Player;

public class RidableParrot extends EntityParrot implements RidableEntity {
    public static final ParrotConfig CONFIG = new ParrotConfig();

    public RidableParrot(World world) {
        super(world);
        moveController = new ParrotWASDController(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.PARROT;
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED); // registerAttribute
        getAttributeMap().b(RidableType.RIDING_MAX_Y); // registerAttribute
        reloadAttributes();
    }

    @Override
    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(RidableType.RIDING_MAX_Y).setValue(CONFIG.RIDING_FLYING_MAX_Y);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
    @Override
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
    @Override
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER;
    }

    @Override
    protected void mobTick() {
        if (getRider() != null) {
            motY += bi > 0 ? 0.07D * CONFIG.RIDING_VERTICAL : 0.04704D - CONFIG.RIDING_GRAVITY; // moveVertical
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
            if (!CONFIG.RIDING_BABIES && isBaby()) {
                return false; // do not ride babies
            }
            return tryRide(entityhuman, CONFIG.RIDING_SADDLE_REQUIRE, CONFIG.RIDING_SADDLE_CONSUME);
        }
        return false;
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        return (!(passenger instanceof Player) || passengers.isEmpty() || !passenger.equals(passengers.get(0))
                || new RidableDismountEvent(this, (Player) passenger).callEvent()) && super.removePassenger(passenger);
    }

    public class ParrotWASDController extends ControllerWASDFlyingWithSpacebar {
        private final RidableParrot parrot;

        public ParrotWASDController(RidableParrot parrot) {
            super(parrot, 0.5D);
            this.parrot = parrot;
        }

        @Override
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
                double speed = parrot.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
                if (parrot.onGround) {
                    speed *= groundSpeedModifier;
                }
                parrot.o((float) (e * speed)); // setAIMoveSpeed
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
