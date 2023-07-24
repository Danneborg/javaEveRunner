package root.test;

import root.enums.Constants;
import root.enums.MouseButton;
import root.enums.WindowName;
import root.functions.*;
import root.indicator.Coordinate;
import root.indicator.Rectangle;
import root.miner.Orca;

import java.awt.*;

public class FunctionsTest {

    public static void main(String[] args) throws AWTException {
        WindowActivate windowActivate = new WindowActivate();
        ComparePixels comparePixels = new ComparePixels();
        Robot robot = new Robot();
        Click click = new Click(robot);
        KeyBoardPress keyBoardPress = new KeyBoardPress();

        Orca m = new Orca(WindowName.TORWAK_MARTIN, windowActivate, comparePixels, click, keyBoardPress);
        m.actTest();
//        windowActivate.activateWindow(WindowName.TORWAK_MARTIN.getTitle());
//        warpOnBelt(2, click);
//        Thread.sleep(500);
//        System.out.println(comparePixels.isLocationPanelOpen());
//        System.out.println(comparePixels.comparePixels(IndicatorColour.TOP_RIGHT_TARGETS_LOCK));
//        Color color1 = new Color(IndicatorColour.GREEN_SPACE_CIRCLE.getColour());

//        var c1 = robot.getPixelColor(1557, 147);
//        var c2 = robot.getPixelColor(IndicatorColour.TOP_RIGHT_TARGETS_LOCK.getCoordinate().getPosX(), IndicatorColour.TOP_RIGHT_TARGETS_LOCK.getCoordinate().getPosY());
//        System.out.println(c1);
//        System.out.println(c2);

//        System.out.println(comparePixels.comparePixels(IndicatorColour.GREEN_SPACE_CIRCLE));
//        System.out.println(comparePixels.comparePixels(IndicatorColour.TOP_RIGHT_TARGETS_LOCK));
//        Color color2 = robot.getPixelColor(IndicatorColour.GREEN_SPACE_CIRCLE.getCoordinate().getPosX(), IndicatorColour.GREEN_SPACE_CIRCLE.getCoordinate().getPosY());
//        System.out.println(color1.get);
//        System.out.println(color2);

//        System.out.println(comparePixels.comparePixels(IndicatorColour.FIRST_LOCKED_TARGET_BRACKET_ACTIVE));
//        System.out.println(comparePixels.comparePixels(IndicatorColour.SECOND_LOCKED_TARGET_BRACKET_NOT_ACTIVE));
//        System.out.println(robot.getPixelColor(IndicatorColour.FIRST_LOCKED_TARGET_BRACKET.getCoordinate().getPosX(), IndicatorColour.FIRST_LOCKED_TARGET_BRACKET.getCoordinate().getPosY()+19));

//        System.out.println(colorSet);

//        System.out.println(comparePixels.numberOfLockedTargets(19));
//        for (int i = 0; i < 10; i++) {
//            System.out.println(comparePixels.isInStation());
//            System.out.println("+++++++++++++++++++++++++++++++");
//            Thread.sleep(500);
//        }
//        for (int i = 0; i < 10; i++) {
//            var start = System.currentTimeMillis();
//            System.out.println(comparePixels.isInSpace());
//            System.out.println(comparePixels.isInWarp());
//            System.out.println(comparePixels.isInStation());
//            System.out.println(System.currentTimeMillis() - start);
//            System.out.println("+++++++++++++");
//        }
//        click.doClick(MouseButton.RIGHT, );
//        for (int i = 0; i < 10; i++) {
//            System.out.println(DefineCoordinate.defineCoordinate(Area.OVERVIEW_FIRST_ROW, 0, 190));
//        }
//        1145, 353
//        1145, 429

//        1145, 429
//        for (int i = 0; i < 10; i++) {
//            var start = System.currentTimeMillis();
//            System.out.println(DefineCoordinate.defineARowToClick(Area.OVERVIEW_FIRST_ROW, comparePixels.numberOfRowsCloserThan10km()));
//            System.out.println(System.currentTimeMillis() - start);
//            System.out.println("+++++++++++++");
//        }
//        for (int i = 0; i < 10; i++) {
//            var start = System.currentTimeMillis();
////            System.out.println(comparePixels.numberOfRowsCloserThan10km());
//            System.out.println(comparePixels.numberOfLockedTargets());
//            System.out.println(System.currentTimeMillis() - start);
//            System.out.println("+++++++++++++");
//        }

//        click.doClick(MouseButton.RIGHT, DefineCoordinate.defineARowToClick(Area.OVERVIEW_FIRST_ROW, comparePixels.numberOfRowsCloserThan10km()));
//        System.out.println(comparePixels.isOnBelt());
    }

    public static void warpOnBelt(int currentBelt, Click click) {

        var rectangleToWarp = Constants.LOCATION_PANEL_FIRST_BOOKMARK_COORDINATE;

        int xRmcTop = rectangleToWarp.getTOP_LEFT_ANGLE().getPosX();
        int xRmcBot = rectangleToWarp.getBOTTOM_RIGHT_ANGLE().getPosX();

        int yRmcTop = rectangleToWarp.getTOP_LEFT_ANGLE().getPosY() + (currentBelt-1) * Constants.BOOK_PANEL_Y_AXIS_BIAS;
        int yRmcBot = rectangleToWarp.getBOTTOM_RIGHT_ANGLE().getPosY() + (currentBelt-1) * Constants.BOOK_PANEL_Y_AXIS_BIAS;

        var leftClickCoordinate = DefineCoordinate.defineCoordinate(new root.indicator.Rectangle(new Coordinate(xRmcTop, yRmcTop), new Coordinate(xRmcBot, yRmcBot)));

        int xLmcTop = leftClickCoordinate.getPosX() + Constants.BOOK_PANEL_X_AXIS_MIN_BIAS_FOR_LEFT_CLICK;
        int xLmcBot = DefineCoordinate.rnd(xLmcTop, xLmcTop + Constants.BOOK_PANEL_X_AXIS_MAX_BIAS_FOR_LEFT_CLICK);

        int yLmcTop = leftClickCoordinate.getPosY() + Constants.BOOK_PANEL_Y_AXIS_MIN_BIAS_FOR_LEFT_CLICK;
        int yLmcBot = DefineCoordinate.rnd(xLmcTop, yLmcTop + Constants.BOOK_PANEL_Y_AXIS_MAX_BIAS_FOR_LEFT_CLICK);

        click.doClick(MouseButton.RIGHT, leftClickCoordinate, DefineCoordinate.rnd(1500, 1750));
        Sleep.sleep(150,250);
        click.doClick(MouseButton.LEFT, DefineCoordinate.defineCoordinate(new Rectangle(new Coordinate(xLmcTop, yLmcTop), new Coordinate(xLmcBot, yLmcBot))), DefineCoordinate.rnd(200, 300));
    }
}
