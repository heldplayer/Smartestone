
package me.heldplayer.mods.Smartestone.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.mods.Smartestone.tileentity.TileEntityRotatable;
import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class Packet6SetInventorySlotContents extends HeldCorePacket {

    public int blockX;
    public int blockY;
    public int blockZ;
    public int slot;
    public ItemStack stack;

    public Packet6SetInventorySlotContents(int packetId) {
        super(packetId, null);
    }

    public Packet6SetInventorySlotContents(TileEntityRotatable tile, int slot, ItemStack stack) {
        super(6, tile.worldObj);

        this.blockX = tile.xCoord;
        this.blockY = tile.yCoord;
        this.blockZ = tile.zCoord;
        this.slot = slot;
        this.stack = stack;
    }

    @Override
    public Side getSendingSide() {
        return Side.SERVER;
    }

    @Override
    public void read(ByteArrayDataInput in) throws IOException {
        this.blockX = in.readInt();
        this.blockY = in.readInt();
        this.blockZ = in.readInt();
        this.slot = in.readInt();

        NBTTagCompound compound = (NBTTagCompound) NBTBase.readNamedTag(in);
        if (compound.hasKey("Stack")) {
            this.stack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Stack"));
        }
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.blockX);
        out.writeInt(this.blockY);
        out.writeInt(this.blockZ);
        out.writeInt(this.slot);

        NBTTagCompound tag = new NBTTagCompound();
        if (this.stack != null) {
            tag.setCompoundTag("Stack", this.stack.writeToNBT(new NBTTagCompound("Stack")));
        }
        NBTBase.writeNamedTag(tag, out);
    }

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        World world = player.worldObj;
        TileEntityRotatable tile = (TileEntityRotatable) world.getBlockTileEntity(this.blockX, this.blockY, this.blockZ);
        if (tile != null && tile instanceof IInventory) {
            ((IInventory) tile).setInventorySlotContents(this.slot, this.stack);
        }
    }

}
