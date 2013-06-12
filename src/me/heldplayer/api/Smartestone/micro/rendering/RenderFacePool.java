
package me.heldplayer.api.Smartestone.micro.rendering;

import java.util.ArrayList;

public class RenderFacePool {

    private static ArrayList<ReusableRenderFace> usedFaces = new ArrayList<ReusableRenderFace>();
    private static ArrayList<ReusableRenderFace> unusedFaces = new ArrayList<ReusableRenderFace>();

    private static int index = 0;

    public static void updateIndex() {
        index = usedFaces.size();
    }

    public static ReusableRenderFace getAFace() {
        if (usedFaces.size() >= 256) {
            for (int i = 0; i < index; i++) {
                ReusableRenderFace face = usedFaces.remove(0);
                unusedFaces.add(face);
            }
        }

        if (unusedFaces.size() == 0) {
            ReusableRenderFace face = new ReusableRenderFace();
            usedFaces.add(face);
            return face;
        }
        else {
            ReusableRenderFace face = unusedFaces.remove(0);
            usedFaces.add(face);
            return face;
        }
    }

    public static String getDebugString() {
        return "ReusableRenderFace pool size: " + usedFaces.size() + "/" + unusedFaces.size();
    }

}
