
package me.heldplayer.mods.Smartestone.item;

import java.util.List;
import java.util.Set;

import me.heldplayer.api.Smartestone.micro.IMicroBlockMaterial;
import me.heldplayer.api.Smartestone.micro.IMicroBlockSubBlock;
import me.heldplayer.api.Smartestone.micro.MicroBlockAPI;
import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityMicro;
import me.heldplayer.mods.Smartestone.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMicroBlock extends Item {

    public ItemMicroBlock(int itemId) {
        super(itemId);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getSpriteNumber() {
        return 0;
    }

    @Override
    public Icon getIcon(ItemStack stack, int pass) {
        NBTTagCompound compound = stack.getTagCompound();

        String identifier = compound != null ? compound.getString("Material") : "null";

        IMicroBlockMaterial material = MicroBlockAPI.getMaterial(identifier);

        if (material == null) {
            return null;
        }

        return material.getIcon(0);
    }

    @Override
    public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        NBTTagCompound compound = stack.getTagCompound();

        String identifier = compound != null ? compound.getString("Material") : "null";

        IMicroBlockMaterial material = MicroBlockAPI.getMaterial(identifier);

        if (material == null) {
            return null;
        }

        return material.getIcon(0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        NBTTagCompound compound = stack.getTagCompound();

        IMicroBlockMaterial material = MicroBlockAPI.getMaterial(compound != null ? compound.getString("Material") : "null");

        if (material != null) {
            list.add("Type: " + material.getDisplayName());
        }
        else {
            list.add("Unknown material");
        }

        if (debug) {
            if (material != null) {
                list.add("Material ID: " + material.getIdentifier());
            }
        }

        super.addInformation(stack, player, list, debug);
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();

        IMicroBlockSubBlock subBlock = MicroBlockAPI.getSubBlock(compound != null ? compound.getString("Type") : "null");

        if (subBlock == null) {
            return "Unknown Microblock";
        }

        return subBlock.getTypeName();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (stack.stackSize == 0) {
            return false;
        }
        if (!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        }

        //if (world.canPlaceEntityOnSide(MicroBlockAPI.microBlockId, x, y, z, false, side, player, stack)) {
        NBTTagCompound compound = stack.getTagCompound();

        IMicroBlockMaterial material = MicroBlockAPI.getMaterial(compound != null ? compound.getString("Material") : "null");
        IMicroBlockSubBlock subBlock = MicroBlockAPI.getSubBlock(compound != null ? compound.getString("Type") : "null");

        if (material == null || subBlock == null) {
            return false;
        }

        MicroBlockInfo info = new MicroBlockInfo(material, subBlock, 0);

        ForgeDirection dir = ForgeDirection.getOrientation(side);

        x += dir.offsetX;
        y += dir.offsetY;
        z += dir.offsetZ;

        if (world.getBlockId(x, y, z) != Objects.blockMicro.blockID) {
            if (world.getBlockId(x, y, z) == 0 && !world.setBlock(x, y, z, MicroBlockAPI.microBlockId, 0, 3)) {
                return false;
            }
        }

        if (world.getBlockId(x, y, z) == MicroBlockAPI.microBlockId) {
            Block block = Objects.blockMicro;
            block.onBlockPlacedBy(world, x, y, z, player, stack);
            block.onPostBlockPlaced(world, x, y, z, 0);

            int data = subBlock.onItemUse(player, side, hitX, hitY, hitZ);

            TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

            if (tile == null) {
                return false;
            }

            MicroBlockInfo result = new MicroBlockInfo(info.getMaterial(), info.getType(), data);
            if (!subBlock.canBeAdded(tile, result)) {
                return false;
            }

            tile.getSubBlocks().add(result);

            world.notifyBlockOfNeighborChange(x, y, z, MicroBlockAPI.microBlockId);
            world.notifyBlockChange(x, y, z, MicroBlockAPI.microBlockId);

            tile.resendTileData();

            world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

            stack.stackSize--;
        }

        return true;
        //}

        //return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int itemId, CreativeTabs tabs, List list) {
        Set<String> subBlocks = MicroBlockAPI.getSubBlockNames();
        Set<String> materials = MicroBlockAPI.getMaterialNames();

        for (String material : materials) {
            IMicroBlockMaterial materialInst = MicroBlockAPI.getMaterial(material);
            for (String subBlock : subBlocks) {
                IMicroBlockSubBlock subBlockInst = MicroBlockAPI.getSubBlock(subBlock);

                if (subBlockInst.isMaterialApplicable(materialInst)) {
                    ItemStack stack = new ItemStack(itemId, 1, MicroBlockAPI.ordinal(subBlockInst));
                    NBTTagCompound compound = new NBTTagCompound("tag");
                    compound.setString("Material", material);
                    compound.setString("Type", subBlock);
                    stack.setTagCompound(compound);
                    list.add(stack);
                }
            }
        }
    }

}
