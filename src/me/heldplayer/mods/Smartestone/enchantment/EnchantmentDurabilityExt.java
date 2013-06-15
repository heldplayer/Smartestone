
package me.heldplayer.mods.Smartestone.enchantment;

import me.heldplayer.mods.Smartestone.item.ItemWaterCore;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.item.ItemStack;

public class EnchantmentDurabilityExt extends EnchantmentDurability {

    EnchantmentDurability original;

    public EnchantmentDurabilityExt(EnchantmentDurability original) {
        super(original.effectId, original.getWeight());

        this.original = original;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        if (this.original.canApply(stack)) {
            return true;
        }
        return stack.getItem() instanceof ItemWaterCore;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        if (this.original.canApplyAtEnchantingTable(stack)) {
            return true;
        }
        return stack.getItem() instanceof ItemWaterCore;
    }

}
