
package me.heldplayer.mods.Smartestone.block;

import me.heldplayer.mods.Smartestone.ModSmartestone;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityCraftingChest;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityInductionishFurnace;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityRotatable;
import me.heldplayer.mods.Smartestone.util.Const;
import me.heldplayer.mods.Smartestone.util.Objects;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMulti1 extends BlockMulti {

    public BlockMulti1(int id, Material material) {
        super(id, material);
    }

    private String[] types = new String[] {};

    @Override
    public String getIdentifier(int meta) {
        if (meta >= 0 || meta < this.types.length) {
            return this.types[meta];
        }

        return null;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return metadata <= 1;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int blockId, int meta) {
        if (world.isRemote) {
            return;
        }

        TileEntityRotatable tile = (TileEntityRotatable) world.getBlockTileEntity(x, y, z);

        if (tile instanceof TileEntityCraftingChest) {
            IInventory inventory = (IInventory) tile;
            for (int i = 0; i < Const.CRAFTINGCHEST_CRAFTRESULT_SLOT; i++) {
                ItemStack stack = inventory.getStackInSlot(i);

                if (stack != null) {
                    float xMotion = rnd.nextFloat() * 0.8F + 0.1F;
                    float yMotion = rnd.nextFloat() * 0.8F + 0.1F;
                    float zMotion = rnd.nextFloat() * 0.8F + 0.1F;

                    while (stack.stackSize > 0) {
                        int size = rnd.nextInt(21) + 10;

                        if (size > stack.stackSize) {
                            size = stack.stackSize;
                        }

                        stack.stackSize -= size;
                        EntityItem item = new EntityItem(world, (x + xMotion), (y + yMotion), (z + zMotion), new ItemStack(stack.itemID, size, stack.getItemDamage()));

                        if (stack.hasTagCompound()) {
                            item.setEntityItemStack(stack);
                        }

                        item.motionX = ((float) rnd.nextGaussian() * 0.05F);
                        item.motionY = ((float) rnd.nextGaussian() * 0.05F + 0.2F);
                        item.motionZ = ((float) rnd.nextGaussian() * 0.05F);
                        world.spawnEntityInWorld(item);
                    }
                }
            }
        }
        else {
            super.breakBlock(world, x, y, z, blockId, meta);
        }
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);

        switch (meta) {
        case 0:
            return Container.calcRedstoneFromInventory((TileEntityCraftingChest) world.getBlockTileEntity(x, y, z));
        case 1:
            return Container.calcRedstoneFromInventory((TileEntityInductionishFurnace) world.getBlockTileEntity(x, y, z));
        }

        return 0;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        switch (metadata) {
        case 0:
            return new TileEntityCraftingChest();
        case 1:
            return new TileEntityInductionishFurnace();
        }

        return null;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
        if (super.onBlockActivated(world, x, y, z, player, side, posX, posY, posZ)) {
            return true;
        }

        if (world.isRemote) {
            if (this.hasTileEntity(world.getBlockMetadata(x, y, z))) {
                return true;
            }
            return false;
        }
        else {
            int meta = world.getBlockMetadata(x, y, z);

            switch (meta) {
            case 0: {
                TileEntityCraftingChest tileEntity = (TileEntityCraftingChest) world.getBlockTileEntity(x, y, z);

                if (tileEntity != null) {
                    player.openGui(ModSmartestone.instance, 0, world, x, y, z);
                }

                return true;
            }
            case 1: {
                TileEntityInductionishFurnace tileEntity = (TileEntityInductionishFurnace) world.getBlockTileEntity(x, y, z);

                if (tileEntity != null) {
                    player.openGui(ModSmartestone.instance, 1, world, x, y, z);
                }

                return true;
            }
            }
            return false;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.missing = register.registerIcon("smartestone:missing");
        this.bottom = new Icon[16];
        this.top = new Icon[16];
        this.front = new Icon[16];
        this.back = new Icon[16];
        this.left = new Icon[16];
        this.right = new Icon[16];

        for (int i = 0; i < 16; i++) {
            if (this.hasTileEntity(i)) {
                this.bottom[i] = register.registerIcon(Objects.TEXTURE_PREFIX + ":multi1_" + i + "_bottom");
                this.top[i] = register.registerIcon(Objects.TEXTURE_PREFIX + ":multi1_" + i + "_top");
                this.front[i] = register.registerIcon(Objects.TEXTURE_PREFIX + ":multi1_" + i + "_front");
                this.back[i] = register.registerIcon(Objects.TEXTURE_PREFIX + ":multi1_" + i + "_back");
                this.left[i] = register.registerIcon(Objects.TEXTURE_PREFIX + ":multi1_" + i + "_left");
                this.right[i] = register.registerIcon(Objects.TEXTURE_PREFIX + ":multi1_" + i + "_right");
            }
            else {
                this.bottom[i] = register.registerIcon("smartestone:missing");
                this.top[i] = register.registerIcon("smartestone:missing");
                this.front[i] = register.registerIcon("smartestone:missing");
                this.back[i] = register.registerIcon("smartestone:missing");
                this.left[i] = register.registerIcon("smartestone:missing");
                this.right[i] = register.registerIcon("smartestone:missing");
            }
        }

        super.registerIcons(register);
    }

}
