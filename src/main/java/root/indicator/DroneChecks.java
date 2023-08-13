package root.indicator;

import lombok.Data;

@Data
public class DroneChecks {

    private final boolean dronesIdle;
    private final boolean dronesReturning;
    private final boolean dronesMining;
    //TODO, сделать для обычных лопат, добавить индикатор и внести в общую функцию проверки
    private final boolean dronesFighting;

}
