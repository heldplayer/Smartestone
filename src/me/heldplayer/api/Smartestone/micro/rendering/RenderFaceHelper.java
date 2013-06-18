
package me.heldplayer.api.Smartestone.micro.rendering;

import java.util.ArrayList;
import java.util.List;

public class RenderFaceHelper {

    private static ArrayList<ReusableRenderFace> usedFaces = new ArrayList<ReusableRenderFace>();
    private static ArrayList<ReusableRenderFace> unusedFaces = new ArrayList<ReusableRenderFace>();

    private static int index = 0;
    private static int maxSize = 256;

    // Fields because highlighting
    private static double startU1;
    private static double startU2;
    private static double startV1;
    private static double startV2;
    private static double endU1;
    private static double endU2;
    private static double endV1;
    private static double endV2;

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

    public static ArrayList<ReusableRenderFace> processFaces(List<ReusableRenderFace> feed) {
        while (feed.size() + 128 > maxSize) {
            maxSize += 128;
        }

        double offset = 0.005D;

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

                startU1 = first.startU;
                startU2 = second.startU;
                startV1 = first.startV;
                startV2 = second.startV;
                endU1 = first.endU;
                endU2 = second.endU;
                endV1 = first.endV;
                endV2 = second.endV;

                if (startU1 >= startU2 && endU1 <= endU2 && startV1 >= startV2 && endV1 <= endV2) {
                    if (mode == 0) {
                        first.renders = false;
                    }
                    if (mode == 1) {
                        first.offset += first.side % 2 == 0 ? offset : -offset;
                    }
                    if (mode == 2) {
                        second.offset += second.side % 2 == 0 ? offset : -offset;
                    }
                    continue;
                }
                if (startU2 >= startU1 && endU2 <= endU1 && startV2 >= startV1 && endV2 <= endV1) {
                    if (mode == 0) {
                        second.renders = false;
                    }
                    if (mode == 1) {
                        first.offset += first.side % 2 == 0 ? offset : -offset;
                    }
                    if (mode == 2) {
                        second.offset += second.side % 2 == 0 ? offset : -offset;
                    }
                    continue;
                }

                if (startU1 >= startU2 && endU1 <= endU2) {
                    if (startV1 >= startV2 && endV1 <= endV2) {
                        if (mode == 0) {
                            first.renders = false;
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? offset : -offset;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? offset : -offset;
                        }
                    }
                    else if (startV1 >= startV2 && startV1 < endV2) {
                        if (mode == 0) {
                            first.startV = endV2;
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? offset : -offset;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? offset : -offset;
                        }
                    }
                    else if (endV1 <= endV2 && endV1 > startV2) {
                        if (mode == 0) {
                            first.endV = startV2;
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? offset : -offset;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? offset : -offset;
                        }
                    }
                }
                else if (startU1 >= startU2 && startU1 <= endU2) {
                    if (startV1 >= startV2 && endV1 <= endV2) {
                        if (mode == 0) {
                            first.startU = endU2;
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? offset : -offset;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? offset : -offset;
                        }
                    }
                    else if (startV1 >= startV2 && startV1 < endV2) {
                        if (mode == 0) {
                            first.startU = endU2;
                            ReusableRenderFace third = getAFace();
                            third.copy(first);
                            third.startV = endV2;
                            result.add(third);
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? offset : -offset;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? offset : -offset;
                        }
                    }
                    else if (endV1 <= endV2 && endV1 > startV2) {
                        if (mode == 0) {
                            first.startU = endU2;
                            ReusableRenderFace third = getAFace();
                            third.copy(first);
                            third.endV = startV2;
                            result.add(third);
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? offset : -offset;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? offset : -offset;
                        }
                    }
                }
                else if (endU1 <= endU2 && endU1 >= startU2) {
                    if (startV1 >= startV2 && endV1 <= endV2) {
                        if (mode == 0) {
                            first.endU = startU2;
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? offset : -offset;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? offset : -offset;
                        }
                    }
                    else if (startV1 >= startV2 && startV1 < endV2) {
                        if (mode == 0) {
                            first.endU = startU2;
                            ReusableRenderFace third = getAFace();
                            third.copy(first);
                            third.startV = endV2;
                            result.add(third);
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? offset : -offset;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? offset : -offset;
                        }
                    }
                    else if (endV1 <= endV2 && endV1 > startV2) {
                        if (mode == 0) {
                            first.endU = startU2;
                            ReusableRenderFace third = getAFace();
                            third.copy(first);
                            third.endV = startV2;
                            result.add(third);
                        }
                        if (mode == 1) {
                            first.offset += first.side % 2 == 0 ? offset : -offset;
                        }
                        if (mode == 2) {
                            second.offset += second.side % 2 == 0 ? offset : -offset;
                        }
                    }
                }
                else if (startU1 > startU2 && endU1 < endU2 && startV1 < startV2 && endV1 > endV2) {
                    if (mode == 0) {
                        ReusableRenderFace third = getAFace();
                        third.copy(first);
                        second.endV = startV1;
                        third.startV = endV1;
                        result.add(third);
                    }
                    if (mode == 1) {
                        first.offset += first.side % 2 == 0 ? offset : -offset;
                    }
                    if (mode == 2) {
                        second.offset += second.side % 2 == 0 ? offset : -offset;
                    }
                }
                else if (startU1 < startU2 && endU1 > endU2 && startV1 > startV2 && endV1 < endV2) {
                    if (mode == 0) {
                        ReusableRenderFace third = getAFace();
                        third.copy(first);
                        first.endU = startU2;
                        third.startU = endU2;
                        result.add(third);
                    }
                    if (mode == 1) {
                        first.offset += first.side % 2 == 0 ? offset : -offset;
                    }
                    if (mode == 2) {
                        second.offset += second.side % 2 == 0 ? offset : -offset;
                    }
                }
            }

            if (first.renders) {
                result.add(first);
            }
        }

        return result;
    }

}
