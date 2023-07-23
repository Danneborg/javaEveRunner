package root.functions;

import root.enums.Area;
import root.enums.Constants;
import root.indicator.Coordinate;

public class DefineCoordinate {

    public static int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    public static Coordinate defineARowToClick(Area area, int numberOfAvailableTargets) {

        int XMin = area.getRectangle().getTOP_LEFT_ANGLE().getPosX(); // Минимальное число для диапазона
        int XMax = area.getRectangle().getBOTTOM_RIGHT_ANGLE().getPosX(); // Максимальное число для диапазона

        int YMin = area.getRectangle().getTOP_LEFT_ANGLE().getPosY(); // Минимальное число для диапазона
        int YMax;
        if (numberOfAvailableTargets == 1) {
            YMax = area.getRectangle().getTOP_LEFT_ANGLE().getPosY() + Constants.OVERVIEW_Y_AXIS_BIAS; // Максимальное число для диапазона
        } else {
            YMax = area.getRectangle().getTOP_LEFT_ANGLE().getPosY() + Constants.OVERVIEW_Y_AXIS_BIAS*rnd(1, numberOfAvailableTargets); // Максимальное число для диапазона
        }

        return new Coordinate(rnd(XMin, XMax), rnd(YMin, YMax));
    }

    public static Coordinate defineCoordinate(Area area, int bottomXBias, int BottomYBias) {

        int XMin = area.getRectangle().getTOP_LEFT_ANGLE().getPosX(); // Минимальное число для диапазона
        int XMax = area.getRectangle().getBOTTOM_RIGHT_ANGLE().getPosX() + bottomXBias; // Максимальное число для диапазона

        int YMin = area.getRectangle().getTOP_LEFT_ANGLE().getPosY(); // Минимальное число для диапазона
        int YMax = area.getRectangle().getTOP_LEFT_ANGLE().getPosY() + BottomYBias; // Максимальное число для диапазона

        return new Coordinate(rnd(XMin, XMax), rnd(YMin, YMax));
    }

}
