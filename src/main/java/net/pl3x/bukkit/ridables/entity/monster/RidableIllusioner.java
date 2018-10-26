package net.pl3x.bukkit.ridables.entity.monster;

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
import net.pl3x.bukkit.ridables.configuration.mob.IllusionerConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIShootBow;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWander;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.illusioner.AIIllusionerBlindnessSpell;
import net.pl3x.bukkit.ridables.entity.ai.goal.illusioner.AIIllusionerCastingSpell;
import net.pl3x.bukkit.ridables.entity.ai.goal.illusioner.AIIllusionerMirrorSpell;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;

public class RidableIllusioner extends EntityIllagerIllusioner implements RidableEntity {
    public static IllusionerConfig CONFIG = new IllusionerConfig();

    public RidableIllusioner(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.ILLUSIONER;
    }

    // initAI - override vanilla AI
    protected void n() {
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
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return getRider() == null ? super.cG() : CONFIG.JUMP_POWER;
    }

    public int getSpellTicks() {
        return b;
    }

    public void setSpellTicks(int ticks) {
        b = ticks;
    }

    // isValidLightLevel
    protected boolean K_() {
        BlockPosition pos = new BlockPosition(locX, getBoundingBox().minY, locZ);
        return (world.Y() ? world.getLightLevel(pos, 10) : world.getLightLevel(pos)) <= CONFIG.SPAWN_LIGHT_LEVEL;
    }

    // func_205022_a
    public float a(BlockPosition pos, IWorldReader world) {
        return 1.0F;
    }

    // canSpawn
    public boolean a(GeneratorAccess world) {
        return super.a(world) && a(new BlockPosition(locX, getBoundingBox().minY, locZ), world) >= 0.0F;
    }

    protected void mobTick() {
        Q = CONFIG.STEP_HEIGHT;
        super.mobTick();
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
