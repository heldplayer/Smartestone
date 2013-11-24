
package me.heldplayer.mods.Smartestone.util;

import net.minecraftforge.common.ForgeDirection;

public enum Side {

    /**
     * Down
     */
    BOTTOM(ForgeDirection.DOWN),
    /**
     * Up
     */
    TOP(ForgeDirection.UP),
    /**
     * North
     */
    FRONT(ForgeDirection.NORTH),
    /**
     * South
     */
    BACK(ForgeDirection.SOUTH),
    /**
     * West
     */
    LEFT(ForgeDirection.WEST),
    /**
     * East
     */
    RIGHT(ForgeDirection.EAST),
    /**
     * Unknown
     */
    UNKNOWN(null);

    public static final Side[] SIDES = new Side[] { BOTTOM, TOP, FRONT, BACK, LEFT, RIGHT };
    public final ForgeDirection dir;

    private Side(ForgeDirection forgeDirection) {
        this.dir = forgeDirection;
    }

    public static Side getSide(int id) {
        if (id >= 0 && id < SIDES.length) {
            return SIDES[id];
        }

        return UNKNOWN;
    }

}
