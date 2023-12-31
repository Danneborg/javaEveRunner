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

    public Void launchDrones(){
        // Simulate pressing the 'A' key
        pressTwoKeys(KeyEvent.VK_SHIFT, KeyEvent.VK_F);
        Sleep.sleep(200,300);
        return null;
    }

    public Void returnDrones(){
        // Simulate pressing the 'A' key
        pressTwoKeys(KeyEvent.VK_SHIFT, KeyEvent.VK_R);
        Sleep.sleep(200,300);
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

    public Void openInventoryHold(){
        pressTwoKeys(KeyEvent.VK_ALT, KeyEvent.VK_C);
        Sleep.sleep(400,500);
        return null;
    }

    public void pressKey(int key){
        robot.keyPress(key);
        Sleep.sleep(4,12);
        robot.keyRelease(key);
    }

    public void pressKey(int key, int numberOfRepeats){
        for(var i =0;i< numberOfRepeats; i++){
            robot.keyPress(key);
            Sleep.sleep(15,25);
            robot.keyRelease(key);
            Sleep.sleep(15,25);
        }
    }

    public void pressKeyWithoutRelease(int key){
        Sleep.sleep(4,12);
        robot.keyPress(key);
        Sleep.sleep(15,25);
    }

    public void keyRelease(int key){
        Sleep.sleep(4,12);
        robot.keyRelease(key);
        Sleep.sleep(15,25);
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
