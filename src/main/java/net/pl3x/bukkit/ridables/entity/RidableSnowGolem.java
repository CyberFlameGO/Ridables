package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntitySnowman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.data.MaterialSetTag;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class RidableSnowGolem extends EntitySnowman implements RidableEntity {
    public static final MaterialSetTag PUMPKIN = new MaterialSetTag()
            .add(Material.CARVED_PUMPKIN, Material.JACK_O_LANTERN, Material.PUMPKIN);

    public RidableSnowGolem(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.SNOWMAN;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
    }

    // canBeRiddenInWater
    public boolean aY() {
        return false;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.SNOWMAN_JUMP_POWER;
    }

    protected void mobTick() {
        Q = Config.SNOWMAN_STEP_HEIGHT;
        super.mobTick();
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
                CraftEventFactory.handleBlockFormEvent(world, pos, block, this);
            }
        }
    }

    public float getSpeed() {
        return Config.SNOWMAN_SPEED;
    }

    // processInteract
    public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        if (passengers.isEmpty() && !entityhuman.isPassenger() && !entityhuman.isSneaking()) {
            return enumhand == EnumHand.MAIN_HAND && tryRide(entityhuman, entityhuman.b(enumhand));
        }
        net.minecraft.server.v1_13_R2.ItemStack itemstack = entityhuman.b(enumhand);
        if (!hasPumpkin() && PUMPKIN.isTagged(CraftItemStack.asCraftMirror(itemstack))) {
            setHasPumpkin(true);
            if (!entityhuman.abilities.canInstantlyBuild) {
                itemstack.subtract(1);
                return true;
            }
        } else if (hasPumpkin() && itemstack.getItem() == Items.SHEARS) {
            getBukkitEntity().getWorld().dropItemNaturally(getBukkitEntity().getLocation(),
                    new ItemStack(Material.CARVED_PUMPKIN));
            return true;
        }
        return passengers.isEmpty() && super.a(entityhuman, enumhand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    // attackEntityWithRangedAttack
    public void a(EntityLiving target, float distanceFactor) {
        if (getRider() == null) {
            super.a(target, distanceFactor);
        }
    }
}
