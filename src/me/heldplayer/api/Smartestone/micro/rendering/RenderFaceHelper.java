
package me.heldplayer.api.Smartestone.micro.rendering;

import java.util.ArrayList;
import java.util.List;

public class RenderFaceHelper {

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

    public static List<ReusableRenderFace> processFaces(List<ReusableRenderFace> feed) {
        ArrayList<ReusableRenderFace> result = new ArrayList<ReusableRenderFace>();

        for (int i1 = 0; i1 < feed.size(); i1++) {
            ReusableRenderFace first = feed.get(i1);

            for (int i2 = 0; i2 < feed.size(); i2++) {
                ReusableRenderFace second = feed.get(i2);

                if (first == second) {
                    continue;
                }

                if (!first.renders) {
                    continue;
                }
                if (!second.renders) {
                    continue;
                }

                // We only handle faces on the same plane and with the same direction
                if (first.side != second.side || first.offset != second.offset) {
                    continue;
                }
                if (first.startU >= second.startU && first.endU <= second.endU && first.startV >= second.startV && first.endV <= second.endV) {
                    first.renders = false;
                    continue;
                }
                if (second.startU >= first.startU && second.endU <= first.endU && second.startV >= first.startV && second.endV <= first.endV) {
                    second.renders = false;
                    continue;
                }

                if (first.startU >= second.startU && first.endU <= second.endU) {
                    if (first.startV >= second.startV && first.endV <= second.endV) {
                        first.renders = false;
                        continue;
                    }
                    else if (first.startV >= second.startV && first.startV < second.endV) {
                        first.startV = second.endV;
                    }
                    else if (first.endV <= second.endV && first.endV > second.startV) {
                        first.endV = second.startV;
                    }
                }
                else if (first.startU >= second.startU && first.startU <= second.endU) {
                    if (first.startV >= second.startV && first.endV <= second.endV) {
                        first.startU = second.endU;
                    }
                    else if (first.startV >= second.startV && first.startV < second.endV) {
                        first.startU = second.endU;
                        ReusableRenderFace third = getAFace();
                        third.copy(first);
                        third.startV = second.endV;
                        result.add(third);
                    }
                    else if (first.endV <= second.endV && first.endV > second.startV) {
                        first.startU = second.endU;
                        ReusableRenderFace third = getAFace();
                        third.copy(first);
                        third.endV = second.startV;
                        result.add(third);
                    }
                }
                else if (first.endU <= second.endU && first.endU >= second.startU) {
                    if (first.startV >= second.startV && first.endV <= second.endV) {
                        first.endU = second.startU;
                    }
                    else if (first.startV >= second.startV && first.startV < second.endV) {
                        first.endU = second.startU;
                        ReusableRenderFace third = getAFace();
                        third.copy(first);
                        third.startV = second.endV;
                        result.add(third);
                    }
                    else if (first.endV <= second.endV && first.endV > second.startV) {
                        first.endU = second.startU;
                        ReusableRenderFace third = getAFace();
                        third.copy(first);
                        third.endV = second.startV;
                        result.add(third);
                    }
                }
            }

            if (first.renders) {
                result.add(first);
                feed.remove(i1);
                i1--;
            }
        }

        return result;
    }

}
