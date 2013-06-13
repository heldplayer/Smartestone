
package me.heldplayer.api.Smartestone.micro.impl;

import java.util.ArrayList;

import me.heldplayer.api.Smartestone.micro.IMicroBlockMaterial;
import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.api.Smartestone.micro.rendering.RenderFacePool;
import me.heldplayer.api.Smartestone.micro.rendering.ReusableRenderFace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

public class MicroBlockCentralWire extends MicroBlockImpl {

    public MicroBlockCentralWire(String typeName) {
        super(typeName);
        this.renderBounds = new double[] { 0.25D, 0.25D, 0.25D, 0.75D, 0.75D, 0.75D };
    }

    @Override
    public AxisAlignedBB getBoundsInBlock(MicroBlockInfo info) {
        return AxisAlignedBB.getAABBPool().getAABB(0.25D, 0.25D, 0.25D, 0.75D, 0.75D, 0.75D);
    }

    @Override
    public boolean isMaterialApplicable(IMicroBlockMaterial material) {
        return material.getClass() == WireMaterial.class;
    }

    @Override
    public ReusableRenderFace[] getRenderFaces(MicroBlockInfo info) {
        // The info actually stores the connectivity
        // bit  stores
        // 0    Redstone state
        // 1    Top
        // 2    Bottom
        // 3    North
        // 4    South
        // 5    West
        // 6    East
        int data = info.getData();
        boolean top = ((data >> 1) & 0x1) == 1;
        boolean bottom = ((data >> 2) & 0x1) == 1;
        boolean north = ((data >> 3) & 0x1) == 1;
        boolean south = ((data >> 4) & 0x1) == 1;
        boolean west = ((data >> 5) & 0x1) == 1;
        boolean east = ((data >> 6) & 0x1) == 1;

        ArrayList<ReusableRenderFace> faceList = new ArrayList<ReusableRenderFace>();
        ReusableRenderFace[] faces = super.getRenderFaces(info);
        for (ReusableRenderFace face : faces) {
            faceList.add(face);
        }

        boolean split = false;
        if (top && !north && !south && !west && !east) {
            faces[2].endV = 1.0D;
            faces[3].endV = 1.0D;
            faces[4].endV = 1.0D;
            faces[5].endV = 1.0D;
        }
        else {
            split = true;
        }
        if (bottom && !north && !south && !west && !east) {
            faces[2].startV = 0.0D;
            faces[3].startV = 0.0D;
            faces[4].startV = 0.0D;
            faces[5].startV = 0.0D;
        }
        else {
            split = true;
        }
        if (split) {
            if (top) {
                for (int i = 2; i <= 5; i++) {
                    ReusableRenderFace face = RenderFacePool.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.25D, 0.75D, 0.25D, 1.0D);
                    faceList.add(face);
                }
            }
            if (bottom) {
                for (int i = 2; i <= 5; i++) {
                    ReusableRenderFace face = RenderFacePool.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.25D, 0.75D, 0.0D, 0.75D);
                    faceList.add(face);
                }
            }
        }

        split = false;
        if (north && !top && !bottom && !west && !east) {
            faces[0].startV = 0.0D;
            faces[1].startV = 0.0D;
            faces[4].startU = 0.0D;
            faces[5].startU = 0.0D;
        }
        else {
            split = true;
        }
        if (south && !top && !bottom && !west && !east) {
            faces[0].endV = 1.0D;
            faces[1].endV = 1.0D;
            faces[4].endU = 1.0D;
            faces[5].endU = 1.0D;
        }
        else {
            split = true;
        }
        if (split) {
            if (north) {
                for (int i = 0; i <= 1; i++) {
                    ReusableRenderFace face = RenderFacePool.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.25D, 0.75D, 0.0D, 0.25D);
                    faceList.add(face);
                }
                for (int i = 4; i <= 5; i++) {
                    ReusableRenderFace face = RenderFacePool.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.0D, 0.25D, 0.25D, 0.75D);
                    faceList.add(face);
                }
            }
            if (south) {
                for (int i = 0; i <= 1; i++) {
                    ReusableRenderFace face = RenderFacePool.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.25D, 0.75D, 0.75D, 1.0D);
                    faceList.add(face);
                }
                for (int i = 4; i <= 5; i++) {
                    ReusableRenderFace face = RenderFacePool.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.75D, 1.0D, 0.25D, 0.75D);
                    faceList.add(face);
                }
            }
        }

        split = false;
        if (west && !top && !bottom && !north && !south) {
            faces[0].startU = 0.0D;
            faces[1].startU = 0.0D;
            faces[2].startU = 0.0D;
            faces[3].startU = 0.0D;
        }
        else {
            split = true;
        }
        if (east && !top && !bottom && !north && !south) {
            faces[0].endU = 1.0D;
            faces[1].endU = 1.0D;
            faces[2].endU = 1.0D;
            faces[3].endU = 1.0D;
        }
        else {
            split = true;
        }
        if (split) {
            if (west) {
                for (int i = 0; i <= 3; i++) {
                    ReusableRenderFace face = RenderFacePool.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.0D, 0.25D, 0.25D, 0.75D);
                    faceList.add(face);
                }
            }
            if (east) {
                for (int i = 0; i <= 3; i++) {
                    ReusableRenderFace face = RenderFacePool.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.75D, 1.0D, 0.25D, 0.75D);
                    faceList.add(face);
                }
            }
        }
        split = false;

        return faceList.toArray(new ReusableRenderFace[0]);
    }

    @Override
    public void drawHitbox(DrawBlockHighlightEvent event, MicroBlockInfo info) {}

    @Override
    public int onItemUse(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return 0;
    }

}
