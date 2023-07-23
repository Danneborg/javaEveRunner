package root.logic;

import root.enums.WindowName;
import root.functions.ComparePixels;
import root.functions.WindowActivate;
import root.miner.Miner;
import root.miner.SimpleMiner;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainLogic {

    private List<Miner> minersList;

    public void run() throws InterruptedException, AWTException {
        minersList = new ArrayList<>();
        WindowActivate windowActivate = new WindowActivate();
        ComparePixels comparePixels = new ComparePixels();
        minersList.add(new SimpleMiner(WindowName.TORWAK_MARTIN, windowActivate, comparePixels));

        //TODO добавить прелоад всех доступных окон, проверить где находится каждое окно(космос, станция)

        while (true){
            Thread.sleep(1000);
            minersList.get(0).act();
        }


    }
}
