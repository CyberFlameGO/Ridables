package net.pl3x.bukkit.ridables.event;

import net.pl3x.bukkit.ridables.entity.RidableEntity;
import org.bukkit.entity.Player;

/**
 * Called when a Player dismounts from a RidableEntity
 * <p>
 * This event is called <b>before</b> {@link org.bukkit.event.vehicle.VehicleExitEvent} and {@link org.spigotmc.event.entity.EntityDismountEvent}
 */
public class RidableDismountEvent extends RidableEvent {
    private final Player player;
    private final boolean isCancellable;

    public RidableDismountEvent(RidableEntity entity, Player player) {
        this(entity, player, true);
    }

    public RidableDismountEvent(RidableEntity entity, Player player, boolean notCancellable) {
        super(entity);
        this.player = player;
        this.isCancellable = !notCancellable;
    }

    /**
     * Gets the Player dismounting the RidableEntity
     *
     * @return Player dismounting the RidableEntity
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setCancelled(boolean cancel) {
        if (cancel && !isCancellable) {
            return;
        }
        super.setCancelled(cancel);
    }

    public boolean isCancellable() {
        return isCancellable;
    }
}
