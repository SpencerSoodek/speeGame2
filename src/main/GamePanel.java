package main;

import Entities.Player;
import Levels.Level;
import Levels.World01;
import Menus.StartMenu;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{

  public int tileSize = 64;
  public int worldCols = 30; //16*16*3 == 768
  public int worldRows = 30; // 912
  public int scale = 2;
  public int screenWidth = 912;
  public int screenHeight = 768;
  public UI ui = new UI(this);
  public GameState gameState;
  Thread gameThread;
  public KeyHandler kh = new KeyHandler();
  public Level gameLevel;// = new World01(this);
  public TileManager tileM;// = new TileManager(this, gameLevel);
  public Player player;// player = new Player(this);
  int fps = 60;
  public CollisionChecker cChecker;
  public StartMenu startMenu = new StartMenu(this);// = new CollisionChecker(this);

  public GamePanel() {
    //this.gameLevel.getTileTypes();
    this.setPreferredSize(new Dimension(screenWidth,screenHeight));
    this.setBackground(Color.BLACK);
    this.setDoubleBuffered(true);
    this.addKeyListener(kh);
    this.setFocusable(true);
    this.gameState = GameState.STARTMENU;
    //this.gameLevel = new World01(this);
    //this.tileM = new TileManager(this, gameLevel);
    //this.player = new Player(this);
  }

  public void startGameThread() {
    gameThread = new Thread(this);
    gameThread.start();
  }

  public void startRun() {
    this.gameLevel = new World01(this);
    this.tileM = new TileManager(this, gameLevel);
    this.player = new Player(this);
    this.gameState = GameState.GAME;
    this.cChecker = new CollisionChecker(this);
  }

  private void update() {
    if (gameState == GameState.STARTMENU) {
      startMenu.update();
    }
    if (gameState == GameState.GAME) {
      player.update();
      gameLevel.monstM.update();
    }
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    if (this.gameState == GameState.STARTMENU) {
      startMenu.draw(g2);
      g2.dispose();
    }
    if (this.gameState == GameState.GAME) {
      tileM.draw(g2);
      player.draw(g2);
      gameLevel.om.drawObjects(g2);
      gameLevel.monstM.drawMonsters(g2);
      ui.draw(g2);
      g2.dispose();
    }
  }

  @Override
  public void run() {
    long lastTime = System.nanoTime();
    long currentTime;
    double drawInterval = 1000000000 / fps;
    double delta = 0;
    while (gameThread != null) {
      currentTime = System.nanoTime();
      delta += (currentTime - lastTime) / drawInterval;
      lastTime = currentTime;
      if (delta > 1) {
        update();
        repaint();
        delta--;
      }
    }
  }

  public boolean checkIfOnscreen() {
    return true;
  }
}
