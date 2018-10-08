package net.pl3x.bukkit.ridables.event;

import net.pl3x.bukkit.ridables.entity.RidableEntity;

/**
 * Called while the player has the spacebar key pressed down while riding a RidableEntity
 */
public class RidableSpacebarEvent extends RidableEvent {
    private boolean handled;

    public RidableSpacebarEvent(RidableEntity entity) {
        super(entity);
    }

    /**
     * Get if a plugin is handling this event
     *
     * @return True if a plugin is handling this event
     */
    public boolean isHandled() {
        return handled;
    }

    /**
     * Set if this event is handled by a plugin
     *
     * @param handled True to mark the event as handled by a plugin
     */
    public void setHandled(boolean handled) {
        this.handled = handled;
    }
}
