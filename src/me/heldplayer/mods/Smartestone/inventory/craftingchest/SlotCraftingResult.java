
package me.heldplayer.mods.Smartestone.inventory.craftingchest;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class SlotCraftingResult extends SlotCrafting {

    private final InventoryCraftingMatrix craftMatrix;
    private EntityPlayer thePlayer;
    private int amountCrafted;

    public SlotCraftingResult(EntityPlayer player, InventoryCraftingMatrix craftMatrix, IInventory inventory, int id, int x, int y) {
        super(player, craftMatrix, inventory, id, x, y);
        this.thePlayer = player;
        this.craftMatrix = craftMatrix;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(amount, this.getStack().stackSize);
        }

        return super.decrStackSize(amount);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
        this.amountCrafted = 0;

        if (stack.itemID == Block.workbench.blockID) {
            this.thePlayer.addStat(AchievementList.buildWorkBench, 1);
        }
        else if (stack.itemID == Item.pickaxeWood.itemID) {
            this.thePlayer.addStat(AchievementList.buildPickaxe, 1);
        }
        else if (stack.itemID == Block.furnaceIdle.blockID) {
            this.thePlayer.addStat(AchievementList.buildFurnace, 1);
        }
        else if (stack.itemID == Item.hoeWood.itemID) {
            this.thePlayer.addStat(AchievementList.buildHoe, 1);
        }
        else if (stack.itemID == Item.bread.itemID) {
            this.thePlayer.addStat(AchievementList.makeBread, 1);
        }
        else if (stack.itemID == Item.cake.itemID) {
            this.thePlayer.addStat(AchievementList.bakeCake, 1);
        }
        else if (stack.itemID == Item.pickaxeStone.itemID) {
            this.thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
        }
        else if (stack.itemID == Item.swordWood.itemID) {
            this.thePlayer.addStat(AchievementList.buildSword, 1);
        }
        else if (stack.itemID == Block.enchantmentTable.blockID) {
            this.thePlayer.addStat(AchievementList.enchantments, 1);
        }
        else if (stack.itemID == Block.bookShelf.blockID) {
            this.thePlayer.addStat(AchievementList.bookcase, 1);
        }
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.amountCrafted += amount;
        this.onCrafting(stack);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        GameRegistry.onItemCrafted(player, stack, this.craftMatrix);
        this.onCrafting(stack);

        boolean canCraft = this.craftMatrix.canCraft();

        for (int slot = 0; slot < this.craftMatrix.getSizeInventory(); ++slot) {
            ItemStack slotStack = this.craftMatrix.getStackInSlot(slot);

            if (slotStack != null) {
                this.craftMatrix.decrStackSize(slot, canCraft ? -999 : 1);

                if (slotStack.getItem().hasContainerItem()) {
                    ItemStack leftoverStack = slotStack.getItem().getContainerItemStack(slotStack);

                    if (leftoverStack.isItemStackDamageable() && leftoverStack.getItemDamage() > leftoverStack.getMaxDamage()) {
                        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(this.thePlayer, leftoverStack));
                        leftoverStack = null;
                    }

                    if (leftoverStack != null && (!slotStack.getItem().doesContainerItemLeaveCraftingGrid(slotStack) || !this.thePlayer.inventory.addItemStackToInventory(leftoverStack))) {
                        if (this.craftMatrix.getStackInSlot(slot) == null) {
                            this.craftMatrix.setInventorySlotContents(slot, leftoverStack);
                        }
                        else {
                            this.thePlayer.dropPlayerItem(leftoverStack);
                        }
                    }
                }
            }
        }
    }

}
