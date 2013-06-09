
package me.heldplayer.mods.Smartestone.util;

import java.util.List;

import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityMicro;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.SideOnly;

public class RayTrace {

    public static AxisAlignedBB aabb = null;
    public static MicroBlockInfo info = null;

    public static void rayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        MicroBlockInfo result = null;
        Vec3 resultVec = null;

        if (tile == null) {
            aabb = null;
            info = null;
            return;
        }

        List<MicroBlockInfo> infos = tile.getSubBlocks();

        if (infos.size() == 0) {
            aabb = null;
            info = null;
            return;
        }

        start = start.addVector((double) (-x), (double) (-y), (double) (-z));
        end = end.addVector((double) (-x), (double) (-y), (double) (-z));

        for (MicroBlockInfo info : infos) {
            AxisAlignedBB aabb = info.getType().getBoundsInBlock(info);

            Vec3 minX = start.getIntermediateWithXValue(end, aabb.minX);
            Vec3 maxX = start.getIntermediateWithXValue(end, aabb.maxX);
            Vec3 minY = start.getIntermediateWithYValue(end, aabb.minY);
            Vec3 maxY = start.getIntermediateWithYValue(end, aabb.maxY);
            Vec3 minZ = start.getIntermediateWithZValue(end, aabb.minZ);
            Vec3 maxZ = start.getIntermediateWithZValue(end, aabb.maxZ);

            if (!isVecInsideYZBounds(minX, aabb.minY, aabb.maxY, aabb.minZ, aabb.maxZ)) {
                minX = null;
            }

            if (!isVecInsideYZBounds(maxX, aabb.minY, aabb.maxY, aabb.minZ, aabb.maxZ)) {
                maxX = null;
            }

            if (!isVecInsideXZBounds(minY, aabb.minX, aabb.maxX, aabb.minZ, aabb.maxZ)) {
                minY = null;
            }

            if (!isVecInsideXZBounds(maxY, aabb.minX, aabb.maxX, aabb.minZ, aabb.maxZ)) {
                maxY = null;
            }

            if (!isVecInsideXYBounds(minZ, aabb.minX, aabb.maxX, aabb.minY, aabb.maxY)) {
                minZ = null;
            }

            if (!isVecInsideXYBounds(maxZ, aabb.minX, aabb.maxX, aabb.minY, aabb.maxY)) {
                maxZ = null;
            }

            Vec3 currentVec = null;

            if (minX != null && (currentVec == null || start.squareDistanceTo(minX) < start.squareDistanceTo(currentVec))) {
                currentVec = minX;
            }

            if (maxX != null && (currentVec == null || start.squareDistanceTo(maxX) < start.squareDistanceTo(currentVec))) {
                currentVec = maxX;
            }

            if (minY != null && (currentVec == null || start.squareDistanceTo(minY) < start.squareDistanceTo(currentVec))) {
                currentVec = minY;
            }

            if (maxY != null && (currentVec == null || start.squareDistanceTo(maxY) < start.squareDistanceTo(currentVec))) {
                currentVec = maxY;
            }

            if (minZ != null && (currentVec == null || start.squareDistanceTo(minZ) < start.squareDistanceTo(currentVec))) {
                currentVec = minZ;
            }

            if (maxZ != null && (currentVec == null || start.squareDistanceTo(maxZ) < start.squareDistanceTo(currentVec))) {
                currentVec = maxZ;
            }

            if (currentVec != null) {
                if (resultVec == null) {
                    result = info;
                    resultVec = currentVec;
                }
                else if (start.squareDistanceTo(currentVec) < start.squareDistanceTo(resultVec)) {
                    result = info;
                    resultVec = currentVec;
                }
            }
        }

        if (result == null) {
            aabb = null;
            info = null;
            return;
        }

        aabb = result.getType().getBoundsInBlock(result);
        info = result;
    }

    public static boolean isVecInsideYZBounds(Vec3 vec, double minY, double maxY, double minZ, double maxZ) {
        return vec == null ? false : vec.yCoord >= minY && vec.yCoord <= maxY && vec.zCoord >= minZ && vec.zCoord <= maxZ;
    }

    public static boolean isVecInsideXZBounds(Vec3 vec, double minX, double maxX, double minZ, double maxZ) {
        return vec == null ? false : vec.xCoord >= minX && vec.xCoord <= maxX && vec.zCoord >= minZ && vec.zCoord <= maxZ;
    }

    public static boolean isVecInsideXYBounds(Vec3 vec, double minX, double maxX, double minY, double maxY) {
        return vec == null ? false : vec.xCoord >= minX && vec.xCoord <= maxX && vec.yCoord >= minY && vec.yCoord <= maxY;
    }

    public static void rayTrace(World world, EntityPlayer player, int x, int y, int z) {
        Vec3 head = Vec3.createVectorHelper(player.posX, player.posY + 1.62D - player.yOffset, player.posZ);
        Vec3 pointing = player.getLook(1.0F);

        double reach = world.isRemote ? getBlockReach() : getBlockReach(player);

        Vec3 result = head.addVector(pointing.xCoord * reach, pointing.yCoord * reach, pointing.zCoord * reach);

        rayTrace(world, x, y, z, head, result);
    }

    @SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
    public static double getBlockReach() {
        return Minecraft.getMinecraft().playerController.getBlockReachDistance();
    }

    public static double getBlockReach(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            return ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        }

        return 0.0D;
    }

}
