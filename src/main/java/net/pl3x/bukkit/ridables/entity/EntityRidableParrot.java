package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.AttributeInstance;
import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityParrot;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDFlyingWithSpacebar;
import net.pl3x.bukkit.ridables.MaterialSetTag;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EntityRidableParrot extends EntityParrot implements RidableEntity {
    public static final MaterialSetTag FOOD = new MaterialSetTag()
            .add(Material.WHEAT_SEEDS, Material.MELON_SEEDS, Material.PUMPKIN_SEEDS, Material.BEETROOT_SEEDS);

    private ControllerMove aiController;
    private ControllerWASDFlyingWithSpacebar wasdController;

    public EntityRidableParrot(World world) {
        super(world);
        aiController = moveController;
        wasdController = new ControllerWASDFlyingWithSpacebar(this);
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return FOOD.isTagged(itemstack) || itemstack.getType() == Material.COOKIE;
    }

    protected void mobTick() {
        EntityPlayer rider = getRider();
        if (rider != null) {
            setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
            motY += bi > 0 ? 0.07F * Config.PARROT_VERTICAL : 0.04704F - Config.PARROT_GRAVITY;
        }
        super.mobTick();
    }

    public void setRotation(float newYaw, float newPitch) {
        setYawPitch(lastYaw = yaw = newYaw, pitch = newPitch * 0.5F);
        aS = aQ = yaw;
    }

    public float getJumpPower() {
        return 0;
    }

    public float getSpeed() {
        AttributeInstance attr = getAttributeInstance(GenericAttributes.e);
        float speed = (float) (attr != null ? attr.getValue() : getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
        return speed * Config.PARROT_SPEED;
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
        }
    }

    public void useWASDController() {
        if (moveController != wasdController) {
            moveController = wasdController;
        }
    }

    public void onSpacebar() {
    }
}
