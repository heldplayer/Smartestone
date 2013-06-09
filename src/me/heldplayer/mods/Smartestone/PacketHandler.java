
package me.heldplayer.mods.Smartestone;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.mods.Smartestone.tileentity.TileEntityMicro;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityRotatable;
import me.heldplayer.mods.Smartestone.util.Direction;
import me.heldplayer.mods.Smartestone.util.Rotation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);

        int type = dat.readUnsignedByte();

        try {
            switch (type) {
            case 0: {
                int posX = dat.readInt();
                int posY = dat.readInt();
                int posZ = dat.readInt();
                int direction = dat.readUnsignedByte();
                int rotation = dat.readUnsignedByte();
                int length = dat.readUnsignedByte();
                byte[] nameBytes = new byte[length];
                dat.readFully(nameBytes);

                NBTTagCompound compound = (NBTTagCompound) NBTBase.readNamedTag(dat);

                World world = ((EntityPlayer) player).worldObj;
                TileEntityRotatable tile = (TileEntityRotatable) world.getBlockTileEntity(posX, posY, posZ);
                if (tile != null) {
                    tile.direction = Direction.getDirection(direction);
                    tile.rotation = Rotation.getRotation(rotation);
                    tile.customName = new String(nameBytes);
                    tile.readNBT(compound);
                }

                world.markBlockForUpdate(posX, posY, posZ);
            }
            break;
            case 1: {
                int posX = dat.readInt();
                int posY = dat.readInt();
                int posZ = dat.readInt();

                NBTTagCompound compound = (NBTTagCompound) NBTBase.readNamedTag(dat);

                World world = ((EntityPlayer) player).worldObj;
                TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(posX, posY, posZ);
                if (tile != null) {
                    tile.readNBT(compound);
                }

                world.markBlockForUpdate(posX, posY, posZ);
            }
            break;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a packet when requested<br/>
     * <br/>
     * Types:<br/>
     * 0: Basic Tile information, args: TileEntityBase;
     * Sends name, direction and rotation
     * 1: MicroBlock tile information, agrs: TileEntityMicro;
     * Sends all sub blocks
     * 
     * @param type
     *        The type of the packet
     * @param args
     *        Arguments
     * @return A packet
     */
    public static Packet getPacket(int type, Object... args) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(32767);
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            dos.writeByte(type);
            switch (type) {
            case 0: {
                TileEntityRotatable tile = (TileEntityRotatable) args[0];
                byte[] nameBytes = tile.customName.getBytes();

                NBTTagCompound compound = new NBTTagCompound();
                tile.writeNBT(compound);

                dos.writeInt(tile.xCoord);
                dos.writeInt(tile.yCoord);
                dos.writeInt(tile.zCoord);
                dos.writeByte(tile.direction.ordinal());
                dos.writeByte(tile.rotation.ordinal());
                dos.writeByte(nameBytes.length);
                dos.write(nameBytes);

                NBTBase.writeNamedTag(compound, dos);
            }
            break;
            case 1: {
                TileEntityMicro tile = (TileEntityMicro) args[0];

                NBTTagCompound compound = new NBTTagCompound();
                tile.writeNBT(compound);

                dos.writeInt(tile.xCoord);
                dos.writeInt(tile.yCoord);
                dos.writeInt(tile.zCoord);

                NBTBase.writeNamedTag(compound, dos);
            }
            break;
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();

        packet.channel = "SSChannel";
        packet.data = bos.toByteArray();
        packet.length = packet.data.length;

        return packet;
    }
}
