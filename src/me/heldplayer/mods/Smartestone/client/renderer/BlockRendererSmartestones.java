
package me.heldplayer.mods.Smartestone.client.renderer;

import me.heldplayer.mods.Smartestone.block.BlockMulti;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityRotatable;
import me.heldplayer.mods.Smartestone.util.Direction;
import me.heldplayer.mods.Smartestone.util.Rotation;
import me.heldplayer.mods.Smartestone.util.Side;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRendererSmartestones implements ISimpleBlockRenderingHandler {

    public final int renderId;

    public BlockRendererSmartestones(int renderId) {
        this.renderId = renderId;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        ((BlockMulti) block).setBlockBoundsForItemRender(metadata);
        renderer.renderMinX = block.getBlockBoundsMinX();
        renderer.renderMinY = block.getBlockBoundsMinY();
        renderer.renderMinZ = block.getBlockBoundsMinZ();
        renderer.renderMaxX = block.getBlockBoundsMaxX();
        renderer.renderMaxY = block.getBlockBoundsMaxY();
        renderer.renderMaxZ = block.getBlockBoundsMaxZ();

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();

        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0, 0, 0, block.getIcon(0, metadata));

        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0, 0, 0, block.getIcon(1, metadata));

        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0, 0, 0, block.getIcon(3, metadata));

        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0, 0, 0, block.getIcon(2, metadata));

        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0, 0, 0, block.getIcon(5, metadata));

        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0, 0, 0, block.getIcon(4, metadata));

        tessellator.draw();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block theBlock, int modelId, RenderBlocks renderer) {
        TileEntityRotatable tile = (TileEntityRotatable) world.getBlockTileEntity(x, y, z);

        Direction direction = Direction.NORTH;
        Rotation rotation = Rotation.DEFAULT;

        if (tile != null) {
            direction = tile.direction;
            rotation = tile.rotation;
        }

        Side[] sides = new Side[6];

        for (int i = 0; i < sides.length; i++) {
            sides[i] = direction.getRelativeSide(Side.getSide(i));
        }

        renderer.uvRotateBottom = rotation.getTextureRotation(sides[0], direction);
        renderer.uvRotateTop = rotation.getTextureRotation(sides[1], direction);
        renderer.uvRotateNorth = rotation.getTextureRotation(sides[2], direction);
        renderer.uvRotateSouth = rotation.getTextureRotation(sides[3], direction);
        renderer.uvRotateWest = rotation.getTextureRotation(sides[4], direction);
        renderer.uvRotateEast = rotation.getTextureRotation(sides[5], direction);

        boolean rendered = renderer.renderStandardBlock(theBlock, x, y, z);

        renderer.uvRotateBottom = 0;
        renderer.uvRotateTop = 0;
        renderer.uvRotateNorth = 0;
        renderer.uvRotateSouth = 0;
        renderer.uvRotateWest = 0;
        renderer.uvRotateEast = 0;

        return rendered;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.renderId;
    }

}
