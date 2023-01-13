package ten3.lib.tile;

import static ten3.lib.tile.CmTileMachine.PROGRESS;

import ten3.lib.wrapper.IntArrayCm;

public class Progressor {

    double timeProgressSpeed;// it decreases, speed increases
    double stressedProgress;

    public void progressOn(IntArrayCm data, int eff) {

        int max = 100;
        timeProgressSpeed = max * eff * 0.0003 + 0.2;

        if (timeProgressSpeed >= 1) {
            data.translate(PROGRESS, (int) timeProgressSpeed);
        } else {
            stressedProgress += timeProgressSpeed;
            if (stressedProgress >= 1) {
                stressedProgress -= 1;
                data.translate(PROGRESS, 1);
            }
        }

    }

}
