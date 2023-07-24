package root.functions;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyBoardPress {

    public static final int KEY_L = KeyEvent.VK_L;

    private final Robot robot;

    public KeyBoardPress() throws AWTException {
        this.robot = new Robot();
    }

    public void pressKey(int key){
        // Simulate pressing the 'A' key
        robot.keyPress(key);
        Sleep.sleep(4,12);
        // Simulate releasing the 'A' key
        robot.keyRelease(key);
    }
}
