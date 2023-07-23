package root.enums;

import lombok.Getter;
import root.indicator.Coordinate;
import root.indicator.Rectangle;

@Getter
public enum Area {

    LOCATION_PANEL_FIRST_BOOKMARK(new Rectangle(new Coordinate(135, 205), new Coordinate(335, 217))),
    //Первый астероид не майним, так как это способ определить на белте мы или нет
    OVERVIEW_FIRST_ROW(new Rectangle(new Coordinate(1153, 216), new Coordinate(1519, 231))),
    ;

    private final Rectangle rectangle;

    Area(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

}
