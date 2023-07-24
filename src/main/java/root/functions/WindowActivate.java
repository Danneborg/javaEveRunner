package root.functions;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WindowActivate {

    User32 user32 = User32.INSTANCE;

    public void activateWindow(String windowHeader) {

        try {

            WinDef.HWND windowHandle = user32.FindWindow(null, windowHeader);

            if (windowHandle != null) {
                WinDef.HWND foregroundWindow = user32.GetForegroundWindow();

                if (!windowHandle.equals(foregroundWindow)) {
                    System.out.printf("Активация окна - %s%n", windowHeader);
                    user32.ShowWindow(windowHandle, User32.SW_RESTORE);
                    user32.SetForegroundWindow(windowHandle);
                    Sleep.sleep(100,200);
                }
                else {
                    System.out.printf("Окно уже активированно - %s%n", windowHeader);
                }
            }

        }catch (Exception e){
            System.out.printf("Произошла ошибка во время активации окна - %s, %s%n", windowHeader, e.getMessage());
        }

    }

}
