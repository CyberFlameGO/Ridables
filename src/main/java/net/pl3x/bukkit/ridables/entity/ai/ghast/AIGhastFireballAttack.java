package net.pl3x.bukkit.ridables.entity.ai.ghast;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.pl3x.bukkit.ridables.entity.RidableGhast;
import net.pl3x.bukkit.ridables.entity.projectile.CustomFireball;

public class AIGhastFireballAttack extends PathfinderGoal {
    private final RidableGhast ghast;
    public int attackTimer;

    public AIGhastFireballAttack(RidableGhast ghast) {
        this.ghast = ghast;
    }

    // shouldExecute
    public boolean a() {
        return ghast.getRider() == null && ghast.getGoalTarget() != null;
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }

    // startExecuting
    public void c() {
        attackTimer = 0;
    }

    // resetTask
    public void d() {
        ghast.a(false); // setAttacking
    }

    // tick
    public void e() {
        EntityLiving target = ghast.getGoalTarget();
        if (target.h(ghast) < 4096.0D && ghast.hasLineOfSight(target)) { // getDistanceSq
            ++attackTimer;
            if (attackTimer == 10) {
                ghast.world.a(null, 1015, new BlockPosition(ghast), 0); // playEvent
            }
            if (attackTimer == 20) {
                Vec3D vec3d = ghast.f(1.0F); // getLook
                ghast.world.a(null, 1016, new BlockPosition(ghast), 0); // playEvent
                CustomFireball fireball = new CustomFireball(ghast.world, ghast, null,
                        target.locX - (ghast.locX + vec3d.x * 4.0D),
                        target.getBoundingBox().minY + (double) (target.length / 2.0F) - (0.5D + ghast.locY + (double) (ghast.length / 2.0F)),
                        target.locZ - (ghast.locZ + vec3d.z * 4.0D),
                        RidableGhast.CONFIG.AI_FIREBALL_SPEED,
                        RidableGhast.CONFIG.AI_FIREBALL_DAMAGE,
                        RidableGhast.CONFIG.AI_FIREBALL_GRIEF);
                fireball.bukkitYield = fireball.yield = ghast.getPower();
                fireball.locX = ghast.locX + vec3d.x * 4.0D;
                fireball.locY = ghast.locY + (double) (ghast.length / 2.0F) + 0.5D;
                fireball.locZ = ghast.locZ + vec3d.z * 4.0D;
                ghast.world.addEntity(fireball);
                attackTimer = -40;
            }
        } else if (attackTimer > 0) {
            --attackTimer;
        }
        ghast.a(attackTimer > 10); // setAttacking
    }
}
