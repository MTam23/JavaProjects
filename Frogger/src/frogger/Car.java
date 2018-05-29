package frogger;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * @author Michael Tam
 * X071008
 * X436.2, Final Project Code
 */
public class Car {

    int xPos;
    int yPos;
    int speed;
    int direction; //+1 or -1
    FroggerMap theMap;
    int width;
    int height;
    Image carPic;

    //each CarLane has some number of cars
    public Car(int laneNumber, int startX, Image cPic, FroggerMap theMap, int sp, int d) {
        this.theMap = theMap;
        this.carPic = cPic;
        this.width = carPic.getWidth(theMap);
        this.height = carPic.getHeight(theMap);
        this.speed = sp;
        this.direction = d;

        yPos = laneNumber * this.theMap.vert / this.theMap.numLanes;
        xPos = startX;

    }

    public void drawCar(Graphics2D g2d) {
        g2d.drawImage(carPic, xPos, yPos, theMap);
    }

    public void moveCar() {
        xPos += speed * direction;
    }

    //has the car hit the frog?
    public boolean overlaps(Frog f) {
        int fX1 = f.getXPos();
        int fY1 = f.getYPos();
        int fX2 = fX1 + f.getWidth();
        int fY2 = fY1 + f.getHeight();

        return !((fX1 > xPos + width) || (fY1 > yPos + height)
                || (fX2 < xPos) || (fY2 < yPos));
    }

    public boolean offMap() {
        if (direction == 1) {
            if (xPos >= theMap.horiz) {
                return true;
            }
        } else if (direction == -1) {
            if (xPos + width < 0) {
                return true;
            }
        }
        return false;
    }

    //car has gone off visible map, let's put it back on the other side.
    public void resetPos() {
        if (direction == 1) {
            xPos = -width;
        } else if (direction == -1) {
            xPos = theMap.horiz;
        }
    }

}
