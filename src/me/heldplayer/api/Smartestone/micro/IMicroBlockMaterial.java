
package me.heldplayer.api.Smartestone.micro;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Icon;

public interface IMicroBlockMaterial {

    public abstract String getIdentifier();

    @SideOnly(Side.CLIENT)
    public abstract Icon getIcon(int side);

    public abstract String getDisplayName();

    public abstract boolean isBlock();

    public abstract int getRenderPass();

}
