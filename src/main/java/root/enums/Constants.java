package root.enums;

import root.indicator.Rectangle;

public final class Constants {

    public static final int NUMBER_OF_OVERVIEW_ROWS = 17;
    public static final int OVERVIEW_Y_AXIS_BIAS = 19;
    public static final int BOOK_PANEL_Y_AXIS_BIAS = 22;
    //TODO (возможно, нужно будет подкорректировать это значение). Количество пикселей, среди которых может быть отклонение в панели букмарок
    public static final int BOOK_PANEL_Y_AXIS_BIAS_RAND = 10;
    public static final int BOOK_PANEL_X_AXIS_MIN_BIAS_FOR_LEFT_CLICK = 50;
    public static final int BOOK_PANEL_X_AXIS_MAX_BIAS_FOR_LEFT_CLICK = 150;
    public static final int BOOK_PANEL_Y_AXIS_MIN_BIAS_FOR_LEFT_CLICK = 8;
    public static final int BOOK_PANEL_Y_AXIS_MAX_BIAS_FOR_LEFT_CLICK = 15;

    public static final int MOUSE_MOVE_SPEED = 4;
    public static Rectangle LOCATION_PANEL_FIRST_BOOKMARK_COORDINATE = Area.LOCATION_PANEL_FIRST_BOOKMARK.getRectangle();

}
