package net.pl3x.bukkit.ridables.entity.monster.guardian;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.LootTables;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import net.minecraft.server.v1_13_R2.MobEffect;
import net.minecraft.server.v1_13_R2.MobEffects;
import net.minecraft.server.v1_13_R2.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_13_R2.SoundEffect;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.ElderGuardianConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPotionEffectEvent;

import javax.annotation.Nullable;
import java.util.List;

public class RidableElderGuardian extends RidableGuardian implements RidableEntity {
    public static final ElderGuardianConfig CONFIG = new ElderGuardianConfig();

    public RidableElderGuardian(World world) {
        super(EntityTypes.ELDER_GUARDIAN, world);
        setSize(width * 2.35F, length * 2.35F);
        di();
        if (goalRandomStroll != null) {
            goalRandomStroll.setTimeBetweenMovement(400);
        }
    }

    @Override
    public RidableType getType() {
        return RidableType.ELDER_GUARDIAN;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !hasCustomName() && !isLeashed();
    }

    @Override
    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_ATTACK_DAMAGE);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return true;
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if ((ticksLived + getId()) % 1200 == 0) {
            List<EntityPlayer> list = world.b(EntityPlayer.class, (player) -> h(player) < 2500.0D && player.playerInteractManager.c());
            for (EntityPlayer player : list) {
                MobEffect effect = player.getEffect(MobEffects.SLOWER_DIG);
                if (effect == null || effect.getAmplifier() < 2 || effect.getDuration() < 1200) {
                    player.playerConnection.sendPacket(new PacketPlayOutGameStateChange(10, 0.0F));
                    player.addEffect(new MobEffect(MobEffects.SLOWER_DIG, 6000, 2), EntityPotionEffectEvent.Cause.ATTACK);
                }
            }
        }
        if (!dw()) { // hasHome
            a(new BlockPosition(this), 16); // setHomePosAndDistance
        }
    }

    @Nullable
    @Override
    protected MinecraftKey getDefaultLootTable() {
        return LootTables.E;
    }

    @Override
    public int l() {
        return 60;
    }

    @Override
    protected SoundEffect D() {
        return this.aq() ? SoundEffects.ENTITY_ELDER_GUARDIAN_AMBIENT : SoundEffects.ENTITY_ELDER_GUARDIAN_AMBIENT_LAND;
    }

    @Override
    protected SoundEffect d(DamageSource damagesource) {
        return this.aq() ? SoundEffects.ENTITY_ELDER_GUARDIAN_HURT : SoundEffects.ENTITY_ELDER_GUARDIAN_HURT_LAND;
    }

    @Override
    protected SoundEffect cs() {
        return this.aq() ? SoundEffects.ENTITY_ELDER_GUARDIAN_DEATH : SoundEffects.ENTITY_ELDER_GUARDIAN_DEATH_LAND;
    }

    @Override
    protected SoundEffect dA() {
        return SoundEffects.ENTITY_ELDER_GUARDIAN_FLOP;
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (super.a(entityhuman, hand)) {
            return true; // handled by vanilla action
        }
        if (hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
            return tryRide(entityhuman, CONFIG.RIDING_SADDLE_REQUIRE, CONFIG.RIDING_SADDLE_CONSUME);
        }
        return false;
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        return (!(passenger instanceof Player) || passengers.isEmpty() || !passenger.equals(passengers.get(0))
                || new RidableDismountEvent(this, (Player) passenger).callEvent()) && super.removePassenger(passenger);
    }
}
