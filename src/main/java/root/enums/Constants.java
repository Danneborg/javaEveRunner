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

    public static final int EXECUMER_MINING_HOLD_SELECT_ALL_X_ASIS_MIN_BIAS = 40;
    public static final int EXECUMER_MINING_HOLD_SELECT_ALL_X_ASIS_MAX_BIAS = 200;
    public static final int EXECUMER_MINING_HOLD_SELECT_ALL_Y_ASIS_MIN_BIAS = 237;
    public static final int EXECUMER_MINING_HOLD_SELECT_ALL_Y_ASIS_MAX_BIAS = 249;
    public static final int EXECUMER_MINING_HOLD_FIRST_ROW_t_Y_ASIS_BIAS = 21;

    public static final int EXECUMER_MINING_HOLD_JETTISON_X_ASIS_MIN_BIAS = 50;
    public static final int EXECUMER_MINING_HOLD_JETTISON_X_ASIS_MAX_BIAS = 130;
    public static final int EXECUMER_MINING_HOLD_JETTISON_Y_ASIS_MIN_BIAS = 205;
    public static final int EXECUMER_MINING_HOLD_JETTISON_Y_ASIS_MAX_BIAS = 210;

    public static final int EXECUMER_MINING_HOLD_SELECT_ALL_JETTISON_Y_ASIS_MIN_BIAS = 110;
    public static final int EXECUMER_MINING_HOLD_SELECT_ALL_JETTISON_Y_ASIS_MAX_BIAS = 120;

    public static final int MOUSE_MOVE_SPEED = 4;
    public static Rectangle LOCATION_PANEL_FIRST_BOOKMARK_COORDINATE = Area.LOCATION_PANEL_FIRST_BOOKMARK.getRectangle();
    public static Rectangle FIRS_ROW_EXECUMER_MINING_HOLD = Area.FIRS_ROW_EXECUMER_MINING_HOLD.getRectangle();
    public static Rectangle ITEM_HANGAR_DROP = Area.ITEM_HANGAR_DROP.getRectangle();

}
