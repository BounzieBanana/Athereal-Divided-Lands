package ADL;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;

import twoclass.SecondClass;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SpaceGame extends JPanel implements ActionListener, MouseMotionListener, MouseListener {
    private int spaceshipHp = 30;
    private int baseHp = 100;
    private int spaceshipWidth = 60;
    private int spaceshipHeight = 60;
    private int spaceshipX = 600;
    private int spaceshipY = 900;
    private int bulletCount = 60;
    private int score = 0;
    private boolean isFiring = false;
    private Timer fireTimer;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Heart> hearts = new ArrayList<>();
    private ArrayList<Shield> shields = new ArrayList<>();
    private Timer gameTimer;
    private Timer enemySpawnTimer;
    private Timer heartSpawnTimer;
    private Timer shieldSpawnTimer;
    private Timer shieldTimer;
    private int enemySpawnRate = 4000;
    private int heartSpawnRate = 25000;
    private int shieldSpawnRate = 35000; 
    private int enemySize = 60;
    private int bulletWidth = 10;
    private int bulletHeight = 20;
    private int heartSize = 20;
    private int shieldSize = 30;
    private boolean isPaused = false;
    private boolean isShieldActive = false;
    private JFrame frame;
    private JPanel pauseMenuPanel;
    private boolean isPauseMenuVisible = false;
    private boolean isReloading = false; 
    private static final String BALANCE_FILE = "balance.txt"; 
    private static final int SCORE_THRESHOLD = 25; 
    private int lastUpdatedScore = 0; 
    private static final int GEMS_PER_SCORE = 45; 
    private int balance;
    
    //WINDOW OPEN
    public SpaceGame() {
        frame = new JFrame("Space Game");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(true);

        frame.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if ((e.getNewState() & Frame.ICONIFIED) == Frame.ICONIFIED) {
                    frame.setState(Frame.NORMAL);
                } else if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
                    frame.setExtendedState(Frame.NORMAL);
                }
            }
        });

        if (!isPaused) {
            frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                    new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                    new Point(0, 0), "blank cursor"));
        }

        frame.add(this);
        frame.setVisible(true);
        frame.addMouseMotionListener(this);
        frame.addMouseListener(this);

       
        Timer delayTimer = new Timer();
        delayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                startSpawning(); 
            }
        }, 2000);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePauseMenu();
                }
            }
        });
    }
//GRACE PERIOD
    private void startSpawning() {
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isPaused) {
                    gameUpdate();
                    repaint();
                }
            }
        }, 0, 16);

        scheduleEnemySpawn();
        heartSpawnTimer = new Timer();
        heartSpawnTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isPaused) {
                    spawnHeart();
                }
            }
        }, 0, heartSpawnRate);

        shieldSpawnTimer = new Timer();
        shieldSpawnTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isPaused) {
                    spawnShield();
                }
            }
        }, 0, shieldSpawnRate);
    }

    private void scheduleEnemySpawn() {
        enemySpawnTimer = new Timer();
        enemySpawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isPaused) {
                    spawnEnemy();
                    adjustEnemySpawnRate(); // Adjust spawn rate based on the score
                    scheduleEnemySpawn(); // Recursively schedule the next enemy spawn
                }
            }
        }, enemySpawnRate);
    }
//MAKES GAME HARDER AS IT LAST LONG
    private void adjustEnemySpawnRate() {
        if (score >= 80 && score < 160 && enemySpawnRate > 3000) {
            enemySpawnRate -= 250;
        } else if (score >= 160 && score < 240 && enemySpawnRate > 2000) {
            enemySpawnRate -= 250;
        } else if (score >= 240 && score < 320 && enemySpawnRate > 1500) {
            enemySpawnRate -= 250;
        } else if (score >= 320 && score < 400 && enemySpawnRate > 1000) {
            enemySpawnRate -= 250;
        } else if (score >= 400 && score < 480 && enemySpawnRate > 500) {
            enemySpawnRate -= 250;
        } else if (score >= 480 && enemySpawnRate > 250) {
            enemySpawnRate -= 50;
        }
    }

    private void gameUpdate() {
    	
    	//BULLETS
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.y -= 20;
            if (b.y < 0) {
                bullets.remove(i);
                i--;
            } else {
                for (int j = 0; j < enemies.size(); j++) {
                    Enemy e = enemies.get(j);
                    if (new Rectangle(b.x, b.y, bulletWidth, bulletHeight).intersects(new Rectangle(e.x, e.y, enemySize, enemySize))) {
                        e.hp -= 5;
                        bullets.remove(i);
                        i--;
                        if (e.hp <= 0) {
                            enemies.remove(j);
                            score += 10;
                        }
                        break;
                    }
                }
            }
        }

        //ENEMIES
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.y += 5;
            if (e.y > spaceshipY && Math.abs(e.x - spaceshipX) < spaceshipWidth) {
                spaceshipHp -= 10;
                enemies.remove(i);
                i--;
                if (spaceshipHp <= 0) {
                    gameOver();
                }
            } else if (e.y >= getHeight() - 30) {
                if (!isShieldActive) {
                    baseHp -= 10;
                    score -= 2;
                }
                enemies.remove(i);
                i--;
                score -= 2;
                if (baseHp <= 0) {
                    gameOver();
                }
            }
        }

        //GREEN HEART ORB THINGY
        for (int i = 0; i < hearts.size(); i++) {
            Heart heart = hearts.get(i);
            heart.y += 3;
            if (new Rectangle(spaceshipX, spaceshipY, spaceshipWidth, spaceshipHeight).intersects(new Rectangle(heart.x, heart.y, heartSize, heartSize))) {
                hearts.remove(i);
                i--;
                spaceshipHp += 10;
                if (spaceshipHp > 30) {
                    spaceshipHp = 30;
                }
            }
        }
        //SCORE TO GEMS
        if (score >= lastUpdatedScore + SCORE_THRESHOLD) {
           
            int scoreDifference = score - lastUpdatedScore;
            int scoreMultiple = scoreDifference / SCORE_THRESHOLD;
            int gemsToAdd = scoreMultiple * GEMS_PER_SCORE;
            
         
            loadBalance();
            

            balance += gemsToAdd;

          
            lastUpdatedScore = score; 
            
      
            saveBalanceToFile(); 
        }
        //SHIELDS
        for (int i = 0; i < shields.size(); i++) {
            Shield shield = shields.get(i);
            shield.y += 3; 
            if (new Rectangle(spaceshipX, spaceshipY, spaceshipWidth, spaceshipHeight).intersects(new Rectangle(shield.x, shield.y, shieldSize, shieldSize))) {
                shields.remove(i);
                i--;
                activateShield();
            } else if (shield.y > getHeight()) {
                shields.remove(i);
                i--;
            }
        }

        //RELOADING - OUT OF BULLETS
        if (bulletCount == 0) {
            reloadBullets();
        }
    }
    //PAUSE when ESC
    private void togglePauseMenu() {
        if (!isPaused && !isPauseMenuVisible) {
            isPaused = true;
            isPauseMenuVisible = true;
            showPauseMenu();
            frame.setCursor(Cursor.getDefaultCursor());
        } else if (isPaused && isPauseMenuVisible) {
            isPaused = false;
            isPauseMenuVisible = false;
            remove(pauseMenuPanel);
            if (!isPaused) {
                frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                        new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                        new Point(0, 0), "blank cursor"));
            }
        }
    }
    //PAUSE MENU
    private void showPauseMenu() {
        pauseMenuPanel = new JPanel();
        pauseMenuPanel.setLayout(new GridLayout(2, 1));
        pauseMenuPanel.setBounds(getWidth() / 2 - 100, getHeight() / 2 - 50, 200, 100);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	loadBalance();
            	EventMenu();
            }

            private void EventMenu() {
            	EventMenu EventMenu = new EventMenu();
           	 EventMenu.setVisible(true);
				
			}
        });

      

        pauseMenuPanel.add(exitButton);

        add(pauseMenuPanel);
        revalidate();
        repaint();
    }

    private void spawnEnemy() {
        int x = (int) (Math.random() * (getWidth() - enemySize));
        enemies.add(new Enemy(x, 0, 20)); 

        if (score >= 50 && score % 100 == 0) {
           
            x = (int) (Math.random() * (getWidth() - enemySize));
            enemies.add(new Enemy(x, 0, 40)); 
        }
    }
//BALANCE CREATOR AND LOADER
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
    private void spawnHeart() {
       
        int minX = heartSize; 
        int maxX = getWidth() - heartSize; 
        
        minX += 20; 
        maxX -= 20; 
        int x = (int) (Math.random() * (maxX - minX)) + minX; 
        hearts.add(new Heart(x, 0));
    }

    private void spawnShield() {
        
        int minX = shieldSize; 
        int maxX = getWidth() - shieldSize; 
      
        minX += 20; 
        maxX -= 20; 
        int x = (int) (Math.random() * (maxX - minX)) + minX; 
        shields.add(new Shield(x, 0)); 
    }

    private void activateShield() {
        isShieldActive = true;
        repaint(); 

        if (shieldTimer != null) {
            shieldTimer.cancel();
        }

        shieldTimer = new Timer();
        shieldTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isShieldActive = false;
                repaint(); 
            }
        }, 5000); 
    }
    private void saveBalanceToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BALANCE_FILE))) {
            writer.println(balance); 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
    private void gameOver() {
        gameTimer.cancel();
        enemySpawnTimer.cancel();
        heartSpawnTimer.cancel();
        shieldSpawnTimer.cancel();
        if (fireTimer != null) {
            fireTimer.cancel();
        }
        JOptionPane.showMessageDialog(this, "Game Over!\nScore: " + score);
        EventMenu();
     	loadBalance();
     	saveBalanceToFile();
    }

    private void EventMenu() {
    	 EventMenu EventMenu = new EventMenu();
    	 EventMenu.setVisible(true);
		
	}

	private void reloadBullets() {
        isReloading = true; 
        repaint(); 
        Timer reloadTimer = new Timer();
        reloadTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                bulletCount = 60;
                isReloading = false; 
                repaint(); 
            }
        }, 2500);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);

        g.setColor(Color.WHITE);
        for (int i = 0; i < 100; i++) {
            int x = (int) (Math.random() * getWidth());
            int y = (int) (Math.random() * getHeight());
            g.fillRect(x, y, 1, 1);
        }

        g.setColor(Color.BLUE);
        g.fillRect(spaceshipX, spaceshipY, spaceshipWidth, spaceshipHeight);

        g.setColor(Color.RED);
        for (Enemy e : enemies) {
            g.fillOval(e.x, e.y, enemySize, enemySize);
        }

        g.setColor(Color.GREEN);
        for (Bullet b : bullets) {
            g.fillRect(b.x, b.y, bulletWidth, bulletHeight);
        }

        g.setColor(Color.GRAY);
        g.fillRect(0, getHeight() - 30, getWidth(), 30);

        g.setColor(Color.WHITE);
        g.drawString("Base HP: " + baseHp, 10, 40);
        g.drawString("Bullets: " + bulletCount, 10, 60);
        g.drawString("Score: " + score, 10, 80);

        g.setColor(Color.GREEN);
        int hpBarWidth = (spaceshipHp * spaceshipWidth) / 30;
        int barOffset = (spaceshipWidth - hpBarWidth) / 2;
        g.fillRect(spaceshipX + barOffset, spaceshipY - 30, hpBarWidth, 20);
        g.setColor(Color.WHITE);
        g.drawString(spaceshipHp + "/30", spaceshipX + 10, spaceshipY - 12);

        g.setColor(Color.GREEN);
        for (Heart heart : hearts) {
            g.fillOval(heart.x, heart.y, heartSize, heartSize);
        }

        g.setColor(Color.YELLOW);
        for (Shield shield : shields) {
            g.fillOval(shield.x, shield.y, shieldSize, shieldSize);
        }

       
        if (isReloading) {
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth("Reloading...")) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString("Reloading...", x, y);
        }

     
        if (isShieldActive) {
            g.setColor(Color.YELLOW);
            g.drawRect(5, 100, 100, 20);
            g.drawString("Shield Active", 10, 115);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isPaused) {
            spaceshipX = e.getX() - spaceshipWidth / 2;
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!isPaused) {
            spaceshipX = e.getX() - spaceshipWidth / 2;
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (!isPaused) {
            if (isReloading) {
                return; 
            }
            
            if (e.getButton() == MouseEvent.BUTTON1) {
                isFiring = true;
                startFiring();
            } else if (e.getButton() == MouseEvent.BUTTON3) { 
                reloadBullets(); 
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!isPaused && !isReloading) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                isFiring = false;
                if (fireTimer != null) {
                    fireTimer.cancel();
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    private void startFiring() {
        fireTimer = new Timer();
        fireTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isFiring && bulletCount > 0) {
                    bullets.add(new Bullet(spaceshipX + spaceshipWidth / 2 - bulletWidth / 2, spaceshipY));
                    bulletCount--;
                } else {
                    fireTimer.cancel();
                }
            }
        }, 0, 100);
    }

    public static void main(String[] args) {
        new SpaceGame();
    }
}

class Bullet {
    int x, y;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Enemy {
    int x, y, hp;

    public Enemy(int x, int y, int hp) {
        this.x = x;
        this.y = y;
        this.hp = hp;
    }
}

class Heart {
    int x, y;

    public Heart(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Shield {
    int x, y;

    public Shield(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public static void main(String[] args) {
   
    SpaceGame SpaceGame = new SpaceGame();
    SpaceGame.setVisible(true);
}}