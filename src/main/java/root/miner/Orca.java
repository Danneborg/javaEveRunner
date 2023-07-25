package root.miner;

import lombok.Getter;
import lombok.Setter;
import root.enums.Role;
import root.enums.State;
import root.enums.WindowName;
import root.functions.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Orca extends Miner implements Mine {

    private List<Miner> minerList = new ArrayList<>();

    public Orca(WindowName windowName, WindowActivate windowActivate, ComparePixels comparePixels, Click click, KeyBoardPress keyBoardPress) {
        super(windowName, windowActivate, comparePixels, click, Role.ORCA, keyBoardPress);
    }


    public void actTest() {
        //В этом блоке мы делаем операции для самой орки
        if (getAwakeMoment() <= System.currentTimeMillis()) {
                orcaAct();
//            while (true){
//                orcaAct();
//                Sleep.sleep(1000,1100);
//            }

        }
    }

    //В это методе идет вечный цикл с опросом зависимых окон и собственного состояния
    @Override
    public void act() {

        while (true) {

            //В этом блоке мы делаем операции для самой орки
            if (getAwakeMoment() <= System.currentTimeMillis()) {
                orcaAct();
            }

            for (var singleMiner : minerList) {
                singleMiner.act();
            }

        }

    }


    //TODO реализовать
    private void orcaAct() {
        activateWindow();
        collectState();

        if (isInInWarp()) {
            setState(State.IN_WARP);
            setSleep(20000L);
            return;
        }

        if (!isInSpace() && !isInInStation()) {
            setState(State.UNKNOWN);
            setSleep(20000L);
            return;
        }

        if (isInInStation()) {

            setState(State.IN_STATION);

            setItemHangarOpen(getComparePixels().isItemHangarOpen());
            setOreHoldOpen(getComparePixels().isMiningHoldOpen());

            if (!isItemHangarOpen()) {
                doOpenFolder(()->getComparePixels().isItemHangarOpen(),()->getKeyBoardPress().openItemHangar(),"Item hangar");
            }

            if (!isOreHoldOpen()) {
                doOpenFolder(()->getComparePixels().isMiningHoldOpen(),()->getKeyBoardPress().openExecumerMiningHold(),"Mining hold");
            }

            setOreHoldFull(getComparePixels().isExecumerMinigHoldAlmostFull());
            setOreHoldEmpty(getComparePixels().isExecumerMinigHoldEmpty());

            if (isOreHoldEmpty()) {
                setState(State.UNDOCKING);

                //TODO добавить команду андока
                setAwakeMoment(10000L);
                return;
            }

            if (isOreHoldFull()) {
                setState(State.IN_STATION_UNLOADING);

                //TODO добавить команду разгрузки

            }

        }

        setOnBelt(getComparePixels().isOnBelt());

        if (isInSpace() && !isOnBelt()) {
            System.out.println("3");
            if (!isLocationPanelOpen()) {
                doOpenFolder(()->getComparePixels().isLocationPanelOpen(),()->getKeyBoardPress().openBookmarkPanel(),"Bookmark panel");
            }

            warpOnBelt();
            setState(State.IN_WARP);
            setSleep(50000L);
            //TODO можно добавить проверку нахождения уже кого-то на белте, в случае с оркой это важно для более быстрого подбора руды

        }

        printState();

    }

    //TODO дополнить по необходимости
    @Override
    public void collectState() {

        setInSpace(getComparePixels().isInSpace());
        setInInWarp(getComparePixels().isInWarp());
        setInInStation(getComparePixels().isInStation());
        setLocationPanelOpen(getComparePixels().isLocationPanelOpen());

    }

    @Override
    public void activateWindow() {
        getWindowActivate().activateWindow(this.getWindowName().getTitle());
    }

    //TODO добавить метод опроса обычных майнеров, для выяснения их текущего положеня
}
