package net.pl3x.bukkit.ridables.entity.ai.goal.dolphin;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityDolphin;
import net.minecraft.server.v1_13_R2.EntityItem;
import net.minecraft.server.v1_13_R2.EnumItemSlot;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.pl3x.bukkit.ridables.entity.animal.RidableDolphin;

import java.util.List;

public class AIDolphinPlayWithItems extends PathfinderGoal {
    private final RidableDolphin dolphin;
    private int delay;

    public AIDolphinPlayWithItems(RidableDolphin dolphin) {
        this.dolphin = dolphin;
    }

    // shouldExecute
    @Override
    public boolean a() {
        if (delay > dolphin.ticksLived) {
            return false;
        }
        if (dolphin.getRider() != null) {
            return false;
        }
        List items = dolphin.world.a(EntityItem.class, dolphin.getBoundingBox().grow(8.0D, 8.0D, 8.0D), EntityDolphin.a);
        return !items.isEmpty() || !dolphin.getEquipment(EnumItemSlot.MAINHAND).isEmpty();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return a();
    }

    // startExecuting
    @Override
    public void c() {
        List items = dolphin.world.a(EntityItem.class, dolphin.getBoundingBox().grow(8.0D, 8.0D, 8.0D), EntityDolphin.a);
        if (!items.isEmpty()) {
            dolphin.getNavigation().a((Entity) items.get(0), (double) 1.2F);
            dolphin.a(SoundEffects.ENTITY_DOLPHIN_PLAY, 1.0F, 1.0F);
        }
        delay = 0;
    }

    // resetTask
    @Override
    public void d() {
        ItemStack stack = dolphin.getEquipment(EnumItemSlot.MAINHAND);
        if (!stack.isEmpty()) {
            dolphin.f(stack);
            dolphin.setSlot(EnumItemSlot.MAINHAND, ItemStack.a);
            delay = dolphin.ticksLived + dolphin.getRandom().nextInt(100);
        }
    }

    // tick
    @Override
    public void e() {
        List items = dolphin.world.a(EntityItem.class, dolphin.getBoundingBox().grow(8.0D, 8.0D, 8.0D), EntityDolphin.a);
        ItemStack itemstack = dolphin.getEquipment(EnumItemSlot.MAINHAND);
        if (!itemstack.isEmpty()) {
            dolphin.f(itemstack);
            dolphin.setSlot(EnumItemSlot.MAINHAND, ItemStack.a);
        } else if (!items.isEmpty()) {
            dolphin.getNavigation().a((Entity) items.get(0), (double) 1.2F);
        }

    }
}
