package root.miner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import root.enums.*;
import root.functions.*;
import root.indicator.Coordinate;
import root.indicator.Rectangle;

import java.awt.event.KeyEvent;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class Miner implements Mine {

    private final WindowName windowName;
    private final WindowActivate windowActivate;
    private final ComparePixels comparePixels;
    private final Click click;
    private final Role role;
    private final KeyBoardPress keyBoardPress;
    private final FleetConstants fleetConstants;

    private State state;
    private boolean isInSpace;
    private boolean dronesLaunched;
    private boolean isInInWarp;
    private boolean isInInStation;
    private boolean isOreHoldOpen;
    private boolean isItemHangarOpen;
    private boolean isFirstMinerActive;
    private boolean isSecondMinerActive;
    private boolean areTargetsLocked;
    private boolean isOreHoldEmpty;
    private boolean isOreHoldFull;
    private boolean isOnBelt;
    private boolean isLocationPanelOpen;
    private List<Coordinate> lockedTargets;
    private int availableTargetsToLock;
    private Long awakeMoment = 0L;
    //Текущее порядковое значение белта окна
    private int currentBelt = 1;
    //Глобальное порядковое значение белта
    //TODO добавить ограничение по максимальному количеству белтов
    private int globalBelt = 1;
    private int availableTargetsCloser10km = 0;
    private int availableTargetsFurther10km = 0;
    private int numberOfRowsInMiningHold = 0;

    public abstract void collectState();

    @Override
    public void setSleep(Long milSec) {
        awakeMoment = System.currentTimeMillis() + milSec;
    }

    @Override
    public void warpOnBelt() {

        if (currentBelt < globalBelt) {
            currentBelt = globalBelt;
        }

        var rectangleToWarp = Constants.LOCATION_PANEL_FIRST_BOOKMARK_COORDINATE;

        int xRmcTop = rectangleToWarp.getTOP_LEFT_ANGLE().getPosX();
        int xRmcBot = rectangleToWarp.getBOTTOM_RIGHT_ANGLE().getPosX();

        int yRmcTop = rectangleToWarp.getTOP_LEFT_ANGLE().getPosY() + (currentBelt - 1) * Constants.BOOK_PANEL_Y_AXIS_BIAS;
        int yRmcBot = rectangleToWarp.getBOTTOM_RIGHT_ANGLE().getPosY() + (currentBelt - 1) * Constants.BOOK_PANEL_Y_AXIS_BIAS;

        var leftClickCoordinate = DefineCoordinate.defineCoordinate(new root.indicator.Rectangle(new Coordinate(xRmcTop, yRmcTop), new Coordinate(xRmcBot, yRmcBot)));

        int xLmcTop = leftClickCoordinate.getPosX() + Constants.BOOK_PANEL_X_AXIS_MIN_BIAS_FOR_LEFT_CLICK;
        int xLmcBot = DefineCoordinate.rnd(xLmcTop, xLmcTop + Constants.BOOK_PANEL_X_AXIS_MAX_BIAS_FOR_LEFT_CLICK);

        int yLmcTop = leftClickCoordinate.getPosY() + Constants.BOOK_PANEL_Y_AXIS_MIN_BIAS_FOR_LEFT_CLICK;
        int yLmcBot = DefineCoordinate.rnd(xLmcTop, yLmcTop + Constants.BOOK_PANEL_Y_AXIS_MAX_BIAS_FOR_LEFT_CLICK);

        click.doClick(MouseButton.RIGHT, leftClickCoordinate, DefineCoordinate.rnd(1500, 1750));
        Sleep.sleep(150, 250);
        click.doClick(MouseButton.LEFT, DefineCoordinate.defineCoordinate(new Rectangle(new Coordinate(xLmcTop, yLmcTop), new Coordinate(xLmcBot, yLmcBot))), DefineCoordinate.rnd(200, 300));
        setState(State.IN_WARP);
    }

    private void deactivateStripMiners() {
        System.out.println("Деактивирую стрипы");
        //TODO вынести это в константы, уточнить количество попыток
        for (int i = 0; i < 5; i++) {

            if (isFirstMinerActive()) {
                keyBoardPress.pressKey(KeyEvent.VK_F1);
            }

            if (isSecondMinerActive()) {
                keyBoardPress.pressKey(KeyEvent.VK_F2);
            }

            Sleep.sleep(400, 500);
            setFirstMinerActive(getComparePixels().checkStripMinerActive(IndicatorColour.GREEN_POINT_MINER_1));
            setSecondMinerActive(getComparePixels().checkStripMinerActive(IndicatorColour.GREEN_POINT_MINER_2));

            if (!isFirstMinerActive() && !isSecondMinerActive()) {
                return;
            }
        }
    }

    //TODO, подумать над возможностью сделать 1 метод для активации/деактивации стрипов
    private void activateStripMiners() {
        //TODO вынести это в константы, уточнить количество попыток
        System.out.println("Активирую стрипы");
        for (int i = 0; i < 5; i++) {

            if (!isFirstMinerActive()) {
                keyBoardPress.pressKey(KeyEvent.VK_F1);
            }

            if (!isSecondMinerActive()) {
                keyBoardPress.pressKey(KeyEvent.VK_F2);
            }

            Sleep.sleep(400, 600);
            setFirstMinerActive(getComparePixels().checkStripMinerActive(IndicatorColour.GREEN_POINT_MINER_1));
            setSecondMinerActive(getComparePixels().checkStripMinerActive(IndicatorColour.GREEN_POINT_MINER_2));

            if (isFirstMinerActive() && isSecondMinerActive()) {
                return;
            }
        }
    }

    public void lockTarget(int numberOfAvailableTargets, int numberTargetsToLock) {

        List<Integer> rowsToClick = new ArrayList<>();

        while (rowsToClick.size() < numberTargetsToLock) {
            int randomIndex = DefineCoordinate.rnd(0, numberOfAvailableTargets);

            if (!rowsToClick.contains(randomIndex)) {
                rowsToClick.add(randomIndex);
            }
        }

        keyBoardPress.pressKeyWithoutRelease(KeyEvent.VK_CONTROL);
        for (int i = 0; i < numberTargetsToLock; i++) {
            var clickPos = DefineCoordinate.defineCoordinate(Area.OVERVIEW_FIRST_ROW, 0, Constants.OVERVIEW_Y_AXIS_BIAS * rowsToClick.get(i));
            click.doClick(MouseButton.LEFT, clickPos, DefineCoordinate.rnd(900, 1200));
            Sleep.sleep(200, 400);
        }
        keyBoardPress.keyRelease(KeyEvent.VK_CONTROL);

    }

    public void align() {
        keyBoardPress.pressKeyWithoutRelease(KeyEvent.VK_Q);
        click.doClick(MouseButton.LEFT, DefineCoordinate.defineCoordinate(Area.OVERVIEW_FIRST_ROW.getRectangle()), DefineCoordinate.rnd(900, 1200));
        Sleep.sleep(200, 400);
        keyBoardPress.keyRelease(KeyEvent.VK_CONTROL);
    }

    @Override
    public void launchDrones() {
        click.doDragAndDrop(DefineCoordinate.defineCoordinate(Constants.DRONE_HOLD_DRONE_IN_A_BAY_LINE), DefineCoordinate.defineCoordinate(Constants.DRONE_HOLD_DRAG_OUT), DefineCoordinate.rnd(1400, 1700));
        keyBoardPress.launchDrones();
    }

    //TODO подумать, как можно взвращаться дронов через драг и дроп
    @Override
    public void returnDrones() {
        click.doClick(MouseButton.LEFT, DefineCoordinate.defineCoordinate(Area.LEFT_CLICK_BEFORE_DRONE_LAUNCH.getRectangle()), DefineCoordinate.rnd(900, 1200));
        keyBoardPress.returnDrones();
    }

    @Override
    public void jettisonExecumer() {

        var countRows = comparePixels.numberRowsInMiningHold();

        //Если строка 1, то просто жмем jettison
        var rectangleRMBClick = DefineCoordinate.defineCoordinate(Constants.FIRS_ROW_EXECUMER_MINING_HOLD);
        if (countRows == 1) {
            var lcmX = rectangleRMBClick.getPosX() + DefineCoordinate.rnd(Constants.EXECUMER_MINING_HOLD_JETTISON_X_ASIS_MIN_BIAS, Constants.EXECUMER_MINING_HOLD_JETTISON_X_ASIS_MAX_BIAS);
            var lcmY = rectangleRMBClick.getPosY() + DefineCoordinate.rnd(Constants.EXECUMER_MINING_HOLD_JETTISON_Y_ASIS_MIN_BIAS, Constants.EXECUMER_MINING_HOLD_JETTISON_Y_ASIS_MAX_BIAS);
            var lcmCoordinate = new Coordinate(lcmX, lcmY);
            click.doClick(MouseButton.RIGHT, rectangleRMBClick, DefineCoordinate.rnd(1500, 1750));
            Sleep.sleep(200, 400);
            click.doClick(MouseButton.LEFT, lcmCoordinate, DefineCoordinate.rnd(1500, 1750));
        }
        //Сначала нужно выделить все строки, а уже потом нажать jettison
        else {
            click.doClick(MouseButton.RIGHT, rectangleRMBClick, DefineCoordinate.rnd(1500, 1750));
            Sleep.sleep(400, 600);
            var lcmX = rectangleRMBClick.getPosX() + DefineCoordinate.rnd(Constants.EXECUMER_MINING_HOLD_SELECT_ALL_X_ASIS_MIN_BIAS, Constants.EXECUMER_MINING_HOLD_SELECT_ALL_X_ASIS_MAX_BIAS);
            var lcmY = rectangleRMBClick.getPosY() + DefineCoordinate.rnd(Constants.EXECUMER_MINING_HOLD_SELECT_ALL_Y_ASIS_MIN_BIAS, Constants.EXECUMER_MINING_HOLD_SELECT_ALL_Y_ASIS_MAX_BIAS);
            var lcmCoordinate = new Coordinate(lcmX, lcmY);
            click.doClick(MouseButton.LEFT, lcmCoordinate, DefineCoordinate.rnd(1500, 1750));
            Sleep.sleep(400, 600);

            var rectangleRMBClickSecond = DefineCoordinate.defineCoordinate(Constants.FIRS_ROW_EXECUMER_MINING_HOLD);
            var lcmXSecond = rectangleRMBClickSecond.getPosX() + DefineCoordinate.rnd(Constants.EXECUMER_MINING_HOLD_JETTISON_X_ASIS_MIN_BIAS, Constants.EXECUMER_MINING_HOLD_JETTISON_X_ASIS_MAX_BIAS);
            var lcmYSecond = rectangleRMBClickSecond.getPosY() + DefineCoordinate.rnd(Constants.EXECUMER_MINING_HOLD_SELECT_ALL_JETTISON_Y_ASIS_MIN_BIAS, Constants.EXECUMER_MINING_HOLD_SELECT_ALL_JETTISON_Y_ASIS_MAX_BIAS);
            var lcmCoordinateSecond = new Coordinate(lcmXSecond, lcmYSecond);
            click.doClick(MouseButton.RIGHT, rectangleRMBClickSecond, DefineCoordinate.rnd(1500, 1750));
            Sleep.sleep(400, 500);
            click.doClick(MouseButton.LEFT, lcmCoordinateSecond, DefineCoordinate.rnd(1500, 1750));

        }
        Sleep.sleep(800, 1200);
    }

    @Override
    public void mine() {

        setLockedTargets(comparePixels.numberOfLockedTargetsWithCoordinates());

        if(getAvailableTargetsCloser10km() == 0){
            setAvailableTargetsCloser10km(comparePixels.numberOfRowsCloserThan10km());
        }

        if(getAvailableTargetsFurther10km() == 0){
            setAvailableTargetsFurther10km(comparePixels.numberOfRowsFurtherThan10km());
        }

        //TODO, возможно, с оркой это правило вообще не нужно, но, тогда нужно переделывать логику лока, добавлять метод, который просто лочит первые N астероидов
        if (availableTargetsCloser10km < 2 && availableTargetsFurther10km > 2) {
            align();
            setSleep(10000L);
            setState(State.ON_BELT_ALIGNING);
            return;
        }

        var lockedTargets = comparePixels.numberOfLockedTargets();
        if (lockedTargets < 2) {
            lockTarget(availableTargetsCloser10km, 4 - lockedTargets);
        }

        setFirstMinerActive(getComparePixels().checkStripMinerActive(IndicatorColour.GREEN_POINT_MINER_1));
        setSecondMinerActive(getComparePixels().checkStripMinerActive(IndicatorColour.GREEN_POINT_MINER_2));

        if (getState() != State.ON_BELT_MINING_STRIP_ACTIVATED) {

            setState(State.ON_BELT_MINING_STRIP_ACTIVATED);
            if (!isFirstMinerActive() || !isSecondMinerActive()) {

                activateStripMiners();

            } else {
                deactivateStripMiners();
                activateStripMiners();
            }
        }

        setSleep(25000L);
    }

    @Override
    public void doUndock() {
        var clickPos = DefineCoordinate.defineCoordinate(Area.UNDOCK_BUTTON.getRectangle());
        click.doClick(MouseButton.LEFT, clickPos, DefineCoordinate.rnd(1500, 1750));
        Sleep.sleep(150, 250);
    }

    @Override
    public void unloadExecumerMinigHold() {

        var countRows = comparePixels.numberRowsInMiningHold();

        if (countRows == 1) {
            click.doDragAndDrop(DefineCoordinate.defineCoordinate(Constants.FIRS_ROW_EXECUMER_MINING_HOLD), DefineCoordinate.defineCoordinate(Constants.ITEM_HANGAR_DROP), DefineCoordinate.rnd(800, 1000));
        } else {
            var rectangleRMBClick = DefineCoordinate.defineCoordinate(Constants.FIRS_ROW_EXECUMER_MINING_HOLD);
            click.doClick(MouseButton.RIGHT, rectangleRMBClick, DefineCoordinate.rnd(1500, 1750));
            Sleep.sleep(300, 500);
            var lcmX = rectangleRMBClick.getPosX() + DefineCoordinate.rnd(Constants.EXECUMER_MINING_HOLD_SELECT_ALL_X_ASIS_MIN_BIAS, Constants.EXECUMER_MINING_HOLD_SELECT_ALL_X_ASIS_MAX_BIAS);
            var lcmY = rectangleRMBClick.getPosY() + DefineCoordinate.rnd(Constants.EXECUMER_MINING_HOLD_SELECT_ALL_Y_ASIS_MIN_BIAS, Constants.EXECUMER_MINING_HOLD_SELECT_ALL_Y_ASIS_MAX_BIAS);
            var lcmCoordinate = new Coordinate(lcmX, lcmY);
            click.doClick(MouseButton.LEFT, lcmCoordinate, DefineCoordinate.rnd(1500, 1750));
            Sleep.sleep(300, 500);

            var dragAndDropFrom = DefineCoordinate.defineCoordinate(Constants.FIRS_ROW_EXECUMER_MINING_HOLD);

            var posYCompensation = Constants.FIRS_ROW_EXECUMER_MINING_HOLD.getTOP_LEFT_ANGLE().getPosY() - dragAndDropFrom.getPosY();

            var yBias = DefineCoordinate.rnd(dragAndDropFrom.getPosY(), (dragAndDropFrom.getPosY() - posYCompensation) + Constants.EXECUMER_MINING_HOLD_FIRST_ROW_t_Y_ASIS_BIAS * (countRows - 1));

            click.doDragAndDrop(new Coordinate(dragAndDropFrom.getPosX(), yBias), DefineCoordinate.defineCoordinate(Constants.ITEM_HANGAR_DROP), DefineCoordinate.rnd(800, 1000));

        }
        numberOfRowsInMiningHold = 0;
        Sleep.sleep(800, 1200);
    }

    public void printState() {
        System.out.println("===============================");
        System.out.printf("Статус - %s. ", this.getState());
        System.out.printf("isInSpace - %s. ", this.isInSpace());
        System.out.printf("isInInWarp - %s. ", this.isInInWarp());
        System.out.printf("isInInStation - %s. ", this.isInInStation());
        System.out.printf("isOreHoldOpen - %s. ", this.isOreHoldOpen());
        System.out.printf("isItemHangarOpen - %s. ", this.isItemHangarOpen());
        System.out.printf("isOnBelt - %s. ", this.isOnBelt());
        System.out.printf("isLocationPanelOpen - %s. ", this.isLocationPanelOpen());
        System.out.printf("currentBelt - %s. ", this.getCurrentBelt());
        System.out.printf("globalBelt - %s. ", this.getGlobalBelt());
        System.out.printf("execumerMiningHoldFull - %s. ", this.isOreHoldFull());
        System.out.printf("execumerMiningHoldEmpty - %s. ", this.isOreHoldEmpty());
        System.out.println("===============================");
    }

    public boolean doOpenFolder(Supplier<Boolean> checker, Supplier<Void> keyPresser, String title) {
        //TODO вынести число попыток в константу, по возможности уточнить количество
        for (int i = 0; i < 4; i++) {

            if (checker.get()) {
                return true;
            }
            keyPresser.get();
            Sleep.sleep(300, 400);
        }

        System.out.printf("Не удалось выполнить действие по открытию окна : %s%n", title);
        return false;
    }
}
