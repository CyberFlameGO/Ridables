package net.pl3x.bukkit.ridables.event;

import net.pl3x.bukkit.ridables.entity.RidableEntity;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nullable;

/**
 * Called when a player clicks a mouse button while riding a RidableEntity
 */
public class RidableClickEvent extends RidableEvent {
    private final Block block;
    private final BlockFace blockFace;
    private final Entity clickedEntity;
    private final EquipmentSlot hand;
    private boolean handled;

    public RidableClickEvent(RidableEntity entity, EquipmentSlot hand) {
        this(entity, null, null, null, hand);
    }

    public RidableClickEvent(RidableEntity entity, Entity clickedEntity, EquipmentSlot hand) {
        this(entity, null, null, clickedEntity, hand);
    }

    public RidableClickEvent(RidableEntity entity, Block block, BlockFace blockFace, EquipmentSlot hand) {
        this(entity, block, blockFace, null, hand);
    }

    public RidableClickEvent(RidableEntity entity, Block block, BlockFace blockFace, Entity clickedEntity, EquipmentSlot hand) {
        super(entity);
        this.block = block;
        this.blockFace = blockFace;
        this.hand = hand;
        this.clickedEntity = clickedEntity;
    }

    /**
     * Gets the block that was clicked
     *
     * @return Block that was clicked, or null
     */
    @Nullable
    public Block getClickedBlock() {
        return block;
    }

    /**
     * Gets the block's face that was clicked
     *
     * @return Block's face that was clicked, or null
     */
    @Nullable
    public BlockFace getBlockFace() {
        return blockFace;
    }

    /**
     * Gets the entity that was clicked
     *
     * @return Entity that was clicked, or null
     */
    public Entity getClickedEntity() {
        return clickedEntity;
    }

    /**
     * Gets the hand used in this click
     * <p>
     * <b>Note:</b> This represents left/right click. This is <b>NOT</b> the packet used to trigger the event
     *
     * @return Hand used in this click
     */
    @Nullable
    public EquipmentSlot getHand() {
        return hand;
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
