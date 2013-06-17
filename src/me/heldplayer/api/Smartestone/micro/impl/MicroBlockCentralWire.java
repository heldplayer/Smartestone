
package me.heldplayer.api.Smartestone.micro.impl;

import java.util.ArrayList;
import java.util.Set;

import me.heldplayer.api.Smartestone.micro.IMicroBlock;
import me.heldplayer.api.Smartestone.micro.IMicroBlockMaterial;
import me.heldplayer.api.Smartestone.micro.MicroBlockAPI;
import me.heldplayer.api.Smartestone.micro.MicroBlockInfo;
import me.heldplayer.api.Smartestone.micro.rendering.RenderFaceHelper;
import me.heldplayer.api.Smartestone.micro.rendering.ReusableRenderFace;
import me.heldplayer.mods.Smartestone.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.ForgeDirection;

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
        int data = info.getData();
        boolean bottom = ((data >> 0) & 0x1) == 1;
        boolean top = ((data >> 1) & 0x1) == 1;
        boolean north = ((data >> 2) & 0x1) == 1;
        boolean south = ((data >> 3) & 0x1) == 1;
        boolean west = ((data >> 4) & 0x1) == 1;
        boolean east = ((data >> 5) & 0x1) == 1;

        ArrayList<ReusableRenderFace> faceList = new ArrayList<ReusableRenderFace>();

        AxisAlignedBB aabb = this.getBoundsInBlock(info);

        ReusableRenderFace[] faces = new ReusableRenderFace[6];

        for (int i = 0; i < faces.length; i++) {
            faces[i] = RenderFaceHelper.getAFace();
            faces[i].setValues(aabb, i);
            faces[i].icon = info.getMaterial().getIcon(i, data);
            faces[i].renderPass = info.getMaterial().getRenderPass();
            faces[i].color = info.getMaterial().getColor(i, info.getData());
            faceList.add(faces[i]);
        }

        boolean split = false;
        if (bottom && !north && !south && !west && !east) {
            faces[2].startV = 0.0D;
            faces[3].startV = 0.0D;
            faces[4].startV = 0.0D;
            faces[5].startV = 0.0D;
            faces[0].offset = 0.0D;
        }
        else {
            split = true;
        }
        if (top && !north && !south && !west && !east) {
            faces[2].endV = 1.0D;
            faces[3].endV = 1.0D;
            faces[4].endV = 1.0D;
            faces[5].endV = 1.0D;
            faces[1].offset = 1.0D;
        }
        else {
            split = true;
        }
        if (split) {
            if (bottom) {
                for (int i = 2; i <= 5; i++) {
                    ReusableRenderFace face = RenderFaceHelper.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.25D, 0.75D, 0.0D, 0.75D);
                    face.icon = info.getMaterial().getIcon(i, data);
                    face.renderPass = info.getMaterial().getRenderPass();
                    face.color = info.getMaterial().getColor(i, info.getData());
                    faceList.add(face);
                }
                faces[0].offset = 0.0D;
            }
            if (top) {
                for (int i = 2; i <= 5; i++) {
                    ReusableRenderFace face = RenderFaceHelper.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.25D, 0.75D, 0.25D, 1.0D);
                    face.icon = info.getMaterial().getIcon(i, data);
                    face.renderPass = info.getMaterial().getRenderPass();
                    face.color = info.getMaterial().getColor(i, info.getData());
                    faceList.add(face);
                }
                faces[1].offset = 1.0D;
            }
        }

        split = false;
        if (north && !top && !bottom && !west && !east) {
            faces[0].startV = 0.0D;
            faces[1].startV = 0.0D;
            faces[4].startU = 0.0D;
            faces[5].startU = 0.0D;
            faces[2].offset = 0.0D;
        }
        else {
            split = true;
        }
        if (south && !top && !bottom && !west && !east) {
            faces[0].endV = 1.0D;
            faces[1].endV = 1.0D;
            faces[4].endU = 1.0D;
            faces[5].endU = 1.0D;
            faces[3].offset = 1.0D;
        }
        else {
            split = true;
        }
        if (split) {
            if (north) {
                for (int i = 0; i <= 1; i++) {
                    ReusableRenderFace face = RenderFaceHelper.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.25D, 0.75D, 0.0D, 0.25D);
                    face.icon = info.getMaterial().getIcon(i, data);
                    face.renderPass = info.getMaterial().getRenderPass();
                    face.color = info.getMaterial().getColor(i, info.getData());
                    faceList.add(face);
                }
                for (int i = 4; i <= 5; i++) {
                    ReusableRenderFace face = RenderFaceHelper.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.0D, 0.25D, 0.25D, 0.75D);
                    face.icon = info.getMaterial().getIcon(i, data);
                    face.renderPass = info.getMaterial().getRenderPass();
                    face.color = info.getMaterial().getColor(i, info.getData());
                    faceList.add(face);
                }
                faces[2].offset = 0.0D;
            }
            if (south) {
                for (int i = 0; i <= 1; i++) {
                    ReusableRenderFace face = RenderFaceHelper.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.25D, 0.75D, 0.75D, 1.0D);
                    face.icon = info.getMaterial().getIcon(i, data);
                    face.renderPass = info.getMaterial().getRenderPass();
                    face.color = info.getMaterial().getColor(i, info.getData());
                    faceList.add(face);
                }
                for (int i = 4; i <= 5; i++) {
                    ReusableRenderFace face = RenderFaceHelper.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.75D, 1.0D, 0.25D, 0.75D);
                    face.icon = info.getMaterial().getIcon(i, data);
                    face.renderPass = info.getMaterial().getRenderPass();
                    face.color = info.getMaterial().getColor(i, info.getData());
                    faceList.add(face);
                }
                faces[3].offset = 1.0D;
            }
        }

        split = false;
        if (west && !top && !bottom && !north && !south) {
            faces[0].startU = 0.0D;
            faces[1].startU = 0.0D;
            faces[2].startU = 0.0D;
            faces[3].startU = 0.0D;
            faces[4].offset = 0.0D;
        }
        else {
            split = true;
        }
        if (east && !top && !bottom && !north && !south) {
            faces[0].endU = 1.0D;
            faces[1].endU = 1.0D;
            faces[2].endU = 1.0D;
            faces[3].endU = 1.0D;
            faces[5].offset = 1.0D;
        }
        else {
            split = true;
        }
        if (split) {
            if (west) {
                for (int i = 0; i <= 3; i++) {
                    ReusableRenderFace face = RenderFaceHelper.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.0D, 0.25D, 0.25D, 0.75D);
                    face.icon = info.getMaterial().getIcon(i, data);
                    face.renderPass = info.getMaterial().getRenderPass();
                    face.color = info.getMaterial().getColor(i, info.getData());
                    faceList.add(face);
                }
                faces[4].offset = 0.0D;
            }
            if (east) {
                for (int i = 0; i <= 3; i++) {
                    ReusableRenderFace face = RenderFaceHelper.getAFace();
                    face.setValues(i, (i % 2) == 0 ? 0.25D : 0.75D, 0.75D, 1.0D, 0.25D, 0.75D);
                    face.icon = info.getMaterial().getIcon(i, data);
                    face.renderPass = info.getMaterial().getRenderPass();
                    face.color = info.getMaterial().getColor(i, info.getData());
                    faceList.add(face);
                }
                faces[5].offset = 1.0D;
            }
        }
        split = false;

        return faceList.toArray(new ReusableRenderFace[0]);
    }

    @Override
    public void onBlockUpdate(MicroBlockInfo info, World world, int x, int y, int z) {
        int origData = info.getData();
        int data = 0;

        int strength = 0;
        Objects.disableRedstoneFlag = true;
        int indirectStrength = world.getStrongestIndirectPower(x, y, z) << 2;
        Objects.disableRedstoneFlag = false;

        if (indirectStrength > 0 && indirectStrength > strength - 1) {
            strength = indirectStrength;
        }

        int highestNeighbour = 0;

        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];

            if (canConnectTo(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, info)) {
                data |= (1 << i);

                highestNeighbour = this.getMaxCurrentStrength(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, highestNeighbour);
            }
        }

        if (highestNeighbour > strength) {
            strength = highestNeighbour - 1;
        }
        else if (strength > 0) {
            strength--;
        }
        else {
            strength = 0;
        }

        if (indirectStrength > strength - 1) {
            strength = indirectStrength;
        }

        data |= (strength << 6);

        if (data != origData) {
            info.setData(data);
            ((IMicroBlock) world.getBlockTileEntity(x, y, z)).modifyInfo(info);

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                world.notifyBlockOfNeighborChange(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, MicroBlockAPI.microBlockId);
            }
        }
    }

    public int getMaxCurrentStrength(World world, int x, int y, int z, int strength) {
        if (world.getBlockId(x, y, z) == MicroBlockAPI.microBlockId) {
            IMicroBlock microBlock = (IMicroBlock) world.getBlockTileEntity(x, y, z);

            if (microBlock == null) {
                return strength;
            }

            Set<MicroBlockInfo> infos = microBlock.getSubBlocks();

            for (MicroBlockInfo currInfo : infos) {
                if (currInfo.getType().equals(this)) {
                    int currStrength = currInfo.getData() >> 6;
                    return currStrength > strength ? currStrength : strength;
                }
            }
        }
        return strength;
    }

    public static boolean canConnectTo(World world, int x, int y, int z, MicroBlockInfo info) {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];

        if (block == null) {
            return false;
        }

        if (block.blockID == MicroBlockAPI.microBlockId) {
            IMicroBlock tile = (IMicroBlock) world.getBlockTileEntity(x, y, z);

            if (tile == null) {
                return false;
            }

            Set<MicroBlockInfo> infos = tile.getSubBlocks();

            for (MicroBlockInfo current : infos) {
                if (current.getType().equals(info.getType()) && current.getMaterial().equals(info.getMaterial())) {
                    return true;
                }
            }
        }
        else if (block.canProvidePower()) {
            return true;
        }
        else if (block.blockID == Block.pistonBase.blockID || block.blockID == Block.pistonStickyBase.blockID || block.blockID == Block.pistonMoving.blockID) {
            return true;
        }
        else if (block.blockID == Block.redstoneLampActive.blockID || block.blockID == Block.redstoneLampIdle.blockID) {
            return true;
        }
        else if (block.blockID == Block.music.blockID) {
            return true;
        }

        return false;
    }

    @Override
    public void drawHitbox(DrawBlockHighlightEvent event, MicroBlockInfo info) {}

    @Override
    public int onItemUse(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return 0;
    }

    @Override
    public int getPowerOutput(MicroBlockInfo info, int side) {
        return info.getData() >> 6;
    }

    @Override
    public boolean canBeAdded(IMicroBlock tile, MicroBlockInfo info) {
        Set<MicroBlockInfo> infos = tile.getSubBlocks();

        for (MicroBlockInfo current : infos) {
            if (current.getType().equals(this)) {
                return false;
            }
        }

        return true;
    }

}
