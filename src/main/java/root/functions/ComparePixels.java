package root.functions;

import root.enums.Constants;
import root.enums.IndicatorColour;
import root.indicator.Coordinate;
import root.indicator.DroneChecks;

import java.awt.*;
import java.util.ArrayList;

public class ComparePixels {

    private final Robot robot;
    //TODO вынести это все в статические константы приложения
    private final int Y_POS_LOCK_TARGET = IndicatorColour.FIRST_LOCKED_TARGET_BRACKET_ACTIVE.getCoordinate().getPosY();
    private final Color NOT_ACTIVE_STANDARD_LOCK_TARGET = new Color(IndicatorColour.FIRST_LOCKED_TARGET_BRACKET_NOT_ACTIVE.getColour());
    private final Color ACTIVE_STANDARD_LOCK_TARGET = new Color(IndicatorColour.FIRST_LOCKED_TARGET_BRACKET_ACTIVE.getColour());

    private final int Y_POS_COUNT_ROWS_OVERVIEW = IndicatorColour.FIRST_LETTER_M_IN_OVERVIEW.getCoordinate().getPosY();
    private final Color STANDARD_M_OVERVIEW = new Color(IndicatorColour.FIRST_LETTER_M_IN_OVERVIEW.getColour());
    private final Color STANDARD_K_OVERVIEW = new Color(IndicatorColour.FIRST_LETTER_K_IN_OVERVIEW.getColour());
    private final Color MINING_HOLD_FIRST_ROW_t = new Color(IndicatorColour.MINING_HOLD_FIRST_ROW_t.getColour());

    public ComparePixels() throws AWTException {
        robot = new Robot();
    }

    public boolean comparePixels(IndicatorColour indicatorColour) {
        Color color1 = new Color(indicatorColour.getColour());
        Color color2 = robot.getPixelColor(indicatorColour.getCoordinate().getPosX(), indicatorColour.getCoordinate().getPosY());

        // Calculate the difference between RGB values
        int rDiff = Math.abs(color1.getRed() - color2.getRed());
        int gDiff = Math.abs(color1.getGreen() - color2.getGreen());
        int bDiff = Math.abs(color1.getBlue() - color2.getBlue());

        // Check if the differences are within the threshold
        return rDiff <= indicatorColour.getShadeDeviation() && gDiff <= indicatorColour.getShadeDeviation()
                && bDiff <= indicatorColour.getShadeDeviation();
    }

    public java.util.List<Coordinate> numberOfLockedTargetsWithCoordinates() {

        java.util.List<Coordinate> coordinates = new ArrayList<>();
        var localYpos = Y_POS_LOCK_TARGET;
        for (int i = 0; i < Constants.NUMBER_OF_OVERVIEW_ROWS; i++) {

            var notActiveSample = robot.getPixelColor(IndicatorColour.FIRST_LOCKED_TARGET_BRACKET_NOT_ACTIVE.getCoordinate().getPosX(), localYpos);
            var activeSample = robot.getPixelColor(IndicatorColour.FIRST_LOCKED_TARGET_BRACKET_ACTIVE.getCoordinate().getPosX(), localYpos);

            if (comparePixels(NOT_ACTIVE_STANDARD_LOCK_TARGET, notActiveSample, IndicatorColour.FIRST_LOCKED_TARGET_BRACKET_NOT_ACTIVE.getShadeDeviation())
                    || comparePixels(ACTIVE_STANDARD_LOCK_TARGET, activeSample, IndicatorColour.FIRST_LOCKED_TARGET_BRACKET_ACTIVE.getShadeDeviation())) {

                coordinates.add(new Coordinate(IndicatorColour.FIRST_LOCKED_TARGET_BRACKET_NOT_ACTIVE.getCoordinate().getPosX(), localYpos));
            }
            localYpos += Constants.OVERVIEW_Y_AXIS_BIAS;
        }

        return coordinates;
    }

    //TODO подумать как можно унифицировать метод передавай X и Y координаты
    //TODO возможно, нужно сделать метод, который возвращает координаты залоченных целей, чтобы рандомно их них выбирать одну
    public int numberOfLockedTargets() {

        var lockedTargets = 0;
        var localYpos = Y_POS_LOCK_TARGET;
        for (int i = 0; i < Constants.NUMBER_OF_OVERVIEW_ROWS; i++) {

            var notActiveSample = robot.getPixelColor(IndicatorColour.FIRST_LOCKED_TARGET_BRACKET_NOT_ACTIVE.getCoordinate().getPosX(), localYpos);
            var activeSample = robot.getPixelColor(IndicatorColour.FIRST_LOCKED_TARGET_BRACKET_ACTIVE.getCoordinate().getPosX(), localYpos);

            if (comparePixels(NOT_ACTIVE_STANDARD_LOCK_TARGET, notActiveSample, IndicatorColour.FIRST_LOCKED_TARGET_BRACKET_NOT_ACTIVE.getShadeDeviation())
                    || comparePixels(ACTIVE_STANDARD_LOCK_TARGET, activeSample, IndicatorColour.FIRST_LOCKED_TARGET_BRACKET_ACTIVE.getShadeDeviation())) {
                lockedTargets++;
            }
            localYpos += Constants.OVERVIEW_Y_AXIS_BIAS;
        }

        return lockedTargets;
    }

    public int numberRowsInMiningHold() {

        var numberOfRows = 0;
        var localYPos = IndicatorColour.MINING_HOLD_FIRST_ROW_t.getCoordinate().getPosY();

        for (int i = 0; i < 5; i++) {

            var rowExists = robot.getPixelColor(IndicatorColour.MINING_HOLD_FIRST_ROW_t.getCoordinate().getPosX(), localYPos);

            if (comparePixels(MINING_HOLD_FIRST_ROW_t, rowExists, IndicatorColour.FIRST_LETTER_M_IN_OVERVIEW.getShadeDeviation())) {
                numberOfRows++;
                localYPos += Constants.EXECUMER_MINING_HOLD_FIRST_ROW_t_Y_ASIS_BIAS;
            } else {
                break;
            }
        }

        return numberOfRows;
    }

    public int numberOfRowsFurtherThan10km() {

        var numberOfRows = 0;
        var localYPos = Y_POS_COUNT_ROWS_OVERVIEW;

        for (int i = 0; i < Constants.NUMBER_OF_OVERVIEW_ROWS; i++) {

            var mSample = robot.getPixelColor(IndicatorColour.FIRST_LETTER_M_IN_OVERVIEW.getCoordinate().getPosX(), localYPos);
            var kSample = robot.getPixelColor(IndicatorColour.FIRST_LETTER_K_IN_OVERVIEW.getCoordinate().getPosX(), localYPos);

            if (comparePixels(STANDARD_M_OVERVIEW, mSample, IndicatorColour.FIRST_LETTER_M_IN_OVERVIEW.getShadeDeviation())
                    && comparePixels(STANDARD_K_OVERVIEW, kSample, IndicatorColour.FIRST_LETTER_K_IN_OVERVIEW.getShadeDeviation())) {
                numberOfRows++;
            }
            localYPos += Constants.OVERVIEW_Y_AXIS_BIAS;
        }

        return numberOfRows;
    }

    public int numberOfRowsCloserThan10km() {

        var numberOfRows = 0;
        var localYPos = Y_POS_COUNT_ROWS_OVERVIEW;

        for (int i = 0; i < Constants.NUMBER_OF_OVERVIEW_ROWS; i++) {

            var mSample = robot.getPixelColor(IndicatorColour.FIRST_LETTER_M_IN_OVERVIEW.getCoordinate().getPosX(), localYPos);
            var kSample = robot.getPixelColor(IndicatorColour.FIRST_LETTER_K_IN_OVERVIEW.getCoordinate().getPosX(), localYPos);

            if (comparePixels(STANDARD_M_OVERVIEW, mSample, IndicatorColour.FIRST_LETTER_M_IN_OVERVIEW.getShadeDeviation())
                    && !comparePixels(STANDARD_K_OVERVIEW, kSample, IndicatorColour.FIRST_LETTER_K_IN_OVERVIEW.getShadeDeviation())) {
                numberOfRows++;
            }
            localYPos += Constants.OVERVIEW_Y_AXIS_BIAS;
        }

        return numberOfRows;
    }

    public boolean comparePixels(Color c1, Color c2, int shadeDeviation) {
        // Calculate the difference between RGB values
        int rDiff = Math.abs(c1.getRed() - c2.getRed());
        int gDiff = Math.abs(c1.getGreen() - c2.getGreen());
        int bDiff = Math.abs(c1.getBlue() - c2.getBlue());

        // Check if the differences are within the threshold
        return rDiff <= shadeDeviation && gDiff <= shadeDeviation && bDiff <= shadeDeviation;
    }

    public boolean isInSpace() {
        return this.comparePixels(IndicatorColour.GREEN_SPACE_CIRCLE)
                && this.comparePixels(IndicatorColour.TOP_RIGHT_TARGETS_LOCK)
                && this.comparePixels(IndicatorColour.MIDDLE_DOT_TOP_RIGHT_OVERVIEW)
                && this.comparePixels(IndicatorColour.PLUS_MARK_NAV_PANEL)
                ;
    }

    public boolean isInStation() {
        System.out.println(this.comparePixels(IndicatorColour.MIDDLE_DOT_TOP_RIGHT_STATION_INFO));

        return (this.comparePixels(IndicatorColour.BLUE_LEFT_TOP_UNDOCK_BUTTON_ALFA) || this.comparePixels(IndicatorColour.BLUE_LEFT_TOP_UNDOCK_BUTTON_OMEGA))
                && (this.comparePixels(IndicatorColour.BLUE_RIGHT_BOTTOM_UNDOCK_BUTTON_ALFA) || this.comparePixels(IndicatorColour.BLUE_RIGHT_BOTTOM_UNDOCK_BUTTON_OMEGA))
                && this.comparePixels(IndicatorColour.LETTER_U_UNDOCK_WORD)
                && this.comparePixels(IndicatorColour.LETTER_k_UNDOCK_WORD)
                && this.comparePixels(IndicatorColour.MIDDLE_DOT_TOP_RIGHT_STATION_INFO)
                && !this.comparePixels(IndicatorColour.GREEN_SPACE_CIRCLE)
                ;
    }

    public boolean isInWarp() {
        return this.comparePixels(IndicatorColour.WARP_DRIVE_WORD_LETTER_NAV_PANEL_W)
                && this.comparePixels(IndicatorColour.WARP_DRIVE_WORD_NAV_PANEL_LETTER_i)
                && this.comparePixels(IndicatorColour.WARP_DRIVE_WORD_NAV_PANEL_LETTER_g)
                ;
    }

    public boolean isItemHangarOpen() {
        return this.comparePixels(IndicatorColour.ITEM_HANGAR_LOCK)
                && this.comparePixels(IndicatorColour.ITEM_HANGAR_LETTER_t)
                && this.comparePixels(IndicatorColour.ITEM_HANGAR_LETTER_r)
                ;
    }

    public boolean isMiningHoldOpen() {
        return this.comparePixels(IndicatorColour.MINING_HOLD_LOCK)
                && this.comparePixels(IndicatorColour.MINING_HOLD_FIRST_LETTER_i)
                && this.comparePixels(IndicatorColour.MINING_HOLD_FIRST_LETTER_d)
                ;
    }

    public boolean isInventoryOpen() {
        return this.comparePixels(IndicatorColour.INVENTORY_HOLD_LOCK)
                && this.comparePixels(IndicatorColour.MINING_HOLD_CROSS_CENTER)
                && this.comparePixels(IndicatorColour.CONTEXT_MENU_WHITE_MIDDLE_DOT)
                ;
    }

    public boolean isExecumerMinigHoldAlmostFull() {
        return this.isMiningHoldOpen()
                && !this.comparePixels(IndicatorColour.MINING_HOLD_ALMOST_FULL)
                ;
    }

    public boolean isExecumerMinigHoldEmpty() {
        return this.isMiningHoldOpen()
                && this.comparePixels(IndicatorColour.MINING_HOLD_ALMOST_FULL)
                && this.comparePixels(IndicatorColour.MINING_HOLD_LESS_THAN_MIDDLE)
                ;
    }

    public boolean isInventoryAlmostFull() {
        return this.isInventoryOpen()
                && !this.comparePixels(IndicatorColour.INVENTORY_ALMOST_FULL)
                ;
    }

    public boolean isInventoryEmpty() {
        return this.isInventoryOpen()
                && this.comparePixels(IndicatorColour.INVENTORY_EMPTY)
                && !this.comparePixels(IndicatorColour.INVENTORY_THIRD_ROW_LETTER_i)
                ;
    }

    public boolean isOnBelt() {
        return this.comparePixels(IndicatorColour.FIRST_ROW_ASTEROID_ICON_PIXEL)
                && this.comparePixels(IndicatorColour.FIRST_ROW_WORD_SIZE_LETTER_m)
                && this.comparePixels(IndicatorColour.FIRST_LETTER_M_IN_OVERVIEW)
                ;
    }

    public boolean isLocationPanelOpen() {
        return this.comparePixels(IndicatorColour.TOP_LEFT_LOCATIONS_LOCK)
                && this.comparePixels(IndicatorColour.TOP_LEFT_MIDDLE_DOT_TOP_LOCATIONS)
                && this.comparePixels(IndicatorColour.TOP_LEFT_GREEN_EYE_LOCATIONS)
                ;
    }

    public boolean doesOreRowExist() {
        return this.comparePixels(IndicatorColour.MINING_HOLD_FIRST_ROW_t)
                ;
    }

    public boolean isVeldsparTabActive() {
        return this.comparePixels(IndicatorColour.VELDSPAR_TAB_LETTER_l)
                && this.comparePixels(IndicatorColour.VELDSPAR_TAB_LETTER_d)
                &&(this.comparePixels(IndicatorColour.VELDSPAR_TAB_UNDERSCORE_WHITE) || this.comparePixels(IndicatorColour.VELDSPAR_TAB_UNDERSCORE_BLUE))
                ;
    }

    public boolean isContainerTabActive() {
        return this.comparePixels(IndicatorColour.CONTAINER_TAB_LETTER_t)
                && this.comparePixels(IndicatorColour.CONTAINER_TAB_LETTER_i)
                &&(this.comparePixels(IndicatorColour.CONTAINER_TAB_UNDERSCORE_WHITE) || this.comparePixels(IndicatorColour.CONTAINER_TAB_UNDERSCORE_BLUE))
                ;
    }

    public boolean checkStripMinerActive(IndicatorColour indicatorColour) {
        //TODO уточнить количество циклов
        for (int i = 0; i < 200; i++) {

            if (this.comparePixels(indicatorColour)) {
                return true;
            }
            //TODO Уточнить время ожидания
            Sleep.sleep(2, 4);
        }
        return false;
    }

    //TODO возможно, понадобится отдельная проверка на каждый из статусов
    public DroneChecks checkDrones() {
        var dronesIdle = false;
        var dronesMining = false;
        var dronesReturning = false;

        var idleLocalYPos = IndicatorColour.MINING_DRONE_WORD_IDLE_LETTER_I.getCoordinate().getPosY();
        var returningLocalYPos = IndicatorColour.MINING_DRONE_WORD_RETURNING_LETTER_t.getCoordinate().getPosY();
        var miningLocalYPos = IndicatorColour.MINING_DRONE_WORD_MINING_LETTER_i.getCoordinate().getPosY();

        for (int i = 0; i <= Constants.MINING_DRONE_NUMBER_FOR_COUNT; i++) {

            var idleSample = robot.getPixelColor(IndicatorColour.MINING_DRONE_WORD_IDLE_LETTER_I.getCoordinate().getPosX(), idleLocalYPos);
            var returningSample = robot.getPixelColor(IndicatorColour.MINING_DRONE_WORD_RETURNING_LETTER_t.getCoordinate().getPosX(), returningLocalYPos);
            var miningSample = robot.getPixelColor(IndicatorColour.MINING_DRONE_WORD_MINING_LETTER_i.getCoordinate().getPosX(), miningLocalYPos);


            if (comparePixels(IndicatorColour.MINING_DRONE_WORD_IDLE_LETTER_I.getAwtColour(), idleSample, IndicatorColour.MINING_DRONE_WORD_IDLE_LETTER_I.getShadeDeviation())) {
                dronesIdle = true;
            }

            if (comparePixels(IndicatorColour.MINING_DRONE_WORD_RETURNING_LETTER_t.getAwtColour(), returningSample, IndicatorColour.MINING_DRONE_WORD_RETURNING_LETTER_t.getShadeDeviation())) {
                dronesReturning = true;
            }

            if (comparePixels(IndicatorColour.MINING_DRONE_WORD_MINING_LETTER_i.getAwtColour(), miningSample, IndicatorColour.MINING_DRONE_WORD_MINING_LETTER_i.getShadeDeviation())) {
                dronesMining = true;
            }

            if (dronesIdle) {
                return new DroneChecks(dronesIdle, false, false, false);
            }

            if (dronesReturning || dronesMining) {
                return new DroneChecks(false, dronesReturning, dronesMining, false);
            }

            idleLocalYPos += Constants.MINING_DRONE_WORD_MINING_LETTER_i_Y_BIAS;
            returningLocalYPos += Constants.MINING_DRONE_WORD_MINING_LETTER_i_Y_BIAS;
            miningLocalYPos += Constants.MINING_DRONE_WORD_MINING_LETTER_i_Y_BIAS;
        }

        return new DroneChecks(false, false, false, false);
    }
}
