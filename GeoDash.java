import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class GeoDash extends JComponent implements ActionListener, KeyListener {
    private static final int WIDTH = 1200; // width of frame
    private static final int HEIGHT = 600; // height of frame
    private static final int SQUARE_SIZE = 40; // square/player size
    private static final int FLOOR_HEIGHT = HEIGHT - 100; // floor 
    private static final int JUMP_SPEED = 20; // jump 
    private static final int GRAVITY = 1; // gravity
    private static int playerSpeed; // speed
    private int velocityY; // jump velocity
    private boolean isJumping, isOnStar, isOnPlatform, gameOver, gameWon;
    private boolean isRunning, isPaused, isMenu;
    private double playerX, playerY; // player/square x y coords 
    private int cameraX; // help determine the "camera view"
    private static final int OBSTACLE_WIDTH = 40; // typical obstacle width
    private static final int OBSTACLE_HEIGHT = 60; // typical obstacle height
    private static final int SPIKE_WIDTH = 40; // typical spike width
    private static final int SPIKE_HEIGHT = 50; // typical spike height 
    private static final int TALL_OBSTACLE_HEIGHT = 120; // typical tall obstacle height
    private List<Obstacle> obstacles; // array of obstacle/block objects
    private List<Star> stars; // array of star objects
    private List<Spikes> spikes; // array of spike objects
    private List<TallObstacles> tallObstacles; // array of tall obstacle/block objects
    private List<Platform> platforms; // array of platform objects
    private Win win; // instance of a win line
    private Clip music, deathNoise, winNoise, menuNoise; // music and audio
    private BufferedImage background, menuBackground, gameOverSprite, 
    gameWonSprite, pausedSprite, playingSprite, restartButtonSprite, 
    playerSprite, playerSprite2, playerSprite3, playerSprite4, playerSprite5, 
    playerSprite6, numberSprite, replaySprite, playingButton, pausedButton,
    gameName, instructions, instructions2, startSprite; // images
    public static BufferedImage selectedSprite; // selected player sprite
    private JButton pauseButton, restartButton, startButton; // buttons
    private JButton p1, p2, p3, p4, p5, p6; // buttons for character selection
    private static int attempts = 0; // number of times to win
    private static int times = 0; // number of total times
    private JLabel attemptsLabel, percentageLabel; // label for the number of attempts
    private Font customFont; // font for the number of attempts
    
    public void init() {
    	playerSpeed = 5;
    	velocityY = 0; // jump velocity
    	isJumping = false; // turns true when player is mid-air
        isOnStar = false; // turns true when player touches a star
		isOnPlatform = false; // turns true when a player is on a platform
        gameOver = false; // turns true when collision detected
        gameWon = false; // turns true when player touches win line
        isRunning = true; // turns false when game ends (either through collision or win)
        if(times == 0) {
        	isMenu = true;
        	isPaused = true;
        } else {
        	isMenu = false;
        	isPaused = false;
        }
        
    	Timer timer = new Timer(10, this);
    	timer.start();
    	
    	playerX = 300; // initial x position
        playerY = FLOOR_HEIGHT - SQUARE_SIZE; // initial y position
        cameraX = 0;
    	
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true); // allows panel to receive key events
        addKeyListener(this);

        obstacles = new ArrayList<>();
        stars = new ArrayList<>();
        spikes = new ArrayList<>();
        tallObstacles = new ArrayList<>();
        platforms = new ArrayList<>();

        generateMap();
		
        win = new Win(15000+WIDTH,0);
        
        images();
        audioStreams();
        font();
    }

    public GeoDash() {
    	init();
    }
    
    public void font() {
    	try {
    		  InputStream is = GeoDash.class.getResourceAsStream("Bubblegum.ttf"); // Adjust the path based on your project structure
              customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(40f); // Specify the font size you want
              GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
              ge.registerFont(customFont);
    	} catch (IOException e) {
    	    e.printStackTrace();
    	} catch(FontFormatException e) {
    	    e.printStackTrace();
    	}
    }
    
    public void images() {
    	try {
			pausedButton = ImageIO.read(getClass().getResource("pause_button.png"));		
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	try {
			playingButton = ImageIO.read(getClass().getResource("play_button.png"));		
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	try {
			restartButtonSprite = ImageIO.read(getClass().getResource("Play_Again.png"));		
    	} catch (IOException e) { 
			e.printStackTrace();
		}
        
        try {
			background = ImageIO.read(getClass().getResource("darkened-background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			menuBackground = ImageIO.read(getClass().getResource("darkened-menu-background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			gameName = ImageIO.read(getClass().getResource("gamename.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			instructions = ImageIO.read(getClass().getResource("instructions.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			instructions2 = ImageIO.read(getClass().getResource("instructions2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			playerSprite = ImageIO.read(getClass().getResource("squareIcon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			playerSprite2 = ImageIO.read(getClass().getResource("squareIcon2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			playerSprite3 = ImageIO.read(getClass().getResource("squareIcon3.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			playerSprite4 = ImageIO.read(getClass().getResource("squareIcon4.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			playerSprite5 = ImageIO.read(getClass().getResource("squareIcon5.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			playerSprite6 = ImageIO.read(getClass().getResource("squareIcon6.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			gameOverSprite = ImageIO.read(getClass().getResource("GameOver.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			gameWonSprite = ImageIO.read(getClass().getResource("YouWin.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			playingSprite = ImageIO.read(getClass().getResource("Playing.png"));
        } catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			pausedSprite = ImageIO.read(getClass().getResource("Paused.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			numberSprite = ImageIO.read(getClass().getResource("NumberOfAttempts.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			replaySprite = ImageIO.read(getClass().getResource("replay.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			startSprite = ImageIO.read(getClass().getResource("start.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}	
    }
    
    public void audioStreams() {
    	try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("background_music.wav"));
            music = AudioSystem.getClip();
            music.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

		try {
        	AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(getClass().getResource("game_start.wav"));
        	menuNoise = AudioSystem.getClip();
        	menuNoise.open(audioInputStream2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
        	AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(getClass().getResource("death_sound.wav"));
        	deathNoise = AudioSystem.getClip();
        	deathNoise.open(audioInputStream2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
        	AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(getClass().getResource("level_complete_sound.wav"));
        	winNoise = AudioSystem.getClip();
        	winNoise.open(audioInputStream2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startMusic() {
        // music in a thread
        new Thread(() -> {
            try {
                music.loop(Clip.LOOP_CONTINUOUSLY); // plays audio in a loop
                while (isRunning && !isPaused) { // plays music while the game is running and game isn't paused
                		Thread.sleep(100); 
                }
                music.stop(); // when the player dies, the clip will stop (while loop ends)
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
	private void generateMap() {
		obstacles.clear();
		spikes.clear();
		tallObstacles.clear();
		platforms.clear();

		// intialize position variables for different obstacles
		int obsPos = FLOOR_HEIGHT - OBSTACLE_HEIGHT;
        int tallPos = FLOOR_HEIGHT - TALL_OBSTACLE_HEIGHT;
    	int starPos = FLOOR_HEIGHT;
    	int spikePos = FLOOR_HEIGHT;
		int platformPos = FLOOR_HEIGHT;
    	
    	obstacles.add(new Obstacle(WIDTH,obsPos));
		obstacles.add(new Obstacle(WIDTH + 40, obsPos));
		obstacles.add(new Obstacle(WIDTH + 80, obsPos));
		obstacles.add(new Obstacle(WIDTH + 120, obsPos));
		obstacles.add(new Obstacle(WIDTH + 160, obsPos));
		//First spike after square obs
		spikes.add(new Spikes(WIDTH + 220, spikePos));
		spikes.add(new Spikes(WIDTH + 260, spikePos));
		spikes.add(new Spikes(WIDTH + 300, spikePos));
		spikes.add(new Spikes(WIDTH + 340, spikePos));

		//Platform at 360
		platforms.add(new Platform(WIDTH+360, platformPos-60, 140, 80));
		obstacles.add(new Obstacle(WIDTH+500, obsPos));

		spikes.add(new Spikes(WIDTH + 500 + 60, spikePos));
		spikes.add(new Spikes(WIDTH + 500 + 100, spikePos));
		spikes.add(new Spikes(WIDTH + 500 + 140, spikePos));
		spikes.add(new Spikes(WIDTH + 500 + 180, spikePos));

		platforms.add(new Platform(WIDTH+700, platformPos-60, 140, 80));
    	obstacles.add(new Obstacle(WIDTH+800,obsPos));
		obstacles.add(new Obstacle(WIDTH+840,obsPos));

		tallObstacles.add(new TallObstacles(WIDTH+1200,tallPos));
		tallObstacles.add(new TallObstacles(WIDTH+1240, tallPos));
		tallObstacles.add(new TallObstacles(WIDTH+1280, tallPos));

		platforms.add(new Platform(WIDTH+1310, platformPos-60, 140, 80));

		spikes.add(new Spikes(WIDTH + 1470, spikePos));
		spikes.add(new Spikes(WIDTH + 1510, spikePos));
		spikes.add(new Spikes(WIDTH + 1550, spikePos));
		spikes.add(new Spikes(WIDTH + 1590, spikePos));

		// obstacles.add(new Obstacle(WIDTH+1610,obsPos));
		platforms.add(new Platform(WIDTH+1610, platformPos-60, 600, 80));

		spikes.add(new Spikes(WIDTH + 1800, spikePos - 60));
		spikes.add(new Spikes(WIDTH + 1800+40, spikePos - 60));

		spikes.add(new Spikes(WIDTH + 2100, spikePos - 60));

		tallObstacles.add(new TallObstacles(WIDTH+2400,tallPos));
        tallObstacles.add(new TallObstacles(WIDTH+2440,tallPos));

		tallObstacles.add(new TallObstacles(WIDTH+2700,tallPos));
        tallObstacles.add(new TallObstacles(WIDTH+2740,tallPos));

		tallObstacles.add(new TallObstacles(WIDTH+3000,tallPos));
		tallObstacles.add(new TallObstacles(WIDTH+3040,tallPos));

		spikes.add(new Spikes(WIDTH + 3400, spikePos));
		spikes.add(new Spikes(WIDTH + 3400+40, spikePos));

		spikes.add(new Spikes(WIDTH + 3700, spikePos));
		spikes.add(new Spikes(WIDTH + 3740, spikePos));

		spikes.add(new Spikes(WIDTH + 4000, spikePos));

		//spike
		spikes.add(new Spikes(WIDTH + 4270, spikePos));
		spikes.add(new Spikes(WIDTH + 4310, spikePos));
    	// square obs between the spikes
    	obstacles.add(new Obstacle(WIDTH+4330,obsPos));
		obstacles.add(new Obstacle(WIDTH+4350,obsPos));
		//spike
		spikes.add(new Spikes(WIDTH + 4410, spikePos));
		spikes.add(new Spikes(WIDTH + 4450 , spikePos));
		spikes.add(new Spikes(WIDTH + 4490, spikePos));
		
		spikes.add(new Spikes(WIDTH + 5000, spikePos));
		spikes.add(new Spikes(WIDTH + 5000+ 40, spikePos));
		spikes.add(new Spikes(WIDTH + 5000 + 80, spikePos));

		obstacles.add(new Obstacle(WIDTH + 5100, obsPos));

		for(int i = 0; i < 23; i++) {
    		spikes.add(new Spikes(WIDTH+5160 + 40*i, spikePos));
    	}
		
		platforms.add(new Platform(WIDTH + 5220, platformPos - 200, 160, 20));
		platforms.add(new Platform(WIDTH + 5220 + 300, platformPos - 350, 160, 20));
		platforms.add(new Platform(WIDTH+5200+600, platformPos - 120, 160, 20));
		spikes.add(new Spikes(WIDTH+5200+740, spikePos-120));
		
		for (int i = 0; i < 5;i++){
			spikes.add(new Spikes(WIDTH + 6700 + 40*i, spikePos));
		}
    	stars.add(new Star(WIDTH+6650+ 120, starPos-100));

    	obstacles.add(new Obstacle(WIDTH+7630,obsPos));
		stars.add(new Star(WIDTH + 7900, starPos - 150));

		for (int i =0; i < 11 ;i++){
			spikes.add(new Spikes(WIDTH + 7650+ 40*i, spikePos));
		}

		platforms.add(new Platform(WIDTH+8070, platformPos-60,40,60));

		tallObstacles.add(new TallObstacles(WIDTH+8270,tallPos));
		tallObstacles.add(new TallObstacles(WIDTH+8590,tallPos));
		tallObstacles.add(new TallObstacles(WIDTH+8890,tallPos));

		for (int i = 0; i < 5; i++){
			spikes.add(new Spikes(WIDTH+8950+40*i,spikePos));
		}

		platforms.add(new Platform(WIDTH+9130, platformPos-60,40,60));

		obstacles.add(new Obstacle(WIDTH + 9450+ 200, obsPos));

		for (int i = 0; i < 9; i++){
			spikes.add(new Spikes(WIDTH+9510+200+40*i,spikePos));
		}
		stars.add(new Star(WIDTH + 9700+200, starPos - 160));
		tallObstacles.add(new TallObstacles(WIDTH+9900+150,tallPos));

    	obstacles.add(new Obstacle(WIDTH+10660,obsPos));
        tallObstacles.add(new TallObstacles(WIDTH+10860,tallPos));

		for (int i =0; i < 55 ;i++){
			spikes.add(new Spikes(WIDTH+10880+40*i,spikePos));
		}

		stars.add(new Star(WIDTH+11080,starPos - 160));
		platforms.add(new Platform(WIDTH+11250,platformPos-200,160,20));

		platforms.add(new Platform(WIDTH+11450,platformPos-400,160,20));
		stars.add(new Star(WIDTH + 11700, starPos - 200));

		// platforms.add(new Platform(WIDTH+11700,platformPos-600,160,20));
		platforms.add(new Platform(WIDTH+11850,platformPos-180,160,20));

		stars.add(new Star(WIDTH + 12100, starPos - 160));

		platforms.add(new Platform(WIDTH+12250,platformPos-180,400,20));
    	spikes.add(new Spikes(WIDTH+12300+100,spikePos-180));
		spikes.add(new Spikes(WIDTH+12300+100+40,spikePos-180));

		platforms.add(new Platform(WIDTH+12800,platformPos-200,170,20));

		// spikes.add(new Spikes(WIDTH+13000+300,spikePos));
        spikes.add(new Spikes(WIDTH+13040+300,spikePos));
        spikes.add(new Spikes(WIDTH+13080+300,spikePos));
        spikes.add(new Spikes(WIDTH+13120+300,spikePos));
		platforms.add(new Platform(WIDTH+13080+40+300, platformPos-120, 160, 20));
        spikes.add(new Spikes(WIDTH+13080+300+180,spikePos-120));

		spikes.add(new Spikes(WIDTH + 13660 + 300, spikePos));
		spikes.add(new Spikes(WIDTH + 13660 + 300 + 40, spikePos));
		obstacles.add(new Obstacle(WIDTH + 13660 + 300 + 60, obsPos));
		for (int i =0; i < 4 ;i++){
			spikes.add(new Spikes(WIDTH+14020+60+40*i,spikePos));
		}
		tallObstacles.add(new TallObstacles(WIDTH + 14220, tallPos));

	}
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        drawPlayer(g);
        drawFloor(g);
        drawObstacles(g);
        drawSpikes(g);
        drawTallObstacles(g);
        drawPlatforms(g);
		drawStars(g, WIDTH - 200, FLOOR_HEIGHT - (TALL_OBSTACLE_HEIGHT + 10), 30); // draw stars into the frame
		
        if(times == 1 && isMenu == true) {
        	int newWidth = (int)(menuBackground.getWidth()*0.495);
			int newHeight = (int)(menuBackground.getHeight()*0.495);
			g.drawImage(menuBackground,0,0, newWidth, newHeight, 0,0, 
					menuBackground.getWidth(), menuBackground.getHeight(),null);
        	
        	int newWidth1 = (int)(gameName.getWidth()*0.8);
			int newHeight1 = (int)(gameName.getHeight()*0.8);
			g.drawImage(gameName,225,20, 225+newWidth1, 20+newHeight1, 0,0, 
					gameName.getWidth(), gameName.getHeight(),null);
        	
        	int newWidth2 = (int)(instructions.getWidth()*0.2);
			int newHeight2 = (int)(instructions.getHeight()*0.2);
			g.drawImage(instructions,320,350, 320+newWidth2, 350+newHeight2, 0,0, 
					instructions.getWidth(), instructions.getHeight(),null);
			
			int newWidth3 = (int)(instructions2.getWidth()*0.15);
			int newHeight3 = (int)(instructions2.getHeight()*0.15);
			g.drawImage(instructions2,375,560, 375+newWidth3, 560+newHeight3, 0,0, 
					instructions2.getWidth(), instructions2.getHeight(),null);
			
			if(selectedSprite == playerSprite) {
				g.setColor(Color.WHITE);
				g.drawRect(299,499,42,42);
			} else if(selectedSprite == playerSprite2) {
				g.setColor(Color.WHITE);
				g.drawRect(399,499,42,42);
			} else if(selectedSprite == playerSprite3) {
				g.setColor(Color.WHITE);
				g.drawRect(499,499,42,42);
			} else if(selectedSprite == playerSprite4) {
				g.setColor(Color.WHITE);
				g.drawRect(599,499,42,42);
			} else if(selectedSprite == playerSprite5) {
				g.setColor(Color.WHITE);
				g.drawRect(699,499,42,42);
			} else if(selectedSprite == playerSprite6) {
				g.setColor(Color.WHITE);
				g.drawRect(799,499,42,42);
			}
        }
        
        if(gameOver == true) {
        	g.drawImage(gameOverSprite, 100, 200, null);
		}
        
        if(gameWon == true) {
        	g.drawImage(gameWonSprite, 175, 200, null);
        	int newWidth = (int)(numberSprite.getWidth()*0.15);
			int newHeight = (int)(numberSprite.getHeight()*0.15);
			
        	g.drawImage(numberSprite,490,420, 490+newWidth, 420+newHeight, 0,0, 
        			numberSprite.getWidth(), numberSprite.getHeight(),null);
        }
        
        if(gameWon == true || gameOver == true) {
        	int newWidth = (int)(restartButtonSprite.getWidth()*0.3);
			int newHeight = (int)(restartButtonSprite.getHeight()*0.3);
        	g.drawImage(restartButtonSprite,440,380, 440+newWidth, 380+newHeight, 0,0, 
        			restartButtonSprite.getWidth(), restartButtonSprite.getHeight(),null);
        }
        
        if(isPaused == false && (!gameOver && !gameWon) && isMenu == false) {
        	int newWidth = (int)(playingSprite.getWidth()*0.3);
			int newHeight = (int)(playingSprite.getHeight()*0.3);
        	g.drawImage(playingSprite,120,60, 120+newWidth, 60+newHeight, 0,0, 
					playingSprite.getWidth(), playingSprite.getHeight(),null);
    	} 
        
        if(isPaused == true && (!gameOver && !gameWon)&& isMenu == false) {
        	int newWidth = (int)(pausedSprite.getWidth()*0.3);
			int newHeight = (int)(pausedSprite.getHeight()*0.3);
        	g.drawImage(pausedSprite, 120,60, 120+newWidth, 60+newHeight, 0,0, 
					pausedSprite.getWidth(), pausedSprite.getHeight(),null);
    	} 
    }

    private void drawPlayer(Graphics g) {
       g.setColor(Color.CYAN);
       g.drawImage(selectedSprite, (int)(playerX - cameraX), (int)playerY, null);
    }
    
    private void drawFloor(Graphics g) {
    	g.setColor(Color.BLACK);
    	g.fillRect(0, FLOOR_HEIGHT, 10000, 400);
    }
    
    private void drawObstacles(Graphics g) {
        g.setColor(Color.BLACK);
        
        for (Obstacle obstacle : obstacles) {
            g.fillRect(obstacle.x - cameraX, obstacle.y, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
        }
    }
    
    private void drawStars(Graphics g, int x, int y, int size ){
    	g.setColor(Color.ORANGE);
        
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];
        double angle = Math.PI/5;

    	for(Star star : stars) {
            for (int i = 0; i < 10; i++){
                // if i is even, calculate point on outside of star
                if ( i % 2 == 0) {
                    xPoints[i] = star.x + (int) (Math.cos(i*angle) * size) - cameraX;
                    yPoints[i] = star.y + (int) (Math.sin(i*angle) * size);
                } else { // odd, calculates point on inside
                    xPoints[i] = star.x + (int) (Math.cos(i*angle) * size * 0.4) - cameraX; //multiplying by 0.4 to change distance from center
                    yPoints[i] = star.y + (int) (Math.sin(i*angle) * size * 0.4);
                }
            }

            g.fillPolygon(xPoints, yPoints, 10);
    	}
    }
    
    private void drawSpikes(Graphics g) {
        g.setColor(Color.BLACK);  // Color of the spikes
        for (Spikes spike : spikes) {
            int[] xPoints = {spike.x-cameraX, spike.x - SPIKE_WIDTH/2-cameraX, spike.x + SPIKE_WIDTH/2-cameraX};
            int[] yPoints = {spike.y - SPIKE_HEIGHT, spike.y, spike.y};  // Adjusting y points to raise the tip above the base
            g.fillPolygon(xPoints, yPoints, 3);  // Draws a filled triangle
        }
    }

    private void drawTallObstacles(Graphics g){
        g.setColor(Color.BLACK);
        for (TallObstacles tallObstacle : tallObstacles) {
            g.fillRect(tallObstacle.x - cameraX, tallObstacle.y, OBSTACLE_WIDTH, TALL_OBSTACLE_HEIGHT);
        }
    }
    
    public void drawPlatforms(Graphics g) {
        g.setColor(Color.BLACK); // Platform color
        for (Platform platform : platforms) {
            g.fillRect(platform.x - cameraX, platform.y, platform.width, platform.height);
        }
    }
    
    public void actionPerformed(ActionEvent e) {
    	if (isRunning) { // if game is running, do this
    	      if(!isPaused) { // if the game is not paused
    	    	  movePlayer();
        	      applyGravity();
        	      moveCamera();
        	      updatePercentageLabel();
        	      checkCollisions();
        	      checkCollisionsSpikes();
        	      checkCollisonsPlatforms();
        	      checkCollisonsStar();
        	      checkCollisionsTall();
        	      checkWinCollision(); 
    	      } 
    	      repaint();
    	 }		
    }
    
    private void movePlayer() {
    	// have to move player in response to camera movement 
        playerX += playerSpeed;
    }
    
    private void applyGravity() {
        if (isJumping) {
        	// if player is in air, apply gravity
            velocityY -= GRAVITY;
            playerY -= velocityY;
            
            if (playerY >= FLOOR_HEIGHT - SQUARE_SIZE) {
            	// if player is on the ground (or below ground), set player position to on ground
            	// and set the y velocity to 0 and jump boolean to false
                playerY = FLOOR_HEIGHT - SQUARE_SIZE;
                velocityY = 0;
                isJumping = false;
            }
        }
    }
    
    private void moveCamera() {
    	// shifts camera view 
        cameraX += playerSpeed;
    }
    
    private void checkCollisions() {
        Rectangle playerBounds = new Rectangle((int)playerX, (int)playerY, SQUARE_SIZE, SQUARE_SIZE);
        
        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleBounds = new Rectangle(obstacle.x, obstacle.y+10, OBSTACLE_WIDTH, OBSTACLE_HEIGHT-20);
            Rectangle safeBounds = new Rectangle(obstacle.x, obstacle.y, OBSTACLE_WIDTH, 20);
            if (playerBounds.intersects(obstacleBounds)) {
                gameOver = true;
                deathNoise.start();
                endOfGame();
            }
            
            if((playerBounds.intersects(safeBounds)) && !(playerBounds.intersects(obstacleBounds))) {
            	playerY = obstacle.y - SQUARE_SIZE; 
                velocityY = 0; 
                isOnPlatform = true; 
            }
        }  
    }

    private void checkCollisionsTall() {
        Rectangle playerBounds = new Rectangle((int)playerX, (int)playerY, SQUARE_SIZE, SQUARE_SIZE);

        for (TallObstacles tallObstacle : tallObstacles) {
            Rectangle tallObstacleBounds = new Rectangle(tallObstacle.x, tallObstacle.y+20, OBSTACLE_WIDTH, TALL_OBSTACLE_HEIGHT-20);
            Rectangle safeBounds = new Rectangle(tallObstacle.x, tallObstacle.y, OBSTACLE_WIDTH, 20);
            if (playerBounds.intersects(tallObstacleBounds)) {
                gameOver = true;
                deathNoise.start();
                endOfGame();
            }
            
            if((playerBounds.intersects(safeBounds)) && !(playerBounds.intersects(tallObstacleBounds))) {
            	playerY = tallObstacle.y - SQUARE_SIZE;  
                velocityY = 0;
                isOnPlatform = true;  
            }
        }  
    }
    
    private void checkCollisionsSpikes() {
        Rectangle playerBounds = new Rectangle((int)playerX, (int)playerY, SQUARE_SIZE, SQUARE_SIZE);
        for (Spikes spikes : spikes) {
            Rectangle spikesBounds = new Rectangle(spikes.x - 10, spikes.y - 10, 30, 30); // Approximate bounds
            if (playerBounds.intersects(spikesBounds)) {
                gameOver = true; // Example action on collision
                deathNoise.start();
                endOfGame();
            }
        }
    }
    
    private void checkCollisonsPlatforms() {
        Rectangle playerBounds = new Rectangle((int)playerX, (int)playerY + SQUARE_SIZE, SQUARE_SIZE, 1);
        for (Platform platform : platforms) {
        	Rectangle platformBounds = new Rectangle(platform.x, platform.y, platform.width, platform.height);
        	if(playerBounds.intersects(platformBounds)) {
        		playerY = platform.y - SQUARE_SIZE;  
        		velocityY = 0;
        		isOnPlatform = true;  
        	}
        }
    }

	private void checkWinCollision() {
		Rectangle playerBounds = new Rectangle((int)playerX, (int)playerY, SQUARE_SIZE, SQUARE_SIZE);
		Rectangle winBounds = new Rectangle(win.x, win.y, 10, FLOOR_HEIGHT);
		if(playerBounds.intersects(winBounds)) {
			gameWon =  true;
			winNoise.start(); 
			endOfGameWon();
		}
	}
    
    private void checkCollisonsStar() {
    	Rectangle playerBounds = new Rectangle((int)playerX, (int)playerY, SQUARE_SIZE, SQUARE_SIZE);
    	for(Star star : stars) {
    		Rectangle starBounds = new Rectangle(star.x-5, star.y-5, 43, 43);
    		//star.x + 5 , star.y - 5
    		if(playerBounds.intersects(starBounds)) {
    			isOnStar = true;
    		} 
    	}
    }
    
    public void endOfGame() {
    	if(gameOver == true) {
    		percentageLabel.setVisible(false);
    		restartButton.setVisible(true);
    		playerSpeed = 0;
    		isJumping = true; // player might not actually be mid-air
    		// just prevents them from jumping again
    		pauseButton.setVisible(false);
    		isRunning = false;
    	}
    }
    
    public void endOfGameWon() {
    	if(gameWon == true) {
    		percentageLabel.setVisible(false);
    		restartButton.setVisible(true);
    		attemptsLabel.setVisible(true);
    		playerSpeed = 0;
    		isJumping = true;
    		pauseButton.setVisible(false);
    		isRunning = false;
    		attempts = 0;
    	}
    }
    
    public void pause() {
    	startMusic(); // will resume music 
    	startButton.setVisible(false);
    	p1.setVisible(false);
    	p2.setVisible(false);
    	p3.setVisible(false);
    	p4.setVisible(false);
    	p5.setVisible(false);
    	p6.setVisible(false);
    	percentageLabel.setVisible(true);
    	
    	pauseButton.setVisible(true);
    	isMenu = false;
    	if(isPaused == false) {
    		this.pauseButton.setIcon(new ImageIcon(this.playingButton));
    	} else if(isPaused == true) {
    		this.pauseButton.setIcon(new ImageIcon(this.pausedButton));
    	}
    	
    	isPaused = !isPaused;
    }
    
    public void selection(int choice) {
    	switch(choice) {
    	case 1:
    		selectedSprite = playerSprite;
    		break;
    	case 2:
    		selectedSprite = playerSprite2;
    		break;
    	case 3:
    		selectedSprite = playerSprite3;
    		break;
    	case 4:
    		selectedSprite = playerSprite4;
    		break;
    	case 5:
    		selectedSprite = playerSprite5;
    		break;
    	case 6:
    		selectedSprite = playerSprite6;
    		break;
    	default:
    		selectedSprite = playerSprite;
    		break;
    	}
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_SPACE) {
        	if(isOnStar  == true || isOnPlatform == true) {
        		isOnStar = false;
        		isJumping = true;
				isOnPlatform = false;
        		velocityY = JUMP_SPEED;
        	} else if (isJumping == false) {
        		isJumping = true;
        		velocityY = JUMP_SPEED;
        	}
        }
    }
    
    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
    
    public void updatePercentageLabel() {
    	double progress = (double)((playerX-300)/(win.x-300))*100;
    	percentageLabel.setText(String.format("%d%%",(int)progress));
    }
    
    public void flush() {
    	background.flush();
        restartButtonSprite.flush();
        gameOverSprite.flush();
        gameWonSprite.flush();
        pausedSprite.flush();
        playingSprite.flush();
        playerSprite.flush();
        menuBackground.flush();
        restartButtonSprite.flush();
        playerSprite.flush();
        playerSprite2.flush();
        playerSprite3.flush();
        playerSprite4.flush();
        playerSprite5.flush();
        playerSprite6.flush();
        numberSprite.flush();
        replaySprite.flush();
        playingButton.flush();
        pausedButton.flush();
        gameName.flush();
        instructions.flush();
        instructions2.flush();
        startSprite.flush();
        selectedSprite.flush();
        winNoise.flush();
        deathNoise.flush();
        music.flush();
    }
    
    public void restart() {
    	attempts++;
    	times++;
    	JFrame frame = new JFrame("Geo Dash");
		frame.add(this, BorderLayout.CENTER);
		
		if(times == 1) {
			selectedSprite = playerSprite;
		}
		
		this.pauseButton = new JButton(new ImageIcon(this.pausedButton));
		if(times == 1 && isMenu == true) {
			menuNoise.start();
			this.pauseButton.setVisible(false);
		}
		
		this.pauseButton.setOpaque(false);
		this.pauseButton.setFocusPainted(false);
		this.pauseButton.setBorderPainted(false);
		this.pauseButton.setContentAreaFilled(false);
		this.pauseButton.setFocusable(false);
		this.pauseButton.setBounds(50, 50, 50, 50);
		
		this.pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(times == 1 && isMenu == true) {
					pause();
					menuNoise.stop();
				} else {
					pause();
				}
			}
		});
		
		frame.add(this.pauseButton);
		
		this.p1 = new JButton(new ImageIcon(this.playerSprite));
		p1.setOpaque(false);
		p1.setFocusPainted(false);
		p1.setBorderPainted(false);
		p1.setContentAreaFilled(false);
		this.p1.setBounds(300,500,40, 40);
		if(times == 1) {
			p1.setVisible(true);
		} else {
			p1.setVisible(false);
		}
		this.p2 = new JButton(new ImageIcon(this.playerSprite2));
		p2.setOpaque(false);
		p2.setFocusPainted(false);
		p2.setBorderPainted(false);
		p2.setContentAreaFilled(false);
		this.p2.setBounds(400,500,40, 40);
		if(times == 1) {
			p2.setVisible(true);
		} else {
			p2.setVisible(false);
		}
		this.p3 = new JButton(new ImageIcon(this.playerSprite3));
		p3.setOpaque(false);
		p3.setFocusPainted(false);
		p3.setBorderPainted(false);
		p3.setContentAreaFilled(false);
		this.p3.setBounds(500,500,40, 40);
		if(times == 1) {
			p3.setVisible(true);
		} else {
			p3.setVisible(false);
		}
		this.p4 = new JButton(new ImageIcon(this.playerSprite4));
		p4.setOpaque(false);
		p4.setFocusPainted(false);
		p4.setBorderPainted(false);
		p4.setContentAreaFilled(false);
		this.p4.setBounds(600,500,40, 40);
		if(times == 1) {
			p4.setVisible(true);
		} else {
			p4.setVisible(false);
		}
		this.p5 = new JButton(new ImageIcon(this.playerSprite5));
		p5.setOpaque(false);
		p5.setFocusPainted(false);
		p5.setBorderPainted(false);
		p5.setContentAreaFilled(false);
		this.p5.setBounds(700,500,40, 40);
		if(times == 1) {
			p5.setVisible(true);
		} else {
			p5.setVisible(false);
		}
		this.p6 = new JButton(new ImageIcon(this.playerSprite6));
		p6.setOpaque(false);
		p6.setFocusPainted(false);
		p6.setBorderPainted(false);
		p6.setContentAreaFilled(false);
		this.p6.setBounds(800,500,40, 40);
		if(times == 1) {
			p6.setVisible(true);
		} else {
			p6.setVisible(false);
		}
		this.p1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selection(1);
			}
		});
		this.p2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selection(2);
			}
		});
		this.p3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selection(3);
			}
		});
		this.p4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selection(4);
			}
		});
		this.p5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selection(5);
			}
		});
		this.p6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selection(6);
			}
		});
		frame.add(this.p1);
		frame.add(this.p2);
		frame.add(this.p3);
		frame.add(this.p4);
		frame.add(this.p5);
		frame.add(this.p6);
		
		this.startButton = new JButton(new ImageIcon(this.startSprite));
		startButton.setOpaque(false);
		startButton.setFocusPainted(false);
		startButton.setBorderPainted(false);
		startButton.setContentAreaFilled(false);
		this.startButton.setBounds(518, 440, 124, 40);
		if(times == 1) {
			startButton.setVisible(true);
		} else {
			startButton.setVisible(false);
		}
		frame.add(this.startButton);
		this.startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pause();
				menuNoise.stop();
				startMusic();
			}
		});
		
		this.restartButton = new JButton(new ImageIcon(this.replaySprite));
		restartButton.setOpaque(false);
		restartButton.setFocusPainted(false);
		restartButton.setBorderPainted(false);
		restartButton.setContentAreaFilled(false);
		setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		this.restartButton.setBounds(670, 375, 40, 40);
		this.restartButton.setVisible(false);
		this.restartButton.setFocusable(false);
		
		this.attemptsLabel = new JLabel(attempts + "");
		this.attemptsLabel.setFont(this.customFont);
		this.attemptsLabel.setForeground(new Color(244, 83, 169));
		this.attemptsLabel.setBounds(610, 390,100,100);
		this.attemptsLabel.setVisible(false);
		frame.add(this.attemptsLabel, BorderLayout.CENTER);
		
		this.restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				flush();
				GeoDash square2 = new GeoDash();
				square2.restart();
			}
		});
		frame.add(this.restartButton);
		
		this.percentageLabel = new JLabel();
		if(times == 1 && isMenu == true) {
			this.percentageLabel.setVisible(false);
		}
		this.percentageLabel.setFont(this.customFont);
		this.percentageLabel.setForeground(new Color(244, 83, 169));
		this.percentageLabel.setBounds(WIDTH-130, 35,90,90);
		frame.add(this.percentageLabel,BorderLayout.CENTER);
		
		this.startMusic();
		frame.add(this, BorderLayout.CENTER);
		frame.setSize(WIDTH,HEIGHT);	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.pack();
    }
    
    public static void main(String[] args){
    	GeoDash square = new GeoDash();
    	square.restart();
	}

    private class Obstacle {
        int x;
        int y;

        Obstacle(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private class TallObstacles {
        int x;
        int y;

        TallObstacles(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    private class Star  {
    	int x;
        int y;

        Star(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    private class Spikes {
        int x;
        int y;
    
        Spikes(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    private class Win {
    	int x;
    	int y;
    	
    	Win(int x, int y) {
    		this.x = x;
    		this.y = y;
    	}
    }
    
    private class Platform {
        int x, y, width, height;

        Platform (int x, int y, int width, int height){
            this.x = x;
            this.y = y;
            this.width= width;
            this.height = height;
        }
    }
}