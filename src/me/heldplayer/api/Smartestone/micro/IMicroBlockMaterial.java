
package me.heldplayer.api.Smartestone.micro;

import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IMicroBlockMaterial {

    public abstract String getIdentifier();

    @SideOnly(Side.CLIENT)
    public abstract Icon getIcon(int side);

    @SideOnly(Side.CLIENT)
    public abstract Icon getIcon(int side, int state);

    @SideOnly(Side.CLIENT)
    public abstract int getColor(int side, int state);

    public abstract String getDisplayName();

    public abstract boolean isBlock();

    public abstract int getRenderPass();

}
