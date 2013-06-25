
package me.heldplayer.mods.Smartestone.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.mods.Smartestone.tileentity.TileEntityMicro;
import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

public class Packet2MicroTile extends HeldCorePacket {

    public int blockX;
    public int blockY;
    public int blockZ;
    public NBTTagCompound compound;

    public Packet2MicroTile(int packetId) {
        super(packetId);
    }

    public Packet2MicroTile(TileEntityMicro tile) {
        super(2);

        this.blockX = tile.xCoord;
        this.blockY = tile.yCoord;
        this.blockZ = tile.zCoord;

        this.compound = new NBTTagCompound();
        tile.writeNBT(this.compound);
    }

    @Override
    public boolean isMapPacket() {
        return true;
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
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(blockX, blockY, blockZ);
        if (tile != null) {
            tile.readNBT(compound);
        }

        world.markBlockForUpdate(blockX, blockY, blockZ);
    }

}
