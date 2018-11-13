package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityItem;
import net.minecraft.server.v1_13_R2.EntitySnowman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.IMonster;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.SnowGolemConfig;
import net.pl3x.bukkit.ridables.data.MaterialSetTag;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackRanged;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
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

    @Override
    public RidableType getType() {
        return RidableType.SNOWMAN;
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED); // registerAttribute
        reloadAttributes();
    }

    @Override
    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        goalSelector.a(1, new AIAttackRanged(this, 1.25D, 20, 10.0F));
        goalSelector.a(2, new AIWanderAvoidWater(this, 1.0D, 0.00001F));
        goalSelector.a(3, new AIWatchClosest(this, EntityHuman.class, 6.0F));
        goalSelector.a(4, new AILookIdle(this));
        targetSelector.a(1, new AIAttackNearest<>(this, EntityInsentient.class, 10, true, false, IMonster.d));
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cG() {
        return getRider() == null ? CONFIG.AI_JUMP_POWER : CONFIG.RIDING_JUMP_POWER;
    }

    @Override
    protected void mobTick() {
        Q = getRider() == null ? CONFIG.AI_STEP_HEIGHT : CONFIG.RIDING_STEP_HEIGHT;
        super.mobTick();
    }

    // onLivingUpdate
    @Override
    public void k() {
        super.k();
        boolean hasRider = getRider() != null;
        if (ap()) { // isWet
            if (hasRider && CONFIG.RIDING_DAMAGE_WHEN_WET > 0.0F) {
                damageEntity(DamageSource.DROWN, CONFIG.RIDING_DAMAGE_WHEN_WET);
            } else if (!hasRider && CONFIG.AI_DAMAGE_WHEN_WET > 0.0F) {
                damageEntity(DamageSource.DROWN, CONFIG.AI_DAMAGE_WHEN_WET);
            }
        }
        int x = MathHelper.floor(locX);
        int y = MathHelper.floor(locY);
        int z = MathHelper.floor(locZ);
        if (world.getBiome(new BlockPosition(x, 0, z)).c(new BlockPosition(x, y, z)) > 1.0F) { // biome.getTemperature(pos)
            if (hasRider && CONFIG.RIDING_DAMAGE_WHEN_HOT > 0.0F) {
                damageEntity(CraftEventFactory.MELTING, CONFIG.RIDING_DAMAGE_WHEN_HOT);
            } else if (!hasRider && CONFIG.AI_DAMAGE_WHEN_HOT > 0.0F) {
                damageEntity(CraftEventFactory.MELTING, CONFIG.AI_DAMAGE_WHEN_HOT);
            }
        }
        if (!world.getGameRules().getBoolean("mobGriefing")) {
            return; // not allowed to grief world (gamerule trumps all)
        } else if (hasRider && !CONFIG.RIDING_SNOW_TRAIL_ENABLED) {
            return; // not allowed to leave trail while riding
        } else if (!hasRider && !CONFIG.AI_SNOW_TRAIL_ENABLED) {
            return; // not allowed to leave trail from ai (not riding)
        }
        IBlockData block = Blocks.SNOW.getBlockData();
        for (int l = 0; l < 4; ++l) {
            BlockPosition pos = new BlockPosition(
                    MathHelper.floor(locX + (double) ((float) (l % 2 * 2 - 1) * 0.25F)),
                    MathHelper.floor(locY),
                    MathHelper.floor(locZ + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F)));
            if (world.getType(pos).isAir() && block.canPlace(world, pos)) {
                float temp = world.getBiome(pos).c(pos); // biome.getTemperature(pos)
                if (hasRider && temp < CONFIG.RIDING_SNOW_TRAIL_MAX_TEMP) {
                    CraftEventFactory.handleBlockFormEvent(world, pos, block, this);
                } else if (!hasRider && temp < CONFIG.AI_SNOW_TRAIL_MAX_TEMP) {
                    CraftEventFactory.handleBlockFormEvent(world, pos, block, this);
                }
            }
        }
    }

    // travel
    @Override
    public void a(float strafe, float vertical, float forward) {
        super.a(strafe, vertical, forward);
        checkMove();
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (passengers.isEmpty()) {
            ItemStack itemstack = entityhuman.b(hand);
            if (hasPumpkin()) {
                if (itemstack.getItem() == Items.SHEARS) {
                    if (!new PlayerShearEntityEvent((Player) entityhuman.getBukkitEntity(), getBukkitEntity()).callEvent()) {
                        return false; // plugin cancelled
                    }
                    setHasPumpkin(false);
                    itemstack.damage(1, entityhuman);
                    if (CONFIG.AI_SHEARS_DROPS_PUMPKIN) {
                        EntityItem pumpkin = new EntityItem(world, locX, locY, locZ, new ItemStack(Blocks.PUMPKIN.getItem()));
                        pumpkin.pickupDelay = 10;
                        world.addEntity(pumpkin, CreatureSpawnEvent.SpawnReason.CUSTOM);
                    }
                    return true; // handled
                }
            } else {
                if (CONFIG.AI_ADD_PUMPKIN_TO_HEAD && PUMPKIN.isTagged(CraftItemStack.asCraftMirror(itemstack))) {
                    setHasPumpkin(true);
                    if (!entityhuman.abilities.canInstantlyBuild) {
                        itemstack.subtract(1);
                    }
                    return true; // handled
                }
            }
            if (hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && !entityhuman.isPassenger()) {
                return tryRide(entityhuman, CONFIG.RIDING_SADDLE_REQUIRE, CONFIG.RIDING_SADDLE_CONSUME);
            }
        }
        return false;
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        return (!(passenger instanceof Player) || passengers.isEmpty() || !passenger.equals(passengers.get(0))
                || new RidableDismountEvent(this, (Player) passenger).callEvent()) && super.removePassenger(passenger);
    }
}
