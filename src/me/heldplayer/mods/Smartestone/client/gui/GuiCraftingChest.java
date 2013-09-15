
package me.heldplayer.mods.Smartestone.client.gui;

import me.heldplayer.mods.Smartestone.Assets;
import me.heldplayer.mods.Smartestone.inventory.craftingchest.ContainerCraftingChest;
import me.heldplayer.mods.Smartestone.tileentity.TileEntityCraftingChest;
import me.heldplayer.mods.Smartestone.util.Const;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCraftingChest extends GuiBase {

    private TileEntityCraftingChest tile;
    private GuiButtonTemplate[] buttons;

    public GuiCraftingChest(InventoryPlayer playerInventory, World world, int x, int y, int z, TileEntityCraftingChest tileEntity) {
        super(new ContainerCraftingChest(playerInventory, world, x, y, z, tileEntity), playerInventory);

        this.tile = tileEntity;

        this.ySize = 215;
        this.xSize = 212;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        if (this.buttons == null) {
            this.buttons = new GuiButtonTemplate[6];
        }

        boolean validRecipe = this.tile.getStackInSlot(Const.CRAFTINGCHEST_CRAFTRESULT_SLOT) != null;

        for (int i = 0; i < this.buttons.length; i++) {
            int x = 7 + (i & 0x1) * 45 + this.guiLeft;
            int y = 16 + ((i >> 1) & 0xF) * 18 + this.guiTop;
            GuiButtonTemplate button = this.buttons[i] = new GuiButtonTemplate(i, x, y, (i & 0x1) == 1);
            ItemStack stack = tile.getStackInSlot(i + Const.CRAFTINGCHEST_CRAFTRESULT_SLOT + 1);
            if (stack == null || stack.getItem() == null) {
                button.enabled = false;
            }
            else {
                button.enabled = validRecipe;
                NBTTagCompound compound = stack.getTagCompound();

                button.isWritten = false;

                if (compound != null && compound.hasKey("Template")) {
                    NBTTagCompound template = compound.getCompoundTag("Template");
                    if (template.hasKey("Ingredients")) {
                        button.isWritten = true;
                        button.enabled = true;
                    }
                }
            }
            this.buttonList.add(button);
        }

        this.buttonList.add(new GuiButtonTiny(100, this.guiLeft + 127, this.guiTop + 16, 0));
        this.buttonList.add(new GuiButtonTiny(101, this.guiLeft + 196, this.guiTop + 71, 18));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        boolean validRecipe = this.tile.getStackInSlot(Const.CRAFTINGCHEST_CRAFTRESULT_SLOT) != null;

        for (int i = 0; i < this.buttons.length; i++) {
            GuiButtonTemplate button = this.buttons[i];
            ItemStack stack = tile.getStackInSlot(i + Const.CRAFTINGCHEST_CRAFTRESULT_SLOT + 1);
            if (stack == null || stack.getItem() == null) {
                button.enabled = false;
            }
            else {
                button.enabled = validRecipe;
                NBTTagCompound compound = stack.getTagCompound();

                button.isWritten = false;

                if (compound != null && compound.hasKey("Template")) {
                    NBTTagCompound template = compound.getCompoundTag("Template");
                    if (template.hasKey("Ingredients")) {
                        button.isWritten = true;
                        button.enabled = true;
                    }
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            int id = button.id;

            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, id);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.renderEngine.bindTexture(Assets.BACKGROUND_CRAFTING_CHEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        this.fontRenderer.drawString(this.tile.isInvNameLocalized() ? "\u00A7o" + this.tile.getInvName() : StatCollector.translateToLocal(this.tile.getInvName()), 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInv.isInvNameLocalized() ? "\u00A7o" + this.playerInv.getInvName() : StatCollector.translateToLocal(this.playerInv.getInvName()), 28, this.ySize - 96 + 2, 4210752);
    }

}
