
package me.heldplayer.mods.Smartestone.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.api.Smartestone.micro.MicroBlockAPI;
import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityMicro;
import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

public class Packet3AddMicroblock extends HeldCorePacket {

    public int blockX;
    public int blockY;
    public int blockZ;
    public MicroBlockInfo info;

    public Packet3AddMicroblock(int packetId) {
        super(packetId);
    }

    public Packet3AddMicroblock(TileEntityMicro tile, MicroBlockInfo info) {
        super(3);

        this.blockX = tile.xCoord;
        this.blockY = tile.yCoord;
        this.blockZ = tile.zCoord;
        this.info = info;
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

        int index = in.readInt();
        byte[] typeBytes = new byte[in.readInt()];
        in.readFully(typeBytes);
        String type = new String(typeBytes);
        byte[] materialBytes = new byte[in.readInt()];
        in.readFully(materialBytes);
        String material = new String(materialBytes);
        int data = in.readInt();

        this.info = new MicroBlockInfo(MicroBlockAPI.getMaterial(material), MicroBlockAPI.getSubBlock(type), data);
        this.info.index = index;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.blockX);
        out.writeInt(this.blockY);
        out.writeInt(this.blockZ);

        byte[] typeBytes = info.getType().getTypeName().getBytes();
        byte[] materialBytes = info.getMaterial().getIdentifier().getBytes();

        out.writeInt(info.index);
        out.writeInt(typeBytes.length);
        out.write(typeBytes);
        out.writeInt(materialBytes.length);
        out.write(materialBytes);
        out.writeInt(info.getData());
    }

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        World world = player.worldObj;
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(blockX, blockY, blockZ);
        if (tile != null) {
            tile.getSubBlocks().add(this.info);
        }

        world.markBlockForUpdate(blockX, blockY, blockZ);
    }

}
