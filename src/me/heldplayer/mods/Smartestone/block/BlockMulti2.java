
package me.heldplayer.mods.Smartestone.block;

import java.util.List;

import me.heldplayer.mods.Smartestone.ModSmartestone;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityItemStand;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityRotatable;
import me.heldplayer.mods.Smartestone.util.Direction;
import me.heldplayer.mods.Smartestone.util.Objects;
import me.heldplayer.mods.Smartestone.util.Rotation;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMulti2 extends BlockMulti {

    public BlockMulti2(int id, Material material) {
        super(id, material);
    }

    private String[] types = new String[] {};

    @Override
    public boolean canBeRotated(int meta) {
        return true;
    }

    @Override
    public boolean canBeRedirected(int meta) {
        return false;
    }

    @Override
    public String getIdentifier(int meta) {
        if (meta >= 0 || meta < this.types.length) {
            return this.types[meta];
        }

        return null;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return metadata <= 0;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);

        switch (meta) {
        case 0:
            return Container.calcRedstoneFromInventory((TileEntityItemStand) world.getBlockTileEntity(x, y, z));
        }

        return 0;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        switch (metadata) {
        case 0:
            return new TileEntityItemStand();
        }

        return null;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity, ItemStack itemStack) {
        TileEntityRotatable tile = (TileEntityRotatable) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            return;
        }

        int meta = itemStack.getItemDamage();

        if (meta == 0) {
            tile.direction = Direction.getDirection(this.side);
            tile.rotation = Rotation.DEFAULT;
        }
        else {
            int rotation = BlockPistonBase.determineOrientation(world, x, y, z, entity);

            tile.direction = Direction.getDirection(rotation);
            tile.rotation = Rotation.DEFAULT;
        }

        if (itemStack.hasDisplayName()) {
            tile.customName = itemStack.getDisplayName();
        }
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
            ItemStack clickedStack = player.getCurrentEquippedItem();

            switch (meta) {
            case 0:
                TileEntityItemStand tileEntity = (TileEntityItemStand) world.getBlockTileEntity(x, y, z);

                if (tileEntity != null) {
                    if (clickedStack != null && clickedStack.itemID != 0 && clickedStack.stackSize != 0) {
                        ItemStack tileItem = tileEntity.getStackInSlot(0);

                        if (tileItem == null || tileItem.itemID == 0 || tileItem.stackSize == 0) {
                            tileEntity.setInventorySlotContents(0, clickedStack.copy());
                            clickedStack.stackSize--;

                            if (clickedStack.stackSize <= 0) {
                                player.setCurrentItemOrArmor(0, null);
                            }
                            tileEntity.onInventoryChanged();
                            return true;
                        }
                        else {
                            if (tileItem.isItemEqual(clickedStack) && ItemStack.areItemStackTagsEqual(tileItem, clickedStack)) {
                                if (clickedStack.stackSize < clickedStack.getMaxStackSize()) {
                                    clickedStack.stackSize++;
                                    tileEntity.setInventorySlotContents(0, null);
                                    tileEntity.onInventoryChanged();
                                    return true;
                                }
                            }
                        }
                    }
                    else if (player.isSneaking()) {
                        ItemStack tileItem = tileEntity.getStackInSlot(0);

                        if (tileItem != null && tileItem.itemID != 0 && tileItem.stackSize != 0) {
                            tileEntity.setInventorySlotContents(0, null);
                            player.setCurrentItemOrArmor(0, tileItem);
                            tileEntity.onInventoryChanged();
                            return true;
                        }
                    }

                    player.openGui(ModSmartestone.instance, 16, world, x, y, z);
                }

                return true;
            }

            return false;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.missing = register.registerIcon("Smartestone:missing");
        this.bottom = new Icon[16];
        this.top = new Icon[16];
        this.front = new Icon[16];
        this.back = new Icon[16];
        this.left = new Icon[16];
        this.right = new Icon[16];

        for (int i = 0; i < 16; i++) {
            if (this.hasTileEntity(i)) {
                this.bottom[i] = register.registerIcon(Objects.TEXTURE_PREFIX + ":multi2_" + i + "_bottom");
                this.top[i] = register.registerIcon(Objects.TEXTURE_PREFIX + ":multi2_" + i + "_top");
                this.front[i] = register.registerIcon(Objects.TEXTURE_PREFIX + ":multi2_" + i + "_front");
                this.back[i] = register.registerIcon(Objects.TEXTURE_PREFIX + ":multi2_" + i + "_back");
                this.left[i] = register.registerIcon(Objects.TEXTURE_PREFIX + ":multi2_" + i + "_left");
                this.right[i] = register.registerIcon(Objects.TEXTURE_PREFIX + ":multi2_" + i + "_right");
            }
            else {
                this.bottom[i] = register.registerIcon("Smartestone:missing");
                this.top[i] = register.registerIcon("Smartestone:missing");
                this.front[i] = register.registerIcon("Smartestone:missing");
                this.back[i] = register.registerIcon("Smartestone:missing");
                this.left[i] = register.registerIcon("Smartestone:missing");
                this.right[i] = register.registerIcon("Smartestone:missing");
            }
        }

        super.registerIcons(register);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void setBlockBoundsForItemRender(int meta) {
        switch (meta) {
        case 0:
            this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F);
            return;
        }

        super.setBlockBoundsForItemRender(meta);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);

        switch (meta) {
        case 0:
            TileEntityItemStand tile = (TileEntityItemStand) world.getBlockTileEntity(x, y, z);

            if (tile == null) {
                break;
            }

            Direction direction = tile.direction;

            switch (direction.ordinal()) {
            case 0:
                this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F);
                return;
            case 1:
                this.setBlockBounds(0.125F, 0.875F, 0.125F, 0.875F, 1.0F, 0.875F);
                return;
            case 2:
                this.setBlockBounds(0.125F, 0.125F, 0.875F, 0.875F, 0.875F, 1.0F);
                return;
            case 3:
                this.setBlockBounds(0.125F, 0.125F, 0.0F, 0.875F, 0.875F, 0.125F);
                return;
            case 4:
                this.setBlockBounds(0.875F, 0.125F, 0.125F, 1.0F, 0.875F, 0.875F);
                return;
            case 5:
                this.setBlockBounds(0.0F, 0.125F, 0.125F, 0.125F, 0.875F, 0.875F);
                return;
            }
        }
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

}
