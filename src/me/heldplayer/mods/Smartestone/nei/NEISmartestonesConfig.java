
package me.heldplayer.mods.Smartestone.nei;

import java.util.Set;

import me.heldplayer.api.Smartestone.micro.IMicroBlockMaterial;
import me.heldplayer.api.Smartestone.micro.IMicroBlockSubBlock;
import me.heldplayer.api.Smartestone.micro.MicroBlockAPI;
import me.heldplayer.mods.Smartestone.client.gui.GuiCraftingChest;
import me.heldplayer.mods.Smartestone.util.Objects;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.DefaultOverlayHandler;

public class NEISmartestonesConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        //API.registerGuiOverlay(GuiCraftingChest.class, "crafting", 19, 11);
        API.registerGuiOverlay(GuiCraftingChest.class, "crafting", 46, 11);
        API.registerGuiOverlayHandler(GuiCraftingChest.class, new DefaultOverlayHandler(), "crafting");

        MultiItemRange blocks = new MultiItemRange();

        blocks.add(Objects.blockMulti1);
        blocks.add(Objects.blockMulti2);

        MultiItemRange items = new MultiItemRange();

        items.add(Objects.itemRotator);

        API.addSetRange("Smartestone.Blocks", blocks);
        API.addSetRange("Smartestone.Items", items);

        Set<String> subBlocks = MicroBlockAPI.getSubBlockNames();
        Set<String> materials = MicroBlockAPI.getMaterialNames();

        for (String subBlock : subBlocks) {
            MultiItemRange multiRange = new MultiItemRange();

            IMicroBlockSubBlock subBlockInst = MicroBlockAPI.getSubBlock(subBlock);
            for (String material : materials) {

                IMicroBlockMaterial materialInst = MicroBlockAPI.getMaterial(material);

                if (subBlockInst.isMaterialApplicable(materialInst)) {
                    NBTTagCompound compound = new NBTTagCompound("tag");
                    compound.setString("Material", material);
                    compound.setString("Type", subBlock);

                    multiRange.add(Objects.itemMicroBlock, MicroBlockAPI.ordinal(subBlockInst), MicroBlockAPI.ordinal(subBlockInst));
                }
            }

            API.addSetRange("Smartestone.MicroBlocks." + subBlockInst.getTypeName(), multiRange);
        }
    }

    @Override
    public String getName() {
        return Objects.MOD_NAME + " NEI Addon";
    }

    @Override
    public String getVersion() {
        return Objects.MOD_VERSION;
    }

}
