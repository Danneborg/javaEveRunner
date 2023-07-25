package root.functions;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyBoardPress {

    private final Robot robot;

    public KeyBoardPress() throws AWTException {
        this.robot = new Robot();
    }

    public Void openBookmarkPanel(){
        pressKey(KeyEvent.VK_L);
        return null;
    }

    public Void openItemHangar(){
        // Simulate pressing the 'A' key
        pressTwoKeys(KeyEvent.VK_ALT, KeyEvent.VK_G);
        Sleep.sleep(400,500);
        return null;
    }

    public Void openExecumerMiningHold(){
        pressTwoKeys(KeyEvent.VK_ALT, KeyEvent.VK_O);
        Sleep.sleep(400,500);
        return null;
    }

    private void pressKey(int key){
        // Simulate pressing the 'A' key
        robot.keyPress(key);
        Sleep.sleep(4,12);
        // Simulate releasing the 'A' key
        robot.keyRelease(key);
    }

    private void pressTwoKeys(int keyFirst, int keySecond){
        robot.keyPress(keyFirst);
        Sleep.sleep(4,12);
        robot.keyPress(keySecond);
        Sleep.sleep(4,12);
        robot.keyRelease(keyFirst);
        Sleep.sleep(4,12);
        robot.keyRelease(keySecond);
    }
}
