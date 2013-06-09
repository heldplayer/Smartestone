
package me.heldplayer.mods.Smartestone.inventory;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class SlotSmeltingOutput extends Slot {

    private EntityPlayer player;
    private int amount;

    public SlotSmeltingOutput(EntityPlayer player, IInventory inventory, int slotId, int slotX, int slotY) {
        super(inventory, slotId, slotX, slotY);
        this.player = player;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.amount += Math.min(amount, this.getStack().stackSize);
        }

        return super.decrStackSize(amount);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        this.onCrafting(stack);
        super.onPickupFromSlot(player, stack);
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.amount += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        stack.onCrafting(this.player.worldObj, this.player, this.amount);

        if (!this.player.worldObj.isRemote) {
            int amount = this.amount;
            float experience = FurnaceRecipes.smelting().getExperience(stack);

            if (experience == 0.0F) {
                amount = 0;
            }
            else if (experience < 1.0F) {
                int orbs = MathHelper.floor_float((float) amount * experience);

                if (orbs < MathHelper.ceiling_float_int((float) amount * experience) && (float) Math.random() < (float) amount * experience - (float) orbs) {
                    ++orbs;
                }

                amount = orbs;
            }

            while (amount > 0) {
                int orbSize = EntityXPOrb.getXPSplit(amount);
                amount -= orbSize;
                this.player.worldObj.spawnEntityInWorld(new EntityXPOrb(this.player.worldObj, this.player.posX, this.player.posY + 0.5D, this.player.posZ + 0.5D, orbSize));
            }
        }

        this.amount = 0;

        GameRegistry.onItemSmelted(this.player, stack);

        if (stack.itemID == Item.ingotIron.itemID) {
            this.player.addStat(AchievementList.acquireIron, 1);
        }

        if (stack.itemID == Item.fishCooked.itemID) {
            this.player.addStat(AchievementList.cookFish, 1);
        }
    }
}
