package frogger;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * @author Michael Tam
 * X071008
 * X436.2, Final Project Code
 */
public class CarLane extends FroggerLane {

    Car[] traffic; // the cars in the lane
       
    int speed;
    int direction;
    int numCars;
    
    int frogWidth;
    Image carPic;
    
    public CarLane(int lNum, Image txture, FroggerMap m, int sp, int dir, Image cPic, int fW) {
        super(lNum, txture, m);
        
        this.laneType = "Car";
        this.speed = sp;
        this.direction = dir;
        this.frogWidth = fW;
        this.carPic = cPic;
        this.numCars = 2; 
        this.traffic = new Car[numCars];
        this.addCars();
        
    }
    
    //populate the cars in the lane
    private void addCars() {
        
        if (direction == 1) {
            for(int i = 0; i < numCars; i++) {
                traffic[i] = new Car(laneNum, 
                        i * theMap.horiz / numCars -carPic.getWidth(theMap),
                        carPic, theMap, speed, direction);
            }
        } else if (direction == -1) {
            for(int i = 0; i < numCars; i++) {
                traffic[numCars - 1 - i] = new Car(laneNum, 
                    i * theMap.horiz / numCars -carPic.getWidth(theMap), 
                    carPic, theMap, speed, direction);
            }
        }
    }
       
    @Override
    public boolean collision(Frog f) {
        for (Car traffic1 : traffic) {
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
        g2d.drawImage(bg,0,yPos,theMap);
        
        //traffic[0].drawCar(g2d);
        
        for (Car traffic1 : traffic) {
            if (traffic1 != null) {
                traffic1.drawCar(g2d);
                if (traffic1.offMap()) {
                    traffic1.resetPos();
                }
            }
            
        }
        
        
    }
    
    @Override
    public void moveLane() {
        for (Car traffic1 : traffic) {
            if (traffic1 != null) {
                traffic1.moveCar();
            }
        }
    }

}
