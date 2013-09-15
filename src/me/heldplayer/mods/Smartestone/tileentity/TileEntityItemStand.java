
package me.heldplayer.mods.Smartestone.tileentity;

import java.util.Arrays;
import java.util.Iterator;

import me.heldplayer.mods.Smartestone.CommonProxy;
import me.heldplayer.mods.Smartestone.packet.Packet6SetInventorySlotContents;
import me.heldplayer.mods.Smartestone.packet.PacketHandler;
import me.heldplayer.util.HeldCore.sync.ISyncable;
import me.heldplayer.util.HeldCore.sync.SInventoryStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityItemStand extends TileEntityRotatable implements IInventory {

    private ItemStack[] inventory = new ItemStack[1];
    public float hover = 0.0F;
    public float prevHover = 0.0F;

    public SInventoryStack stack;

    public TileEntityItemStand() {
        super("SSItemStand");
        this.hover = this.prevHover = CommonProxy.rand.nextFloat() * 1000.0F;

        this.stack = new SInventoryStack(this, this, 0);
        this.syncables = Arrays.asList((ISyncable) this.direction, this.rotation, this.customName, this.stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getAABBPool().getAABB(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void updateEntity() {
        if (this.worldObj.isRemote) {
            this.prevHover = this.hover;
            this.hover += 0.1F;

            if (this.hover >= 6.28F) {
                this.hover -= 6.28F;
                this.prevHover -= 6.28F;
            }
        }
        else {
            ItemStack stack = this.inventory[0];
            if (stack != null && stack.getItem() == Item.map) {
                MapData data = Item.map.getMapData(stack, this.worldObj);
                Iterator iterator = this.worldObj.playerEntities.iterator();

                while (iterator.hasNext()) {
                    Object obj = iterator.next();
                    EntityPlayerMP player = (EntityPlayerMP) obj;
                    data.func_82568_a(player);

                    if (player.playerNetServerHandler.packetSize() < 5) {
                        Packet packet = Item.map.createMapDataPacket(stack, this.worldObj, player);

                        if (packet != null) {
                            player.playerNetServerHandler.sendPacketToPlayer(packet);
                        }
                    }
                }
            }
        }
    }

    // TileEntityBase
    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList items = new NBTTagList();

        for (int slot = 0; slot < this.inventory.length; ++slot) {
            if (this.inventory[slot] != null) {
                NBTTagCompound itemCompound = new NBTTagCompound();
                itemCompound.setByte("Slot", (byte) slot);
                this.inventory[slot].writeToNBT(itemCompound);
                items.appendTag(itemCompound);
            }
        }

        compound.setTag("Items", items);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList items = compound.getTagList("Items");
        this.inventory = new ItemStack[1];

        for (int i = 0; i < items.tagCount(); ++i) {
            NBTTagCompound itemCompound = (NBTTagCompound) items.tagAt(i);
            byte slot = itemCompound.getByte("Slot");

            if (slot >= 0 && slot < this.inventory.length) {
                this.inventory[slot] = ItemStack.loadItemStackFromNBT(itemCompound);
            }
        }
    }

    // IInventory
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) < 64;
    }

    @Override
    public boolean isInvNameLocalized() {
        return super.isInvNameLocalized();
    }

    @Override
    public String getInvName() {
        return super.getInvName();
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.inventory[slot] != null) {
            ItemStack stack = this.inventory[slot];
            this.inventory[slot] = null;
            return stack;
        }
        else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.inventory[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack newStack) {
        this.inventory[slot] = newStack;
        if (slot == 0) {
            this.stack.setChanged(true);
        }

        if (newStack != null && newStack.stackSize > this.getInventoryStackLimit()) {
            newStack.stackSize = this.getInventoryStackLimit();
        }

        if (slot == 0 && !this.worldObj.isRemote) {
            Chunk chunk = this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord);
            Packet6SetInventorySlotContents packet = new Packet6SetInventorySlotContents(this, slot, newStack);
            me.heldplayer.util.HeldCore.packet.PacketHandler.sendPacketToPlayersWatching(PacketHandler.instance.createPacket(packet), this.worldObj.getWorldInfo().getVanillaDimension(), chunk.xPosition, chunk.zPosition);
        }

        this.onInventoryChanged();
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (this.inventory[slot] != null) {
            ItemStack stack;

            if (this.inventory[slot].stackSize <= amount) {
                stack = this.inventory[slot];
                this.inventory[slot] = null;

                if (slot == 0) {
                    this.stack.setChanged(true);
                }

                this.onInventoryChanged();

                return stack;
            }
            else {
                stack = this.inventory[slot].splitStack(amount);

                if (this.inventory[slot].stackSize == 0) {
                    this.inventory[slot] = null;

                    if (slot == 0) {
                        this.stack.setChanged(true);
                    }
                }

                this.onInventoryChanged();

                return stack;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {
        return true;
    }

}
