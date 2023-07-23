package root.miner;

import lombok.AllArgsConstructor;
import lombok.Data;
import root.enums.IndicatorColour;
import root.enums.WindowName;
import root.functions.ComparePixels;
import root.functions.WindowActivate;

@Data
@AllArgsConstructor
public abstract class Miner implements Mine{

    private final WindowName windowName;
    private final WindowActivate windowActivate;
    private final ComparePixels comparePixels;

    @Override
    public void act() {
        windowActivate.activateWindow(this.getWindowName().getTitle());
        System.out.println(comparePixels.comparePixels(IndicatorColour.GREEN_SPACE_CIRCLE));
        System.out.println(comparePixels.comparePixels(IndicatorColour.TOP_RIGHT_TARGETS_LOCK));
    }

    @Override
    public void activateWindow() {
        windowActivate.activateWindow(this.getWindowName().getTitle());
    }

}
