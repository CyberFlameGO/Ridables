package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntitySquid;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.SquidConfig;
import net.pl3x.bukkit.ridables.entity.ai.squid.AISquidFlee;
import net.pl3x.bukkit.ridables.entity.ai.squid.AISquidMoveRandom;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDWater;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RidableSquid extends EntitySquid implements RidableEntity {
    public static final SquidConfig CONFIG = new SquidConfig();

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

    public boolean onSpacebar() {
        if (spacebarCooldown == 0 && hasSpecialPerm(getRider().getBukkitEntity())) {
            spacebarCooldown = CONFIG.INK_COOLDOWN;
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
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }
}
