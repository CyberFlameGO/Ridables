package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.Blocks;
import net.minecraft.server.v1_13_R1.ControllerLook;
import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.DamageSource;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EntitySnowman;
import net.minecraft.server.v1_13_R1.EnumHand;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.IBlockData;
import net.minecraft.server.v1_13_R1.Items;
import net.minecraft.server.v1_13_R1.MathHelper;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.MaterialSetTag;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.BlankLookController;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.event.CraftEventFactory;
import org.bukkit.inventory.ItemStack;

public class EntityRidableSnowman extends EntitySnowman implements RidableEntity {
    public static final MaterialSetTag PUMPKIN = new MaterialSetTag()
            .add(Material.CARVED_PUMPKIN, Material.JACK_O_LANTERN, Material.PUMPKIN);

    private ControllerMove aiController;
    private ControllerWASD wasdController;
    private ControllerLook defaultLookController;
    private BlankLookController blankLookController;

    public EntityRidableSnowman(World world) {
        super(world);
        aiController = moveController;
        wasdController = new ControllerWASD(this);
        defaultLookController = lookController;
        blankLookController = new BlankLookController(this);
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return PUMPKIN.isTagged(itemstack) || itemstack.getType() == Material.SHEARS;
    }

    protected boolean isTypeNotPersistent() {
        return false;
    }

    public boolean aY() {
        return true; // dont eject passengers when in water
    }

    protected void mobTick() {
        EntityPlayer rider = getRider();
        if (rider != null) {
            setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
        }
        super.mobTick();
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return super.cG() * getJumpPower() * 2.2F;
    }

    public void setRotation(float newYaw, float newPitch) {
        setYawPitch(lastYaw = yaw = newYaw, pitch = newPitch * 0.5F);
        aS = aQ = yaw;
    }

    public float getJumpPower() {
        return Config.SNOWMAN_JUMP_POWER;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.SNOWMAN_SPEED;
    }

    public EntityPlayer getRider() {
        if (passengers != null && !passengers.isEmpty()) {
            Entity entity = passengers.get(0);
            if (entity instanceof EntityPlayer) {
                return (EntityPlayer) entity;
            }
        }
        return null;
    }

    public void useAIController() {
        if (moveController != aiController) {
            moveController = aiController;
            lookController = defaultLookController;
        }
    }

    public void useWASDController() {
        if (moveController != wasdController) {
            moveController = wasdController;
            lookController = blankLookController;
        }
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
        if (!hasPumpkin() && PUMPKIN.isTagged(itemstack.asBukkitMirror())) {
            setHasPumpkin(true);
            if (!player.abilities.canInstantlyBuild) {
                itemstack.subtract(1);
            }
        } else if (hasPumpkin() && itemstack.getItem() == Items.SHEARS) {
            getBukkitEntity().getWorld().dropItemNaturally(getBukkitEntity().getLocation(),
                    new ItemStack(Material.CARVED_PUMPKIN));
        }
        return super.a(player, hand);
    }
}
