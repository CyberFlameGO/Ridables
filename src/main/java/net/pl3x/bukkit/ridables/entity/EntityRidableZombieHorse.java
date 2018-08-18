package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityHorseZombie;
import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.listener.RideListener;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class EntityRidableZombieHorse extends EntityHorseZombie implements RidableEntity {
    public EntityRidableZombieHorse(World world) {
        super(world);
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return f(CraftItemStack.asNMSCopy(itemstack));
    }

    public boolean aY() {
        return true;
    }

    public void setRotation(float newYaw, float newPitch) {
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

    // make swim
    protected void dJ() {
        goalSelector.a(0, new PathfinderGoalFloat(this));
    }
}
