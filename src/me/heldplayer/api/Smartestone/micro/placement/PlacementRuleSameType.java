
package me.heldplayer.api.Smartestone.micro.placement;

import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;

public class PlacementRuleSameType implements IMicroBlockPlacementRule {

    @Override
    public boolean conflicts(MicroBlockInfo first, MicroBlockInfo second) {
        if (first.getType().getClass() == second.getType().getClass() && first.getData() == second.getData()) {
            return true;
        }
        return false;
    }

}
