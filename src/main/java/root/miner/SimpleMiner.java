package root.miner;

import root.enums.WindowName;
import root.functions.ComparePixels;
import root.functions.WindowActivate;

public class SimpleMiner extends Miner{


    public SimpleMiner(WindowName windowName, WindowActivate windowActivate, ComparePixels comparePixels) {
        super(windowName, windowActivate, comparePixels);
    }
}
