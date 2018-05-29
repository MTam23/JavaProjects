package frogger;

import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author Michael Tam
 * X071008
 * X436.2, Final Project Code
 */
public class FroggerMap extends JPanel implements ActionListener {

    private final Font menuFont = new Font("Helvetica", Font.BOLD, 20);

    public final int horiz;
    public final int vert;

    //game states
    private final static int WIN = 0;
    private final static int LOSE = 1;
    private final static int PLAY = 2;
    private final static int PAUSE = 3;
    private final static int MENU = -1;
    private final static int LOSTLIFE = 4;

    int gameState = MENU;

    private Timer timer;

    //determines speed of logs and cars
    private int speedModifier;
    private final int maxSpeed = 20;
    private final int minSpeed = 5;

    //determines frog horizontal movement distance
    private final int frogSpeed = 60;

    Frog myFrog;
    
    private int xChange;
    private int yChange;

    private Image frogPic;
    private Image grassPic;
    private Image streetPic;
    private Image riverPic;
    private Image carPicR;
    private Image carPicL;
    private Image logPic;

    //each level made up of <numlanes> "lanes"
    ArrayList<FroggerLane> lanes;

    public final int numLanes = 10;

    //0 grass 
    //1 street Right
    //2 street Left
    //3 river Right
    //4 river Left
    private int[] levelData;

    public FroggerMap(int h, int v) {

        this.lanes = new ArrayList<>();

        speedModifier = 1;
        horiz = h;
        vert = v;
        addKeyListener(new kAdapter());
        
        loadImages(); //get image resources

        initializeVars(); //set lanes, etc.

        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

    }

    private void initializeVars() {
        myFrog = new Frog(horiz / 2, 10, frogPic, this);
        initializeGame(true);
        timer = new Timer(40, this);
        timer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        initializeGame(false);
    }

    private void playGame(Graphics2D g2d) {
        for (int i = 0; i < lanes.size(); i++) {
            lanes.get(i).drawLane(g2d);
            lanes.get(i).moveLane();
        }
        if (gameState == LOSE) {
            gameOver(g2d);
        } else {
            myFrog.moveFrog(xChange, yChange);
            myFrog.drawFrog(g2d);
            checkFrog(g2d);
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //for each gamestate, show a different screen/message
        switch (gameState) {
            case MENU:
                displayStart(g2d);
                drawLives(g2d);
                break;
            case PLAY:
                playGame(g2d);
                drawLives(g2d);
                break;
            case PAUSE:
                pauseMenu(g2d);
                drawLives(g2d);
                break;
            case WIN:
                //gameWin(g2d);
                playGame(g2d);
                drawLives(g2d);
                break;
            case LOSE:
                //gameOver(g2d);
                playGame(g2d);
                drawLives(g2d);
                break;
            case LOSTLIFE:
                playGame(g2d);
                lostLife(g2d);
                drawLives(g2d);
                break;
            default:
                break;
        }
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();

    }

    //draw the currentlives at the top right
    private void drawLives(Graphics2D g2d) {
        String l = "Lives: " + myFrog.livesLeft();
        g2d.setFont(menuFont);
        g2d.setColor(Color.white);
        FontMetrics metric = this.getFontMetrics(menuFont);
        g2d.drawString(l, horiz - 2 * metric.stringWidth(l),
                metric.getHeight());

    }

    //is the frog dead at it's current location?
    private void checkFrog(Graphics2D g2d) {
        int laneCheck = (int) ((myFrog.getYPos() * numLanes) / vert); 
        
        FroggerLane temp = lanes.get(laneCheck); 
        boolean died = false;
        if ("Car".equals(temp.laneType())) {
                died = temp.collision(myFrog);
        } else if ("Log".equals(temp.laneType())) {
                died = !(temp.collision(myFrog));
        }
        if (died) {
            //Toolkit.getDefaultToolkit().beep(); //death sound? removed because annoying
            if (myFrog.death()) {
                gameState = LOSTLIFE;
                initializeGame(false);
                lostLife(g2d);
                //initializeGame();
            } else {
                gameState = LOSE;
                gameOver(g2d);
            }
        }
        xChange = 0;
        yChange = 0;
        //check if at end
        if (myFrog.yPos <= 0) {
            gameState = WIN;
            gameWin(g2d);
        }

    }

    //display start screen
    private void displayStart(Graphics2D g2d) {
        g2d.setColor(new Color(0, 105, 154));
        g2d.fillRect(horiz / 4, vert / 2, horiz / 2, vert / 10);
        g2d.setColor(Color.white);
        g2d.drawRect(horiz / 4, vert / 2, horiz / 2, vert / 10);

        String a = "Press 's' to start. 'esc' to pause.";
        FontMetrics metric = this.getFontMetrics(menuFont);

        g2d.setColor(Color.white);
        g2d.setFont(menuFont);
        g2d.drawString(a, (horiz - metric.stringWidth(a)) / 2,
                vert / 2 + 2 * metric.getHeight());
    }

    //display pause screen
    private void pauseMenu(Graphics2D g2d) {
        g2d.setColor(new Color(0, 105, 154));
        g2d.fillRect(horiz / 4, vert / 2, horiz / 2, vert / 10);
        g2d.setColor(Color.white);
        g2d.drawRect(horiz / 4, vert / 2, horiz / 2, vert / 10);

        String a = "Paused. Press 'esc' to resume.";
        FontMetrics metric = this.getFontMetrics(menuFont);

        g2d.setColor(Color.white);
        g2d.setFont(menuFont);
        g2d.drawString(a, (horiz - metric.stringWidth(a)) / 2,
                vert / 2 + 2 * metric.getHeight());
    }

    //display lost a life message
    private void lostLife(Graphics2D g2d) {
        g2d.setColor(new Color(0, 105, 154));
        g2d.fillRect(horiz / 4, vert / 2, horiz / 2, vert / 10);
        g2d.setColor(Color.white);
        g2d.drawRect(horiz / 4, vert / 2, horiz / 2, vert / 10);

        String a = "Lost a life. Press 's' to continue.";
        FontMetrics metric = this.getFontMetrics(menuFont);

        g2d.setColor(Color.white);
        g2d.setFont(menuFont);
        g2d.drawString(a, (horiz - metric.stringWidth(a)) / 2,
                vert / 2 + 2 * metric.getHeight());
    }

    //display you won message
    private void gameWin(Graphics2D g2d) {
        g2d.setColor(new Color(0, 105, 154));
        g2d.fillRect(horiz / 4, vert / 2, horiz / 2, vert / 10);
        g2d.setColor(Color.white);
        g2d.drawRect(horiz / 4, vert / 2, horiz / 2, vert / 10);

        String a = "You Won! Press 's' to play again. (Speed Increases)";
        FontMetrics metric = this.getFontMetrics(menuFont);

        g2d.setColor(Color.white);
        g2d.setFont(menuFont);
        g2d.drawString(a, (horiz - metric.stringWidth(a)) / 2,
                vert / 2 + 2 * metric.getHeight());
    }

    //display game over message
    private void gameOver(Graphics2D g2d) {
        g2d.setColor(new Color(0, 105, 154));
        g2d.fillRect(horiz / 4, vert / 2, horiz / 2, vert / 10);
        g2d.setColor(Color.white);
        g2d.drawRect(horiz / 4, vert / 2, horiz / 2, vert / 10);

        String a = "Game Over. Press 's' to play agin.";
        FontMetrics metric = this.getFontMetrics(menuFont);

        g2d.setColor(Color.white);
        g2d.setFont(menuFont);
        g2d.drawString(a, (horiz - metric.stringWidth(a)) / 2,
                vert / 2 + 2 * metric.getHeight());
    }
    
    //set the frog back to the beginning. create a new level if n is true
    private void initializeGame(Boolean n) {
        if (n) {
            reInitializeBoard();
        }
        myFrog.setPos((horiz - myFrog.width) / 2, 10);
    }

    //create a new level
    private void reInitializeBoard() { // should start and end with 0
        //levelData = new int[] {0, 1, 2, 3, 0, 1, 4, 2, 1, 0}; // test default
        //levelData = new int[] {0,3,4, 3,4,3,4,3,3,0}; // test all water

        levelData = new int[numLanes];
        
        //create random levels
        for (int i = 0; i < numLanes; i++) {
            switch (i) {
                case 0:
                case numLanes - 1:
                    levelData[i] = 0;
                    break;
                case 1:
                case numLanes - 2: {
                    Random r = new Random();
                    levelData[i] = r.nextInt(3) + 1;
                    break;
                }
                default: {
                    Random r = new Random();
                    levelData[i] = r.nextInt(4);
                    break;
                }
            }
        }

        //create lanes based on level data.
        this.lanes = new ArrayList<>();
        for (int i = 0; i < numLanes; i++) {
            switch (levelData[i]) {
                case 0:
                    lanes.add(new FroggerLane(i, grassPic, this));
                    break;
                case 1: {
                    int s = (int) (maxSpeed * speedModifier * Math.random());
                    if (s < minSpeed) {
                        s = (int) (minSpeed * (1 + Math.random()));
                    } else if (s > maxSpeed * 2) {
                        s = maxSpeed * 2;
                    }
                    lanes.add(new CarLane(i, streetPic, this,
                            s, 1, carPicR, myFrog.getWidth()));
                    break;
                }
                case 2: {
                    int s = (int) (maxSpeed * speedModifier * Math.random());
                    if (s < minSpeed) {
                        s = (int) (minSpeed * (1 + Math.random()));
                    } else if (s > maxSpeed * 2) {
                        s = maxSpeed * 2;
                    }
                    lanes.add(new CarLane(i, streetPic, this,
                            s, -1, carPicL, myFrog.getWidth()));
                    break;
                }
                case 3: {
                    int s = (int) (maxSpeed * speedModifier * Math.random() / 3);
                    if (s < minSpeed) {
                        s = (int) (minSpeed * (1 + Math.random()));
                    } else if (s > maxSpeed * 2) {
                        s = maxSpeed * 2;
                    }
                    lanes.add(new LogLane(i, riverPic, this,
                            s, 1, logPic, myFrog.getWidth(), myFrog));
                    break;
                }
                case 4: {
                    int s = (int) (maxSpeed * speedModifier * Math.random() / 3);
                    if (s < minSpeed) {
                        s = (int) (minSpeed * (1 + Math.random()));
                    } else if (s > maxSpeed * 2) {
                        s = maxSpeed * 2;
                    }
                    lanes.add(new LogLane(i, riverPic, this,
                            s, -1, logPic, myFrog.getWidth(), myFrog));
                    break;
                }
                default:
                    break;
            }
        }

    }

    private void loadImages() {
        //set images
        frogPic = new ImageIcon(Frogger.class.getResource("/frog.png")).getImage();
        grassPic = new ImageIcon(Frogger.class.getResource("/grass.png")).getImage();
        streetPic = new ImageIcon(Frogger.class.getResource("/street.png")).getImage();
        riverPic = new ImageIcon(Frogger.class.getResource("/water.png")).getImage();
        carPicL = new ImageIcon(Frogger.class.getResource("/carL.png")).getImage();
        carPicR = new ImageIcon(Frogger.class.getResource("/carR.png")).getImage();
        logPic = new ImageIcon(Frogger.class.getResource("/log.png")).getImage();
        
        //frogPic = new ImageIcon("images/frog.png").getImage();
        //grassPic = new ImageIcon("images/grass.png").getImage();
        //streetPic = new ImageIcon("images/street.png").getImage();
        //riverPic = new ImageIcon("images/water.png").getImage();
        //carPicL = new ImageIcon("images/carL.png").getImage();
        //carPicR = new ImageIcon("images/carR.png").getImage();
        //logPic = new ImageIcon("images/log.png").getImage();
    }

    class kAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent v) {

            int key = v.getKeyCode();

            switch (gameState) {
                case PLAY:
                    switch (key) {
                        case (KeyEvent.VK_LEFT):
                            xChange = -frogSpeed;
                            yChange = 0;
                            break;
                        case (KeyEvent.VK_RIGHT):
                            xChange = frogSpeed;
                            yChange = 0;
                            break;
                        case (KeyEvent.VK_UP):
                            xChange = 0;
                            yChange = -vert / numLanes;
                            break;
                        case (KeyEvent.VK_DOWN):
                            xChange = 0;
                            yChange = vert / numLanes;
                            break;
                        case (KeyEvent.VK_ESCAPE):
                            gameState = PAUSE;
                        default:
                            xChange = 0;
                            yChange = 0;
                            break;
                    }
                    break;
                case MENU:
                    if (key == 's' || key == 'S') {
                        gameState = PLAY;
                        initializeGame(false);
                    }
                    break;
                case PAUSE:
                    if (key == KeyEvent.VK_ESCAPE) {
                        gameState = PLAY;
                    }
                    break;
                case WIN:
                    if (key == 's' || key == 'S') {
                        speedModifier++;
                        gameState = PLAY;
                        initializeGame(true);
                    }
                    break;
                case LOSE:
                    if (key == 's' || key == 'S') {
                        //myFrog.resetLives();
                        speedModifier = 1;
                        gameState = PLAY;
                        initializeVars();
                    }
                    break;
                case LOSTLIFE:
                    if (key == 's' || key == 'S') {
                        gameState = PLAY;
                        initializeGame(false);
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent v) {
            int key = v.getKeyCode();
            if (key == Event.LEFT || key == Event.RIGHT || key == Event.UP || key == Event.DOWN) {
                xChange = 0;
                yChange = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

}
