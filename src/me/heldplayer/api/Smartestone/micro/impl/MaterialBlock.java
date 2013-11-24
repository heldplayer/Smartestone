
package me.heldplayer.api.Smartestone.micro.impl;

import me.heldplayer.api.Smartestone.micro.IMicroBlockMaterial;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MaterialBlock implements IMicroBlockMaterial {

    public final int id;
    public final int meta;
    public final ItemStack stack;
    public final String identifier;
    public final int renderPass;

    public MaterialBlock(ItemStack stack) {
        this.id = stack.itemID;
        this.meta = stack.getItemDamage();
        this.stack = stack;
        this.identifier = "B" + this.id + "@" + this.meta;

        Block block = Block.blocksList[this.id];
        if (block == null) {
            this.renderPass = 0;
        }
        else {
            this.renderPass = block.getRenderBlockPass();
        }
    }

    public MaterialBlock(ItemStack stack, int forcedRenderPass) {
        this.id = stack.itemID;
        this.meta = stack.getItemDamage();
        this.stack = stack;
        this.identifier = "B" + this.id + "@" + this.meta;
        this.renderPass = forcedRenderPass;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side) {
        return Block.blocksList[this.id].getIcon(side, this.meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColor(int side, int state) {
        return Block.blocksList[this.id].getRenderColor(side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int state) {
        return this.getIcon(side);
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
    public int getRenderPass() {
        return this.renderPass;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        result = prime * result + ((this.identifier == null) ? 0 : this.identifier.hashCode());
        result = prime * result + this.meta;
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
        MaterialBlock other = (MaterialBlock) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.identifier == null) {
            if (other.identifier != null) {
                return false;
            }
        }
        else if (!this.identifier.equals(other.identifier)) {
            return false;
        }
        if (this.meta != other.meta) {
            return false;
        }
        return true;
    }

}
