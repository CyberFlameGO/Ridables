package net.pl3x.bukkit.ridables.event;

import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
 * Represents a RidableEntity-related event
 */
public abstract class RidableEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final RidableEntity ridable;

    public RidableEvent(RidableEntity entity) {
        super(entity.getBukkitEntity());
        this.ridable = entity;
    }

    /**
     * Returns the RidableEntity involved in this event
     *
     * @return RidableEntity who is involved in this event
     */
    public RidableEntity getRidable() {
        return ridable;
    }

    /**
     * Gets the RidableType f th RidableEntity involved in this event
     *
     * @return RidableType of the RidableEntity involved in this event
     */
    public RidableType getRidableType() {
        return ridable.getType();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
