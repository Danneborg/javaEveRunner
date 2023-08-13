package root.miner;

import root.enums.Role;
import root.enums.WindowName;
import root.functions.Click;
import root.functions.ComparePixels;
import root.functions.KeyBoardPress;
import root.functions.WindowActivate;

public class SimpleMiner extends Miner {


    public SimpleMiner(WindowName windowName, WindowActivate windowActivate, ComparePixels comparePixels, Click click, KeyBoardPress keyBoardPress, FleetConstants fleetConstants) {
        super(windowName, windowActivate, comparePixels, click, Role.MINER, keyBoardPress, fleetConstants);
    }

    @Override
    public void activateWindow() {
        getWindowActivate().activateWindow(this.getWindowName().getTitle());
    }

    @Override
    public void act() {

    }

    @Override
    public void collectState() {

    }
}
