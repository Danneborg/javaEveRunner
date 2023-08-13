package root.logic;

import root.enums.WindowName;
import root.functions.Click;
import root.functions.ComparePixels;
import root.functions.KeyBoardPress;
import root.functions.WindowActivate;
import root.miner.FleetConstants;
import root.miner.Miner;
import root.miner.Orca;
import root.miner.SimpleMiner;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainLogic {

    public void run() throws AWTException {
        WindowActivate windowActivate = new WindowActivate();
        ComparePixels comparePixels = new ComparePixels();
        KeyBoardPress keyBoardPress = new KeyBoardPress();
        Click click = new Click();
        FleetConstants fleetConstants = new FleetConstants();
        var orca = new Orca(WindowName.TORWAK_MARTIN, windowActivate, comparePixels, click, keyBoardPress, fleetConstants);

        //TODO добавить прелоад основного окна
        orca.act();

    }
}
