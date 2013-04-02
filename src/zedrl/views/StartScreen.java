/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zedrl.views;
import asciiPanel.AsciiPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import squidpony.squidgrid.gui.swing.SwingPane;
/**
 *
 * @author Brandon
 */
public class StartScreen implements Screen, KeyListener
{
    private JFrame frame;
    
    public StartScreen(JFrame frame){
        this.frame = frame;
        //frame = new JFrame("Welcome to ZedRL!");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //display = new SwingPane();
        //display.initialize(50, 24, new Font("Arial Black",Font.PLAIN, 18));
        //frame.add(display);
        //display.placeHorizontalString(0, 0, "Welcome", Color.red, Color.darkGray);
        //display.refresh();
        
        //frame.setVisible(true);
        //frame.pack();
        //frame.repaint();
       //frame.setLocationRelativeTo(null);
        //frame.addKeyListener(this);
    }
    @Override
    public void displayOutput(SwingPane display)
    {
        display.placeHorizontalString(5, 5, "Press [Enter] To Continue!", Color.WHITE, Color.BLACK);
        display.refresh();

    }

    @Override
    public Screen respondToUserInput(KeyEvent key)
    {
        if (key.getKeyCode() == KeyEvent.VK_ENTER)
        {
            return new PlayScreen(frame);
        } else
        {
            return this;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            respondToUserInput(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

}
