
package me.heldplayer.mods.Smartestone.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTemplate extends Item {

    @SideOnly(Side.CLIENT)
    private Icon iconEmpty;
    @SideOnly(Side.CLIENT)
    public Icon iconOverlay;

    public ItemTemplate(int itemId) {
        super(itemId);
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        super.registerIcons(register);
        this.iconEmpty = register.registerIcon(this.iconString + "_empty");
        this.iconOverlay = register.registerIcon(this.iconString + "_overlay");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta) {
        if (meta > 0) {
            return super.getIconFromDamage(meta);
        }
        return this.iconEmpty;
    }

    @Override
    public Icon getIcon(ItemStack stack, int pass) {
        NBTTagCompound compound = stack.getTagCompound();

        if (compound != null && compound.hasKey("Template")) {
            return super.getIcon(stack, pass);
        }
        else {
            return this.iconEmpty;
        }
    }

    public String getItemDisplayName(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();

        if (compound != null && compound.hasKey("Template")) {
            NBTTagCompound template = compound.getCompoundTag("Template");
            if (template.hasKey("Simple")) {
                NBTTagCompound simple = template.getCompoundTag("Simple");
                if (simple.hasKey("Output")) {
                    return StatCollector.translateToLocalFormatted("item.SSTemplate.written.name", simple.getString("Output"));
                }
            }
        }
        return StatCollector.translateToLocal("item.SSTemplate.empty.name");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        NBTTagCompound compound = stack.getTagCompound();

        if (compound != null && compound.hasKey("Template")) {
            NBTTagCompound template = compound.getCompoundTag("Template");
            if (template.hasKey("Simple")) {
                NBTTagCompound simple = template.getCompoundTag("Simple");
                if (simple.hasKey("Input")) {
                    NBTTagList input = simple.getTagList("Input");
                    for (int i = 0; i < input.tagCount(); i++) {
                        list.add(((NBTTagString) input.tagAt(i)).data);
                    }
                }
            }
        }
        else if (stack.getItemDamage() > 0) {
            list.add("Broken template");
        }

        super.addInformation(stack, player, list, debug);
    }

}
