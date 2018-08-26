package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHorseZombie;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.listener.RideListener;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class EntityRidableZombieHorse extends EntityHorseZombie implements RidableEntity {
    private EntityPlayer rider;

    public EntityRidableZombieHorse(World world) {
        super(world);
    }

    public RidableType getType() {
        return RidableType.ZOMBIE_HORSE;
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return f(CraftItemStack.asNMSCopy(itemstack));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return true;
    }

    public void mobTick() {
        updateRider();
        super.mobTick();
    }

    public void setRotation(float newYaw, float newPitch) {
    }

    public EntityPlayer getRider() {
        return rider;
    }

    public EntityPlayer updateRider() {
        if (passengers == null || passengers.isEmpty()) {
            rider = null;
        } else {
            Entity entity = passengers.get(0);
            rider = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
        }
        return rider;
    }

    public void useAIController() {
    }

    public void useWASDController() {
    }

    // mountTo
    protected void g(EntityHuman entityhuman) {
        RideListener.override.add(entityhuman.getUniqueID());
        super.g(entityhuman);
        RideListener.override.remove(entityhuman.getUniqueID());
    }

    public boolean isTamed() {
        return true;
    }

    // addSwimmingPathfinder
    protected void dI() {
        goalSelector.a(0, new PathfinderGoalFloat(this));
    }
}
