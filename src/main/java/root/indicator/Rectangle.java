package root.indicator;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Rectangle {
    private final Coordinate TOP_LEFT_ANGLE;
    private final Coordinate BOTTOM_RIGHT_ANGLE;
}
