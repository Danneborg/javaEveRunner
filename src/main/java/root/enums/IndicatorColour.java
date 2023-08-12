package root.enums;

import root.indicator.Coordinate;

public enum IndicatorColour {

    //Пиксели для определения нахождения в космосе
    GREEN_SPACE_CIRCLE(0x30D24B, 10, new Coordinate(1001, 723)),//зеленый пиксель в кружочке уровня безопасности
    TOP_RIGHT_TARGETS_LOCK(0xD7D7D7, 10, new Coordinate(1558, 147)),//белый пиксель в икноке замка панели овервью
    MIDDLE_DOT_TOP_RIGHT_OVERVIEW(0xD6D6D6, 10, new Coordinate(1533, 151)),
    PLUS_MARK_NAV_PANEL(0xFFFFFF, 10, new Coordinate(1114, 833)),

    TOP_LEFT_LOCATIONS_LOCK(0xD7D7D7, 10, new Coordinate(312, 21)),//белый пиксель в икноке замка панели локаций
    TOP_LEFT_MIDDLE_DOT_TOP_LOCATIONS(0xD6D6D6, 10, new Coordinate(287, 29)),//белый пиксель в средней точке троеточия панели закладок
    TOP_LEFT_GREEN_EYE_LOCATIONS(0xA5D881, 10, new Coordinate(179, 141)),//зеленый пиксель в иконке глаза панели закладок

    //Пиксели для определения открыт ли ангар на станции
    ITEM_HANGAR_LOCK(0xD6D6D6, 10, new Coordinate(610, 13)),//белый пиксель в икноке замка вкладки ангара
    ITEM_HANGAR_LETTER_t(0xE8E8E8, 10, new Coordinate(392, 15)),//белый пиксель в букве t вкладки ангара
    ITEM_HANGAR_LETTER_r(0xDCDCDC, 10, new Coordinate(447, 15)),//белый пиксель в букве r вкладки ангара

    //Пиксели для определения открыт ли ангар для руды майнера на экзекюмере
    MINING_HOLD_LOCK(0xD6D6D6, 10, new Coordinate(610, 508)),//белый пиксель в икноке замка вкладки руды
    MINING_HOLD_FIRST_LETTER_i(0xE6E6E6, 10, new Coordinate(396, 508)),//белый в первой букве i вкладки руды
    MINING_HOLD_FIRST_LETTER_d(0xDFDFDF, 10, new Coordinate(445, 508)),//белый букве d вкладки руды
    MINING_HOLD_FIRST_ROW_t(0xC5C5C5, 10, new Coordinate(622, 574)),//белый пиксель букве t в слове Asteroid в строке с рудой, нужен для подсчета зоны куда сделать клик при перетаскивании руды

    //Пиксели для определения заполненности ангара руды на экзекюмере
    MINING_HOLD_LESS_THAN_MIDDLE(0x0E0E0E, 10, new Coordinate(402, 530)),//черный пиксель в полоске заполненности руды
    MINING_HOLD_LESS_AROUND_MIDDLE(0x0E0E0E, 10, new Coordinate(502, 530)),//черный пиксель в полоске заполненности руды
    MINING_HOLD_ALMOST_FULL(0x0E0E0E, 10, new Coordinate(657, 530)),//черный пиксель в полоске заполненности руды

    //TODO подумать, как определить есть ли дроны в космосе
    DRONE_HOLD_LOCK(0xE3E3E3, 10, new Coordinate(1558, 583)),//белый пиксель в икноке замка вкладки дронов

    GREEN_POINT_MINER_1(0x4C6F41, 10, new Coordinate(1174, 736)),//зеленый пиксель в иконке 1ого слота майнингового оборудования
    GREEN_POINT_MINER_2(0x4C6F41, 10, new Coordinate(1225, 736)),//зеленый пиксель в иконке 2ого слота майнингового оборудования

    //Пиксели для захвата целей в овервью, первый астероид мы не трогаем, так как это способ определить на белте мы или нет
    FIRST_LOCKED_TARGET_BRACKET_NOT_ACTIVE(0x8A8B8C, 12, new Coordinate(1145, 220)), //+19 по Y
    FIRST_LOCKED_TARGET_BRACKET_ACTIVE(0xFFFFFF, 12, new Coordinate(1145, 220)), //+19 по Y
    FIRST_LETTER_M_IN_OVERVIEW(0xE9EBEB, 10, new Coordinate(1281, 223)), //+19 по Y
    FIRST_LETTER_K_IN_OVERVIEW(0xE6E6E6, 10, new Coordinate(1271, 220)), //+19 по Y
    FIRST_ROW_ASTEROID_ICON_PIXEL(0x494949, 10, new Coordinate(1152, 203)),
    FIRST_ROW_WORD_SIZE_LETTER_m(0xE8E8E8, 10, new Coordinate(1473, 204)),


    //Пиксели в слове Warping
    WARP_DRIVE_WORD_LETTER_NAV_PANEL_W(0xE5E9EB, 10, new Coordinate(1052, 847)),
    WARP_DRIVE_WORD_NAV_PANEL_LETTER_i(0xE3E6E7, 10, new Coordinate(1079, 847)),
    WARP_DRIVE_WORD_NAV_PANEL_LETTER_g(0xE1E6E7, 10, new Coordinate(1091, 849)),

    //Пиксели для определения нахождения внутри станции
    BLUE_LEFT_TOP_UNDOCK_BUTTON(0x58A7BF, 10, new Coordinate(1340, 269)),
    BLUE_RIGHT_BOTTOM_UNDOCK_BUTTON(0x58A7BF, 10, new Coordinate(1581, 308)),
    LETTER_U_UNDOCK_WORD(0xC6CCCE, 32, new Coordinate(1446, 284)),
    LETTER_k_UNDOCK_WORD(0xC6CCCE, 32, new Coordinate(1480, 284)),
    MIDDLE_DOT_TOP_RIGHT_STATION_INFO(0xD6D6D6, 10, new Coordinate(1533, 18)),

    ;

    private final int colour;
    private final int shadeDeviation;
    private final Coordinate coordinate;

    IndicatorColour(int colour, int shadeDeviation, Coordinate coordinate) {
        this.colour = colour;
        this.shadeDeviation = shadeDeviation;
        this.coordinate = coordinate;
    }

    public int getColour() {
        return colour;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getShadeDeviation() {
        return shadeDeviation;
    }
}
