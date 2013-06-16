
package me.heldplayer.api.Smartestone.micro;

import java.util.Comparator;

public class MicroBlockInfoSorter implements Comparator<MicroBlockInfo> {

    @Override
    public int compare(MicroBlockInfo left, MicroBlockInfo right) {
        return left.index - right.index;
    }

}
