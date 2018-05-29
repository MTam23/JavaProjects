package frogger;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * @author Michael Tam
 * X071008
 * X436.2, Final Project Code
 */
public class LogLane extends FroggerLane {

    Log[] traffic; //the logs in the lane
    
    int speed;
    int direction;
    int numLogs;

    int frogWidth;
    Image logPic;
    Frog theFrog;

    public LogLane(int lNum, Image txture, FroggerMap m, int sp, int dir, Image cPic, int fW, Frog f) {
        super(lNum, txture, m);

        this.laneType = "Log";
        this.theFrog = f;
        this.speed = sp;
        this.direction = dir;
        this.frogWidth = fW;
        this.logPic = cPic;
        this.numLogs = 2;
        this.traffic = new Log[numLogs];
        this.addLogs();

    }

    //populate the logs in the lane
    private void addLogs() {

        if (direction == 1) {
            for (int i = 0; i < numLogs; i++) {
                traffic[i] = new Log(laneNum,
                        i * theMap.horiz / numLogs - logPic.getWidth(theMap),
                        logPic, theMap, speed, direction);
            }
        } else if (direction == -1) {
            for (int i = 0; i < numLogs; i++) {
                traffic[numLogs - 1 - i] = new Log(laneNum,
                        i * theMap.horiz / numLogs - logPic.getWidth(theMap),
                        logPic, theMap, speed, direction);
            }
        }
    }

    @Override
    public boolean collision(Frog f) {
        for (Log traffic1 : traffic) {
            if (traffic1 != null && traffic1.overlaps(f)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param g2d
     */
    @Override
    public void drawLane(Graphics2D g2d) {
        g2d.drawImage(bg, 0, yPos, theMap);

        for (Log traffic1 : traffic) {
            if (traffic1 != null) {
                traffic1.drawLog(g2d);
                if (traffic1.offMap()) {
                    traffic1.resetPos();
                }
            }
        }
    }

    @Override
    public void moveLane() {
        int laneCheck1 = (int) ((theFrog.getYPos() * theMap.numLanes) / theMap.vert);
        for (Log traffic1 : traffic) {
            if (traffic1 != null) {
                traffic1.moveLog(theFrog, laneCheck1 == laneNum);
            }
        }
    }
}