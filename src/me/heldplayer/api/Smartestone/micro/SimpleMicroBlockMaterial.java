
package me.heldplayer.api.Smartestone.micro;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class SimpleMicroBlockMaterial implements IMicroBlockMaterial {

    public final int id;
    public final int meta;
    public final ItemStack stack;
    public final String identifier;

    public SimpleMicroBlockMaterial(ItemStack stack) {
        this.id = stack.itemID;
        this.meta = stack.getItemDamage();
        this.stack = stack;
        this.identifier = "B" + this.id + "@" + this.meta;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public Icon getIcon(int side) {
        return Block.blocksList[this.id].getIcon(side, this.meta);
    }

    @Override
    public String getDisplayName() {
        return this.stack.getDisplayName();
    }

    @Override
    public boolean isBlock() {
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
        result = prime * result + meta;
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
        if (id != other.id)
            return false;
        if (identifier == null) {
            if (other.identifier != null)
                return false;
        }
        else if (!identifier.equals(other.identifier))
            return false;
        if (meta != other.meta)
            return false;
        return true;
    }

}
