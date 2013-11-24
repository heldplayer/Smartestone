
package me.heldplayer.mods.Smartestone.util;

public enum Rotation {

    /**
     * Default
     */
    DEFAULT(new int[][] { //
    { 2, 3, 1, 0, 4, 5 }, // Down 2 3 1 0 4 5
    { 3, 2, 0, 1, 4, 5 }, // Up 3 2 0 1 4 5
    { 0, 1, 2, 3, 4, 5 }, // North
    { 0, 1, 3, 2, 5, 4 }, // South
    { 0, 1, 5, 4, 2, 3 }, // West
    { 0, 1, 4, 5, 3, 2 }, // East
    }, new int[][] { //
    { 1, 2, 0, 0, 0, 0 }, // Down
    { 1, 2, 3, 3, 3, 3 }, // Up
    { 3, 3, 0, 0, 0, 0 }, // North
    { 0, 0, 0, 0, 0, 0 }, // South
    { 2, 1, 0, 0, 0, 0 }, // West
    { 1, 2, 0, 0, 0, 0 }, // East
    }, new boolean[][] { //
    { true, false, true, false, false, true }, // Down
    { true, false, false, true, false, true }, // Up
    { false, false, false, false, false, false }, // North
    { false, false, false, false, false, false }, // South
    { false, false, false, false, false, false }, // West
    { false, false, false, false, false, false }, // East
    }),
    /**
     * Quarter, clock-wise
     */
    QUARTER(new int[][] { //
    { 2, 3, 5, 4, 1, 0 }, // Down 2 3 4 5 1 0
    { 3, 2, 4, 5, 1, 0 }, // Up 3 2 4 5 1 0
    { 4, 5, 2, 3, 1, 0 }, // North 4 5 2 3 1 0
    { 4, 5, 3, 2, 0, 1 }, // South 4 5 2 3 0 1
    { 4, 5, 0, 1, 2, 3 }, // West 4 5 0 1 2 3
    { 4, 5, 1, 0, 3, 2 }, // East
    }, new int[][] { //
    { 0, 0, 1, 2, 2, 1 }, // Down
    { 3, 3, 2, 1, 2, 1 }, // Up
    { 1, 2, 1, 2, 2, 1 }, // North
    { 2, 1, 1, 2, 2, 1 }, // South
    { 3, 3, 1, 2, 2, 1 }, // West
    { 0, 0, 1, 2, 2, 1 }, // East
    }, new boolean[][] { //
    { false, true, true, false, false, true }, // Down
    { true, false, false, true, false, true }, // Up
    { true, false, true, false, false, false }, // North
    { true, false, false, true, true, true }, // South
    { true, false, false, true, false, false }, // West
    { true, false, true, false, true, true }, // East
    }),
    /**
     * Half rotation
     */
    HALF(new int[][] { //
    { 2, 3, 0, 1, 5, 4 }, // Down 2 3 0 1 5 4
    { 3, 2, 1, 0, 5, 4 }, // Up 3 2 1 0 5 4
    { 1, 0, 2, 3, 5, 4 }, // North 1 0 2 3 5 4
    { 1, 0, 3, 2, 4, 5 }, // South 5 4 2 3 1 0
    { 1, 0, 4, 5, 2, 3 }, // West
    { 1, 0, 5, 4, 3, 2 }, // East
    }, new int[][] { //
    { 2, 1, 3, 3, 0, 0 }, // Down
    { 2, 1, 0, 0, 3, 3 }, // Up
    { 3, 3, 3, 3, 3, 3 }, // North
    { 0, 0, 3, 3, 3, 3 }, // South
    { 2, 1, 3, 3, 3, 3 }, // West
    { 1, 2, 3, 3, 3, 3 }, // East
    }, new boolean[][] { //
    { false, true, true, false, false, true }, // Down
    { false, true, false, true, false, true }, // Up
    { true, true, false, false, false, false }, // North
    { true, true, false, false, false, false }, // South
    { true, true, false, false, false, false }, // West
    { true, true, false, false, false, false }, // East
    }),
    /**
     * Quarter, counter-clock-wise
     */
    CQUARTER(new int[][] { //
    { 2, 3, 4, 5, 0, 1 }, // Down 2 3 4 5 0 1
    { 3, 2, 5, 4, 0, 1 }, // Up
    { 5, 4, 2, 3, 0, 1 }, // North 5 4 2 3 0 1
    { 5, 4, 3, 2, 1, 0 }, // South 0 1 2 3 5 4
    { 5, 4, 1, 0, 2, 3 }, // West 5 4 1 0 2 3
    { 5, 4, 0, 1, 3, 2 }, // East
    }, new int[][] { //
    { 0, 0, 2, 1, 1, 2 }, // Down
    { 3, 3, 1, 2, 1, 2 }, // Up
    { 2, 1, 1, 2, 1, 2 }, // North
    { 1, 2, 1, 2, 1, 2 }, // South
    { 0, 0, 1, 2, 1, 2 }, // West
    { 3, 3, 1, 2, 1, 2 }, // East
    }, new boolean[][] { //
    { false, true, true, false, true, false }, // Down
    { true, false, false, true, true, false }, // Up
    { true, false, true, false, true, true }, // North
    { true, false, false, true, false, false }, // South
    { true, false, false, true, true, true }, // West
    { true, false, true, false, false, false }, // East
    }),
    /**
     * Unknown rotation
     */
    UNKNOWN(new int[][] {}, new int[][] {}, new boolean[][] {});

    public static final Rotation[] ROTATIONS = new Rotation[] { DEFAULT, QUARTER, HALF, CQUARTER };
    private final int[][] indices;
    private final int[][] rotations;
    private boolean[][] flipped;

    private Rotation(int[][] indices, int[][] rotations, boolean[][] flipped) {
        this.indices = indices;
        this.rotations = rotations;
        this.flipped = flipped;
    }

    public static Rotation getRotation(int id) {
        if (id >= 0 && id < ROTATIONS.length) {
            return ROTATIONS[id];
        }

        return UNKNOWN;
    }

    public Rotation next() {
        Rotation next = getRotation(this.ordinal() + 1);

        if (next == UNKNOWN) {
            next = DEFAULT;
        }

        return next;
    }

    /**
     * Gets the texture index modification this rotation gives to a direction
     * 
     * @param side
     * @param direction
     * @return
     */
    public int getTextureIndex(Side side, Direction direction) {
        if (direction.ordinal() < 0 || direction.ordinal() >= this.indices.length) {
            return 0;
        }
        if (side.ordinal() < 0 || side.ordinal() >= this.indices[direction.ordinal()].length) {
            return 0;
        }

        //CommonProxy.log.info("Rotation." + this.toString() + ".getTextureIndex(" + side.toString() + ", " + direction.toString() + ") = " + this.indices[direction.ordinal()][side.ordinal()]);

        return this.indices[direction.ordinal()][side.ordinal()];
    }

    /**
     * Gets the texture rotation modification this rotation gives to a direction
     * 
     * @param side
     * @param direction
     * @return
     */
    public int getTextureRotation(Side side, Direction direction) {
        if (direction.ordinal() < 0 || direction.ordinal() >= this.rotations.length) {
            return 0;
        }
        if (side.ordinal() < 0 || side.ordinal() >= this.rotations[direction.ordinal()].length) {
            return 0;
        }

        //CommonProxy.log.info("Rotation." + this.toString() + ".getTextureRotation(" + side.toString() + ", " + direction.toString() + ") = " + this.rotations[direction.ordinal()][side.ordinal()]);

        return this.rotations[direction.ordinal()][side.ordinal()];
    }

    /**
     * Gets if the texture should be flipped from given direction
     * 
     * @param side
     * @param direction
     * @return
     */
    public boolean getTextureFlipped(Side side, Direction direction) {
        if (direction.ordinal() < 0 || direction.ordinal() >= this.flipped.length) {
            return false;
        }
        if (side.ordinal() < 0 || side.ordinal() >= this.flipped[direction.ordinal()].length) {
            return false;
        }

        //CommonProxy.log.info("Rotation." + this.toString() + ".getTextureFlipped(" + side.toString() + ", " + direction.toString() + ") = " + this.flipped[direction.ordinal()][side.ordinal()]);

        return this.flipped[direction.ordinal()][side.ordinal()];
    }

}
