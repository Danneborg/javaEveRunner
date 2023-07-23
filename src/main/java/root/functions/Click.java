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

    public void mouseMove(int destinationX, int destinationY) throws InterruptedException {

        Point currentMousePosition = MouseInfo.getPointerInfo().getLocation();
        int currentX = currentMousePosition.x;
        int currentY = currentMousePosition.y;

        int deltaX = destinationX - currentX;
        int deltaY = destinationY - currentY;
        int distance = (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        int steps = distance / DefineCoordinate.rnd(MOUSE_MOVE_SPEED-2, MOUSE_MOVE_SPEED);
        if (steps < 1) steps = 1;

        int stepX = deltaX / steps;
        int stepY = deltaY / steps;

        for (int i = 0; i < steps; i++) {
            currentX += stepX;
            currentY += stepY;
            robot.mouseMove(currentX, currentY);
            Thread.sleep(4); // Adjust this value if needed for smoother movement
        }

        robot.mouseMove(destinationX, destinationY);
    }

    public void doClick(MouseButton mouseButton, Coordinate coordinate) throws InterruptedException {

        //TODO сделать время ожидания рандомизированным
        this.mouseMove(coordinate.getPosX(), coordinate.getPosY());
        switch (mouseButton){
            case LEFT:
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(40);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                break;
            case RIGHT:
                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                Thread.sleep(40);
                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                break;
        }

    }

}
