
package me.heldplayer.api.Smartestone.micro.rendering;

import java.util.ArrayList;
import java.util.List;

public class RenderFaceHelper {

    private static ArrayList<ReusableRenderFace> usedFaces = new ArrayList<ReusableRenderFace>();
    private static ArrayList<ReusableRenderFace> unusedFaces = new ArrayList<ReusableRenderFace>();

    private static int index = 0;
    private static int maxSize = 256;

    public static void updateIndex() {
        index = usedFaces.size();
    }

    public static ReusableRenderFace getAFace() {
        if (usedFaces.size() > maxSize) {
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
        return "ReusableRenderFace pool size: " + usedFaces.size() + "/" + maxSize;
    }

    public static List<ReusableRenderFace> processFaces(List<ReusableRenderFace> feed) {
        while (feed.size() + 128 > maxSize) {
            maxSize += 128;
        }

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

                int mode = 0;
                if (first.renderPass != second.renderPass) {
                    if (first.renderPass == 0) {
                        mode = 1;
                    }
                    else {
                        mode = 2;
                    }
                }

                if (first.startU >= second.startU && first.endU <= second.endU && first.startV >= second.startV && first.endV <= second.endV) {
                    if (mode == 0) {
                        first.renders = false;
                    }
                    if (mode == 1) {
                        first.offset += first.side % 2 == 0 ? 0.005D : -0.005D;
                    }
                    if (mode == 2) {
                        second.offset += second.side % 2 == 0 ? 0.005D : -0.005D;
                    }
                    continue;
                }
                if (second.startU >= first.startU && second.endU <= first.endU && second.startV >= first.startV && second.endV <= first.endV) {
                    if (mode == 0) {
                        second.renders = false;
                    }
                    if (mode == 1) {
                        first.offset += first.side % 2 == 0 ? 0.005D : -0.005D;
                    }
                    if (mode == 2) {
                        second.offset += second.side % 2 == 0 ? 0.005D : -0.005D;
                    }
                    continue;
                }

                if (first.startU >= second.startU && first.endU <= second.endU) {
                    if (first.startV >= second.startV && first.endV <= second.endV) {
                        if (mode == 0) {
                            first.renders = false;
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                    }
                    else if (first.startV >= second.startV && first.startV < second.endV) {
                        if (mode == 0) {
                            first.startV = second.endV;
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                    }
                    else if (first.endV <= second.endV && first.endV > second.startV) {
                        if (mode == 0) {
                            first.endV = second.startV;
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                    }
                }
                else if (first.startU >= second.startU && first.startU <= second.endU) {
                    if (first.startV >= second.startV && first.endV <= second.endV) {
                        if (mode == 0) {
                            first.startU = second.endU;
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                    }
                    else if (first.startV >= second.startV && first.startV < second.endV) {
                        if (mode == 0) {
                            first.startU = second.endU;
                            ReusableRenderFace third = getAFace();
                            third.copy(first);
                            third.startV = second.endV;
                            result.add(third);
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                    }
                    else if (first.endV <= second.endV && first.endV > second.startV) {
                        if (mode == 0) {
                            first.startU = second.endU;
                            ReusableRenderFace third = getAFace();
                            third.copy(first);
                            third.endV = second.startV;
                            result.add(third);
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                    }
                }
                else if (first.endU <= second.endU && first.endU >= second.startU) {
                    if (first.startV >= second.startV && first.endV <= second.endV) {
                        if (mode == 0) {
                            first.endU = second.startU;
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                    }
                    else if (first.startV >= second.startV && first.startV < second.endV) {
                        if (mode == 0) {
                            first.endU = second.startU;
                            ReusableRenderFace third = getAFace();
                            third.copy(first);
                            third.startV = second.endV;
                            result.add(third);
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                    }
                    else if (first.endV <= second.endV && first.endV > second.startV) {
                        if (mode == 0) {
                            first.endU = second.startU;
                            ReusableRenderFace third = getAFace();
                            third.copy(first);
                            third.endV = second.startV;
                            result.add(third);
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? 0.005D : -0.005D;
                        }
                    }
                }
            }

            if (first.renders) {
                result.add(first);
                feed.remove(i1);
                i1--;
            }
            else {
                //Objects.log.log(Level.INFO, "A face was not rendered! " + first.toString());
            }
        }

        return result;
    }

}
