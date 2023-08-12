package root.enums;

import lombok.Getter;
import root.indicator.Coordinate;
import root.indicator.Rectangle;

@Getter
//TODO все Area это просто прямоугольники, класс Area избыточен, нужно просто сделать константы в виде прямоугольников
public enum Area {

    LOCATION_PANEL_FIRST_BOOKMARK(new Rectangle(new Coordinate(135, 205), new Coordinate(335, 217))),
    //Первый астероид не майним, так как это способ определить на белте мы или нет
    OVERVIEW_FIRST_ROW(new Rectangle(new Coordinate(1153, 216), new Coordinate(1519, 231))),
    FIRS_ROW_EXECUMER_MINING_HOLD(new Rectangle(new Coordinate(450, 570), new Coordinate(600, 580))),
    ITEM_HANGAR_DROP(new Rectangle(new Coordinate(400, 90), new Coordinate(600, 370))),
    UNDOCK_BUTTON(new Rectangle(new Coordinate(1350, 280), new Coordinate(1560, 300))),
    LEFT_CLICK_BEFORE_DRONE_LAUNCH(new Rectangle(new Coordinate(770, 340), new Coordinate(1000, 500))),
    ;

    private final Rectangle rectangle;

    Area(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

}
