package root.miner;

import lombok.Getter;
import lombok.Setter;
import root.enums.Constants;
import root.enums.Role;
import root.enums.State;
import root.enums.WindowName;
import root.functions.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Orca extends Miner implements Mine {

    private boolean firstBonusActivated;
    private boolean secondBonusActivated;
    private boolean areDronesIdle;
    private boolean areDronesMining;
    private boolean areDronesReturning;
    private boolean isInventoryOpen;

    private List<Miner> minerList = new ArrayList<>();

    public Orca(WindowName windowName, WindowActivate windowActivate, ComparePixels comparePixels, Click click, KeyBoardPress keyBoardPress, FleetConstants fleetConstants) {
        super(windowName, windowActivate, comparePixels, click, Role.ORCA, keyBoardPress, fleetConstants);
    }


    //TODO установить в орку шилдбустер и добавить механизм его активации на белте
    public void actTest() {
        //В этом блоке мы делаем операции для самой орки
        while (true) {
            //TODO
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
            //TODO все, что выше, это логика для копания, для подъема контов у нас должна быть отдельная логика, которая должна поднимать контейнеры
            takeContainers();

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

            setNumberOfRowsInMiningHold(getComparePixels().numberRowsInMiningHold());

            setItemHangarOpen(getComparePixels().isItemHangarOpen());
            if (!isItemHangarOpen()) {
                doOpenFolder(() -> getComparePixels().isItemHangarOpen(), () -> getKeyBoardPress().openItemHangar(), "Item hangar");
                Sleep.sleep(200, 300);
            }

            checkAndOpenOreAndItemHolds();

            setOreHoldFull(getComparePixels().isExecumerMinigHoldAlmostFull());
            setOreHoldEmpty(getComparePixels().isExecumerMinigHoldEmpty());

            if (isOreHoldEmpty() && getNumberOfRowsInMiningHold() == 0) {
                setState(State.UNDOCKING);
                doUndock();
                setSleep(15000L);
                return;
            }

            if (isOreHoldFull() || !isOreHoldEmpty() || getNumberOfRowsInMiningHold() != 0) {
                setState(State.IN_STATION_UNLOADING);
                unloadExecumerMinigHold();
                return;
            }

        }

        setOnBelt(getComparePixels().isOnBelt());

        if (isInSpace() && !isOnBelt()) {

            setState(State.IN_SPACE);

            setItemHangarOpen(getComparePixels().isItemHangarOpen());

            if (!isLocationPanelOpen()) {
                doOpenFolder(() -> getComparePixels().isLocationPanelOpen(), () -> getKeyBoardPress().openBookmarkPanel(), "Bookmark panel");
            }

            checkAndOpenOreAndItemHolds();

            warpOnBelt();
            setSleep(50000L);
            //TODO можно добавить проверку нахождения уже кого-то на белте, в случае с оркой это важно для более быстрого подбора руды

        }

        if (isInSpace() && isOnBelt() || Constants.ON_BELT_STATES.contains(getState())) {

            if (!Constants.ON_BELT_STATES.contains(getState())) {
                setState(State.ON_BELT);
            }

            setAvailableTargetsCloser10km(getComparePixels().numberOfRowsCloserThan10km());
            setAvailableTargetsFurther10km(getComparePixels().numberOfRowsFurtherThan10km());

            if (getAvailableTargetsCloser10km() + getAvailableTargetsFurther10km() < 2) {
                //TODO добавить механику варпа на другой белт всем флотом
                for (var singleMiner : minerList) {
                    singleMiner.setGlobalBelt(getGlobalBelt());
                    singleMiner.setCurrentBelt(getCurrentBelt());
                    singleMiner.setSleep(60000L);
                }
            }

            checkAndOpenOreAndItemHolds();
            setOreHoldFull(getComparePixels().isExecumerMinigHoldAlmostFull());

            //TODO добавить логику реварпа на другой белт, если общее количество целей для лока меньше 2

            //TODO у орки 2 отсека для руды, нужно добавить проверку на заполненность второго отсека
            //TODO для орки так же добавить проверку инвентаря на наличие руды и так же выбрасо ее в ангар станции
            if (isOreHoldFull()) {
                //TODO УБРАТЬ ПОСЛЕ ТЕСТА, ДРОП КОНТОВ НУЖЕН ТОЛЬКО ДЛЯ ОБЫЧНЫХ ЛОПАТ
//                jettisonExecumer();
                Sleep.sleep(200, 300);
                returnDrones();
                //TODO добавить команду варпа на станцию или сброса контейнера, для орки это варп на станцию
            } else {

                if (getFleetConstants().getAvailableContainers() > 0) {
                    //TODO
                    takeContainers();
                }

                //TODO Сначала нужно раздать бонусы, но у Орки это заряды, поэтому нужно лишь 1 раз нажать кнопку и все

                mine();
            }

        }

    }

    private void takeContainers() {
        //TODO так же вся логика выполнятся, только если орка на белте, то есть отсекаются все стаутсы, которые не относятся к положению на белте
        //TODO реализовать логику переключения на вкладку с контейнерами и сброса их содержимого в отсеки
        if (isInSpace() && isOnBelt() || Constants.ON_BELT_STATES.contains(getState())) {



        }
    }

    //Орка может копать только дронами, вся логика построена лишь на использовании дронов
    //TODO орке нужно еще поднимать конты своих лопат, подумать как модно организовать майнинг и подбор контейнеров
    @Override
    public void mine() {
        setLockedTargets(getComparePixels().numberOfLockedTargetsWithCoordinates());

        if (getAvailableTargetsCloser10km() == 0) {
            setAvailableTargetsCloser10km(getComparePixels().numberOfRowsCloserThan10km());
        }

        if (getAvailableTargetsFurther10km() == 0) {
            setAvailableTargetsFurther10km(getComparePixels().numberOfRowsFurtherThan10km());
        }

        if (getAvailableTargetsCloser10km() < 2 && getAvailableTargetsFurther10km() > 2) {
            align();
            setSleep(10000L);
            setState(State.ON_BELT_ALIGNING);
            return;
        }

        var lockedTargets = getComparePixels().numberOfLockedTargets();
        if (lockedTargets < 2) {
            lockTarget(getAvailableTargetsCloser10km(), 4 - lockedTargets);
        }

        var dronesCheck = getComparePixels().checkDrones();

        if (!dronesCheck.isDronesMining() && !dronesCheck.isDronesIdle() && !dronesCheck.isDronesReturning()) {
            launchDrones();
        }

        //TODO добавить алгоритм поиска залоченной цели, которая не будет КОНТЕЙНЕРОМ, так как орка еще подбирает конты
        if (!isAreDronesMining() && !isAreDronesReturning()) {
            getKeyBoardPress().pressKey(KeyEvent.VK_F);
            setState(State.ON_BELT_DRONES_MINING);
            //TODO уточнить тайминги пробуждения для копания, для орки можно придумать алгоритм лока целей снизу списка, чтобы не пересекаться с обычными лопатами
            setSleep(90000L);
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


    private void checkAndOpenOreAndItemHolds() {
        setOreHoldOpen(getComparePixels().isMiningHoldOpen());
        setInventoryOpen(getComparePixels().isInventoryOpen());

        if (!isOreHoldOpen()) {
            doOpenFolder(() -> getComparePixels().isMiningHoldOpen(), () -> getKeyBoardPress().openExecumerMiningHold(), "Mining hold");
            Sleep.sleep(300, 400);
        }

        if (!isInventoryOpen()) {
            doOpenFolder(() -> getComparePixels().isInventoryOpen(), () -> getKeyBoardPress().openInventoryHold(), "Inventory hold");
        }
    }
    //TODO добавить метод опроса обычных майнеров, для выяснения их текущего положеня
}
