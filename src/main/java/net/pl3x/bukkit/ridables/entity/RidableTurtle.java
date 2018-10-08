package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.CriterionTriggers;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityTurtle;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDWater;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public class RidableTurtle extends EntityTurtle implements RidableEntity {
    private ControllerWASD wasdControllerLand;
    private ControllerWASDWater wasdControllerWater;

    public RidableTurtle(World world) {
        super(world);
        wasdControllerLand = new ControllerWASD(this);
        wasdControllerWater = new ControllerWASDWater(this);
        moveController = wasdControllerLand;
        initAI();
    }

    public RidableType getType() {
        return RidableType.TURTLE;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
    }

    // canBeRiddenInWater
    public boolean aY() {
        return true;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.TURTLE_JUMP_POWER;
    }

    protected void mobTick() {
        Q = Config.TURTLE_STEP_HEIGHT;
        if (isInWater() && getRider() != null) {
            motY += 0.005D;
        }
        super.mobTick();
    }

    public float getSpeed() {
        return isInWater() ? Config.TURTLE_SPEED_WATER : Config.TURTLE_SPEED_LAND;
    }

    // processInteract
    public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        if (itemstack.getItem() == Items.WATER_BUCKET && isAlive() && hasCollectPerm((Player) entityhuman.getBukkitEntity())) {
            a(SoundEffects.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
            itemstack.subtract(1);
            ItemStack bucket = CraftItemStack.asNMSCopy(Bucket.TURTLE.getItemStack());
            // TODO set custom name
            CriterionTriggers.j.a((EntityPlayer) entityhuman, bucket);
            if (itemstack.isEmpty()) {
                entityhuman.a(enumhand, bucket);
            } else if (!entityhuman.inventory.pickup(bucket)) {
                entityhuman.drop(bucket, false);
            }
            die();
            return true;
        }
        if (passengers.isEmpty() && !entityhuman.isPassenger() && !entityhuman.isSneaking()) {
            return enumhand == EnumHand.MAIN_HAND && tryRide(entityhuman, itemstack);
        }
        return passengers.isEmpty() && super.a(entityhuman, enumhand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }
}
