
package me.heldplayer.api.Smartestone.micro.impl;

import me.heldplayer.api.Smartestone.micro.IMicroBlockMaterial;
import me.heldplayer.api.Smartestone.micro.IconProvider;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WireMaterial implements IMicroBlockMaterial {

    public final String identifier;
    public final String displayName;
    public final IconProvider offState;
    public final IconProvider onState;

    public WireMaterial(String identifier, String displayName, IconProvider offState, IconProvider onState) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.offState = offState;
        this.onState = onState;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side) {
        return this.offState.icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int state) {
        return (state >> 6) > 1 ? onState.icon : offState.icon;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public boolean isBlock() {
        return false;
    }

    @Override
    public int getRenderPass() {
        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimpleMicroBlockMaterial other = (SimpleMicroBlockMaterial) obj;
        if (identifier == null) {
            if (other.identifier != null)
                return false;
        }
        else if (!identifier.equals(other.identifier))
            return false;
        return true;
    }

}
