package ADL;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import twoclass.SecondClass;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.swing.event.HyperlinkEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Finals extends JFrame implements ActionListener {
    private JButton playButton, settingsButton, exitButton;
    private JLabel titleLabel;

    public Finals() {
     //FRAME1
        setTitle("Athereal Divided Lands");

        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true); 

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());

       
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
               
                ImageIcon gif = new ImageIcon("C:/Users/lukej/eclipse-workspace/Java.luke/src/ADL/Background/J4o3.gif");
                
                g.drawImage(gif.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

       
        titleLabel = new JLabel("Athereal Divided Lands ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 88));
        titleLabel.setForeground(Color.WHITE); 
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

       
        JPanel buttonsPanel = new JPanel(new GridBagLayout()); 
        buttonsPanel.setOpaque(false); 

       
        playButton = new JButton("PLAY");
        settingsButton = new JButton("ABOUT US");
        exitButton = new JButton("EXIT");

        
        Font buttonFont = new Font("Bauhaus 93", Font.BOLD, 35);
        playButton.setFont(buttonFont);
        settingsButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        
        playButton.setUI(new CustomButtonUI());
        settingsButton.setUI(new CustomButtonUI());
        exitButton.setUI(new CustomButtonUI());

        
        playButton.addActionListener(this);
        settingsButton.addActionListener(this);
        exitButton.addActionListener(this);

        Dimension buttonSize = new Dimension(300, 60); 
        playButton.setPreferredSize(buttonSize);
        settingsButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

       
        playButton.setForeground(Color.MAGENTA);
        settingsButton.setForeground(Color.MAGENTA);
        exitButton.setForeground(Color.MAGENTA);
        playButton.setBackground(Color.BLACK);
        settingsButton.setBackground(Color.BLACK);
        exitButton.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 20, 5, 0); 
        buttonsPanel.add(playButton, gbc);

        gbc.gridy++;
        buttonsPanel.add(settingsButton, gbc);

        gbc.gridy++;
        buttonsPanel.add(exitButton, gbc);

      
        backgroundPanel.add(buttonsPanel, BorderLayout.WEST);

       
        JLabel disclaimerLabel = new JLabel("DISCLAIMER: THIS GAME UTILIZES AI FOR IMAGE GENERATION (PLEASE FEEL FREE TO EXIT IF IT IS NOT YOU PREFERENCE)", SwingConstants.CENTER);
        disclaimerLabel.setFont(new Font("Arial", Font.BOLD, 25));
        disclaimerLabel.setForeground(Color.WHITE);
        backgroundPanel.add(disclaimerLabel, BorderLayout.SOUTH);

       
        getContentPane().setLayout(new OverlayLayout(getContentPane()));
        getContentPane().add(backgroundPanel);

       
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            JOptionPane.showMessageDialog(this, "Starting the game...");
           
            Menu cardGame = new Menu();
            cardGame.setVisible(true);
          
            dispose();
        } else if (e.getSource() == settingsButton) {
            JOptionPane.showMessageDialog(this, "ABOUT US" + '\n' + "People who made the game possible (we think)" + '\n' + '\n' + '\n' + "Luke Jamis (Head Developer)" + '\n' +  "Mikhail Jaffar (Researcher and Co-Developer)" + '\n' +  "France Esclamado(Menu Genius)" + '\n' + "Christian Pante (The driver)" + '\n' + "Thirdy Villalveto (Documentation Maker and Driver)" + '\n');
        } else if (e.getSource() == exitButton) {
            int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the program?", "Exit Program Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmed == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Exiting the program.");
                System.exit(0);
            }
        }
    }

   
    private static class CustomButtonUI extends BasicButtonUI {
        @Override
        protected void installDefaults(AbstractButton button) {
            super.installDefaults(button);
            button.setOpaque(false);
            button.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40)); 
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Finals();
            }
        });
    }
}
//SECOND MENU
class Menu extends JFrame {
    private Map<String, Integer> inventory; 
    private static final String INVENTORY_FILE = "inventory.txt";
    private static final String BALANCE_FILE = "balance.txt";
    private static final int DAILY_REWARD = 1500;
    private static final Duration CLAIM_COOLDOWN = Duration.ofHours(22); 
    private int balance;
    private LocalDateTime lastClaimTime;
    private static final String LAST_CLAIM_FILENAME = "LastClaimTime.txt"; 
    private boolean bannerSelected = false;
    private JLabel backgroundLabel;

    public Menu() {
        setTitle("ADL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); 

        
        loadInventory();
        loadBalance();

        
        JLabel backgroundLabel = new JLabel(new ImageIcon("C:/Users/lukej/eclipse-workspace/Java.luke/src/ADL/Background/J4o1.gif"));
        setContentPane(backgroundLabel);

        
        setExtendedState(JFrame.MAXIMIZED_BOTH);

     
        JButton bannerButton = new JButton("Banner");
        bannerButton.setBounds(50, 50, 300, 70);
        bannerButton.setForeground(Color.decode("#EE82EE")); 
        bannerButton.setBackground(Color.BLACK); 
        bannerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
             
                String[] options = {"Banner X", "Banner Y", "[★]Event Banner X" , "[★]Event Banner Y", "[★]Event Banner Z"};
                String selectedBanner = (String) JOptionPane.showInputDialog(null, "Choose Banner:", "Banner Options", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                if (selectedBanner != null) {
                    if (selectedBanner.equals("Banner X")) {
                    
                        showBannerXOptions();
                    } else if (selectedBanner.equals("Banner Y")) {
                     
                        showBannerYOptions();
                    } else if (selectedBanner.equals("[★]Event Banner X")) {
                    	showEBXOptions();
                    } else if (selectedBanner.equals("[★]Event Banner Y")) {
                    	showEBYOptions();
                    } else if (selectedBanner.equals("[★]Event Banner Z")) {
                    	showEBZOptions();
                    }
                }
            }
        });
        backgroundLabel.add(bannerButton);

        // Event Button
        JButton eventButton = new JButton("Event");
        eventButton.setBounds(1000, 90, 300, 100);
        eventButton.setForeground(Color.decode("#EE82EE")); 
        eventButton.setBackground(Color.BLACK); 
        eventButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventMenu();
            }
        });
        backgroundLabel.add(eventButton);

        // Daily Button
        JButton dailyButton = new JButton("Daily");
        dailyButton.setBounds(50, 130, 300, 70);
        dailyButton.setForeground(Color.decode("#EE82EE")); 
        dailyButton.setBackground(Color.BLACK); 
        dailyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                claimDailyReward();
            }
        });
        backgroundLabel.add(dailyButton);

        // Balance Button
        JButton balanceButton = new JButton("Balance");
        balanceButton.setBounds(50, 210, 300, 70);
        balanceButton.setForeground(Color.decode("#EE82EE")); 
        balanceButton.setBackground(Color.BLACK); 
        balanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Your current balance is: " + balance + " gems", "Balance", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        backgroundLabel.add(balanceButton);

        // Inventory Button
        JButton inventoryButton = new JButton("Inventory");
        inventoryButton.setBounds(50, 290, 300, 70);
        inventoryButton.setForeground(Color.decode("#EE82EE"));
        inventoryButton.setBackground(Color.BLACK); 
        inventoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
             
                showInventory();
            }
        });
        backgroundLabel.add(inventoryButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(50, 370, 300, 70);
        exitButton.setForeground(Color.decode("#EE82EE")); 
        exitButton.setBackground(Color.BLACK); 
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Exiting the program.");
                System.exit(0); 
            }
        });
        backgroundLabel.add(exitButton);
    
    }
//POSITIONING
private void positionButtons(JButton[] buttons) {
    int buttonWidth = 300;
    int buttonHeight = 70;
    int buttonSpacing = 20;
    int xMargin = 50;
    int yMargin = 50;
    int screenWidth = getContentPane().getWidth();
    int screenHeight = getContentPane().getHeight();
    int totalHeight = buttons.length * (buttonHeight + buttonSpacing) - buttonSpacing;

    int x = xMargin;
    int y = (screenHeight - totalHeight) / 2;

    for (JButton button : buttons) {
        button.setBounds(x, y, buttonWidth, buttonHeight);
        y += buttonHeight + buttonSpacing;
    }
}

   
   
    private void claimDailyReward() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BALANCE_FILE))) {
            String balanceStr = reader.readLine();
            if (balanceStr == null) {
             
                try {
                    PrintWriter writer = new PrintWriter(new FileWriter(BALANCE_FILE));
                    writer.println("0"); 
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace(); 
                }
            } else {
                balance = Integer.parseInt(balanceStr); 
            }

            LocalDateTime lastClaimTime = getLastClaimTime();
            LocalDateTime currentTime = LocalDateTime.now();
            Duration duration = Duration.between(lastClaimTime, currentTime);
            long hoursPassed = duration.toHours();
            //24 hours CHECKER
            if (hoursPassed >= 22) { 
                balance += DAILY_REWARD; 
                saveBalanceToFile(); 
                saveLastClaimTime();

                JOptionPane.showMessageDialog(null, "Congratulations! You claimed your daily reward of " + DAILY_REWARD + " gems.");
            } else {
                long hoursLeft = 22 - hoursPassed;
                long minutesLeft = 60 - (duration.toMinutes() % 60);
                JOptionPane.showMessageDialog(null, "You have already claimed your daily reward. Please wait " +
                        hoursLeft + " hours and " + minutesLeft + " minutes.");
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error reading balance: " + e.getMessage());
        }
    }
//SAVE BAL
    private void saveBalanceToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BALANCE_FILE))) {
            writer.println(balance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  //LOAD BAL
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

    private void saveLastClaimTime() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LAST_CLAIM_FILENAME))) {
            writer.println(LocalDateTime.now().toString());
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    private LocalDateTime getLastClaimTime() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LAST_CLAIM_FILENAME))) {
            String lastClaimTimeString = reader.readLine();
            if (lastClaimTimeString != null) {
                return LocalDateTime.parse(lastClaimTimeString);
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        return LocalDateTime.now().minus(CLAIM_COOLDOWN);
    }

    private void loadLastClaimTime() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LAST_CLAIM_FILENAME))) {
            String lastClaimTimeString = reader.readLine();
            if (lastClaimTimeString != null) {
                lastClaimTime = LocalDateTime.parse(lastClaimTimeString);
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    
    private void openWebPage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
//SHOW BANNER X
    private void showBannerXOptions() {
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
       
        JLabel bannerLabel = new JLabel("You have chosen banner X", SwingConstants.CENTER);
        panel.add(bannerLabel, gbc);
        
       
        JTextArea bannerCardsArea = new JTextArea(
            "Available Cards:\n" +
            "• | SSR | Genshiro #00019\n" +
            "• | R | Enara Horus #00016\n" +
            "• | C | Student Y #00012\n" +
            "• | C | Student X #00011\n" +
            "• | R | Kaden #00017\n" +
            "• | SR | Voss Ignatius #00018\n" +
            "• | SR | Vlad #00015\n" +
            "• | SR | Li Zhiya #00013\n" +
            "• | SR | Valerie #00014\n"
        );
        bannerCardsArea.setEditable(false);
        bannerCardsArea.setOpaque(false); 
        bannerCardsArea.setFocusable(false); 
        bannerCardsArea.setLineWrap(true);
        bannerCardsArea.setWrapStyleWord(true); 
        
        gbc.gridy++;
        panel.add(new JScrollPane(bannerCardsArea), gbc);

       
        JButton showInfoButton = new JButton("Show Info");
        showInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              
                openWebPage("https://ibb.co/tHff2Nk");
            }
        });
        
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(showInfoButton, gbc);
        
      
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton xTenButton = new JButton("Ten (1000 Gems)");
        xTenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (balance >= 1000) {
                    XpullTen();
                    balance -= 1000; 
                    saveBalanceToFile(); 
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough gems for X Ten. You need at least 1000 gems.", "Insufficient Gems", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(xTenButton);
        
        JButton xOneButton = new JButton("One (120 Gems)");
        xOneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (balance >= 120) {
                    XpullOne();
                    balance -= 120; 
                    saveBalanceToFile(); 
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough gems for X One. You need at least 120 gems.", "Insufficient Gems", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(xOneButton);
        
        gbc.gridy++;
        panel.add(buttonPanel, gbc);
        
       
        JOptionPane.showMessageDialog(null, panel, "Banner X Options", JOptionPane.PLAIN_MESSAGE);
    }
  //SHOW BANNER Y
    private void showBannerYOptions() {
       
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        
        JLabel bannerLabel = new JLabel("You have chosen banner Y", SwingConstants.CENTER);
        panel.add(bannerLabel, gbc);
        
    
        JTextArea bannerCardsArea = new JTextArea(
            "Available Cards:\n" +
            "| SSR | Genshiro #00019\n" +
            "| R | Lucas Garcia #00001\n" +
            "| R | Eva Larrson #00002\n" +
            "| R | Kana #00003\n" +
            "| R | Gustion Divon #00005\n" +
            "| R | Cynd #00004\n" +
            "| R | Gann Ignatius #00006\n" +
            "| R | Doroid #00007\n" +
            "| C | Student X #00011\n" +
            "| C | Student Y #00012\n" +
            "| C | Bupi #00013\n"
        );
        bannerCardsArea.setEditable(false);
        bannerCardsArea.setOpaque(false); 
        bannerCardsArea.setFocusable(false); 
        bannerCardsArea.setLineWrap(true); 
        bannerCardsArea.setWrapStyleWord(true); 
        gbc.gridy++;
        panel.add(new JScrollPane(bannerCardsArea), gbc);

       
        JButton showInfoButton = new JButton("Show Info");
        showInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
                openWebPage("https://ibb.co/dbgjvMz");
            }
        });
        
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(showInfoButton, gbc);
        
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton xTenButton = new JButton("Ten (1000 Gems)");
        xTenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (balance >= 1000) {
                    XpullTen();
                    balance -= 1000; 
                    saveBalanceToFile(); 
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough gems for X Ten. You need at least 1000 gems.", "Insufficient Gems", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(xTenButton);
        
        JButton xOneButton = new JButton("One (120 Gems)");
        xOneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (balance >= 120) {
                    XpullOne();
                    balance -= 120; 
                    saveBalanceToFile(); 
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough gems for X One. You need at least 120 gems.", "Insufficient Gems", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(xOneButton);
        
        gbc.gridy++;
        panel.add(buttonPanel, gbc);
        
     
        JOptionPane.showMessageDialog(null, panel, "Banner Y Options", JOptionPane.PLAIN_MESSAGE);
    }
  //SHOW EVENT BANNER X
    private void showEBXOptions() {
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        
        JLabel bannerLabel = new JLabel("You have chosen Event Banner X", SwingConstants.CENTER);
        panel.add(bannerLabel, gbc);
        
       
        JTextArea bannerCardsArea = new JTextArea(
            "Available Cards:\n" +
            "| SSR | Robert Reid #00021\n" +
            "| SR | S. R. Enara Horus #00022\n" +
            "| SR | S. R. Eva Larsson #00023\n" +
            "| R | Space Ranger X #00024\n" +
            "| C | Globo #00025\n" +
            "| C | Stellar Voyager MK II #00026\n"
        );
        bannerCardsArea.setEditable(false); 
        bannerCardsArea.setOpaque(false); 
        bannerCardsArea.setFocusable(false); 
        bannerCardsArea.setLineWrap(true); 
        bannerCardsArea.setWrapStyleWord(true); 
        
        gbc.gridy++;
        panel.add(new JScrollPane(bannerCardsArea), gbc);

       
        JButton showInfoButton = new JButton("Show Info");
        showInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
                openWebPage("https://ibb.co/GHd2rCK");
            }
        });
        
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(showInfoButton, gbc);
        

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton xTenButton = new JButton("Ten (1000 Gems)");
        xTenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (balance >= 1000) {
                	EBXpullTen();
                    balance -= 1000; 
                    saveBalanceToFile(); 
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough gems for X Ten. You need at least 1000 gems.", "Insufficient Gems", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(xTenButton);
        
        JButton xOneButton = new JButton("One (120 Gems)");
        xOneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (balance >= 120) {
                	EBXpullOne();
                    balance -= 120; 
                    saveBalanceToFile(); 
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough gems for X One. You need at least 120 gems.", "Insufficient Gems", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(xOneButton);
        
        gbc.gridy++;
        panel.add(buttonPanel, gbc);
        
     
        JOptionPane.showMessageDialog(null, panel, "Event Banner X Options", JOptionPane.PLAIN_MESSAGE);
    }
  //SHOW EVENT BANNER Y
    private void showEBYOptions() {
       
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
     
        JLabel bannerLabel = new JLabel("You have chosen Event Banner Y", SwingConstants.CENTER);
        panel.add(bannerLabel, gbc);
        
   
        JTextArea bannerCardsArea = new JTextArea(
            "Available Cards:\n" +
                    "| SSR | S. R. Voss Ignatius #00020\n" +
                    "| SSR | Robert Reid #00021\n" +
                    "| SR | Voss Ignatius #00018\n" +
                    "| R | Gann Ignatius #00006\n" +
                    "| R | Space Ranger X #00024\n" +
                    "| C | Stellar Voyager MK II #00026\n"

        );
        bannerCardsArea.setEditable(false);
        bannerCardsArea.setOpaque(false);
        bannerCardsArea.setFocusable(false);
        bannerCardsArea.setLineWrap(true);
        bannerCardsArea.setWrapStyleWord(true); 
        
        gbc.gridy++;
        panel.add(new JScrollPane(bannerCardsArea), gbc);

      
        JButton showInfoButton = new JButton("Show Info");
        showInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              
                openWebPage("https://ibb.co/0VpPwm8");
            }
        });
        
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(showInfoButton, gbc);
        
       
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton xTenButton = new JButton("Ten (1000 Gems)");
        xTenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (balance >= 1000) {
                	EBYpullTen();
                    balance -= 1000; 
                    saveBalanceToFile(); 
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough gems for X Ten. You need at least 1000 gems.", "Insufficient Gems", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(xTenButton);
        
        JButton xOneButton = new JButton("One (120 Gems)");
        xOneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (balance >= 120) {
                	EBYpullOne();
                    balance -= 120; 
                    saveBalanceToFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough gems for X One. You need at least 120 gems.", "Insufficient Gems", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(xOneButton);
        
        gbc.gridy++;
        panel.add(buttonPanel, gbc);
        
     
        JOptionPane.showMessageDialog(null, panel, "Event Banner Y Options", JOptionPane.PLAIN_MESSAGE);
    }
  //SHOW EVENT BANNER Z
    private void showEBZOptions() {
      
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
      
        JLabel bannerLabel = new JLabel("You have chosen Event Banner Z", SwingConstants.CENTER);
        panel.add(bannerLabel, gbc);
        
        
        JTextArea bannerCardsArea = new JTextArea(
            "Available Cards:\n" +
                    "| SR | S. R. Enara Horus #00022\n" +
                    "| SR | S. R. Eva Larsson #00023\n" +
                    "| SSR | Robert Reid #00021\n" +
                    "| SSR | S. R. Voss Ignatius #00020\n" +
                    "| R | Space Ranger X #00024\n" +
                    "| C | Stellar Voyager MK II #00026\n" +
                    "| C | Globo #00025\n" 


        );
        bannerCardsArea.setEditable(false);
        bannerCardsArea.setOpaque(false); 
        bannerCardsArea.setFocusable(false); 
        bannerCardsArea.setLineWrap(true); 
        bannerCardsArea.setWrapStyleWord(true);
        
        gbc.gridy++;
        panel.add(new JScrollPane(bannerCardsArea), gbc);

      
        JButton showInfoButton = new JButton("Show Info");
        showInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
                openWebPage("https://ibb.co/LxQD5gR");
            }
        });
        
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(showInfoButton, gbc);
        
       
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton xTenButton = new JButton("Ten (1000 Gems)");
        xTenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (balance >= 1000) {
                	EBZpullTen();
                    balance -= 1000;
                    saveBalanceToFile(); 
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough gems for X Ten. You need at least 1000 gems.", "Insufficient Gems", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(xTenButton);
        
        JButton xOneButton = new JButton("One (120 Gems)");
        xOneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (balance >= 120) {
                	EBZpullOne();
                    balance -= 120; 
                    saveBalanceToFile(); 
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough gems for X One. You need at least 120 gems.", "Insufficient Gems", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(xOneButton);
        
        gbc.gridy++;
        panel.add(buttonPanel, gbc);
        
       
        JOptionPane.showMessageDialog(null, panel, "Event Banner Z Options", JOptionPane.PLAIN_MESSAGE);
    }
    // FOR THE INVENTORY
    private void showInventory() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              
                JFrame inventoryFrame = new JFrame("Inventory");
                inventoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

               
                JPanel contentPane = new JPanel();
                contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

               
                for (String option : inventory.keySet()) {
                    JPanel itemPanel = createInventoryItemPanel(option, inventory.get(option));
                    contentPane.add(itemPanel);
                }

               
                inventoryFrame.setContentPane(contentPane);

               
                inventoryFrame.pack();
                inventoryFrame.setVisible(true);
                saveInventory();
            }
        });
    }
// INFO BUTTON INVENTORY
    private JPanel createInventoryItemPanel(String option, int quantity) {
       
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        
        JButton infoButton = new JButton("[Info]");
        infoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showInfo(option);
            }
        });

        
        JLabel itemLabel = new JLabel(option + " – owned: " + quantity);

        
        itemPanel.add(infoButton);
        itemPanel.add(itemLabel);

        return itemPanel;
    }

    private String getOptionURL(String option) {
        
        if (option.contains("#00007")) {
            return "https://ibb.co/wBfrr1m";
        } else if (option.contains("#00S13")) {
            return "https://ibb.co/mCfFcb4";
        } else if (option.contains("#00011")) {
            return "https://ibb.co/Wzmw776";
        } else if (option.contains("#00012")) {
            return "https://ibb.co/MnzFs4J";
        } else if (option.contains("#00013")) {
            return "https://ibb.co/6WHjKSG";
        } else if (option.contains("#00014")) {
            return "https://ibb.co/n097gGC";
        } else if (option.contains("#00015")) {
            return "https://ibb.co/pvfJtgD";
        } else if (option.contains("#00017")) {
            return "https://ibb.co/4fyFCCr";
        } else if (option.contains("#00018")) {
            return "https://ibb.co/cJR5DwG";
        } else if (option.contains("#00019")) {
            return "https://ibb.co/7KwbvXt";
        } else if (option.contains("#00016")) {
            return "https://ibb.co/8zRN4jm";
        } else if (option.contains("#00S18")) {
            return "https://ibb.co/nQ2zXrg";
        } else if (option.contains("#000S3")) {
            return "https://ibb.co/VS1w1PX";
        } else if (option.contains("#00003")) {
            return "https://ibb.co/y8sXMGV";
        } else if (option.contains("#00009")) {
            return "https://ibb.co/PThtWT3";
        } else if (option.contains("#00005")) {
            return "https://ibb.co/mvCKF7K";
        } else if (option.contains("#00004")) {
            return "https://ibb.co/r4QCNN1";
        } else if (option.contains("#00002")) {
            return "https://ibb.co/fNq3sCd";
        } else if (option.contains("#000S1")) {
            return "https://ibb.co/0nN7rH2";
        } else if (option.contains("#00001")) {
            return "https://ibb.co/fpxnsfq";
        } else if (option.contains("#00006")) {
            return "https://ibb.co/M7VnFSr";
        } else if (option.contains("#00021")) {
            return "https://ibb.co/3dH5LWm"; 
        } else if (option.contains("#00S21")) {
            return "https://ibb.co/rxtXkCZ"; 
        } else if (option.contains("#00G21")) {
            return "https://ibb.co/mHtHSbP"; 
        } else if (option.contains("#00020")) {
            return "https://ibb.co/WvvnXY1"; 
        } else if (option.contains("#00S20")) {
            return "https://ibb.co/7tNTSjg"; 
        } else if (option.contains("#00G20")) {
            return "https://ibb.co/HKxTQ3x"; 
        } else if (option.contains("#00022")) {
            return "https://ibb.co/Y71ZVKx"; 
        } else if (option.contains("#00S22")) {
            return "https://ibb.co/KG1YqNZ"; 
        } else if (option.contains("#00G22")) {
            return "https://ibb.co/92bthXH"; 
        } else if (option.contains("#00023")) {
            return "https://ibb.co/yfN9gqp"; 
        } else if (option.contains("#00S23")) {
            return "https://ibb.co/yBkVjNw"; 
        } else if (option.contains("#00G23")) {
            return "https://ibb.co/ZYfN6ws"; 
        } else if (option.contains("#00024")) {
            return "https://ibb.co/P5vBcxn"; 
        } else if (option.contains("#00S24")) {
            return "https://ibb.co/0tY3bn1"; 
        } else if (option.contains("#00025")) {
            return "https://ibb.co/PtF5BNr"; 
        } else if (option.contains("#00026")) {
            return "https://ibb.co/r52Gfs6"; 
        } else if (option.contains("#00S19")) {
            return "https://ibb.co/hsKnZbt";
        } else {
            return ""; 
        }
    }

    private void showInfo(String option) {
        String link = getOptionURL(option);
        if (!link.isEmpty()) {
            try {
                
                Desktop.getDesktop().browse(new URI(link));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace(); 
                JOptionPane.showMessageDialog(null, "Failed to open URL. Please check if the URL is valid.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        
        }
    }

   
    
   
    private void XpullOne() {
   
        String[] names = {
                "| SSR | SHINY Genshiro #00S19",
                "| SSR | Genshiro #00019",
                "| R | Enara Horus #00016",
                "| C | Student Y #00012",
                "| C | Student X #00011",
                "| SR | SHINY Voss Ignatius #00S18",
                "| R | Kaden #00017",
                "| SR | Voss Ignatius #00018",
                "| SR | Vlad #00015",
                "| SR | SHINY Li Zhiya #00S13",
                "| SR | Li Zhiya #00013",
                "| SR | Valerie #00014"
        };
        double[] probabilities = {
        		0.0003,
        		0.0010, //
        		0.1955,
                0.2985,
                0.2985,
                0.0005, //
                0.1955,
                0.0030, //
                0.0080, //
                0.0006, //
                0.0080, //
                0.0080 //
        };

       
        Random random = new Random();
        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        int selectedIndex = -1;
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                selectedIndex = i;
                break;
            }
        }

       
        if (selectedIndex != -1) {
            String selectedName = names[selectedIndex];
            JOptionPane.showMessageDialog(null, "You got: " + selectedName, "Pull 1 Result", JOptionPane.INFORMATION_MESSAGE);
            
            if (inventory.containsKey(selectedName)) {
              
                inventory.put(selectedName, inventory.get(selectedName) + 1);
            } else {
                
                inventory.put(selectedName, 1);
            }
            saveInventory(); 
        } else {
            JOptionPane.showMessageDialog(null, "Failed to pull a name", "Pull 1 Result", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void XpullTen() {
        for (int i = 0; i < 10; i++) {
            XpullOne();
        }
    }

    
    private void YpullOne() {
       
        String[] names = {
                "| SSR | SHINY Genshiro #00S19",
                "| SSR | Genshiro #00019",
                "| R | Lucas Garcia #00001",
                "| R | SHINY Lucas Garcia #00009",
                "| R | Eva Larrson #00002",
                "| R | Kana #00003",
                "| R | SHINY Kana #000S3",
                "| R | Gustion Divon #00005",
                "| R | Cynd #00004",
                "| R | Gann Ignatius #00006",
                "| R | Doroid #00007",
                "| C | Student X #00011",
                "| C | Student Y #00012",
                "| C | Bupi #00013"
        };
        double[] probabilities = {
                0.0005, //
                0.0030, //
                0.0854, 
                0.0008,//
                0.0853,
                0.0854,
                0.0008,//
                0.0854,
                0.0854,
                0.0854,
                0.0854,
                0.1324,
                0.1324,
                0.1324,
        };

     
        Random random = new Random();
        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        int selectedIndex = -1;
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                selectedIndex = i;
                break;
            }
        }

        
        if (selectedIndex != -1) {
            String selectedName = names[selectedIndex];
            JOptionPane.showMessageDialog(null, "You got: " + selectedName, "Pull 1 Result", JOptionPane.INFORMATION_MESSAGE);
           
            if (inventory.containsKey(selectedName)) {
                
                inventory.put(selectedName, inventory.get(selectedName) + 1);
            } else {
              
                inventory.put(selectedName, 1);
            }
            saveInventory();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to pull a name", "Pull 1 Result", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void YpullTen() {
        for (int i = 0; i < 10; i++) {
            YpullOne();
        }
    }
  
    private void EBXpullOne() {
     
        String[] names = {
                "| SSR | Robert Reid #00021",
                "| SSR | SHINY Robert Reid #00S21",
                "| SSR | GALACTIC Robert Reid #00G21",
                "| SR | S. R. Enara Horus #00022",
                "| SR | SHINY S. R. Enara Horus #00S22",
                "| SR | GALACTIC S. R. Enara Horus #00G22",
                "| SR | S. R. Eva Larsson #00023",
                "| SR | SHINY S. R. Eva Larsson #00S23",
                "| SR | GALACTIC S. R. Eva Larsson #00G23",
                "| R | Space Ranger X #00024",
                "| R | SHINY Space Ranger X #00S24",
                "| C | Globo #00025",
                "| C | Stellar Voyager MK II #00026",
            
        };
        double[] probabilities = {
                0.0050, //
                0.0008, //
                0.0001, // 
                0.0060,//
                0.0005,//
                0.0001,//
                0.0060,//
                0.0005,//
                0.0001,//
                0.0657, 
                0.0010,//
                0.4571,
                0.4571,
           
        };

       
        Random random = new Random();
        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        int selectedIndex = -1;
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                selectedIndex = i;
                break;
            }
        }

       
        if (selectedIndex != -1) {
            String selectedName = names[selectedIndex];
            JOptionPane.showMessageDialog(null, "You got: " + selectedName, "Pull 1 Result", JOptionPane.INFORMATION_MESSAGE);
           
            if (inventory.containsKey(selectedName)) {
              
                inventory.put(selectedName, inventory.get(selectedName) + 1);
            } else {
                
                inventory.put(selectedName, 1);
            }
            saveInventory(); 
            JOptionPane.showMessageDialog(null, "Failed to pull a name", "Pull 1 Result", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void EBXpullTen() {
        for (int i = 0; i < 10; i++) {
            EBXpullOne();
            
        }
    }
   
    private void EBYpullOne() {
        
        String[] names = {
                "| SSR | Robert Reid #00021",
                "| SSR | SHINY Robert Reid #00S21",
                "| SSR | GALACTIC Robert Reid #00G21",
                "| SSR | S. R. Voss Ignatius #00020",
                "| SSR | SHINY S. R. Voss Ignatius #00S20",
                "| SSR | GALACTIC S. R. Voss Ignatius #00G20",
                "| SR | Voss Ignatius #00018",
                "| SR | SHINY Voss Ignatius #00018",
                "| R | Gann Ignatius #00006",
                "| R | Space Ranger X #00024",
                "| R | SHINY Space Ranger X #00S24",
                "| C | Stellar Voyager MK II #00026",
            
        };
        double[] probabilities = {
                0.0020, //
                0.0003, //
                0.0001, // 
                0.0050,//
                0.0008,//
                0.0001,//
                0.0080,//
                0.0010,//
                0.1600,
                0.1600,
                0.0010,//
                0.6617,
           
        };

       
        Random random = new Random();
        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        int selectedIndex = -1;
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                selectedIndex = i;
                break;
            }
        }

       
        if (selectedIndex != -1) {
            String selectedName = names[selectedIndex];
            JOptionPane.showMessageDialog(null, "You got: " + selectedName, "Pull 1 Result", JOptionPane.INFORMATION_MESSAGE);
          
            if (inventory.containsKey(selectedName)) {
               
                inventory.put(selectedName, inventory.get(selectedName) + 1);
            } else {
             
                inventory.put(selectedName, 1);
            }
            saveInventory(); 
        } else {
            JOptionPane.showMessageDialog(null, "Failed to pull a name", "Pull 1 Result", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void EBYpullTen() {
        for (int i = 0; i < 10; i++) {
            EBYpullOne();
        }
    }
 
    private void EBZpullOne() {
      
        String[] names = {
                "| SSR | Robert Reid #00021",
                "| SSR | SHINY Robert Reid #00S21",
                "| SSR | GALACTIC Robert Reid #00G21",
                "| SSR | S. R. Voss Ignatius #00020",
                "| SSR | SHINY S. R. Voss Ignatius #00S20",
                "| SSR | GALACTIC S. R. Voss Ignatius #00G20",
                "| SR | S. R. Enara Horus #00022",
                "| SR | SHINY S. R. Enara Horus #00S22",
                "| SR | GALACTIC S. R. Enara Horus #00G22",
                "| SR | S. R. Eva Larsson #00023",
                "| SR | SHINY S. R. Eva Larsson #00S23",
                "| SR | GALACTIC S. R. Eva Larsson #00G23",
                "| R | Space Ranger X #00024",
                "| R | SHINY Space Ranger X #00S24",
                "| C | Stellar Voyager MK II #00026",
                "| C | Globo #00025",
            
        };
        double[] probabilities = {
                0.0021, //
                0.0003, //
                0.0001, // 
                0.0021,//
                0.0003,//
                0.0001,//
                0.0100,//
                0.0009,//
                0.0001,//
                0.0100,//
                0.0009,//
                0.0001,//
                0.1700,
                0.0010,//
                0.4010,
                0.4010,
              
           
        };

        
        Random random = new Random();
        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        int selectedIndex = -1;
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                selectedIndex = i;
                break;
            }
        }

      
        if (selectedIndex != -1) {
            String selectedName = names[selectedIndex];
            JOptionPane.showMessageDialog(null, "You got: " + selectedName, "Pull 1 Result", JOptionPane.INFORMATION_MESSAGE);
           
            if (inventory.containsKey(selectedName)) {
          
                inventory.put(selectedName, inventory.get(selectedName) + 1);
            } else {
               
                inventory.put(selectedName, 1);
            }
            saveInventory(); 
        } else {
            JOptionPane.showMessageDialog(null, "Failed to pull a name", "Pull 1 Result", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void EBZpullTen() {
        for (int i = 0; i < 10; i++) {
            EBZpullOne();
        }
    }
//SAVE INVENTORY
    private void saveInventory() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(INVENTORY_FILE))) {
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
//LOAD INVENTORY
    private void loadInventory() {
        inventory = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String itemName = parts[0];
                    int itemCount = Integer.parseInt(parts[1]);
                    inventory.put(itemName, itemCount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
    //OPEN UP EVENT MENU
    private void EventMenu() {
    	 EventMenu EventMenu = new EventMenu();
    	 EventMenu.setVisible(true);
    }
       
   //CLASS NAME
    public static void main(String[] args) {
    	 Finals Finals = new Finals();
         Finals.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Menu game = new Menu();
                game.setExtendedState(JFrame.MAXIMIZED_BOTH);
                game.setVisible(true);
            }
        });
    }
}
