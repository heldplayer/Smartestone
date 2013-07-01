
package me.heldplayer.api.Smartestone.micro.placement;

import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;

public class PlacementRuleCentralWire implements IMicroBlockPlacementRule {

    @Override
    public boolean conflicts(MicroBlockInfo first, MicroBlockInfo second) {
        if (!first.getType().getTypeName().equalsIgnoreCase("Central Wire")) {
            return false;
        }
        if (!second.getType().getTypeName().equalsIgnoreCase("Central Wire")) {
            return false;
        }
        return true;
    }

}
