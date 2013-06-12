
package me.heldplayer.mods.Smartestone.client.renderer;

import java.util.ArrayList;
import java.util.List;

import me.heldplayer.api.Smartestone.micro.IMicroBlockMaterial;
import me.heldplayer.api.Smartestone.micro.IMicroBlockSubBlock;
import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.api.Smartestone.micro.rendering.RenderFacePool;
import me.heldplayer.api.Smartestone.micro.rendering.ReusableRenderFace;
import me.heldplayer.mods.Smartestone.client.ClientProxy;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityMicro;
import me.heldplayer.mods.Smartestone.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRendererMicroBlock implements ISimpleBlockRenderingHandler {

    public final int renderId;

    public BlockRendererMicroBlock(int renderId) {
        this.renderId = renderId;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block theBlock, int modelId, RenderBlocks renderer) {
        TileEntityMicro tile = (TileEntityMicro) world.getBlockTileEntity(x, y, z);

        if (tile == null) {
            return false;
        }

        boolean rendered = false;

        List<ReusableRenderFace> faces = new ArrayList<ReusableRenderFace>();

        List<MicroBlockInfo> infos = tile.getSubBlocks();

        Tessellator tes = Tessellator.instance;

        int brightness = theBlock.getMixedBrightnessForBlock(world, x, y, z);

        for (MicroBlockInfo info : infos) {
            IMicroBlockMaterial material = info.getMaterial();
            IMicroBlockSubBlock type = info.getType();

            Icon[] icons = new Icon[6];

            for (int i = 0; i < icons.length; i++) {
                if (material != null) {
                    icons[i] = material.getIcon(i);
                }
                else {
                    icons[i] = ClientProxy.missingTextureIcon;
                }
            }

            if (type == null || icons[0] == null) {
                continue;
            }

            if (material.getRenderPass() == Objects.blockMicro.renderPass) {
                ReusableRenderFace[] currFaces = type.getRenderFaces(info);

                if (currFaces == null) {
                    continue;
                }

                for (int i = 0; i < currFaces.length; i++) {
                    ReusableRenderFace face = currFaces[i];
                    face.icon = icons[i];
                    faces.add(face);
                }

                rendered = true;
                // renderer.renderMinX = aabb.minX;
                // renderer.renderMaxX = aabb.maxX;
                // renderer.renderMinY = aabb.minY;
                // renderer.renderMaxY = aabb.maxY;
                // renderer.renderMinZ = aabb.minZ;
                // renderer.renderMaxZ = aabb.maxZ;
                //
                // tes.setBrightness(renderer.renderMinY > 0.0D ? brightness : theBlock.getMixedBrightnessForBlock(world, x, y - 1, z));
                // tes.setColorOpaque_F(0.5F, 0.5F, 0.5F);
                // renderer.renderFaceYNeg(null, x, y, z, icons[0]);
                //
                // tes.setBrightness(renderer.renderMaxY < 1.0D ? brightness : theBlock.getMixedBrightnessForBlock(world, x, y + 1, z));
                // tes.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                // renderer.renderFaceYPos(null, x, y, z, icons[1]);
                //
                // tes.setBrightness(renderer.renderMinZ > 0.0D ? brightness : theBlock.getMixedBrightnessForBlock(world, x, y, z - 1));
                // tes.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                // renderer.renderFaceZNeg(null, x, y, z, icons[2]);
                //
                // tes.setBrightness(renderer.renderMaxZ < 1.0D ? brightness : theBlock.getMixedBrightnessForBlock(world, x, y, z + 1));
                // tes.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                // renderer.renderFaceZPos(null, x, y, z, icons[3]);
                //
                // tes.setBrightness(renderer.renderMinX > 0.0D ? brightness : theBlock.getMixedBrightnessForBlock(world, x - 1, y, z));
                // tes.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                // renderer.renderFaceXNeg(null, x, y, z, icons[4]);
                //
                // tes.setBrightness(renderer.renderMaxX < 1.0D ? brightness : theBlock.getMixedBrightnessForBlock(world, x + 1, y, z));
                // tes.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                // renderer.renderFaceXPos(null, x, y, z, icons[5]);
                //
            }
        }

        RenderFacePool.updateIndex();

        for (ReusableRenderFace face : faces) {
            switch (face.side) {
            case 0:
                renderer.renderMinX = face.startU;
                renderer.renderMaxX = face.endU;
                renderer.renderMinY = face.offset;
                renderer.renderMaxY = face.offset;
                renderer.renderMinZ = face.startV;
                renderer.renderMaxZ = face.endV;
                tes.setBrightness(renderer.renderMinY > 0.0D ? brightness : theBlock.getMixedBrightnessForBlock(world, x, y - 1, z));
                tes.setColorOpaque_F(0.5F, 0.5F, 0.5F);
                renderer.renderFaceYNeg(null, x, y, z, face.icon);
            break;
            case 1:
                renderer.renderMinX = face.startU;
                renderer.renderMaxX = face.endU;
                renderer.renderMinY = face.offset;
                renderer.renderMaxY = face.offset;
                renderer.renderMinZ = face.startV;
                renderer.renderMaxZ = face.endV;
                tes.setBrightness(renderer.renderMaxY < 1.0D ? brightness : theBlock.getMixedBrightnessForBlock(world, x, y + 1, z));
                tes.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                renderer.renderFaceYPos(null, x, y, z, face.icon);
            break;
            case 2:
                renderer.renderMinX = face.startU;
                renderer.renderMaxX = face.endU;
                renderer.renderMinY = face.startV;
                renderer.renderMaxY = face.endV;
                renderer.renderMinZ = face.offset;
                renderer.renderMaxZ = face.offset;
                tes.setBrightness(renderer.renderMinZ > 0.0D ? brightness : theBlock.getMixedBrightnessForBlock(world, x, y, z - 1));
                tes.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                renderer.renderFaceZNeg(null, x, y, z, face.icon);
            break;
            case 3:
                renderer.renderMinX = face.startU;
                renderer.renderMaxX = face.endU;
                renderer.renderMinY = face.startV;
                renderer.renderMaxY = face.endV;
                renderer.renderMinZ = face.offset;
                renderer.renderMaxZ = face.offset;
                tes.setBrightness(renderer.renderMaxZ < 1.0D ? brightness : theBlock.getMixedBrightnessForBlock(world, x, y, z + 1));
                tes.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                renderer.renderFaceZPos(null, x, y, z, face.icon);
            break;
            case 4:
                renderer.renderMinX = face.offset;
                renderer.renderMaxX = face.offset;
                renderer.renderMinY = face.startV;
                renderer.renderMaxY = face.endV;
                renderer.renderMinZ = face.startU;
                renderer.renderMaxZ = face.endU;
                tes.setBrightness(renderer.renderMinX > 0.0D ? brightness : theBlock.getMixedBrightnessForBlock(world, x - 1, y, z));
                tes.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                renderer.renderFaceXNeg(null, x, y, z, face.icon);
            break;
            case 5:
                renderer.renderMinX = face.offset;
                renderer.renderMaxX = face.offset;
                renderer.renderMinY = face.startV;
                renderer.renderMaxY = face.endV;
                renderer.renderMinZ = face.startU;
                renderer.renderMaxZ = face.endU;
                tes.setBrightness(renderer.renderMaxX < 1.0D ? brightness : theBlock.getMixedBrightnessForBlock(world, x + 1, y, z));
                tes.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                renderer.renderFaceXPos(null, x, y, z, face.icon);
            break;
            }
        }

        return rendered;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return false;
    }

    @Override
    public int getRenderId() {
        return this.renderId;
    }

}
