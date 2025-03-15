package main;
// adapted from https://www.youtube.com/watch?v=om59cwR7psI
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import entity.GuardManager;
import entity.Player;
import object.ObjectSpawner;
import object.SuperObject;
import tile.TileManager;

/**
 * Sets the screen and tile dimensions, FPS.
 * Sets up and starts the game.
 * Holds and manages the components that comprise the game
 */
public class GamePanel extends JPanel implements Runnable{

    // Screen Settings
    final int orginalTileSize = 16; // each tile is size 16x16
    final int scale = 3; // scale each tile to 48x48
    public final int tileSize = orginalTileSize * scale;

    // set 3:4 aspect ratio
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixel width
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixel height
    
    //World Settings
    public final int fullMapCol = 50;
    public final int fullMapRow = 50;

    // FPS
    int FPS = 60;

    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);


    public Player player = new Player(this, keyH);
    public SuperObject[] obj = new SuperObject[20];

    public int score = 0;
    private ObjectSpawner objectSpawner = new ObjectSpawner(this);

    private GuardManager guardManager; // manages the guards

    // GAME STATE
    public enum gameState{
        title,
        playing,
        paused,
        gameOver,
        win
    }
    public gameState currentGameState;

    /**
     * Manages and holds the game state
     */
    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // perform drawing in a buffer (improves rendering performance)
        this.addKeyListener(keyH);
        this.setFocusable(true); // required for the keyHandler

        this.guardManager = new GuardManager(this);

    }

    /**
     * invokes an AssetSetter to setup objects and sets the game in an initial tile state.
     */
    public void setupGame() {

        aSetter.setObject();
        currentGameState = gameState.title;
    }

    /**
     * Starts execution of the game thread
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * sets the game state
     * @param x 0=title screen, 1=in execution, 2=paused, 3=game over, 4=game win
     */
    public void setcurrentGameState(gameState gs) {
        this.currentGameState = gs;
    }

    @Override
    /**
     * Performs a continual loop.
     * 1. Update game state
     * 2. Repaint game state
     * 3. Waits for the next frame
     */
    public void run(){

        double drawInterval = 1e9/FPS; // 1/60 of a second
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null) {

            // Update information
            update();

            // Draw the screen once information is updated
            repaint();
            
            // Sleep until the next frame
            try{
                double remainingTime = (nextDrawTime - System.nanoTime())/1e6;
                remainingTime = Math.max(0, remainingTime); // incase the thread took longer than the interval
                Thread.sleep((long)remainingTime);
                nextDrawTime += drawInterval;
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            
        }
    }

    /**
     * Updates information such as player position, battery spawn status, guard position, if the game is in a playstate
     */
    public void update(){
        if(currentGameState == gameState.playing){
            player.update();
            objectSpawner.update();
            guardManager.update(player, this);
        }
    }

    /**
     * Resets the game to its initial starting playstate
     */
    public void restart(){
        player.setDefaultValues();
        player.restoreScore();
        resetObjects();
        aSetter.setObject();
        this.guardManager = new GuardManager(this);
        this.ui = new UI(this);
    }

    /**
     * Resets the objects to their initial state
     */
    private void resetObjects(){
        this.obj = new SuperObject[20];
        this.objectSpawner = new ObjectSpawner(this);
    }
    
    /**
     * Draws the screen. Called by repaint().
     */
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g; // Cast Graphics object to Graphics2D for more functionality

        // Title screen
        if(currentGameState == gameState.title){
            ui.draw(g2);
        }
        // Other
        else {
            // draw all tiles visible on the screen
            tileM.draw(g2);

            // draw all objects on the screen
            for(int i = 0; i < obj.length; i++) {
                if(obj[i] != null) {
                    obj[i].draw(g2, this);

                }
            }
            // draw the player character
            player.draw(g2);

            // Draw the PrisonGuards
            guardManager.draw(g2); 

            // draw the UI
            ui.draw(g2);
        }

        g2.dispose(); // frees memory associated with Graphics2D
    }
}
