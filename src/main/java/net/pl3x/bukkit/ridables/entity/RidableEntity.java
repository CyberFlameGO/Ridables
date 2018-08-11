package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.EntityPlayer;
import org.bukkit.inventory.ItemStack;

public interface RidableEntity {
    boolean isActionableItem(ItemStack itemstack);

    void setRotation(float yaw, float pitch);

    float getJumpPower();

    float getSpeed();

    EntityPlayer getRider();

    void useAIController();

    void useWASDController();
}
