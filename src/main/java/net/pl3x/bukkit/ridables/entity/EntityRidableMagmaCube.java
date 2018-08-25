package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.DamageSource;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.EnumDifficulty;
import net.minecraft.server.v1_13_R1.FluidType;
import net.minecraft.server.v1_13_R1.GeneratorAccess;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.IWorldReader;
import net.minecraft.server.v1_13_R1.LootTables;
import net.minecraft.server.v1_13_R1.MinecraftKey;
import net.minecraft.server.v1_13_R1.ParticleParam;
import net.minecraft.server.v1_13_R1.Particles;
import net.minecraft.server.v1_13_R1.SoundEffect;
import net.minecraft.server.v1_13_R1.SoundEffects;
import net.minecraft.server.v1_13_R1.Tag;
import net.minecraft.server.v1_13_R1.TagsFluid;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;

import javax.annotation.Nullable;

public class EntityRidableMagmaCube extends EntityRidableSlime implements RidableEntity {
    public EntityRidableMagmaCube(World world) {
        super(EntityTypes.MAGMA_CUBE, world);
    }

    public RidableType getType() {
        return RidableType.MAGMA_CUBE;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.MAGMA_CUBE_SPEED;
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.20000000298023224D);
    }

    // canSpawn
    public boolean a(GeneratorAccess generatoraccess) {
        return generatoraccess.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    // isNotColliding
    public boolean a(IWorldReader iworldreader) {
        return iworldreader.b(this, this.getBoundingBox()) && iworldreader.getCubes(this, this.getBoundingBox()) && !iworldreader.containsLiquid(this.getBoundingBox());
    }

    public void setSize(int i, boolean flag) {
        super.setSize(i, flag);
        this.getAttributeInstance(GenericAttributes.h).setValue((double) (i * 3));
    }

    // getBrightness
    public float az() {
        return 1.0F;
    }

    // flame particles
    protected ParticleParam l() {
        return Particles.y;
    }

    // getLootTable
    protected MinecraftKey G() {
        return this.dz() ? LootTables.a : LootTables.ap;
    }

    public boolean isBurning() {
        return false;
    }

    // getJumpDelay
    protected int ds() {
        return super.ds() * 4;
    }

    // alterSquishAmount
    protected void dt() {
        this.a *= 0.9F;
    }

    // jump
    protected void cH() {
        this.motY = (double) (0.42F + (float) this.getSize() * 0.1F);
        this.impulse = true;
    }

    // handleFluidJump
    protected void c(Tag<FluidType> tag) {
        if (tag == TagsFluid.b) {
            this.motY = (double) (0.22F + (float) this.getSize() * 0.05F);
            this.impulse = true;
        } else {
            super.c(tag);
        }

    }

    // fall
    public void c(float f, float f1) {
    }

    // canDamagePlayer
    protected boolean du() {
        return true;
    }

    // getAttackStrength
    protected int dv() {
        return super.dv() + 2;
    }

    // getHurtSound
    protected SoundEffect d(DamageSource damagesource) {
        return this.dz() ? SoundEffects.ENTITY_MAGMA_CUBE_HURT_SMALL : SoundEffects.ENTITY_MAGMA_CUBE_HURT;
    }

    // getDeathSound
    protected SoundEffect cs() {
        return this.dz() ? SoundEffects.ENTITY_MAGMA_CUBE_DEATH_SMALL : SoundEffects.ENTITY_MAGMA_CUBE_DEATH;
    }

    // getSquishSound
    protected SoundEffect dw() {
        return this.dz() ? SoundEffects.ENTITY_MAGMA_CUBE_SQUISH_SMALL : SoundEffects.ENTITY_MAGMA_CUBE_SQUISH;
    }

    // getJumpSound
    protected SoundEffect dx() {
        return SoundEffects.ENTITY_MAGMA_CUBE_JUMP;
    }
}
