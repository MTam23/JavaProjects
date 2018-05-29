package frogger;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * @author Michael Tam
 * X071008
 * X436.2, Final Project Code
 */
public class Frog {

    int xPos;
    int yPos;

    int lives = 3;

    FroggerMap theMap;

    int width;
    int height;

    Image frogPic;

    public Frog(int x, int l, Image fPic, FroggerMap theMap) {
        this.xPos = x;

        this.theMap = theMap;
        this.frogPic = fPic;
        this.width = frogPic.getWidth(theMap);
        this.height = frogPic.getHeight(theMap);
        this.yPos = (l - 1) * (theMap.vert / theMap.numLanes) + 5;

    }

    //return number of lives the frog has
    public int livesLeft() {
        return this.lives;
    }

    //reduces lives and returns true if you still have lives, else false
    public boolean death() {
        this.lives--;
        return lives > 0; 
    }

    //move frog left or right, up or down
    public void moveFrog(int dx, int dy) { 
        if (xPos + dx < 0) {
            xPos = 0;
        } else if (xPos + dx + width > theMap.horiz) {
            xPos = theMap.horiz - width;
        } else {
            xPos += dx;
        }
        if (!(yPos + dy > theMap.vert)) {
            yPos += dy;
        }
    }

    public void drawFrog(Graphics2D g2d) {
        g2d.drawImage(frogPic, xPos, yPos, theMap);
    }

    //set the frog in a spot, at a certain lane num
    public void setPos(int x, int l) {
        this.xPos = x;
        this.yPos = (l - 1) * (theMap.vert / theMap.numLanes) + 5;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void resetLives() {
        this.lives = 3;
    }
}
