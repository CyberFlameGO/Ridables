package net.pl3x.bukkit.ridables.entity.animal.fish;

import net.minecraft.server.v1_13_R2.ControllerMove;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityCod;
import net.minecraft.server.v1_13_R2.EntityFish;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.IEntitySelector;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.TagsFluid;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.CodConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASDWater;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIPanic;
import net.pl3x.bukkit.ridables.entity.ai.goal.fish.AIFishFollowLeader;
import net.pl3x.bukkit.ridables.entity.ai.goal.fish.AIFishSwim;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.entity.Player;

public class RidableCod extends EntityCod implements RidableEntity, RidableFishSchool {
    public static final CodConfig CONFIG = new CodConfig();

    public RidableCod(World world) {
        super(world);
        moveController = new FishWASDController(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.COD;
    }

    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED); // registerAttribute
        reloadAttributes();
    }

    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
    }

    // initAI - override vanilla AI
    protected void n() {
        // from EntityFish
        goalSelector.a(0, new AIPanic(this, 1.25D));
        goalSelector.a(2, new AIAvoidTarget<>(this, EntityHuman.class, 8.0F, 1.6D, 1.4D, IEntitySelector.f));
        goalSelector.a(4, new AIFishSwim(this));

        // from EntityCod
        goalSelector.a(5, new AIFishFollowLeader(this));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return true;
    }

    public boolean isFollowing() {
        return dy();
    }

    // onLivingUpdate
    public void k() {
        if (getRider() != null) {
            motY += 0.005D;
        }
        super.k();
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
    public boolean removePassenger(Entity passenger) {
        return (!(passenger instanceof Player) || passengers.isEmpty() || !passenger.equals(passengers.get(0))
                || new RidableDismountEvent(this, (Player) passenger).callEvent()) && super.removePassenger(passenger);
    }

    public static class FishWASDController extends ControllerWASDWater {
        private final EntityFish fish;

        public FishWASDController(RidableEntity ridable) {
            super(ridable);
            this.fish = (EntityFish) ridable;
        }

        public void tick() {
            if (fish.a(TagsFluid.WATER)) {
                fish.motY += 0.005D;
            }
            if (h == ControllerMove.Operation.MOVE_TO && !fish.getNavigation().p()) {
                double x = b - fish.locX;
                double y = c - fish.locY;
                double z = d - fish.locZ;
                y /= (double) MathHelper.sqrt(x * x + y * y + z * z);
                fish.aQ = fish.yaw = a(fish.yaw, (float) (MathHelper.c(z, x) * (double) (180F / (float) Math.PI)) - 90.0F, 90.0F);
                fish.o(fish.cK() + ((float) (e * fish.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()) - fish.cK()) * 0.125F);
                fish.motY += (double) fish.cK() * y * 0.1D;
            } else {
                fish.o(0.0F);
            }
        }
    }
}
