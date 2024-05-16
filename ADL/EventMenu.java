package ADL;

import javax.swing.*;

import twoclass.SecondClass;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class EventMenu extends JFrame {
    private static final String BALANCE_FILE = "balance.txt";
    private int balance;

    //EVENT MENU WINDOW
    public EventMenu() {
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout()); 

      
        JLabel titleLabel1 = new JLabel("Athereal Divided Lands", SwingConstants.CENTER);
        titleLabel1.setFont(new Font("Arial", Font.BOLD, 88));
        titleLabel1.setForeground(Color.WHITE);
        titleLabel1.setOpaque(true); 
        titleLabel1.setBackground(Color.BLACK);

     
        JLabel titleLabel2 = new JLabel("Galaxy Odyssey", SwingConstants.CENTER);
        Font galaxyFont = new Font("Arial", Font.BOLD, 55);
        titleLabel2.setFont(galaxyFont);
        titleLabel2.setForeground(new Color(30, 144, 255)); 

      
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.add(titleLabel1);
        titlePanel.add(titleLabel2);

      
        JButton eventInfoButton = new JButton("Event Introduction");
        JButton eventGameButton = new JButton("Space Defender (Event Game)");
        JButton eventMoreInfoButton = new JButton("Event More Info");
        JButton backButton = new JButton("Back");

      
        Color buttonBackgroundColor = Color.BLACK;
        Color buttonFontColor = Color.MAGENTA;
        Font buttonFont = new Font("Arial", Font.BOLD, 24);

        eventInfoButton.setBackground(buttonBackgroundColor);
        eventInfoButton.setForeground(buttonFontColor);
        eventInfoButton.setFont(buttonFont);

        eventGameButton.setBackground(buttonBackgroundColor);
        eventGameButton.setForeground(buttonFontColor);
        eventGameButton.setFont(buttonFont);

        eventMoreInfoButton.setBackground(buttonBackgroundColor);
        eventMoreInfoButton.setForeground(buttonFontColor);
        eventMoreInfoButton.setFont(buttonFont);

        backButton.setBackground(buttonBackgroundColor);
        backButton.setForeground(buttonFontColor);
        backButton.setFont(buttonFont);

     
        Dimension buttonSize = new Dimension(500, 200); 
        eventInfoButton.setPreferredSize(buttonSize);
        eventGameButton.setPreferredSize(buttonSize);
        eventMoreInfoButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

     
        eventInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                intro();
            }
        });

        eventGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                new SpaceGame();
            }
        });
        
        eventMoreInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              
               Info();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
      
                Finals();
            }
        });

       
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 20, 20, 20); 

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.25; 
        gbc.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(eventInfoButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(eventGameButton, gbc);

        gbc.gridy = 2;
        buttonPanel.add(eventMoreInfoButton, gbc);

        gbc.gridy = 3;
        buttonPanel.add(backButton, gbc);

      
        JPanel picturePanel = new JPanel();
        picturePanel.setBackground(Color.BLACK); 
        JLabel pictureLabel = new JLabel();
        ImageIcon icon = new ImageIcon("C:/Users/lukej/eclipse-workspace/Java.luke/src/ADL/Background/Galaxy Odyssey.PNG"); 
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(900, 850, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        pictureLabel.setIcon(icon);
        picturePanel.add(pictureLabel);

    
        add(titlePanel, BorderLayout.NORTH);

 
        add(buttonPanel, BorderLayout.WEST);
        add(picturePanel, BorderLayout.CENTER);

     
        setVisible(true);
    }

    public void intro() {
        try {
       
            Desktop.getDesktop().browse(new URI("https://ibb.co/hmsb190"));
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }
    public void Info() {
        try {
       
            Desktop.getDesktop().browse(new URI("https://smallpdf.com/file#s=0ff61547-309e-4c27-99c2-ba6c5d9bf13e"));
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }
    private void loadBalance() {
        File balanceFile = new File(BALANCE_FILE);
        if (!balanceFile.exists()) {
        
            try {
                balanceFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace(); 
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(balanceFile))) {
            String line = reader.readLine();
            if (line != null) {
                balance = Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
    private void Finals() {
   
    	  Menu cardGame = new Menu();
          cardGame.setVisible(true);
          loadBalance();
    } 
    private void SpaceGame() {
  
    	 SpaceGame SpaceGame = new SpaceGame();
    	   SpaceGame.setVisible(true);
    }
    public static void main(String[] args) {
    	 EventMenu EventMenu = new EventMenu();
    	 EventMenu.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EventMenu();
            }
        });
    }
}
