
package me.heldplayer.mods.Smartestone.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.mods.Smartestone.tileentity.TileEntityRotatable;
import me.heldplayer.mods.Smartestone.util.Direction;
import me.heldplayer.mods.Smartestone.util.Rotation;
import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class Packet1RotatableTile extends HeldCorePacket {

    public int blockX;
    public int blockY;
    public int blockZ;
    public int direction;
    public int rotation;
    public String name;
    public NBTTagCompound compound;

    public Packet1RotatableTile(int packetId) {
        super(packetId, null);
    }

    public Packet1RotatableTile(TileEntityRotatable tile) {
        super(1, tile.worldObj);

        this.blockX = tile.xCoord;
        this.blockY = tile.yCoord;
        this.blockZ = tile.zCoord;
        this.direction = tile.direction.ordinal();
        this.rotation = tile.rotation.ordinal();
        this.name = tile.customName;

        this.compound = new NBTTagCompound();
        tile.writeNBT(this.compound);
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
        this.direction = in.readUnsignedByte();
        this.rotation = in.readUnsignedByte();

        int length = in.readInt();
        byte[] nameBytes = new byte[length];
        in.readFully(nameBytes);
        this.name = new String(nameBytes);

        this.compound = (NBTTagCompound) NBTBase.readNamedTag(in);
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        byte[] nameBytes = this.name.getBytes();

        out.writeInt(this.blockX);
        out.writeInt(this.blockY);
        out.writeInt(this.blockZ);
        out.writeByte(this.direction);
        out.writeByte(this.rotation);
        out.writeInt(nameBytes.length);
        out.write(nameBytes);

        NBTBase.writeNamedTag(this.compound, out);
    }

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        World world = player.worldObj;
        TileEntityRotatable tile = (TileEntityRotatable) world.getBlockTileEntity(blockX, blockY, blockZ);
        if (tile != null) {
            tile.direction = Direction.getDirection(direction);
            tile.rotation = Rotation.getRotation(rotation);
            tile.customName = name;
            tile.readNBT(compound);
        }

        world.markBlockForUpdate(blockX, blockY, blockZ);
    }

}
