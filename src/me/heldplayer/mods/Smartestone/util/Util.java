
package me.heldplayer.mods.Smartestone.util;

public final class Util {

    public static int getScaled(int scale, int amount, int total) {
        if (amount > total) {
            amount = total;
        }

        return amount * scale / total;
    }

}
