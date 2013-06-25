
package me.heldplayer.mods.Smartestone;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

import me.heldplayer.api.Smartestone.micro.MicroBlockAPI;
import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
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
            case 1: {
                int posX = dat.readInt();
                int posY = dat.readInt();
                int posZ = dat.readInt();
                int direction = dat.readUnsignedByte();
                int rotation = dat.readUnsignedByte();
                int length = dat.readInt();
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
            case 2: {
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
            case 3: {
                int posX = dat.readInt();
                int posY = dat.readInt();
                int posZ = dat.readInt();
                int index = dat.readInt();
                int length = dat.readInt();
                byte[] typeBytes = new byte[length];
                dat.readFully(typeBytes);
                length = dat.readInt();
                byte[] materialBytes = new byte[length];
                dat.readFully(materialBytes);
                int data = dat.readInt();

                World world = ((EntityPlayer) player).worldObj;
                TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(posX, posY, posZ);
                if (tile != null) {
                    MicroBlockInfo info = new MicroBlockInfo(MicroBlockAPI.getMaterial(new String(materialBytes)), MicroBlockAPI.getSubBlock(new String(typeBytes)), data);
                    info.index = index;
                    tile.getSubBlocks().add(info);
                }

                world.markBlockForUpdate(posX, posY, posZ);
            }
            break;
            case 4: {
                int posX = dat.readInt();
                int posY = dat.readInt();
                int posZ = dat.readInt();
                int index = dat.readInt();

                World world = ((EntityPlayer) player).worldObj;
                TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(posX, posY, posZ);
                if (tile != null) {
                    Set<MicroBlockInfo> infos = tile.getSubBlocks();

                    MicroBlockInfo removed = null;
                    for (MicroBlockInfo info : infos) {
                        if (info.index == index) {
                            removed = info;
                            break;
                        }
                    }

                    if (removed != null) {
                        infos.remove(removed);
                    }
                }

                world.markBlockForUpdate(posX, posY, posZ);
            }
            break;
            case 5: {
                int posX = dat.readInt();
                int posY = dat.readInt();
                int posZ = dat.readInt();
                int index = dat.readInt();
                int data = dat.readInt();

                World world = ((EntityPlayer) player).worldObj;
                TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(posX, posY, posZ);
                if (tile != null) {
                    Set<MicroBlockInfo> infos = tile.getSubBlocks();

                    for (MicroBlockInfo info : infos) {
                        if (info.index == index) {
                            info.setData(data);

                            break;
                        }
                    }
                }

                world.markBlockForUpdate(posX, posY, posZ);
            }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a packet when requested<br/>
     * <br/>
     * Types:
     * <ol>
     * <li>
     * <b>Basic Tile information</b><br>
     * <b>Arguments:</b> TileEntityRotatable<br>
     * Sends name, direction, rotation and custom data</li>
     * <li>
     * <b>MicroBlock tile information</b><br>
     * <b>Arguments:</b> TileEntityMicro;<br>
     * Sends all sub blocks</li>
     * <li>
     * <b>Add Microblock</b><br>
     * <b>Arguments: TileEntityMicro, MicroBlockInfo</b><br>
     * Sends to the client that a new block has been added to a microblock</li>
     * <li>
     * <b>Remove Microblock</b><br>
     * <b>Arguments: TileEntityMicro, MicroBlockInfo</b><br>
     * Sends to the client that a block has been removed from a microblock</li>
     * <li>
     * <b>Modify Microblock</b><br>
     * <b>Arguments: TileEntityMicro, MicroBlockInfo</b><br>
     * Sends to the client that a block has been modifed in a microblock</li>
     * </ol>
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

        boolean mapPacket = false;

        try {
            dos.writeByte(type);
            switch (type) {
            case 1: {
                TileEntityRotatable tile = (TileEntityRotatable) args[0];
                byte[] nameBytes = tile.customName.getBytes();

                NBTTagCompound compound = new NBTTagCompound();
                tile.writeNBT(compound);

                dos.writeInt(tile.xCoord);
                dos.writeInt(tile.yCoord);
                dos.writeInt(tile.zCoord);
                dos.writeByte(tile.direction.ordinal());
                dos.writeByte(tile.rotation.ordinal());
                dos.writeInt(nameBytes.length);
                dos.write(nameBytes);

                mapPacket = true;

                NBTBase.writeNamedTag(compound, dos);
            }
            break;
            case 2: {
                TileEntityMicro tile = (TileEntityMicro) args[0];

                NBTTagCompound compound = new NBTTagCompound();
                tile.writeNBT(compound);

                dos.writeInt(tile.xCoord);
                dos.writeInt(tile.yCoord);
                dos.writeInt(tile.zCoord);

                mapPacket = true;

                NBTBase.writeNamedTag(compound, dos);
            }
            break;
            case 3: {
                TileEntityMicro tile = (TileEntityMicro) args[0];
                MicroBlockInfo info = (MicroBlockInfo) args[1];

                byte[] typeBytes = info.getType().getTypeName().getBytes();
                byte[] materialBytes = info.getMaterial().getIdentifier().getBytes();

                dos.writeInt(tile.xCoord);
                dos.writeInt(tile.yCoord);
                dos.writeInt(tile.zCoord);
                dos.writeInt(info.index);
                dos.writeInt(typeBytes.length);
                dos.write(typeBytes);
                dos.writeInt(materialBytes.length);
                dos.write(materialBytes);
                dos.writeInt(info.getData());

                mapPacket = true;
            }
            break;
            case 4: {
                TileEntityMicro tile = (TileEntityMicro) args[0];
                MicroBlockInfo info = (MicroBlockInfo) args[1];

                dos.writeInt(tile.xCoord);
                dos.writeInt(tile.yCoord);
                dos.writeInt(tile.zCoord);
                dos.writeInt(info.index);

                mapPacket = true;
            }
            break;
            case 5: {
                TileEntityMicro tile = (TileEntityMicro) args[0];
                MicroBlockInfo info = (MicroBlockInfo) args[1];

                dos.writeInt(tile.xCoord);
                dos.writeInt(tile.yCoord);
                dos.writeInt(tile.zCoord);
                dos.writeInt(info.index);
                dos.writeInt(info.getData());

                mapPacket = true;
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
        packet.isChunkDataPacket = mapPacket;

        return packet;
    }
}
