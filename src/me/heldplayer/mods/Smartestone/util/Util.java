
package me.heldplayer.mods.Smartestone.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public final class Util {

    public static int getScaled(int scale, int amount, int total) {
        if (amount > total) {
            amount = total;
        }

        return amount * scale / total;
    }

    @Deprecated
    public static void resendTileData(TileEntity tile) {
        if (!(tile.worldObj instanceof WorldServer)) {
            return;
        }

        WorldServer world = (WorldServer) tile.worldObj;

        world.getPlayerManager().flagChunkForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
    }

}
