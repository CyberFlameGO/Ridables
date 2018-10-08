package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class RidableIronGolem extends EntityIronGolem implements RidableEntity {
    public RidableIronGolem(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.IRON_GOLEM;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.IRON_GOLEM_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.IRON_GOLEM_JUMP_POWER;
    }

    protected void mobTick() {
        Q = Config.IRON_GOLEM_STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return Config.IRON_GOLEM_SPEED;
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

    public boolean onClick(org.bukkit.entity.Entity entity, EnumHand hand) {
        handleClick(hand);
        return false;
    }

    public boolean onClick(Block block, BlockFace blockFace, EnumHand hand) {
        handleClick(hand);
        return false;
    }

    public boolean onClick(EnumHand hand) {
        handleClick(hand);
        return false;
    }

    private void handleClick(EnumHand hand) {
        if (hand == EnumHand.OFF_HAND) {
            a(dz() == 0); // toggle rose on right click
        } else {
            if (dz() > 0) {
                a(false); // remove rose
            }
            world.broadcastEntityEffect(this, (byte) 4);
        }
    }
}
