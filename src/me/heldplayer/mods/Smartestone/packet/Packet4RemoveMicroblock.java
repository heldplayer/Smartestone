
package me.heldplayer.mods.Smartestone.packet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.mods.Smartestone.block.BlockMicro;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityMicro;
import me.heldplayer.mods.Smartestone.util.Objects;
import me.heldplayer.util.HeldCore.client.MC;
import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class Packet4RemoveMicroblock extends HeldCorePacket {

    public int blockX;
    public int blockY;
    public int blockZ;
    public int index;

    public Packet4RemoveMicroblock(int packetId) {
        super(packetId, null);
    }

    public Packet4RemoveMicroblock(TileEntityMicro tile, MicroBlockInfo info) {
        super(4, tile.worldObj);

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
    public Side getSendingSide() {
        return Side.SERVER;
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
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(this.blockX, this.blockY, this.blockZ);
        if (tile != null) {
            Set<MicroBlockInfo> infos = tile.getSubBlocks();

            MicroBlockInfo removed = null;
            for (MicroBlockInfo info : infos) {
                if (info.index == this.index) {
                    removed = info;
                    break;
                }
            }

            if (removed != null) {
                infos.remove(removed);
            }

            EffectRenderer effectRenderer = MC.getEffectRenderer();

            byte pps = 4;

            for (int tX = 0; tX < pps; ++tX) {
                for (int tY = 0; tY < pps; ++tY) {
                    for (int tZ = 0; tZ < pps; ++tZ) {
                        double pX = (double) this.blockX + ((double) tX + 0.5D) / (double) pps;
                        double pY = (double) this.blockY + ((double) tY + 0.5D) / (double) pps;
                        double pZ = (double) this.blockZ + ((double) tZ + 0.5D) / (double) pps;
                        int side = BlockMicro.rnd.nextInt(6);

                        EntityDiggingFX fx = (new EntityDiggingFX(world, pX, pY, pZ, pX - (double) this.blockX - 0.5D, pY - (double) this.blockY - 0.5D, pZ - (double) this.blockZ - 0.5D, Objects.blockMicro, side, 0));
                        if (removed.getMaterial() != null) {
                            fx.func_110125_a(removed.getMaterial().getIcon(0));
                        }
                        effectRenderer.addEffect(fx);
                    }
                }
            }
        }

        world.markBlockForUpdate(this.blockX, this.blockY, this.blockZ);
    }

}
