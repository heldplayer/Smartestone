
package me.heldplayer.api.Smartestone.micro;

import java.util.List;

public interface IMicroBlock {

    public abstract List<MicroBlockInfo> getSubBlocks();

    public abstract void resendTileData();

}
