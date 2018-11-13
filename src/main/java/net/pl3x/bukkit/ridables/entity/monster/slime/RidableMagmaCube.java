package net.pl3x.bukkit.ridables.entity.monster.slime;

import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.EnumDifficulty;
import net.minecraft.server.v1_13_R2.FluidType;
import net.minecraft.server.v1_13_R2.GeneratorAccess;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.IWorldReader;
import net.minecraft.server.v1_13_R2.LootTables;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import net.minecraft.server.v1_13_R2.ParticleParam;
import net.minecraft.server.v1_13_R2.Particles;
import net.minecraft.server.v1_13_R2.SoundEffect;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.Tag;
import net.minecraft.server.v1_13_R2.TagsFluid;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.MagmaCubeConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;

public class RidableMagmaCube extends RidableSlime implements RidableEntity {
    public static final MagmaCubeConfig CONFIG = new MagmaCubeConfig();

    public RidableMagmaCube(World world) {
        super(EntityTypes.MAGMA_CUBE, world);
        fireProof = true;
    }

    @Override
    public RidableType getType() {
        return RidableType.MAGMA_CUBE;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !hasCustomName() && !isLeashed();
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.2D);
    }

    // canSpawn
    @Override
    public boolean a(GeneratorAccess world, boolean var2) {
        return world.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    // isNotColliding
    @Override
    public boolean a(IWorldReader world) {
        return world.a_(this, getBoundingBox()) && world.getCubes(this, getBoundingBox()) && !world.containsLiquid(getBoundingBox());
    }

    @Override
    public void setSize(int size, boolean flag) {
        super.setSize(size, flag);
        getAttributeInstance(GenericAttributes.h).setValue((double) (size * 3));
    }

    // getBrightness
    @Override
    public float az() {
        return 1.0F;
    }

    // flame particles
    @Override
    protected ParticleParam l() {
        return Particles.y;
    }

    // getLootTable
    @Override
    protected MinecraftKey getDefaultLootTable() {
        return dy() ? LootTables.a : LootTables.ap;
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    // getJumpDelay
    @Override
    protected int dr() {
        return super.dr() * 4;
    }

    // alterSquishAmount
    @Override
    protected void ds() {
        a *= 0.9F;
    }

    // jump
    @Override
    protected void cH() {
        motY = (double) (0.42F + (float) getSize() * 0.1F) * getJumpCharge();
        impulse = true;
    }

    // handleFluidJump
    @Override
    protected void c(Tag<FluidType> tag) {
        if (tag == TagsFluid.LAVA) {
            motY = (double) (0.22F + (float) getSize() * 0.05F) * getJumpCharge();
            impulse = true;
        } else {
            super.c(tag);
        }
    }

    // fall
    @Override
    public void c(float f, float f1) {
        // does not take fall damage
    }

    // canDamagePlayer
    @Override
    protected boolean dt() {
        return cP();
    }

    // getAttackStrength
    @Override
    protected int du() {
        return super.du() + 2;
    }

    // getHurtSound
    @Override
    protected SoundEffect d(DamageSource damagesource) {
        return dz() ? SoundEffects.ENTITY_MAGMA_CUBE_HURT_SMALL : SoundEffects.ENTITY_MAGMA_CUBE_HURT;
    }

    // getDeathSound
    @Override
    protected SoundEffect cs() {
        return dz() ? SoundEffects.ENTITY_MAGMA_CUBE_DEATH_SMALL : SoundEffects.ENTITY_MAGMA_CUBE_DEATH;
    }

    // getSquishSound
    @Override
    protected SoundEffect dv() {
        return dz() ? SoundEffects.ENTITY_MAGMA_CUBE_SQUISH_SMALL : SoundEffects.ENTITY_MAGMA_CUBE_SQUISH;
    }

    // getJumpSound
    @Override
    protected SoundEffect dw() {
        return SoundEffects.ENTITY_MAGMA_CUBE_JUMP;
    }
}
