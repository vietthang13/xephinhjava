/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Tu Heo
 */
public class Main extends JFrame implements MouseListener {

    private JPanel panel;
    static int WIDTH = 565;
    static int HEIGHT = 590;
    static String username = "sss";
    Image background_main;

    public Main() {
        try {
            background_main = ImageIO.read(new File("Images/background_main.jpg"));
        } catch (Exception e) {
        };
        addMouseListener(this);
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background_main, 0, 0, null);
            }
        };

        //bt.addActionListener(this);
        setContentPane(panel);
        //add(panel);
        setSize(WIDTH, HEIGHT);
        setTitle("Game Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

    }

    public static void main(String[] args) {
        Main main = new Main();
        
        main.setVisible(true);
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        //Khi click vào tọa độ của nút Play
        if (x > 210 && x < 350 && y > 340 && y < 392) {
            Tetris tetris = new Tetris();
            tetris.setVisible(true);
            this.setVisible(false);

        }
        //Khi click vào toạn độ nút Quit
        if (x > 210 && x < 350 && y > 480 && y < 535) {
            System.exit(0);
        }
        //Khi click vào nút xếp hạng

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }
}
