
package me.heldplayer.mods.Smartestone.enchantment;

import me.heldplayer.mods.Smartestone.item.ItemWaterCore;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.item.ItemStack;

public class EnchantmentDurabilityExt extends EnchantmentDurability {

    public EnchantmentDurabilityExt(EnchantmentDurability original) {
        super(original.effectId, original.getWeight());
    }

    @Override
    public boolean canApply(ItemStack stack) {
        if (super.canApply(stack)) {
            return true;
        }
        return stack.getItem() instanceof ItemWaterCore;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        if (super.canApplyAtEnchantingTable(stack)) {
            return true;
        }
        return stack.getItem() instanceof ItemWaterCore;
    }

}
