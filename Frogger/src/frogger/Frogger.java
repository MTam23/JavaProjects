package frogger;

import java.awt.EventQueue;
import javax.swing.JFrame;


/**
 * @author Michael Tam
 * X071008
 * X436.2, Final Project Code
 */
public class Frogger extends JFrame {

    int horiz = 1500;
    int vert = 1000;

    public Frogger() {
        initUI();
    }

    //create JFrame parameters
    public void initUI() {
        add(new FroggerMap(horiz, vert));
        setTitle("Frogger");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(horiz, vert);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Frogger fr = new Frogger();
                fr.setVisible(true);
            }
        });
    }
}
