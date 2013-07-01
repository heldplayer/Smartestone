
package me.heldplayer.api.Smartestone.micro.placement;

import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;

public interface IMicroBlockPlacementRule {

    public abstract boolean conflicts(MicroBlockInfo first, MicroBlockInfo second);

}
