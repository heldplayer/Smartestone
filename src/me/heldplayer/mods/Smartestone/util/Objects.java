
package me.heldplayer.mods.Smartestone.util;

import java.util.logging.Logger;

import me.heldplayer.api.Smartestone.micro.IconProvider;
import me.heldplayer.mods.Smartestone.block.BlockMicro;
import me.heldplayer.mods.Smartestone.block.BlockMulti1;
import me.heldplayer.mods.Smartestone.block.BlockMulti2;
import me.heldplayer.mods.Smartestone.item.ItemMicroBlock;
import me.heldplayer.mods.Smartestone.item.ItemRotator;
import me.heldplayer.mods.Smartestone.item.ItemTemplate;
import me.heldplayer.mods.Smartestone.item.ItemWaterCore;
import me.heldplayer.util.HeldCore.ModInfo;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Smartestones mod Objects
 * 
 */
public final class Objects {

    public static final String MOD_ID = "Smartestone";
    public static final String MOD_NAME = "Smartestone";
    public static final String MOD_VERSION = "0.01.02.01";
    public static final String MOD_CHANNEL = "SSChannel";
    public static final String CLIENT_PROXY = "me.heldplayer.mods.Smartestone.client.ClientProxy";
    public static final String SERVER_PROXY = "me.heldplayer.mods.Smartestone.CommonProxy";
    public static String TEXTURE_PREFIX = "smartestone";

    public static final ModInfo MOD_INFO = new ModInfo(MOD_ID, MOD_NAME, MOD_VERSION);

    public static boolean disableRedstoneFlag = true;

    public static Logger log;

    public static BlockMulti1 blockMulti1;
    public static BlockMulti2 blockMulti2;
    public static BlockMicro blockMicro;
    public static ItemRotator itemRotator;
    public static ItemMicroBlock itemMicroBlock;
    public static ItemWaterCore itemWaterCore;
    public static ItemTemplate itemTemplate;
    public static CreativeTabs creativeTab;
    public static CreativeTabs creativeTabMicroblocks;

    public static IconProvider redstoneIcon;
    public static IconProvider bluestoneIcon;
    public static IconProvider greenstoneIcon;
    public static IconProvider yellowstoneIcon;

}
