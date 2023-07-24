package root.functions;

import lombok.RequiredArgsConstructor;
import root.enums.MouseButton;
import root.indicator.Coordinate;

import java.awt.*;
import java.awt.event.InputEvent;

import static root.enums.Constants.MOUSE_MOVE_SPEED;

@RequiredArgsConstructor
public class Click {

    private final Robot robot;

    public Click() throws AWTException {
        this.robot = new Robot();
    }

    public void moveMouse(int x, int y, int speed) {
        // Get the current mouse position
        int currentX = java.awt.MouseInfo.getPointerInfo().getLocation().x;
        int currentY = java.awt.MouseInfo.getPointerInfo().getLocation().y;

        // Calculate the distance to move
        int deltaX = x - currentX;
        int deltaY = y - currentY;

        // Calculate the total distance and time required for the movement
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double duration = distance / speed * 1000; // Convert speed to pixels per millisecond

        // Move the mouse in small steps towards the target coordinate
        int steps = (int) (duration / 10); // Move every 10 milliseconds
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps; // Calculate the current position in time (0.0 to 1.0)
            int moveX = currentX + (int) (deltaX * t);
            int moveY = currentY + (int) (deltaY * t);

            // Move the mouse
            robot.mouseMove(moveX, moveY);

            // Pause between each step
            robot.delay(10);
        }

        // Optionally, perform a mouse click at the final position
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void mouseMove(int destinationX, int destinationY) {

        Sleep.sleep(12, 16);

        Point currentMousePosition = MouseInfo.getPointerInfo().getLocation();
        int currentX = currentMousePosition.x;
        int currentY = currentMousePosition.y;

        int deltaX = destinationX - currentX;
        int deltaY = destinationY - currentY;
        int distance = (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        int steps = distance / DefineCoordinate.rnd(MOUSE_MOVE_SPEED - 2, MOUSE_MOVE_SPEED);
        if (steps < 1) steps = 1;

        int stepX = deltaX / steps;
        int stepY = deltaY / steps;

        for (int i = 0; i < steps; i++) {
            currentX += stepX;
            currentY += stepY;
            robot.mouseMove(currentX, currentY);
            Sleep.sleep(4, 7);
        }

        robot.mouseMove(destinationX, destinationY);
    }

    public void doClick(MouseButton mouseButton, Coordinate coordinate, int speed) {

//        this.mouseMove(coordinate.getPosX(), coordinate.getPosY());
        this.moveMouse(coordinate.getPosX(), coordinate.getPosY(), speed);
        switch (mouseButton) {
            case LEFT:
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Sleep.sleep(14, 35);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                Sleep.sleep(14, 35);
                break;
            case RIGHT:
                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                Sleep.sleep(14, 35);
                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                Sleep.sleep(14, 35);
                break;
        }

    }

}
