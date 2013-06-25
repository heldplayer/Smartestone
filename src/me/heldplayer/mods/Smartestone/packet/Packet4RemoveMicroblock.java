
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

public class Packet4RemoveMicroblock extends HeldCorePacket {

    public int blockX;
    public int blockY;
    public int blockZ;
    public int index;

    public Packet4RemoveMicroblock(int packetId) {
        super(packetId);
    }

    public Packet4RemoveMicroblock(TileEntityMicro tile, MicroBlockInfo info) {
        super(4);

        this.blockX = tile.xCoord;
        this.blockY = tile.yCoord;
        this.blockZ = tile.zCoord;
        this.index = info.index;
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
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.blockX);
        out.writeInt(this.blockY);
        out.writeInt(this.blockZ);
        out.writeInt(this.index);
    }

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        World world = player.worldObj;
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(blockX, blockY, blockZ);
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

        world.markBlockForUpdate(blockX, blockY, blockZ);
    }

}
