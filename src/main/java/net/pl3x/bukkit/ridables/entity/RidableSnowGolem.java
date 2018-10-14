package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityItem;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntitySnowman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.IMonster;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.SnowGolemConfig;
import net.pl3x.bukkit.ridables.data.MaterialSetTag;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackRanged;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class RidableSnowGolem extends EntitySnowman implements RidableEntity {
    public static final SnowGolemConfig CONFIG = new SnowGolemConfig();
    public static final MaterialSetTag PUMPKIN = new MaterialSetTag()
            .add(Material.CARVED_PUMPKIN, Material.JACK_O_LANTERN, Material.PUMPKIN);

    public RidableSnowGolem(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.SNOWMAN;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(1, new AIAttackRanged(this, 1.25D, 20, 10.0F));
        goalSelector.a(2, new AIWanderAvoidWater(this, 1.0D, 0.00001F));
        goalSelector.a(3, new AIWatchClosest(this, EntityHuman.class, 6.0F));
        goalSelector.a(4, new AILookIdle(this));
        targetSelector.a(1, new AIAttackNearest<>(this, EntityInsentient.class, 10, true, false, IMonster.d));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return CONFIG.JUMP_POWER;
    }

    protected void mobTick() {
        Q = CONFIG.STEP_HEIGHT;
        super.mobTick();
    }

    // onLivingUpdate
    public void k() {
        super.k();
        int x = MathHelper.floor(locX);
        int y = MathHelper.floor(locY);
        int z = MathHelper.floor(locZ);
        if (ap() && CONFIG.DAMAGE_WHEN_WET) { // isWet
            damageEntity(DamageSource.DROWN, 1.0F);
        }
        if (world.getBiome(new BlockPosition(x, 0, z)).c(new BlockPosition(x, y, z)) > 1.0F && CONFIG.DAMAGE_WHEN_HOT) { // biome.getTemperature(pos)
            damageEntity(CraftEventFactory.MELTING, 1.0F);
        }
        if (!(world.getGameRules().getBoolean("mobGriefing") && CONFIG.LEAVE_SNOW_TRAIL)) {
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
        return CONFIG.SPEED;
    }

    // processInteract
    public boolean a(EntityHuman player, EnumHand hand) {
        ItemStack itemstack = player.b(hand);
        if (!hasPumpkin() && PUMPKIN.isTagged(CraftItemStack.asCraftMirror(itemstack))) {
            setHasPumpkin(true);
            if (!player.abilities.canInstantlyBuild) {
                itemstack.subtract(1);
            }
            return true; // handled
        } else if (hasPumpkin() && itemstack.getItem() == Items.SHEARS) {
            PlayerShearEntityEvent event = new PlayerShearEntityEvent((Player) player.getBukkitEntity(), getBukkitEntity());
            world.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return false;
            }
            setHasPumpkin(false);
            itemstack.damage(1, player);
            EntityItem pumpkin = new EntityItem(world, locX, locY, locZ, new ItemStack(Blocks.PUMPKIN.getItem()));
            pumpkin.pickupDelay = 10;
            world.addEntity(pumpkin, CreatureSpawnEvent.SpawnReason.CUSTOM);
            return true; // handled
        }
        return super.a(player, hand) || processInteract(player, hand);
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
