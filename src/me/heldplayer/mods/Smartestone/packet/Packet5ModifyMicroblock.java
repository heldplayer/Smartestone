
package me.heldplayer.mods.Smartestone.packet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityMicro;
import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class Packet5ModifyMicroblock extends HeldCorePacket {

    public int blockX;
    public int blockY;
    public int blockZ;
    public int index;
    public int data;

    public Packet5ModifyMicroblock(int packetId) {
        super(packetId, null);
    }

    public Packet5ModifyMicroblock(TileEntityMicro tile, MicroBlockInfo info) {
        super(5, tile.worldObj);

        this.blockX = tile.xCoord;
        this.blockY = tile.yCoord;
        this.blockZ = tile.zCoord;
        this.index = info.index;
        this.data = info.getData();
    }

    @Override
    public Side getSendingSide() {
        return Side.SERVER;
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
        this.index = in.readInt();
        this.data = in.readInt();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.blockX);
        out.writeInt(this.blockY);
        out.writeInt(this.blockZ);
        out.writeInt(this.index);
        out.writeInt(this.data);
    }

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        World world = player.worldObj;
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(blockX, blockY, blockZ);
        if (tile != null) {
            Set<MicroBlockInfo> infos = tile.getSubBlocks();

            for (MicroBlockInfo info : infos) {
                if (info.index == index) {
                    info.setData(data);

                    break;
                }
            }
        }

        world.markBlockForUpdate(blockX, blockY, blockZ);
    }

}
