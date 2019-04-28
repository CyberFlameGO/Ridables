package net.pl3x.bukkit.ridables.entity.npc;

import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EntityVillager;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.Vec3D;
import net.minecraft.server.v1_14_R1.World;
import net.pl3x.bukkit.ridables.configuration.mob.VillagerConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableVillager extends EntityVillager implements RidableEntity {
    private static VillagerConfig config;

    private final ControllerWASD controllerWASD;

    public RidableVillager(EntityTypes<? extends EntityVillager> entitytypes, World world) {
        super(entitytypes, world);
        moveController = controllerWASD = new ControllerWASD(this);
        lookController = new LookController(this);

        if (config == null) {
            config = getConfig();
        }
    }

    @Override
    public RidableType getType() {
        return RidableType.VILLAGER;
    }

    @Override
    public ControllerWASD getController() {
        return controllerWASD;
    }

    @Override
    public VillagerConfig getConfig() {
        return (VillagerConfig) getType().getConfig();
    }

    @Override
    public double getRidingSpeed() {
        return config.RIDING_SPEED;
    }

    @Override
    protected void initPathfinder() {
        // TODO - where the hell did their AI go?!
    }

    // canBeRiddenInWater
    @Override
    public boolean be() {
        return config.RIDING_RIDE_IN_WATER;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cW() {
        return getRider() == null ? super.cW() : config.RIDING_JUMP_POWER;
    }

    @Override
    protected void mobTick() {
        K = getRider() == null ? 0.6F : config.RIDING_STEP_HEIGHT;
        super.mobTick();
    }

    // travel
    @Override
    public void e(Vec3D motion) {
        super.e(motion);
        checkMove();
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
            if (!config.RIDING_BABIES && isBaby()) {
                return false; // do not ride babies
            }
            return tryRide(entityhuman, config.RIDING_SADDLE_REQUIRE, config.RIDING_SADDLE_CONSUME);
        }
        return super.a(entityhuman, hand); // handle vanilla actions last because of trade menu
    }
}
