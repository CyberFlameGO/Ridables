package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntitySquid;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.Particles;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.minecraft.server.v1_13_R2.World;
import net.minecraft.server.v1_13_R2.WorldServer;
import net.pl3x.bukkit.ridables.configuration.mob.SquidConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASDWater;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.squid.AISquidFlee;
import net.pl3x.bukkit.ridables.entity.ai.goal.squid.AISquidMove;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import net.pl3x.bukkit.ridables.util.Const;
import org.bukkit.entity.Player;

public class RidableSquid extends EntitySquid implements RidableEntity {
    public static final SquidConfig CONFIG = new SquidConfig();

    private int spacebarCooldown = 0;

    public RidableSquid(World world) {
        super(world);
        moveController = new ControllerWASDWater(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.SQUID;
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
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        goalSelector.a(0, new AISquidMove(this));
        goalSelector.a(1, new AISquidFlee(this));
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return true;
    }

    @Override
    protected void mobTick() {
        if (spacebarCooldown > 0) {
            spacebarCooldown--;
        }
    }

    // travel
    @Override
    public void a(float strafe, float vertical, float forward) {
        super.a(strafe, vertical, forward);
        checkMove();
    }

    @Override
    public boolean onSpacebar() {
        if (spacebarCooldown == 0 && getRider().getBukkitEntity().hasPermission("allow.special.squid")) {
            spacebarCooldown = CONFIG.RIDING_INK_COOLDOWN;
            squirtInk();
        }
        return false;
    }

    public void squirtInk() {
        a(SoundEffects.ENTITY_SQUID_SQUIRT, getDeathSoundVolume(), getDeathSoundPitch()); // playSound
        Vec3D pos = applyCurrentRotation(new Vec3D(0.0D, -1.0D, 0.0D)).add(locX, locY, locZ);
        for (int i = 0; i < 30; ++i) {
            Vec3D offset = applyCurrentRotation(new Vec3D(random.nextDouble() * 0.6D - 0.3D, -1.0D, random.nextDouble() * 0.6D - 0.3D)).a(random.nextDouble() * 2.0D + 0.3D); // scale
            ((WorldServer) world).a(Particles.V, pos.x, pos.y + 0.5D, pos.z, 0, offset.x, offset.y, offset.z, 0.1D); // spawnParticle SQUID_INK
        }
    }

    private Vec3D applyCurrentRotation(Vec3D vec) {
        return vec.a(b * Const.DEG2RAD_FLOAT).b(-aR * Const.DEG2RAD_FLOAT); // rotatePitch prevSquidPitch rotateYaw prevRenderYawOffset
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (super.a(entityhuman, hand)) {
            return true; // handled by vanilla action
        }
        if (collectInWaterBucket(entityhuman, hand)) {
            return true; // handled
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
}
