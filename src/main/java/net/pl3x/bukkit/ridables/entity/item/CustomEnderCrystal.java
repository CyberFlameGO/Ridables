package net.pl3x.bukkit.ridables.entity.item;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.EntityEnderCrystal;
import net.minecraft.server.v1_13_R2.EntityPhantom;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.entity.monster.RidablePhantom;

public class CustomEnderCrystal extends EntityEnderCrystal {
    private EntityPhantom targetPhantom;
    private int phantomBeamTicks = 0;
    private int phantomDamageCooldown = 0;
    private int idleCooldown = 0;

    public CustomEnderCrystal(World world) {
        super(world);
    }

    public CustomEnderCrystal(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public void tick() {
        super.tick();

        if (RidablePhantom.CONFIG.AI_ENDER_CRYSTALS_DAMAGE <= 0.0F) {
            return; // do not attack phantoms
        }

        if (--idleCooldown > 0) {
            return; // on cooldown
        }

        if (targetPhantom == null) {
            for (EntityPhantom phantom : world.a(EntityPhantom.class, getBoundingBox().grow(16, 16, 16))) {
                if (phantom.hasLineOfSight(this)) {
                    attackPhantom(phantom);
                    break;
                }
            }
        } else {
            setBeamTarget(new BlockPosition(targetPhantom).add(0, -2, 0));
            if (--phantomBeamTicks > 0 && targetPhantom.isAlive()) {
                phantomDamageCooldown--;
                if (targetPhantom.hasLineOfSight(this)) {
                    if (phantomDamageCooldown <= 0) {
                        phantomDamageCooldown = RidablePhantom.CONFIG.AI_ENDER_CRYSTALS_DAMAGE_COOLDOWN;
                        targetPhantom.damageEntity(DamageSource.MAGIC, RidablePhantom.CONFIG.AI_ENDER_CRYSTALS_DAMAGE);
                    }
                } else {
                    forgetPhantom(); // no longer in sight
                }
            } else {
                forgetPhantom(); // attacked long enough
            }
        }
    }

    private void attackPhantom(EntityPhantom phantom) {
        phantomDamageCooldown = 0;
        phantomBeamTicks = RidablePhantom.CONFIG.AI_ENDER_CRYSTALS_BEAM_TICKS;
        targetPhantom = phantom;
    }

    private void forgetPhantom() {
        targetPhantom = null;
        setBeamTarget(null);
        phantomBeamTicks = 0;
        phantomDamageCooldown = 0;
        idleCooldown = RidablePhantom.CONFIG.AI_ENDER_CRYSTALS_IDLE_COOLDOWN;
    }
}
