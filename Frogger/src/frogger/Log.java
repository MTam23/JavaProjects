package frogger;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * @author Michael Tam
 * X071008
 * X436.2, Final Project Code
 */
public class Log {

    int xPos;
    int yPos;
    int speed;
    int direction; //+1 or -1
    FroggerMap theMap;
    int width;
    int height;
    Image logPic;
    
    public Log(int laneNumber, int startX, Image lPic, FroggerMap theMap, int sp, int d) {
        this.theMap = theMap;
        this.logPic = lPic;
        this.width = logPic.getWidth(theMap);
        this.height = logPic.getHeight(theMap);
        this.speed = sp;
        this.direction = d;
        
        yPos = laneNumber * this.theMap.vert/this.theMap.numLanes;
        xPos = startX;
        
    }
    
    public void drawLog(Graphics2D g2d) {
        g2d.drawImage(logPic, xPos, yPos, theMap);
    }
    
    //if the frog is on the log, the frog moves also
    public void moveLog(Frog f, boolean moveFrog){
        xPos += speed * direction;
        if (this.overlaps(f) && moveFrog) {
            f.moveFrog(speed * direction, 0);
        }
    }
    
    //is the frog on the log?
    public boolean overlaps(Frog f) {
        int fX1 = f.getXPos();
        int fY1 = f.getYPos();
        int fX2 = fX1 + f.getWidth();
        int fY2 = fY1 + f.getHeight();
        if((fX1 > xPos + width) || (fY1 > yPos + height) 
                || (fX2 < xPos) || (fY2 < yPos)) {
            return false;
        } else {
            return true;
        }
    }
    
    public boolean offMap() {
        if (direction == 1) {
            if(xPos >= theMap.horiz) {
                return true;
            }
        } else if (direction == -1) {
            if (xPos + width < 0) {
                return true;
            }
        }
        return false;
    }
    
    //log is off map, put it back at the beginning
    public void resetPos() {
        if (direction == 1) {
            xPos = -width;
        } else if (direction == -1) {
            xPos = theMap.horiz;
        }
    }
    
}
