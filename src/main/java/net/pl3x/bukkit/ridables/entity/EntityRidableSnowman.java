package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.Blocks;
import net.minecraft.server.v1_13_R1.DamageSource;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EntitySnowman;
import net.minecraft.server.v1_13_R1.EnumHand;
import net.minecraft.server.v1_13_R1.IBlockData;
import net.minecraft.server.v1_13_R1.MathHelper;
import net.minecraft.server.v1_13_R1.MobEffect;
import net.minecraft.server.v1_13_R1.MobEffects;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.util.Mover;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.event.CraftEventFactory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class EntityRidableSnowman extends EntitySnowman implements RidableEntity {
    private static Field jumping;
    private boolean isJumping = false;

    public EntityRidableSnowman(World world) {
        super(world);

        if (jumping == null) {
            try {
                jumping = EntityLiving.class.getDeclaredField("bg");
                jumping.setAccessible(true);
            } catch (NoSuchFieldException ignore) {
            }
        }
    }

    public boolean isFood(ItemStack itemstack) {
        return itemstack.getType() == Material.CARVED_PUMPKIN || itemstack.getType() == Material.SHEARS;
    }

    // travel(strafe, vertical, forward)
    @Override
    public void a(float f, float f1, float f2) {
        EntityPlayer rider = getRider();
        if (rider != null) {
            // do not target anything while being ridden
            setGoalTarget(null, null, false);

            // eject rider if in water or lava
            if (isInWater() || ax()) {
                ejectPassengers();
                rider.stopRiding();
                return;
            }

            // rotation
            setYawPitch(lastYaw = yaw = rider.yaw, pitch = rider.pitch * 0.5F);
            aS = aQ = yaw;

            // controls
            float forward = rider.bj;
            float strafe = rider.bh * 0.5F;
            if (forward <= 0.0F) {
                forward *= 0.25F;
            }

            if (jumping != null && !isJumping) {
                try {
                    isJumping = jumping.getBoolean(rider);
                } catch (IllegalAccessException ignore) {
                }
            }

            if (isJumping && onGround) {
                motY = (double) Config.SNOWMAN_JUMP_POWER;
                MobEffect jump = getEffect(MobEffects.JUMP);
                if (jump != null) {
                    motY += (double) ((float) (jump.getAmplifier() + 1) * 0.1F);
                }
                impulse = true;
                if (forward > 0.0F) {
                    motX += (double) (-0.4F * MathHelper.sin(yaw * 0.017453292F) * Config.SNOWMAN_JUMP_POWER);
                    motZ += (double) (0.4F * MathHelper.cos(yaw * 0.017453292F) * Config.SNOWMAN_JUMP_POWER);
                }
            }

            // move
            Mover.moveOnLand(this, strafe, f1, forward, 0.5F * Config.SNOWMAN_SPEED);

            if (onGround) {
                isJumping = false;
            }
            return;
        }
        super.a(f, f1, f2);
    }

    // onLivingUpdate
    public void k() {
        super.k();
        int x = MathHelper.floor(locX);
        int y = MathHelper.floor(locY);
        int z = MathHelper.floor(locZ);
        if (ap() && Config.SNOWMAN_DAMAGE_WHEN_WET) { // isWet
            damageEntity(DamageSource.DROWN, 1.0F);
        }
        if (world.getBiome(new BlockPosition(x, 0, z)).c(new BlockPosition(x, y, z)) > 1.0F && Config.SNOWMAN_DAMAGE_WHEN_HOT) { // biome.getTemperature(pos)
            damageEntity(CraftEventFactory.MELTING, 1.0F);
        }
        if (!(world.getGameRules().getBoolean("mobGriefing") && Config.SNOWMAN_LEAVE_SNOW_TRAIL)) {
            return; // not allowed to grief world (placing snow layers where walking)
        }
        IBlockData block = Blocks.SNOW.getBlockData();
        for (int l = 0; l < 4; ++l) {
            x = MathHelper.floor(locX + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
            y = MathHelper.floor(locY);
            z = MathHelper.floor(locZ + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
            BlockPosition pos = new BlockPosition(x, y, z);
            if (world.getType(pos).isAir() && world.getBiome(pos).c(pos) < 0.8F && block.canPlace(world, pos)) {
                org.bukkit.craftbukkit.v1_13_R1.event.CraftEventFactory.handleBlockFormEvent(world, pos, block, this);
            }
        }
    }

    // attackEntityWithRangedAttack
    public void a(EntityLiving target, float distanceFactor) {
        if (getRider() == null) {
            super.a(target, distanceFactor);
        }
    }

    // processInteract
    protected boolean a(EntityHuman player, EnumHand hand) {
        net.minecraft.server.v1_13_R1.ItemStack itemstack = player.b(hand);
        if (itemstack.getItem() == Blocks.CARVED_PUMPKIN.getItem() && !hasPumpkin()) {
            setHasPumpkin(true);
            if (!player.abilities.canInstantlyBuild) {
                itemstack.subtract(1);
            }
        }
        return super.a(player, hand);
    }

    private EntityPlayer getRider() {
        if (passengers != null && !passengers.isEmpty()) {
            Entity entity = passengers.get(0); // only care about first rider
            if (entity instanceof EntityPlayer) {
                return (EntityPlayer) entity;
            }
        }
        return null; // aww, lonely snowman is lonely
    }
}
