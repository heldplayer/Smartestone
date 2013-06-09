
package me.heldplayer.mods.Smartestone.util;

public enum Direction {

    /**
     * Down
     */
    DOWN(new Side[] { Side.FRONT, Side.BACK, Side.TOP, Side.BOTTOM, Side.LEFT, Side.RIGHT }),
    /**
     * Up
     */
    UP(new Side[] { Side.BACK, Side.FRONT, Side.BOTTOM, Side.TOP, Side.LEFT, Side.RIGHT }),
    /**
     * North
     */
    NORTH(new Side[] { Side.BOTTOM, Side.TOP, Side.FRONT, Side.BACK, Side.LEFT, Side.RIGHT }),
    /**
     * South
     */
    SOUTH(new Side[] { Side.BOTTOM, Side.TOP, Side.BACK, Side.FRONT, Side.RIGHT, Side.LEFT }),
    /**
     * West
     */
    WEST(new Side[] { Side.BOTTOM, Side.TOP, Side.RIGHT, Side.LEFT, Side.FRONT, Side.BACK }),
    /**
     * East
     */
    EAST(new Side[] { Side.BOTTOM, Side.TOP, Side.LEFT, Side.RIGHT, Side.BACK, Side.FRONT }),
    /**
     * Unknown
     */
    UNKNOWN(new Side[] {});

    public static final Direction[] DIRECTIONS = new Direction[] { DOWN, UP, NORTH, SOUTH, WEST, EAST };
    private Side[] sides;

    private Direction(Side[] sides) {
        this.sides = sides;
    }

    /**
     * Gets the direction based on the ID
     * 
     * @param id
     * @return
     */
    public static Direction getDirection(int id) {
        if (id >= 0 && id < DIRECTIONS.length) {
            return DIRECTIONS[id];
        }

        return UNKNOWN;
    }

    /**
     * Gets the next rotation in line
     * 
     * @return
     */
    public Direction next() {
        Direction next = getDirection(this.ordinal() + 1);

        if (next == UNKNOWN) {
            next = DOWN;
        }

        return next;
    }

    /**
     * Gets the relative side for an absolute side
     * 
     * @param side
     * @return
     */
    public Side getRelativeSide(Side side) {
        if (side.ordinal() < 0 || side.ordinal() >= this.sides.length) {
            return side;
        }

        return this.sides[side.ordinal()];
    }

    public Side getRelativeSide(Side side, Rotation rotation) {
        if (side.ordinal() < 0 || side.ordinal() >= this.sides.length) {
            return side;
        }

        Side result = this.sides[side.ordinal()];

        if (result == Side.FRONT || result == Side.BACK) {
            return result;
        }

        for (int i = 0; i < rotation.ordinal(); i++) {
            if (side == Side.TOP) {
                side = Side.LEFT;
            }
            else if (side == Side.LEFT) {
                side = Side.BOTTOM;
            }
            else if (side == Side.BOTTOM) {
                side = Side.RIGHT;
            }
            else if (side == Side.RIGHT) {
                side = Side.TOP;
            }
        }

        return this.sides[side.ordinal()];
    }

}
