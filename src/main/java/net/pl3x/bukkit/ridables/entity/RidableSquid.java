package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.CriterionTriggers;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntitySquid;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.entity.ai.squid.AISquidFlee;
import net.pl3x.bukkit.ridables.entity.ai.squid.AISquidMoveRandom;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDWater;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RidableSquid extends EntitySquid implements RidableEntity {
    private static Method dy;

    static {
        try {
            dy = EntitySquid.class.getDeclaredMethod("dy");
            dy.setAccessible(true);
        } catch (NoSuchMethodException ignore) {
        }
    }

    private int spacebarCooldown = 0;

    public RidableSquid(World world) {
        super(world);
        moveController = new ControllerWASDWater(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.SQUID;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(0, new AISquidMoveRandom(this));
        goalSelector.a(1, new AISquidFlee(this));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return true;
    }

    protected void mobTick() {
        if (spacebarCooldown > 0) {
            spacebarCooldown--;
        }
    }

    public float getSpeed() {
        return Config.SQUID_SPEED;
    }

    public boolean onSpacebar() {
        if (spacebarCooldown == 0 && hasSpecialPerm(getRider().getBukkitEntity())) {
            spacebarCooldown = Config.SQUID_INK_COOLDOWN;
            squirtInk();
        }
        return false;
    }

    public void squirtInk() {
        try {
            dy.invoke(this);
        } catch (IllegalAccessException | InvocationTargetException ignore) {
        }
    }

    // processInteract
    public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        if (itemstack.getItem() == Items.WATER_BUCKET && isAlive() && hasCollectPerm((Player) entityhuman.getBukkitEntity())) {
            a(SoundEffects.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
            itemstack.subtract(1);
            ItemStack bucket = CraftItemStack.asNMSCopy(Bucket.SQUID.getItemStack());
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
            return enumhand == EnumHand.MAIN_HAND && tryRide(entityhuman, entityhuman.b(enumhand));
        }
        return passengers.isEmpty() && super.a(entityhuman, enumhand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }
}
