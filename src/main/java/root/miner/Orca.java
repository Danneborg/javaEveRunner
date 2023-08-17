package root.miner;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import root.enums.*;
import root.functions.*;
import root.indicator.Coordinate;

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
    //TODO добавить орке дополнительный таймер или сделать логику, что даже если она спит во время майнинга, то она все равно может проверять есть новые конты и собирать их
    private boolean isInventoryFull;
    private boolean isInventoryEmpty;
    private boolean isVeldsparTabActive;
    private boolean isContainerTabActive;

    private List<Miner> minerList = new ArrayList<>();

    public Orca(WindowName windowName, WindowActivate windowActivate, ComparePixels comparePixels, Click click, KeyBoardPress keyBoardPress, FleetConstants fleetConstants) {
        super(windowName, windowActivate, comparePixels, click, Role.ORCA, keyBoardPress, fleetConstants);
    }

    @Override
    public void printState(){
        System.out.println("===============================");
        super.printState();
        System.out.printf("%nisInventoryEmpty - %s. ", this.isInventoryEmpty());
        System.out.println("%n===============================");
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

            //TODO добавить опрос всех майнеров на предмет статуса и последующей раздачей команд каждому окну
            if(CollectionUtils.isNotEmpty(minerList)){
                for (var singleMiner : minerList) {
                    singleMiner.act();
                }
            }

        }

    }

    //TODO реализовать
    private void orcaAct() {

        if (getFleetConstants().getAvailableContainers() > 0) {
            //TODO
            activateWindow();
            collectState();
            takeContainers();
        }

        //Время копать еще не пришло
        if (getAwakeMoment() > System.currentTimeMillis()) {
            return;
        }

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

            setInventoryFull(getComparePixels().isInventoryAlmostFull());
            setInventoryEmpty(getComparePixels().isInventoryEmpty());

            if (isOreHoldEmpty() && getNumberOfRowsInMiningHold() == 0 && isInventoryEmpty()) {
                setState(State.UNDOCKING);
                doUndock();
                setSleep(15000L);
                return;
            }

            if (isOreHoldFull() || !isOreHoldEmpty() || getNumberOfRowsInMiningHold() != 0 || !isInventoryEmpty()) {
                setState(State.IN_STATION_UNLOADING);
                if (isOreHoldFull() || !isOreHoldEmpty() || getNumberOfRowsInMiningHold() != 0) {
                    unloadExecumerMinigHold();
                    if (isInventoryEmpty()) {
                        return;
                    }
                }
                if (!isInventoryEmpty()) {
                    unloadInventory();
                }
                return;
            }

        }

        setOnBelt(getComparePixels().isOnBelt());
        setVeldsparTabActive(getComparePixels().isVeldsparTabActive());

        if (isInSpace() && !isOnBelt()) {

            if(!isVeldsparTabActive()){
                getClick().doClick(MouseButton.LEFT, DefineCoordinate.defineCoordinate(Constants.VELDSPAR_TAB), DefineCoordinate.rnd(1200, 1300));
            }

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

            if (getState() == null || !Constants.ON_BELT_STATES.contains(getState())) {
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
            setInventoryFull(getComparePixels().isInventoryAlmostFull());

            //TODO добавить логику реварпа на другой белт, если общее количество целей для лока меньше 2

            if (isOreHoldFull() && isInventoryFull()) {
                returnDrones();
                //TODO добавить команду варпа на станцию или сброса контейнера, для орки это варп на станцию
            } else {
                setVeldsparTabActive(getComparePixels().isVeldsparTabActive());
                if(!isVeldsparTabActive()){
                    getClick().doClick(MouseButton.LEFT, DefineCoordinate.defineCoordinate(Constants.VELDSPAR_TAB), DefineCoordinate.rnd(1200, 1300));
                }
                mine();
            }

        }

    }

    private void unloadInventory(){

        getKeyBoardPress().pressKeyWithoutRelease(KeyEvent.VK_CONTROL);
        getClick().doClick(MouseButton.LEFT, DefineCoordinate.defineCoordinate(Constants.ORCA_INVENTORY_FIRST_ROW), DefineCoordinate.rnd(1000,1200));
        Sleep.sleep(50,100);
        getClick().doClick(MouseButton.LEFT, DefineCoordinate.defineCoordinate(Constants.ORCA_INVENTORY_SECOND_ROW), DefineCoordinate.rnd(800,900));
        Sleep.sleep(50,80);
        getKeyBoardPress().keyRelease(KeyEvent.VK_CONTROL);
        var coordinateRMBC = DefineCoordinate.defineCoordinate(Constants.ORCA_INVENTORY_FIRST_ROW);
        getClick().doClick(MouseButton.RIGHT, coordinateRMBC, DefineCoordinate.rnd(800,900));
        Sleep.sleep(400,500);
        int xLmcTop = coordinateRMBC.getPosX() + Constants.INVENTORY_INVERT_SELECTION_X_MIN_BIAS;
        int xLmcBot = DefineCoordinate.rnd(xLmcTop, xLmcTop + Constants.INVENTORY_INVERT_SELECTION_X_MAX_BIAS);

        var topLeftAngel= new Coordinate(xLmcTop, Constants.INVENTORY_INVERT_SELECTION_Y_MIN);
        var botRightAngel= new Coordinate(xLmcBot, Constants.INVENTORY_INVERT_SELECTION_Y_MAX);

        getClick().doClick(MouseButton.LEFT, DefineCoordinate.defineCoordinate(topLeftAngel, botRightAngel), DefineCoordinate.rnd(800,900));

        Sleep.sleep(100,200);
        getClick().doDragAndDrop(DefineCoordinate.defineCoordinate(Constants.ORCA_INVENTORY_THIRD_ROW), DefineCoordinate.defineCoordinate(Constants.ITEM_HANGAR_DROP), DefineCoordinate.rnd(1400, 1700));
    }

    private void takeContainers() {
        //TODO так же вся логика выполнятся, только если орка на белте, то есть отсекаются все стаутсы, которые не относятся к положению на белте
        //TODO реализовать логику переключения на вкладку с контейнерами и сброса их содержимого в отсеки
        if (isInSpace() && isOnBelt() || Constants.ON_BELT_STATES.contains(getState())) {

            setContainerTabActive(getComparePixels().isContainerTabActive());
            if(!isContainerTabActive()){
                getClick().doClick(MouseButton.LEFT, DefineCoordinate.defineCoordinate(Constants.CONTAINER_TAB), DefineCoordinate.rnd(1200, 1300));
            }


            if(!isOreHoldFull()){

            }

            if(!isInventoryFull()){

            }

            //TODO, в конце цикла нужно проверить, что оба отсека еще имею место, если нет, то варпать на станцию
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
        //Астероид у орки лочится примерно 12 секунд, она не главная лопата, поэтому, нет смысла ждать, пока она долочит 1ую цель в виде астероида
        if (lockedTargets < 2) {
            lockTarget(getAvailableTargetsCloser10km(), 4 - lockedTargets);
        }

        var dronesCheck = getComparePixels().checkDrones();

        if (!dronesCheck.isDronesMining() && !dronesCheck.isDronesIdle() && !dronesCheck.isDronesReturning()) {
            launchDrones();
        }

        //TODO добавить алгоритм поиска залоченной цели, которая не будет КОНТЕЙНЕРОМ, так как орка еще подбирает конты
        if (!isAreDronesMining() && !isAreDronesReturning()) {
            var currentTargets = getComparePixels().numberOfLockedTargets();
            if(currentTargets > 0){
                getKeyBoardPress().pressKey(KeyEvent.VK_F, 3);
                setState(State.ON_BELT_DRONES_MINING);
                //TODO уточнить тайминги пробуждения для копания, для орки можно придумать алгоритм лока целей снизу списка, чтобы не пересекаться с обычными лопатами
                setSleep(90000L);
            }else {
                setSleep(15000L);
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
