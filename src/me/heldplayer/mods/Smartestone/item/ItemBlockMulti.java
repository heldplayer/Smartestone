
package me.heldplayer.mods.Smartestone.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockMulti extends ItemBlock {

    private Block block;

    public ItemBlockMulti(int blockId) {
        super(blockId);

        this.block = Block.blocksList[this.getBlockID()];
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta) {
        return this.block.getIcon(0, meta);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.block.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int itemId, CreativeTabs creativeTab, List list) {
        for (int i = 0; i < 16; i++) {
            if (this.block.hasTileEntity(i)) {
                list.add(new ItemStack(itemId, 1, i));
            }
        }
    }

}
