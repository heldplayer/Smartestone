
package me.heldplayer.mods.Smartestone.util;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInstance;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public final class Util {

    public static int getScaled(int scale, int amount, int total) {
        if (amount > total) {
            amount = total;
        }

        return amount * scale / total;
    }

    @Deprecated
    public static void resendTileData(TileEntity tile) {
        if (!(tile.worldObj instanceof WorldServer)) {
            return;
        }

        WorldServer world = (WorldServer) tile.worldObj;

        world.getPlayerManager().flagChunkForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public static void sendPacketToPlayersWatching(Packet packet, int dimensionId, int chunkX, int chunkZ) {
        MinecraftServer server = MinecraftServer.getServer();

        if (server != null) {
            for (WorldServer world : server.worldServers) {
                PlayerManager manager = world.getPlayerManager();
                PlayerInstance instance = manager.getOrCreateChunkWatcher(chunkX, chunkZ, false);

                if (instance != null) {
                    instance.sendToAllPlayersWatchingChunk(packet);
                }
            }
        }

        // if (server != null) {
        // List<EntityPlayerMP> players = server.getConfigurationManager().playerEntityList;
        //
        // for (EntityPlayerMP player : players) {
        //
        // if (player.dimension == dimensionId) {
        // List<ChunkCoordIntPair> chunks = player.loadedChunks;
        //
        // for (ChunkCoordIntPair chunk : chunks) {
        // if (chunk.chunkXPos == chunkX && chunk.chunkZPos == chunkZ) {
        // player.playerNetServerHandler.sendPacketToPlayer(packet);
        // break;
        // }
        // }
        // }
        // }
        // }
    }

    @SuppressWarnings("unchecked")
    public static void sendPacketToPlayersInDim(Packet packet, int dimensionId) {
        MinecraftServer server = MinecraftServer.getServer();

        if (server != null) {
            List<EntityPlayerMP> players = server.getConfigurationManager().playerEntityList;

            for (EntityPlayerMP player : players) {
                if (player.dimension == dimensionId) {
                    player.playerNetServerHandler.sendPacketToPlayer(packet);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void sendPacketToAllPlayers(Packet packet) {
        MinecraftServer server = MinecraftServer.getServer();

        if (server != null) {
            List<EntityPlayerMP> players = server.getConfigurationManager().playerEntityList;

            for (EntityPlayerMP player : players) {
                player.playerNetServerHandler.sendPacketToPlayer(packet);
            }
        }
    }

}
