
package me.heldplayer.api.Smartestone.micro;

import java.util.List;

public interface IMicroBlock {

    public abstract List<MicroBlockInfo> getSubBlocks();

    @Deprecated
    // This method is highly inefficient and won't be supported in the future, however it will be used temporarilly
    public abstract void resendTileData();

}
