
package me.heldplayer.api.Smartestone.micro;

import net.minecraft.util.Icon;

public interface IMicroBlockMaterial {

    public abstract String getIdentifier();

    public abstract Icon getIcon(int side);

    public abstract String getDisplayName();

    public abstract boolean isBlock();

}
