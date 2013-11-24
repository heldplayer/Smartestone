
package me.heldplayer.api.Smartestone.micro;

import java.util.Set;

public interface IMicroBlock {

    public abstract Set<MicroBlockInfo> getSubBlocks();

    public abstract void removeInfo(MicroBlockInfo info);

    public abstract void addInfo(MicroBlockInfo info);

    public abstract void modifyInfo(MicroBlockInfo info);

}
