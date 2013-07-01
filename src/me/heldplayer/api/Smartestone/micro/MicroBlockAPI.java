
package me.heldplayer.api.Smartestone.micro;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

import me.heldplayer.api.Smartestone.micro.placement.IMicroBlockPlacementRule;

public final class MicroBlockAPI {

    public static int microBlockId = 0;

    private static final TreeMap<String, IMicroBlockMaterial> materials = new TreeMap<String, IMicroBlockMaterial>();
    private static final TreeMap<String, IMicroBlockSubBlock> blocks = new TreeMap<String, IMicroBlockSubBlock>();
    private static final LinkedList<IMicroBlockMaterial> materialsList = new LinkedList<IMicroBlockMaterial>();
    private static final LinkedList<IMicroBlockSubBlock> blocksList = new LinkedList<IMicroBlockSubBlock>();
    private static final LinkedList<IMicroBlockPlacementRule> placementRules = new LinkedList<IMicroBlockPlacementRule>();

    public static void registerMaterial(IMicroBlockMaterial material) {
        materials.put(material.getIdentifier(), material);
        materialsList.add(material);
    }

    public static void registerSubBlock(IMicroBlockSubBlock block) {
        blocks.put(block.getTypeName(), block);
        blocksList.add(block);
    }

    public static void registerPlacementRule(IMicroBlockPlacementRule rule) {
        placementRules.add(rule);
    }

    public static Set<String> getMaterialNames() {
        return materials.keySet();
    }

    public static Set<String> getSubBlockNames() {
        return blocks.keySet();
    }

    public static IMicroBlockMaterial getMaterial(String identifier) {
        return materials.get(identifier);
    }

    public static IMicroBlockSubBlock getSubBlock(String identifier) {
        return blocks.get(identifier);
    }

    /**
     * Method to get ordinal of a material. Note that there is no guarantee that
     * this will remain the same between sessions.
     */
    public static int ordinal(IMicroBlockMaterial material) {
        return materialsList.indexOf(material);
    }

    /**
     * Method to get ordinal of a subBlock. Note that there is no guarantee that
     * this will remain the same between sessions.
     */
    public static int ordinal(IMicroBlockSubBlock block) {
        return blocksList.indexOf(block);
    }

    public static boolean canBeAdded(MicroBlockInfo first, MicroBlockInfo second) {
        for (IMicroBlockPlacementRule rule : placementRules) {
            if (rule.conflicts(first, second)) {
                return false;
            }
        }

        return true;
    }

    public static float getU(int side, float hitX, float hitY, float hitZ) {
        switch (side) {
        case 0:
        case 1:
            return 1.0F - hitX;
        case 2:
            return 1.0F - hitX;
        case 3:
            return hitX;
        case 4:
            return 1.0F - hitZ;
        case 5:
            return hitZ;
        }

        return 0.0F;
    }

    public static float getV(int side, float hitX, float hitY, float hitZ) {
        switch (side) {
        case 0:
            return hitZ;
        case 1:
            return 1.0F - hitZ;
        case 4:
        case 5:
        case 2:
        case 3:
            return hitY;
        }

        return 0.0F;
    }

}
