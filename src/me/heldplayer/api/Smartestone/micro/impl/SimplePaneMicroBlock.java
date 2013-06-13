
package me.heldplayer.api.Smartestone.micro.impl;

import me.heldplayer.api.Smartestone.micro.MicroBlockAPI;
import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.api.Smartestone.micro.rendering.MicroBlockRenderHelper;
import me.heldplayer.mods.Smartestone.util.Direction;
import me.heldplayer.mods.Smartestone.util.Side;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.SideOnly;

public class SimplePaneMicroBlock extends MicroBlockImpl {

    public final double width;

    public SimplePaneMicroBlock(String typeName, double width) {
        super(typeName);
        this.renderBounds = new double[] { 0.0D, 0.0D, 0.5D - width / 2.0D, 1.0D, 1.0D, 0.5D + width / 2.0D };
        this.width = width;
    }

    @Override
    public AxisAlignedBB getBoundsInBlock(MicroBlockInfo info) {
        switch (info.getData() & 0x7) {
        case 0:
            return AxisAlignedBB.getAABBPool().getAABB(0.0D, 1.0D - width, 0.0D, 1.0D, 1.0D, 1.0D);
        case 1:
            return AxisAlignedBB.getAABBPool().getAABB(0.0D, 0.0D, 0.0D, 1.0D, width, 1.0D);
        case 2:
            return AxisAlignedBB.getAABBPool().getAABB(0.0D, 0.0D, 1.0D - width, 1.0D, 1.0D, 1.0D);
        case 3:
            return AxisAlignedBB.getAABBPool().getAABB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, width);
        case 4:
            return AxisAlignedBB.getAABBPool().getAABB(1.0D - width, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        case 5:
            return AxisAlignedBB.getAABBPool().getAABB(0.0D, 0.0D, 0.0D, width, 1.0D, 1.0D);
        }

        return AxisAlignedBB.getAABBPool().getAABB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    }

    @Override
    public boolean isSideSolid(MicroBlockInfo info, int side) {
        switch (info.getData() & 0x7) {
        case 0:
            return side == 1;
        case 1:
            return side == 0;
        case 2:
            return side == 3;
        case 3:
            return side == 2;
        case 4:
            return side == 5;
        case 5:
            return side == 4;
        }

        return false;
    }

    @Override
    @SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
    public void drawHitbox(DrawBlockHighlightEvent event, MicroBlockInfo info) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.8F);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);

        Tessellator tessellator = Tessellator.instance;

        MovingObjectPosition pos = event.target;

        MicroBlockInfo usedInfo = new MicroBlockInfo(info.getMaterial(), info.getType(), 0);
        usedInfo.setData(this.onItemUse(event.player, pos.sideHit, (float) pos.hitVec.xCoord - (float) pos.blockX, (float) pos.hitVec.yCoord - (float) pos.blockY, (float) pos.hitVec.zCoord - (float) pos.blockZ));

        float outset = 0.003F;
        double x = event.player.lastTickPosX + (event.player.posX - event.player.lastTickPosX) * (double) event.partialTicks;
        double y = event.player.lastTickPosY + (event.player.posY - event.player.lastTickPosY) * (double) event.partialTicks;
        double z = event.player.lastTickPosZ + (event.player.posZ - event.player.lastTickPosZ) * (double) event.partialTicks;

        Block block = Block.blocksList[event.player.worldObj.getBlockId(pos.blockX, pos.blockY, pos.blockZ)];
        AxisAlignedBB bAabb = block.getSelectedBoundingBoxFromPool(event.player.worldObj, pos.blockX, pos.blockY, pos.blockZ).expand((double) outset, (double) outset, (double) outset).getOffsetBoundingBox(-x, -y, -z);
        AxisAlignedBB aabb = AxisAlignedBB.getAABBPool().getAABB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D).offset(event.target.blockX, event.target.blockY, event.target.blockZ).expand((double) outset, (double) outset, (double) outset).getOffsetBoundingBox(-x, -y, -z);

        double minX = aabb.minX;
        double minX2 = minX + 0.1875F;
        double minY = aabb.minY;
        double minY2 = minY + 0.1875F;
        double minZ = aabb.minZ;
        double minZ2 = minZ + 0.1875F;
        double maxX = aabb.maxX;
        double maxX2 = maxX - 0.1875F;
        double maxY = aabb.maxY;
        double maxY2 = maxY - 0.1875F;
        double maxZ = aabb.maxZ;
        double maxZ2 = maxZ - 0.1875F;
        boolean swapped = false;

        switch (pos.sideHit) {
        case 0:
            maxY = minY = bAabb.minY;
            maxY2 = minY2 = bAabb.minY;
        break;
        case 1:
            minY = maxY = bAabb.maxY;
            minY2 = maxY2 = bAabb.maxY;
        break;
        case 2:
            maxZ = minZ = bAabb.minZ;
            maxZ2 = minZ2 = bAabb.minZ;
        break;
        case 3:
            minZ = maxZ = bAabb.maxZ;
            minZ2 = maxZ2 = bAabb.maxZ;
        break;
        case 4:
            maxX = minX = bAabb.minX;
            maxX2 = minX2 = bAabb.minX;
            swapped = true;
        break;
        case 5:
            minX = maxX = bAabb.maxX;
            minX2 = maxX2 = bAabb.maxX;
            swapped = true;
        break;
        }

        tessellator.startDrawing(1);

        tessellator.addVertex(minX, minY, minZ);
        tessellator.addVertex(minX2, minY2, minZ2);

        tessellator.addVertex(maxX2, maxY2, maxZ2);
        tessellator.addVertex(maxX, maxY, maxZ);

        if (swapped) {
            tessellator.addVertex(minX, minY, maxZ);
            tessellator.addVertex(minX2, minY2, maxZ2);

            tessellator.addVertex(maxX2, maxY2, minZ2);
            tessellator.addVertex(maxX, maxY, minZ);
        }
        else {
            tessellator.addVertex(minX, maxY, maxZ);
            tessellator.addVertex(minX2, maxY2, maxZ2);

            tessellator.addVertex(maxX2, minY2, minZ2);
            tessellator.addVertex(maxX, minY, minZ);
        }

        tessellator.draw();

        tessellator.startDrawing(3);
        tessellator.addVertex(minX2, minY2, minZ2);
        if (swapped) {
            tessellator.addVertex(maxX2, maxY2, minZ2);
            tessellator.addVertex(maxX2, maxY2, maxZ2);
            tessellator.addVertex(minX2, minY2, maxZ2);
        }
        else {
            tessellator.addVertex(maxX2, minY2, minZ2);
            tessellator.addVertex(maxX2, maxY2, maxZ2);
            tessellator.addVertex(minX2, maxY2, maxZ2);
        }
        tessellator.addVertex(minX2, minY2, minZ2);

        tessellator.draw();

        tessellator.startDrawing(3);
        tessellator.addVertex(minX, minY, minZ);
        if (swapped) {
            tessellator.addVertex(maxX, maxY, minZ);
            tessellator.addVertex(maxX, maxY, maxZ);
            tessellator.addVertex(minX, minY, maxZ);
        }
        else {
            tessellator.addVertex(maxX, minY, minZ);
            tessellator.addVertex(maxX, maxY, maxZ);
            tessellator.addVertex(minX, maxY, maxZ);
        }
        tessellator.addVertex(minX, minY, minZ);

        tessellator.draw();

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        MicroBlockRenderHelper.renderMicroBlock(usedInfo, event);

        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public int onItemUse(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        float u = MicroBlockAPI.getU(side, hitX, hitY, hitZ);
        float v = MicroBlockAPI.getV(side, hitX, hitY, hitZ);

        if (u > 0.1875F && u < 0.8125F && v > 0.1875F && v < 0.8125F) {
            return side | (side << 3);
        }
        else {
            if (u < v) {
                if (u < 1.0F - v) {
                    return Direction.getDirection(side).getRelativeSide(Side.LEFT).ordinal();
                }
                else {
                    return Direction.getDirection(side).getRelativeSide(Side.BOTTOM).ordinal();
                }
            }
            else {
                if (1.0F - u < v) {
                    return Direction.getDirection(side).getRelativeSide(Side.RIGHT).ordinal();
                }
                else {
                    return Direction.getDirection(side).getRelativeSide(Side.TOP).ordinal();
                }
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
        long temp;
        temp = Double.doubleToLongBits(width);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimplePaneMicroBlock other = (SimplePaneMicroBlock) obj;
        if (typeName == null) {
            if (other.typeName != null)
                return false;
        }
        else if (!typeName.equals(other.typeName))
            return false;
        if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width))
            return false;
        return true;
    }

}
