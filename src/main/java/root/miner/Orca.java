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
        while (true) {
            if (getAwakeMoment() <= System.currentTimeMillis()) {
                orcaAct();
                printState();
            }
            Sleep.sleep(800, 1100);
        }
    }

    //В это методе идет вечный цикл с опросом зависимых окон и собственного состояния
    @Override
    //TODO добавить замер времени выполнения каждого шага в алгоритме для оптимизации и повышения быстродействия
    public void act() {

        while (true) {

            //В этом блоке мы делаем операции для самой орки
            if (getAwakeMoment() <= System.currentTimeMillis()) {
                orcaAct();
                printState();
            }

            //TODO добавить опрос всех майнеров на предмет статуса и последующей раздачей команд каждому окну
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
                doOpenFolder(() -> getComparePixels().isItemHangarOpen(), () -> getKeyBoardPress().openItemHangar(), "Item hangar");
            }

            if (!isOreHoldOpen()) {
                doOpenFolder(() -> getComparePixels().isMiningHoldOpen(), () -> getKeyBoardPress().openExecumerMiningHold(), "Mining hold");
            }

            setOreHoldFull(getComparePixels().isExecumerMinigHoldAlmostFull());
            setOreHoldEmpty(getComparePixels().isExecumerMinigHoldEmpty());

            if (isOreHoldEmpty()) {
                setState(State.UNDOCKING);
                doUndock();
                setSleep(10000L);
                return;
            }

            if (isOreHoldFull() || !isOreHoldEmpty()) {
                setState(State.IN_STATION_UNLOADING);
                unloadExecumerMinigHold();
                return;
            }

        }

        setOnBelt(getComparePixels().isOnBelt());

        if (isInSpace() && !isOnBelt()) {

            setState(State.IN_SPACE);

            if (!isLocationPanelOpen()) {
                doOpenFolder(() -> getComparePixels().isLocationPanelOpen(), () -> getKeyBoardPress().openBookmarkPanel(), "Bookmark panel");
            }

            warpOnBelt();
            setSleep(50000L);
            //TODO можно добавить проверку нахождения уже кого-то на белте, в случае с оркой это важно для более быстрого подбора руды

        }

        if (isInSpace() && isOnBelt()
                || State.ON_BELT.equals(getState())
                || State.ON_BELT_ALIGNING.equals(getState())
                || State.ON_BELT_MINING_STRIP_ACTIVATED.equals(getState())) {

            if (!State.ON_BELT.equals(getState())) {
                setState(State.ON_BELT);
            }

            setOreHoldFull(getComparePixels().isExecumerMinigHoldAlmostFull());
            setOreHoldOpen(getComparePixels().isMiningHoldOpen());

            //TODO добавить логику реварпа на другой белт, если общее количество целей для лока меньше 2

            if (!isOreHoldOpen()) {
                doOpenFolder(() -> getComparePixels().isMiningHoldOpen(), () -> getKeyBoardPress().openExecumerMiningHold(), "Mining hold");
                Sleep.sleep(300, 400);
            }

            if (isOreHoldFull()) {
                //TODO УБРАТЬ ПОСЛЕ ТЕСТА, ДРОП КОНТОВ НУЖЕН ТОЛЬКО ДЛЯ ОБЫЧНЫХ ЛОПАТ
                jettisonExecumer();
                Sleep.sleep(200, 300);
                returnDrones();
                //TODO добавить команду варпа на станцию или сброса контейнера, для орки это варп на станцию
                return;
            }else {

                if(!isDronesLaunched()){
                    launchDrones();
                }

                mine();
            }

        }

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
