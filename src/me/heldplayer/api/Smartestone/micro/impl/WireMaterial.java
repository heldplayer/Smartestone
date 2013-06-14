
package me.heldplayer.api.Smartestone.micro.impl;

import me.heldplayer.api.Smartestone.micro.IMicroBlockMaterial;
import me.heldplayer.api.Smartestone.micro.IconProvider;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WireMaterial implements IMicroBlockMaterial {

    public final String identifier;
    public final String displayName;
    public final IconProvider icon;

    public WireMaterial(String identifier, String displayName, IconProvider onState) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.icon = onState;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side) {
        return this.icon.icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int state) {
        return this.icon.icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColor(int side, int state) {
        if ((state >> 6) > 1) {
            int power = state >> 6;

            int k = 0xFF - (60 - power) * 3;
            return k | (k << 8) | (k << 16);
        }
        return 0x4B4B4B;
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
        result = prime * result + ((this.identifier == null) ? 0 : this.identifier.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        WireMaterial other = (WireMaterial) obj;
        if (this.identifier == null) {
            if (other.identifier != null) {
                return false;
            }
        }
        else if (!this.identifier.equals(other.identifier)) {
            return false;
        }
        return true;
    }

}
