package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityDolphin;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.Particles;
import net.minecraft.server.v1_13_R1.SoundEffect;
import net.minecraft.server.v1_13_R1.SoundEffects;
import net.minecraft.server.v1_13_R1.World;
import net.minecraft.server.v1_13_R1.WorldServer;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDWater;
import net.pl3x.bukkit.ridables.util.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class EntityRidableDolphin extends EntityDolphin implements RidableEntity {
    private ControllerMove aiController;
    private ControllerWASDWater wasdController;

    private int bounceCounter = 0;
    private boolean bounceUp = false;

    private int spacebarCooldown = 0;

    private boolean dashing = false;
    private int dashCounter = 0;

    public EntityRidableDolphin(World world) {
        super(world);
        persistent = true;
        aiController = moveController;
        wasdController = new ControllerWASDWater(this);
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return Tag.ITEMS_FISHES.isTagged(itemstack.getType());
    }

    protected boolean isTypeNotPersistent() {
        return false;
    }

    protected void mobTick() {
        if (++bounceCounter > 10) {
            bounceCounter = 0;
            bounceUp = !bounceUp;
        }
        if (spacebarCooldown > 0) {
            spacebarCooldown--;
        }

        EntityPlayer rider = getRider();
        if (rider != null && getAirTicks() > 150) {
            setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();


            if (ReflectionUtil.isJumping(rider) && spacebarCooldown == 0) {
                if (Config.DOLPHIN_SPACEBAR_MODE != null) {
                    if (Config.DOLPHIN_SPACEBAR_MODE.equalsIgnoreCase("shoot")) {
                        shoot(rider);
                    } else if (Config.DOLPHIN_SPACEBAR_MODE.equalsIgnoreCase("dash")) {
                        spacebarCooldown = Config.DOLPHIN_DASH_COOLDOWN;
                        if (!dashing && rider.getBukkitEntity().hasPermission("allow.dash.dolphin")) {
                            dashing = true;
                            dashCounter = 0;
                            playSound(SoundEffects.ENTITY_DOLPHIN_JUMP);
                        }
                    }
                }
            }

            if (dashing) {
                if (++dashCounter > Config.DOLPHIN_DASH_DURATION) {
                    dashCounter = 0;
                    dashing = false;
                }
            }

            if (isInWater()) {
                if (Config.DOLPHIN_BUBBLES) {
                    double velocity = motX * motX + motY * motY + motZ * motZ;
                    if (velocity > 0.2 || velocity < -0.2) {
                        int i = (int) (velocity * 5);
                        for (int j = 0; j < i; j++) {
                            ((WorldServer) world).sendParticles(null, Particles.e,
                                    lastX + random.nextFloat() / 2 - 0.25F,
                                    lastY + random.nextFloat() / 2 - 0.25F,
                                    lastZ + random.nextFloat() / 2 - 0.25F,
                                    1, 0, 0, 0, 0);
                        }
                    }
                }

                motY += 0.005D;
                if (Config.DOLPHIN_BOUNCE && rider.bj == 0) {
                    motY += bounceUp ? 0.005D : -0.005D;
                }
            }
        } else {
            useAIController();
        }
        super.mobTick();
    }

    @Override
    public void a(float f, float f1, float f2) {
        EntityPlayer rider = getRider();
        if (rider != null && !isInWater()) {
            f2 = rider.bj;
            f = rider.bh;
        }
        super.a(f, f1, f2);
    }

    public void setRotation(float newYaw, float newPitch) {
        setYawPitch(lastYaw = yaw = newYaw, pitch = newPitch * 0.5F);
        aS = aQ = yaw;
    }

    public float getJumpPower() {
        return 0;
    }

    public float getSpeed() {
        float speed = (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
        return speed * Config.DOLPHIN_SPEED * (dashing ? Config.DOLPHIN_DASH_BOOST : 1) * 0.2F;
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
        }
    }

    public void useWASDController() {
        if (moveController != wasdController) {
            moveController = wasdController;
        }
    }

    private void shoot(EntityPlayer rider) {
        spacebarCooldown = Config.DOLPHIN_SHOOT_COOLDOWN;

        if (rider == null) {
            return;
        }

        CraftPlayer player = rider.getBukkitEntity();
        if (!player.hasPermission("allow.shoot.dolphin")) {
            Lang.send(player, Lang.SHOOT_NO_PERMISSION);
            return;
        }

        EntityDolphinSpit spit = new EntityDolphinSpit(world, this, rider);

        Location loc = rider.getBukkitEntity().getEyeLocation();
        loc.setPitch(loc.getPitch() - 10);
        Vector target = loc.getDirection().normalize().multiply(10).add(loc.toVector());

        spit.shoot(target.getX() - locX, target.getY() - locY, target.getZ() - locZ, Config.DOLPHIN_SHOOT_SPEED, 5.0F);

        playSound(SoundEffects.ENTITY_DOLPHIN_ATTACK);
        world.addEntity(spit);
    }

    private void playSound(SoundEffect sound) {
        a(sound, 1.0F, 1.0F);
    }
}
