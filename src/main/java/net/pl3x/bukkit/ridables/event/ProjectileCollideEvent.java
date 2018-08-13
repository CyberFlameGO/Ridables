package net.pl3x.bukkit.ridables.event;

import net.minecraft.server.v1_13_R1.MovingObjectPosition;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
 * Called when an projectile collides with an entity
 * <p>
 * This event is called <b>before</b> {@link org.bukkit.event.entity.EntityDamageByEntityEvent}, and cancelling it will allow the projectile to continue flying
 */
public class ProjectileCollideEvent extends EntityEvent implements Cancellable {
    private final Entity collidedWith;

    /**
     * Get the entity the projectile collided with
     *
     * @return the entity collided with
     */
    public Entity getCollidedWith() {
        return collidedWith;
    }

    public ProjectileCollideEvent(Projectile what, Entity collidedWith) {
        super(what);
        this.collidedWith = collidedWith;
    }

    /**
     * Get the projectile that collided
     *
     * @return the projectile that collided
     */
    public Projectile getEntity() {
        return (Projectile) super.getEntity();
    }

    private static final HandlerList handlerList = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static ProjectileCollideEvent callProjectileCollideEvent(net.minecraft.server.v1_13_R1.Entity entity, MovingObjectPosition position) {
        Projectile projectile = (Projectile) entity.getBukkitEntity();
        org.bukkit.entity.Entity collided = position.entity.getBukkitEntity();
        ProjectileCollideEvent event = new ProjectileCollideEvent(projectile, collided);
        if (projectile.getShooter() instanceof Player && collided instanceof Player && !((Player) projectile.getShooter()).canSee((Player) collided)) {
            event.setCancelled(true);
            return event;
        } else {
            Bukkit.getPluginManager().callEvent(event);
            return event;
        }
    }
}
