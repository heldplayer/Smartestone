
package me.heldplayer.mods.Smartestone.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class Packet2SerializeableTile extends HeldCorePacket {

    public int blockX;
    public int blockY;
    public int blockZ;
    public NBTTagCompound compound;

    public Packet2SerializeableTile(int packetId) {
        super(packetId, null);
    }

    public Packet2SerializeableTile(ISerializableTile tile) {
        super(2, tile.getWorld());

        this.blockX = tile.getX();
        this.blockY = tile.getY();
        this.blockZ = tile.getZ();

        this.compound = new NBTTagCompound();
        tile.writeToTag(this.compound);
    }

    @Override
    public boolean isMapPacket() {
        return true;
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

        this.compound = (NBTTagCompound) NBTBase.readNamedTag(in);
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.blockX);
        out.writeInt(this.blockY);
        out.writeInt(this.blockZ);

        NBTBase.writeNamedTag(this.compound, out);
    }

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        World world = player.worldObj;
        ISerializableTile tile = (ISerializableTile) world.getBlockTileEntity(this.blockX, this.blockY, this.blockZ);
        if (tile != null) {
            tile.readFromTag(this.compound);
        }

        world.markBlockForUpdate(this.blockX, this.blockY, this.blockZ);
    }

}
