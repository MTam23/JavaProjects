package frogger;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * @author Michael Tam
 * X071008
 * X436.2, Final Project Code
 */
public class FroggerLane {

    Image bg;
    FroggerMap theMap;
    int yPos;
    int laneNum;
    String laneType; // type of lane, default is Grass. Also have Car or Log

    public FroggerLane(int lNum, Image txture, FroggerMap m) {
        this.laneType = "Grass";
        this.bg = txture;
        this.theMap = m;
        this.laneNum = lNum;
        this.yPos = laneNum * this.theMap.vert / this.theMap.numLanes;
    }

    public void drawLane(Graphics2D g2d) {
        g2d.drawImage(bg, 0, yPos, theMap);
    }

    //default lanes have nothing to collide with the frog
    public boolean collision(Frog f) {
        return false;
    }

    public int[] changeFrogPos() {
        int[] change = {0, 0};
        return change;
    }

    public void moveLane() {
        //override in log and car lanes. Tells the cars/logs to move position
    }

    public String laneType() {
        return this.laneType;
    }

}
