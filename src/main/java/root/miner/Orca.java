package root.miner;

import root.enums.WindowName;
import root.functions.ComparePixels;
import root.functions.WindowActivate;

public class Orca extends Miner implements Mine {


    public Orca(WindowName windowName, WindowActivate windowActivate, ComparePixels comparePixels) {
        super(windowName, windowActivate, comparePixels);
    }
}
