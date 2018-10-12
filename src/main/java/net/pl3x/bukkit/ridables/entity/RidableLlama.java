package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLlama;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableLlama extends EntityLlama implements RidableEntity {
    public RidableLlama(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.LLAMA;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.LLAMA_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.LLAMA_JUMP_POWER;
    }

    protected void mobTick() {
        Q = Config.LLAMA_STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return Config.LLAMA_SPEED;
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

    public RidableLlama createChild(EntityAgeable entity) {
        return b(entity);
    }

    // createChild (bukkit's weird duplicate method)
    public RidableLlama b(EntityAgeable entity) {
        RidableLlama baby = new RidableLlama(world);
        a(entity, baby); // setOffspringAttributes
        EntityLlama otherParent = (EntityLlama) entity;
        int strength = random.nextInt(Math.max(getStrength(), otherParent.getStrength())) + 1;
        if (random.nextFloat() < 0.03F) {
            ++strength;
        }
        baby.setStrength(strength);
        baby.setVariant(random.nextBoolean() ? getVariant() : otherParent.getVariant());
        return baby;
    }

    public boolean isLeashed() {
        return getRider() != null || super.isLeashed();
    }

    public Entity getLeashHolder() {
        EntityPlayer rider = getRider();
        return rider != null ? rider : super.getLeashHolder();
    }

    // hasCaravan
    public boolean em() {
        return (getRider() != null && Config.LLAMA_CARAVAN) || super.em();
    }
}
