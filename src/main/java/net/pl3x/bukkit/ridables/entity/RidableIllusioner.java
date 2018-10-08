package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityIllagerIllusioner;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EntityVillager;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GeneratorAccess;
import net.minecraft.server.v1_13_R2.IWorldReader;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AIShootBow;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AIWander;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.illusioner.AIIllusionerBlindnessSpell;
import net.pl3x.bukkit.ridables.entity.ai.illusioner.AIIllusionerCastingSpell;
import net.pl3x.bukkit.ridables.entity.ai.illusioner.AIIllusionerMirrorSpell;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableIllusioner extends EntityIllagerIllusioner implements RidableEntity {
    public RidableIllusioner(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.ILLUSIONER;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
        goalSelector.a(0, new AISwim(this));
        goalSelector.a(1, new AIIllusionerCastingSpell(this));
        goalSelector.a(4, new AIIllusionerMirrorSpell(this));
        goalSelector.a(5, new AIIllusionerBlindnessSpell(this));
        goalSelector.a(6, new AIShootBow<>(this, 0.5D, 20, 15.0F));
        goalSelector.a(8, new AIWander(this, 0.6D));
        goalSelector.a(9, new AIWatchClosest(this, EntityHuman.class, 3.0F, 1.0F));
        goalSelector.a(10, new AIWatchClosest(this, EntityInsentient.class, 8.0F));
        targetSelector.a(1, new AIHurtByTarget(this, true, EntityIllagerIllusioner.class));
        targetSelector.a(2, (new AIAttackNearest<>(this, EntityHuman.class, true)).b(300));
        targetSelector.a(3, (new AIAttackNearest<>(this, EntityVillager.class, false)).b(300));
        targetSelector.a(3, (new AIAttackNearest<>(this, EntityIronGolem.class, false)).b(300));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.ILLUSIONER_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.ILLUSIONER_JUMP_POWER;
    }

    public int getSpellTicks() {
        return b;
    }

    public void setSpellTicks(int ticks) {
        b = ticks;
    }

    // isValidLightLevel
    protected boolean K_() {
        BlockPosition pos = new BlockPosition(locX, getBoundingBox().b, locZ);
        return (world.Y() ? world.getLightLevel(pos, 10) : world.getLightLevel(pos)) <= Config.ILLUSIONER_SPAWN_LIGHT_LEVEL;
    }

    // func_205022_a
    public float a(BlockPosition pos, IWorldReader world) {
        return 1.0F;
    }

    // canSpawn
    public boolean a(GeneratorAccess world) {
        return super.a(world) && a(new BlockPosition(locX, getBoundingBox().b, locZ), world) >= 0.0F;
    }

    protected void mobTick() {
        Q = Config.ILLUSIONER_STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return Config.ILLUSIONER_SPEED;
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
}
