
package me.heldplayer.mods.Smartestone.client.renderer.tileentity;

import me.heldplayer.mods.Smartestone.tileentity.TileEntityInductionishFurnace;
import me.heldplayer.mods.Smartestone.util.Const;
import me.heldplayer.mods.Smartestone.util.Direction;
import me.heldplayer.mods.Smartestone.util.Rotation;
import me.heldplayer.mods.Smartestone.util.Side;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
public class TileEntityInductionishFurnaceRenderer extends TileEntitySpecialRenderer {

    private final EntityItem entityitem = new EntityItem(null);

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTicks) {
        if (tileentity == null || !(tileentity instanceof TileEntityInductionishFurnace)) {
            return;
        }

        TileEntityInductionishFurnace tile = (TileEntityInductionishFurnace) tileentity;
        Direction direction = tile.direction.getValue();
        Rotation rotation = tile.rotation.getValue();
        Side front = direction.getRelativeSide(Side.FRONT, rotation);
        Side top = direction.getRelativeSide(Side.TOP, rotation);

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y, (float) z);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);

        switch (direction.ordinal()) {
        case 0:
            GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
        break;
        case 1:
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        break;
        case 3:
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        break;
        case 4:
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        break;
        case 5:
            GL11.glRotatef(90.0F, 0.0F, -1.0F, 0.0F);
        break;
        }
        switch (rotation.ordinal()) {
        case 1:
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        break;
        case 2:
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        break;
        case 3:
            GL11.glRotatef(90.0F, 0.0F, 0.0F, -1.0F);
        break;
        }

        GL11.glTranslatef(0.0F, 0.6F, 0.0F);

        int brightness = tile.worldObj.getLightBrightnessForSkyBlocks(tile.xCoord + top.dir.offsetX, tile.yCoord + top.dir.offsetY, tile.zCoord + top.dir.offsetZ, 0);
        int u = brightness % 65536;
        int v = brightness / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) u / 1.0F, (float) v / 1.0F);

        this.entityitem.worldObj = tile.worldObj;
        this.entityitem.hoverStart = tile.prevHover + (tile.hover - tile.prevHover) * partialTicks;

        ItemStack stack = tile.getStackInSlot(Const.INDUCTIONISHFURNACE_OUTPUT_SLOT);
        if (stack != null) {
            this.entityitem.setEntityItemStack(stack);
            //this.entityitem.setEntityItemStack(stack.copy());
            //this.entityitem.getEntityItem().stackSize = 1;

            GL11.glPushMatrix();
            switch (rotation.ordinal()) {
            case 1:
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
            break;
            case 2:
                GL11.glRotatef(-180.0F, 0.0F, 0.0F, 1.0F);
            break;
            case 3:
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, -1.0F);
            break;
            }
            switch (direction.ordinal()) {
            case 0:
                GL11.glRotatef(-90.0F, -1.0F, 0.0F, 0.0F);
            break;
            case 1:
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            break;
            case 3:
                GL11.glRotatef(-180.0F, 0.0F, 1.0F, 0.0F);
            break;
            case 4:
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            break;
            case 5:
                GL11.glRotatef(-90.0F, 0.0F, -1.0F, 0.0F);
            break;
            }
            GL11.glScalef(0.5F, 0.5F, 0.5F);

            RenderManager.instance.renderEntityWithPosYaw(this.entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            GL11.glPopMatrix();
        }

        brightness = tile.worldObj.getLightBrightnessForSkyBlocks(tile.xCoord + front.dir.offsetX, tile.yCoord + front.dir.offsetY, tile.zCoord + front.dir.offsetZ, 0);
        u = brightness % 65536;
        v = brightness / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) u / 1.0F, (float) v / 1.0F);

        this.entityitem.hoverStart += 2.3F;
        stack = tile.getStackInSlot(Const.INDUCTIONISHFURNACE_INPUT_SLOT);
        if (stack != null) {
            this.entityitem.setEntityItemStack(stack);
            //this.entityitem.setEntityItemStack(stack.copy());
            //this.entityitem.getEntityItem().stackSize = 1;

            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, -0.5F, -0.7F);
            switch (rotation.ordinal()) {
            case 1:
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
            break;
            case 2:
                GL11.glRotatef(-180.0F, 0.0F, 0.0F, 1.0F);
            break;
            case 3:
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, -1.0F);
            break;
            }
            switch (direction.ordinal()) {
            case 0:
                GL11.glRotatef(-90.0F, -1.0F, 0.0F, 0.0F);
            break;
            case 1:
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            break;
            case 3:
                GL11.glRotatef(-180.0F, 0.0F, 1.0F, 0.0F);
            break;
            case 4:
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            break;
            case 5:
                GL11.glRotatef(-90.0F, 0.0F, -1.0F, 0.0F);
            break;
            }
            GL11.glScalef(0.5F, 0.5F, 0.5F);

            RenderManager.instance.renderEntityWithPosYaw(this.entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();
    }
}
