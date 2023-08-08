package root.miner;

public interface Mine {

    void activateWindow();

    void act();

    void setSleep(Long milSec);

    void warpOnBelt() throws InterruptedException;

    void unloadExecumerMinigHold() throws InterruptedException;

    void doUndock();

    void mine();

    void jettisonExecumer();
}
