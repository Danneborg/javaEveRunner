package root.enums;

import root.indicator.Coordinate;
import root.indicator.Rectangle;

import java.util.Set;

public final class Constants {

    public static final int NUMBER_OF_OVERVIEW_ROWS = 17;
    public static final int OVERVIEW_Y_AXIS_BIAS = 19;
    public static final int BOOK_PANEL_Y_AXIS_BIAS = 22;
    //TODO (возможно, нужно будет подкорректировать это значение). Количество пикселей, среди которых может быть отклонение в панели букмарок
    public static final int BOOK_PANEL_Y_AXIS_BIAS_RAND = 10;
    public static final int BOOK_PANEL_X_AXIS_MIN_BIAS_FOR_LEFT_CLICK = 55;
    public static final int BOOK_PANEL_X_AXIS_MAX_BIAS_FOR_LEFT_CLICK = 150;
    public static final int BOOK_PANEL_Y_AXIS_MIN_BIAS_FOR_LEFT_CLICK = 13;
    public static final int BOOK_PANEL_Y_AXIS_MAX_BIAS_FOR_LEFT_CLICK = 16;

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

    public static final int MINING_DRONE_WORD_MINING_LETTER_i_Y_BIAS = 22;
    public static final int MINING_DRONE_NUMBER_FOR_COUNT = 4;

    public static final int MOUSE_MOVE_SPEED = 4;
    public static Rectangle LOCATION_PANEL_FIRST_BOOKMARK_COORDINATE = Area.LOCATION_PANEL_FIRST_BOOKMARK.getRectangle();
    public static Rectangle FIRS_ROW_EXECUMER_MINING_HOLD = Area.FIRS_ROW_EXECUMER_MINING_HOLD.getRectangle();
    public static Rectangle ITEM_HANGAR_DROP = Area.ITEM_HANGAR_DROP.getRectangle();
    public static Rectangle DRONE_HOLD_DRONE_IN_A_BAY_LINE = new Rectangle(new Coordinate(1310, 583), new Coordinate(1400, 591));//место с надписью drone in bay которое можно тянуть за лкм
    public static Rectangle DRONE_HOLD_DRAG_OUT = new Rectangle(new Coordinate(1270, 790), new Coordinate(1540, 870));//свободная часть пространства куда можно перетянуть дронов для запуска

    public static Rectangle ORCA_INVENTORY_FIRST_ROW = new Rectangle(new Coordinate(414, 775), new Coordinate(570, 785));
    public static Rectangle ORCA_INVENTORY_SECOND_ROW = new Rectangle(new Coordinate(410, 795), new Coordinate(560, 805));
    public static Rectangle ORCA_INVENTORY_THIRD_ROW = new Rectangle(new Coordinate(420, 815), new Coordinate(570, 825));

    public static final int INVENTORY_INVERT_SELECTION_X_MIN_BIAS = 50;
    public static final int INVENTORY_INVERT_SELECTION_X_MAX_BIAS = 110;
    public static final int INVENTORY_INVERT_SELECTION_Y_MIN = 832;
    public static final int INVENTORY_INVERT_SELECTION_Y_MAX = 842;

    public static Rectangle VELDSPAR_TAB = new Rectangle(new Coordinate(1280, 151), new Coordinate(1313, 160));
    public static Rectangle CONTAINER_TAB = new Rectangle(new Coordinate(1354, 151), new Coordinate(1390, 160));


    public static Set<State> ON_BELT_STATES = Set.of(State.ON_BELT, State.ON_BELT_ALIGNING, State.ON_BELT_DRONES_MINING, State.ON_BELT_MINING_STRIP_ACTIVATED);
}
