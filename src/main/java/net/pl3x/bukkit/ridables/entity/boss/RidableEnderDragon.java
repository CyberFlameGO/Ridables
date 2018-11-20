package net.pl3x.bukkit.ridables.entity.boss;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import net.minecraft.server.v1_13_R2.AxisAlignedBB;
import net.minecraft.server.v1_13_R2.Block;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.DragonControllerPhase;
import net.minecraft.server.v1_13_R2.EnderDragonBattle;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityComplexPart;
import net.minecraft.server.v1_13_R2.EntityEnderCrystal;
import net.minecraft.server.v1_13_R2.EntityEnderDragon;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.EnumMoveType;
import net.minecraft.server.v1_13_R2.Explosion;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.IDragonController;
import net.minecraft.server.v1_13_R2.MCUtil;
import net.minecraft.server.v1_13_R2.Material;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.Particles;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.EnderDragonConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASDFlying;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import net.pl3x.bukkit.ridables.util.Const;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import javax.annotation.Nullable;
import java.util.List;

public class RidableEnderDragon extends EntityEnderDragon implements RidableEntity {
    public static final EnderDragonConfig CONFIG = new EnderDragonConfig();

    private Explosion explosionSource = new Explosion(null, this, Double.NaN, Double.NaN, Double.NaN, Float.NaN, true, true);
    private boolean hadRider;

    public RidableEnderDragon(World world) {
        super(world);
        moveController = new ControllerWASDFlying(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.ENDER_DRAGON;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !hasCustomName() && !isLeashed();
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED); // registerAttribute
        getAttributeMap().b(RidableType.RIDING_MAX_Y); // registerAttribute
        reloadAttributes();
    }

    @Override
    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(RidableType.RIDING_MAX_Y).setValue(CONFIG.RIDING_FLYING_MAX_Y);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.c).setValue(CONFIG.AI_KNOCKBACK_RESISTANCE); // KNOCKBACK_RESISTANCE
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.h).setValue(CONFIG.AI_ARMOR); // ARMOR
        getAttributeInstance(GenericAttributes.i).setValue(CONFIG.AI_ARMOR_TOUGHNESS); // ARMOR_TOUGHNESS
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return false;
    }

    // canBeRidden
    @Override
    protected boolean n(Entity entity) {
        return k <= 0; // rideCooldown
    }

    // travel
    @Override
    public void a(float strafe, float vertical, float forward) {
        super.a(strafe, vertical, forward);
        checkMove();
    }

    // onLivingUpdate
    @Override
    public void k() {
        boolean hasRider = getRider() != null;
        if (hasRider) {
            if (!hadRider) {
                hadRider = true;
                noclip = false;
                setSize(4.0F, 2.0F);
            }

            moveController.a(); // ender dragon doesnt use the controller so call manually

            a(-bh, bi, -bj, getAttributeInstance(RidableType.RIDING_SPEED).getValue() * 0.1F); // moveRelative
            move(EnumMoveType.PLAYER, motX, motY, motZ);

            motX *= 0.9F;
            motY *= 0.9F;
            motZ *= 0.9F;

            // control wing flap speed on client
            getDragonControllerManager().setControllerPhase(motX * motX + motZ * motZ < 0.005F ? DragonControllerPhase.k : DragonControllerPhase.a);
        } else if (hadRider) {
            hadRider = false;
            noclip = true;
            setSize(16.0F, 8.0F);
            getDragonControllerManager().setControllerPhase(DragonControllerPhase.a); // HoldingPattern
        }
        super_k(hasRider);
    }

    @Override
    public boolean onSpacebar() {
        // TODO flames!
        return true;
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

    // onLivingUpdate (modified from super class)
    private void super_k(boolean hasRider) {
        bL = bM; // prevAnimTime = animTime
        if (getHealth() <= 0.0F) {
            if (hasRider) {
                ejectPassengers();
            }
            world.addParticle(Particles.u,
                    locX + ((random.nextFloat() - 0.5F) * 8.0F),
                    locY + 2.0D + ((random.nextFloat() - 0.5F) * 4.0F),
                    locZ + ((random.nextFloat() - 0.5F) * 8.0F),
                    0.0D, 0.0D, 0.0D);
            return;
        }
        updateDragonEnderCrystal();
        float animTime = (0.2F / (MathHelper.sqrt(motX * motX + motZ * motZ) * 10.0F + 1.0F)) * (float) Math.pow(2.0D, motY);
        if (!hasRider && getDragonControllerManager().a().a()) { // getCurrentPhase getIsStationary
            bM += 0.1F; // animTime
        } else if (!hasRider && bN) { // isSlowed
            bM += animTime * 0.5F; // animTime
        } else {
            bM += animTime; // animTime
        }
        yaw = MathHelper.g(yaw); //wrapDegrees
        if (isNoAI()) {
            bM = 0.5F; // animTime
            return;
        }
        if (c < 0) { // ringBufferIndex
            for (int i = 0; i < b.length; ++i) { // ringBuffer
                b[i][0] = (double) yaw; // ringBuffer
                b[i][1] = locY; // ringBuffer
            }
        }
        if (++c == b.length) { // ringBufferIndex ringBuffer
            c = 0; // ringBufferIndex
        }
        b[c][0] = (double) yaw; // ringBuffer ringBufferIndex
        b[c][1] = locY; // ringBuffer ringBufferIndex
        if (!hasRider) {
            IDragonController phase = getDragonControllerManager().a(); // getPhase
            phase.c(); // serverTick
            if (getDragonControllerManager().a() != phase) { // getCurrentPhase
                phase = getDragonControllerManager().a(); // getCurrentPhase
                phase.c(); // serverTick
            }
            Vec3D targetLoc = phase.g(); // getTargetLocation
            if (targetLoc != null /*&& phase.getControllerPhase() != DragonControllerPhase.k*/) { // CB cancels movement if in hover phase, lets revert that
                double x = targetLoc.x - locX;
                double y = targetLoc.y - locY;
                double z = targetLoc.z - locZ;
                float maxRiseOrFall = phase.f(); // getMaxRiseOrFall
                y = MathHelper.a(y / (double) MathHelper.sqrt(x * x + z * z), (double) (-maxRiseOrFall), (double) maxRiseOrFall); // clamp
                motY += y * (double) 0.1F;
                yaw = MathHelper.g(yaw); // wrapDegrees
                double d5 = MathHelper.a(MathHelper.g(180.0D - MathHelper.c(x, z) * Const.RAD2DEG - (double) yaw), -50.0D, 50.0D); // clamp wrapDegrees atan2
                Vec3D vecDiff = (new Vec3D(targetLoc.x - locX, targetLoc.y - locY, targetLoc.z - locZ)).a(); // normalize
                Vec3D vecRot = (new Vec3D((double) MathHelper.sin(yaw * Const.DEG2RAD_FLOAT), motY, (double) (-MathHelper.cos(yaw * Const.DEG2RAD_FLOAT)))).a(); // normalize
                float max = Math.max(((float) vecRot.b(vecDiff) + 0.5F) / 1.5F, 0.0F); // dotProduct
                bk = (float) ((double) (bk * 0.8F) + d5 * (double) phase.h()); // randomYawVelocity getYawFactor
                yaw += bk * 0.1F; // randomYawVelocity
                float diff = (float) (2.0D / ((x * x + y * y + z * z) + 1.0D));
                a(0.0F, 0.0F, -1.0F, 0.06F * (max * diff + (1.0F - diff))); // moveRelative
                if (bN) { // slowed
                    move(EnumMoveType.SELF, motX * (double) 0.8F, motY * (double) 0.8F, motZ * (double) 0.8F);
                } else {
                    move(EnumMoveType.SELF, motX, motY, motZ);
                }
                float friction = 0.8F + 0.15F * (((float) (new Vec3D(motX, motY, motZ)).a().b(vecRot) + 1.0F) / 2.0F); // normalize dotProduct
                motX *= (double) friction;
                motZ *= (double) friction;
                motY *= (double) 0.91F;
            }
        }
        aQ = yaw; // renderYawOffset
        bD.width = 1.0F;  // head
        bD.length = 1.0F; // head
        bE.width = 3.0F;  // neck
        bE.length = 3.0F; // neck
        bG.width = 2.0F;  // tail1
        bG.length = 2.0F; // tail1
        bH.width = 2.0F;  // tail2
        bH.length = 2.0F; // tail2
        bI.width = 2.0F;  // tail3
        bI.length = 2.0F; // tail3
        bF.length = 3.0F; // body
        bF.width = 5.0F;  // body
        bJ.length = 2.0F; // wing1
        bJ.width = 4.0F;  // wing1
        bK.length = 3.0F; // wing2
        bK.width = 4.0F;  // wing2
        Vec3D[] dragonParts = new Vec3D[children.length];
        for (int j = 0; j < children.length; ++j) {
            dragonParts[j] = new Vec3D(children[j].locX, children[j].locY, children[j].locZ);
        }
        float offset = (float) (getMovementOffsets(5, 1.0F)[1] - getMovementOffsets(10, 1.0F)[1]) * 10.0F * Const.DEG2RAD_FLOAT;
        float offsetCos = MathHelper.cos(offset);
        float offsetSin = MathHelper.sin(offset);
        float offsetYaw = yaw * Const.DEG2RAD_FLOAT;
        float offsetYawCos = MathHelper.sin(offsetYaw);
        float offsetYawSin = MathHelper.cos(offsetYaw);
        bF.tick(); // body
        bF.setPositionRotation(locX + (double) (offsetYawCos * 0.5F), locY, locZ - (double) (offsetYawSin * 0.5F), 0.0F, 0.0F);
        bJ.tick(); // wing1
        bJ.setPositionRotation(locX + (double) (offsetYawSin * 4.5F), locY + 2.0D, locZ + (double) (offsetYawCos * 4.5F), 0.0F, 0.0F);
        bK.tick(); // wing2
        bK.setPositionRotation(locX - (double) (offsetYawSin * 4.5F), locY + 2.0D, locZ - (double) (offsetYawCos * 4.5F), 0.0F, 0.0F);
        if (!hasRider && hurtTicks == 0) {
            collideWithEntities(world.getEntities(this, bJ.getBoundingBox().grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D))); // wing1
            collideWithEntities(world.getEntities(this, bK.getBoundingBox().grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D))); // wing2
            attackEntitiesInList(world.getEntities(this, bD.getBoundingBox().g(1.0D))); // head
            attackEntitiesInList(world.getEntities(this, bE.getBoundingBox().g(1.0D))); // neck
        }
        double[] movementOffsets = getMovementOffsets(5, 1.0F);
        float randomYaw = (yaw * Const.DEG2RAD_FLOAT) - (bk * 0.01F); // randomYawVelocity
        float randomYawSin = MathHelper.sin(randomYaw);
        float randomYawCos = MathHelper.cos(randomYaw);
        float headYOffset = getHeadYOffset();
        bD.tick(); // head
        bD.setPositionRotation( // head
                locX + (double) (randomYawSin * 6.5F * offsetCos),
                locY + (double) headYOffset + (double) (offsetSin * 6.5F),
                locZ - (double) (randomYawCos * 6.5F * offsetCos),
                0.0F, 0.0F);
        bE.tick(); // neck
        bE.setPositionRotation( // neck
                locX + (double) (randomYawSin * 5.5F * offsetCos),
                locY + (double) headYOffset + (double) (offsetSin * 5.5F),
                locZ - (double) (randomYawCos * 5.5F * offsetCos),
                0.0F, 0.0F);
        for (int k = 0; k < 3; ++k) {
            EntityComplexPart dragonPart = null;
            if (k == 0) dragonPart = bG; // tail1
            if (k == 1) dragonPart = bH; // tail2
            if (k == 2) dragonPart = bI; // tail3
            double[] movementOffsets1 = getMovementOffsets(12 + k * 2, 1.0F);
            float f15 = yaw * Const.DEG2RAD_FLOAT + (float) MathHelper.g(movementOffsets1[0] - movementOffsets[0]) * Const.DEG2RAD_FLOAT; // wrapDegrees
            float f4 = (float) (k + 1) * 2.0F;
            dragonPart.tick();
            dragonPart.setPositionRotation(
                    locX - (double) ((offsetYawCos * 1.5F + MathHelper.sin(f15) * f4) * offsetCos),
                    locY + (movementOffsets1[1] - movementOffsets[1]) - (double) ((f4 + 1.5F) * offsetSin) + 1.5D,
                    locZ + (double) ((offsetYawSin * 1.5F + MathHelper.cos(f15) * f4) * offsetCos),
                    0.0F, 0.0F);
        }
        bN = !hasRider && destroyBlocks(bD.getBoundingBox()) | destroyBlocks(bE.getBoundingBox()) | destroyBlocks(bF.getBoundingBox()); // slowed head neck body
        if (getBattleManager() != null) {
            getBattleManager().b(this); // dragonUpdate
        }
        for (int k = 0; k < children.length; ++k) {
            children[k].lastX = dragonParts[k].x;
            children[k].lastY = dragonParts[k].y;
            children[k].lastZ = dragonParts[k].z;
        }
    }

    public void updateDragonEnderCrystal() {
        if (currentEnderCrystal != null) {
            if (currentEnderCrystal.dead) {
                currentEnderCrystal = null;
            } else if (ticksLived % 10 == 0 && getHealth() < getMaxHealth()) {
                EntityRegainHealthEvent event = new EntityRegainHealthEvent(getBukkitEntity(), 1.0F, EntityRegainHealthEvent.RegainReason.ENDER_CRYSTAL);
                if (event.callEvent()) {
                    setHealth((float) (getHealth() + event.getAmount()));
                }
            }
        }
        if (random.nextInt(10) == 0) {
            List<EntityEnderCrystal> nearbyCrystals = world.a(EntityEnderCrystal.class, getBoundingBox().g(32.0D)); // getEntitiesWithinAABB
            EntityEnderCrystal closestCrystal = null;
            double closestDistance = Double.MAX_VALUE;
            for (EntityEnderCrystal crystal : nearbyCrystals) {
                double distance = crystal.h(this); // getDistanceSq
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestCrystal = crystal;
                }
            }
            currentEnderCrystal = closestCrystal;
        }
    }

    public boolean destroyBlocks(AxisAlignedBB aabb) {
        int minX = MathHelper.floor(aabb.minX);
        int minY = MathHelper.floor(aabb.minY);
        int minZ = MathHelper.floor(aabb.minZ);
        int maxX = MathHelper.floor(aabb.maxX);
        int maxY = MathHelper.floor(aabb.maxY);
        int maxZ = MathHelper.floor(aabb.maxZ);
        boolean hitHardBlock = false;
        boolean brokeBlock = false;

        List<org.bukkit.block.Block> destroyedBlocks = new java.util.ArrayList<>();
        CraftWorld craftWorld = world.getWorld();

        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    IBlockData state = world.getType(new BlockPosition(x, y, z));
                    if (state.isAir() || state.getMaterial() == Material.FIRE) {
                        continue;
                    }
                    Block block = state.getBlock();
                    if (!world.getGameRules().getBoolean("mobGriefing") ||
                            block == Blocks.BARRIER ||
                            block == Blocks.OBSIDIAN ||
                            block == Blocks.END_STONE ||
                            block == Blocks.BEDROCK ||
                            block == Blocks.END_PORTAL ||
                            block == Blocks.END_PORTAL_FRAME ||
                            block == Blocks.COMMAND_BLOCK ||
                            block == Blocks.REPEATING_COMMAND_BLOCK ||
                            block == Blocks.CHAIN_COMMAND_BLOCK ||
                            block == Blocks.IRON_BARS ||
                            block == Blocks.END_GATEWAY) {
                        hitHardBlock = true;
                    } else {
                        destroyedBlocks.add(craftWorld.getBlockAt(x, y, z));
                    }
                }
            }
        }

        EntityExplodeEvent event = new EntityExplodeEvent(getBukkitEntity(), MCUtil.toLocation(this), destroyedBlocks, 0F);
        if (!event.callEvent()) { // cancelled
            return hitHardBlock;
        }

        if (event.getYield() == 0F) {
            // Yield zero ==> no drops
            for (org.bukkit.block.Block block : event.blockList()) {
                world.setAir(new BlockPosition(block.getX(), block.getY(), block.getZ()));
                brokeBlock = true;
            }
        } else {
            for (org.bukkit.block.Block block : event.blockList()) {
                org.bukkit.Material blockId = block.getType();
                if (blockId == org.bukkit.Material.AIR) {
                    continue;
                }

                int blockX = block.getX();
                int blockY = block.getY();
                int blockZ = block.getZ();

                Block nmsBlock = CraftMagicNumbers.getBlock(blockId);
                if (nmsBlock.a(explosionSource)) {
                    BlockPosition pos = new BlockPosition(blockX, blockY, blockZ);
                    nmsBlock.dropNaturally(world.getType(pos), world, pos, event.getYield(), 0);
                }
                org.bukkit.block.Block tntBlock = world.getWorld().getBlockAt(blockX, blockY, blockZ);
                if (new TNTPrimeEvent(tntBlock, TNTPrimeEvent.PrimeReason.EXPLOSION, explosionSource.getSource().getBukkitEntity()).callEvent()) {
                    nmsBlock.wasExploded(world, new BlockPosition(blockX, blockY, blockZ), explosionSource);
                    world.setAir(new BlockPosition(blockX, blockY, blockZ));
                    brokeBlock = true;
                }
            }
        }

        if (brokeBlock) {
            world.addParticle(Particles.u, // EXPLOSION
                    aabb.minX + (aabb.maxX - aabb.minX) * (double) random.nextFloat(),
                    aabb.minY + (aabb.maxY - aabb.minY) * (double) random.nextFloat(),
                    aabb.minZ + (aabb.maxZ - aabb.minZ) * (double) random.nextFloat(),
                    0.0D, 0.0D, 0.0D);
        }
        return hitHardBlock;
    }

    public void collideWithEntities(List<Entity> entities) {
        double xDiff = (bF.getBoundingBox().minX + bF.getBoundingBox().maxX) / 2.0D; // body
        double zDiff = (bF.getBoundingBox().minZ + bF.getBoundingBox().maxZ) / 2.0D; // body
        for (Entity entity : entities) {
            if (entity instanceof EntityLiving) {
                double x = entity.locX - xDiff;
                double z = entity.locZ - zDiff;
                double distance = x * x + z * z;
                entity.f(x / distance * 4.0D, (double) 0.2F, z / distance * 4.0D); // addVelocity
                if (!getDragonControllerManager().a().a() && ((EntityLiving) entity).cg() < entity.ticksLived - 2) { // getCurrentPhase getIsStationary
                    entity.damageEntity(DamageSource.mobAttack(this), CONFIG.AI_MELEE_DAMAGE / 2);
                    a(this, entity); // applyEnchantments
                }
            }
        }
    }

    public void attackEntitiesInList(List<Entity> entities) {
        for (Entity target : entities) {
            if (target instanceof EntityLiving) {
                target.damageEntity(DamageSource.mobAttack(this), CONFIG.AI_MELEE_DAMAGE);
                a(this, target); // applyEnchantments
            }
        }
    }

    public float getHeadYOffset() {
        if (getDragonControllerManager().a().a()) { // getCurrentPhase getIsStationary
            return (float) -1.0D;
        }
        return (float) (getMovementOffsets(5, 1.0F)[1] - getMovementOffsets(0, 1.0F)[1]);
    }

    public double[] getMovementOffsets(int i, float j) {
        return a(i, j); // getMovementOffsets
    }

    @Nullable
    public EnderDragonBattle getBattleManager() {
        return ds(); // getBattleManager
    }
}
