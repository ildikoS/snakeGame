/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import javax.swing.*;


/**
 *
 * @author Ildikó
 */
public class SnakeGameBoard extends JPanel{
    Random rand = new Random();
    private final int rows = 20, cols = 20;
    private final int TILE_SIZE     = 32;
    private final int SCREEN_WIDTH  = TILE_SIZE*rows;
    private final int SCREEN_HEIGHT = TILE_SIZE*cols;
    private final int ALL_TILES     = (SCREEN_WIDTH*SCREEN_HEIGHT)/TILE_SIZE;
    private Image foodImage, enemyImage, background;
    Snake snake;
    Food food;
    Rock rock;
    boolean running = false;
    Timer timer;
    private int score;

    public SnakeGameBoard() {
        setImages('S');
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(true);
        newGame();
        
        addKeyListener(snakeKeyListener);
    }
    
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        drawSnake(grphcs);
        drawRocks(grphcs);
        drawFood(grphcs);
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    /**
     * Resets game settings when user open the game, but game just won't run
     */
    private void newGame(){
        snake = new Snake();
        food = new Food();
        rock = new Rock(4,10);
        snake.setDirect(randomDirection());
        snake.setBodyParts(2);
        snake.setSnakeX(new int[ALL_TILES]);
        snake.setSnakeY(new int[ALL_TILES]);
        genNewSnake();
        genNewRocks();
        do{
            genNewFood();
        }while(samePos(food.getFoodX(), food.getFoodY(), snake.getStartHeadX(), snake.getStartHeadY()));
        
        
        timer = new Timer(240, oneGameCycleAction);
        timer.start();
        score=0;
        refresh();
    }
    
    /**
     * Resets game settings when user starts new game, and game starts
     */
    public void startNewGame(){
        timer.stop();
        newGame();
        running = true;
    }
    
    /**
     * Resets panel
     */
    public void refresh() {
        Dimension dim = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
        setPreferredSize(dim);
        setMaximumSize(dim);
        setSize(dim);
        repaint();
    }
    
    /**
     * Sets the proper images
     * @param c The argument from the game type menu
     */
    public void setImages(char c){
        if(c == 'S'){
            background = new ImageIcon("datas/light_sand_template.jpg").getImage();
            foodImage = new ImageIcon("datas/strawberry.png").getImage();
            enemyImage = new ImageIcon("datas/rock.png").getImage();
        }else{
            background = new ImageIcon("datas/snowy.png").getImage();
            foodImage = new ImageIcon("datas/gift.png").getImage();
            enemyImage = new ImageIcon("datas/crampus.png").getImage();
        }
    }
    
    /**
     * Actionlistener, it called in timer with 240ms
     */
    private final Action oneGameCycleAction = new AbstractAction() {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(running){
                move();
                checkCollision();
                checkFood();
            }
            repaint();
        }
    };
    /**
     * Keylistener, senses the pressed key
     */
    private final KeyListener snakeKeyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:  if(snake.getDirect() != 'D' && running) 
                    snake.setDirect('U'); 
                    break;
                case KeyEvent.VK_A:  if(snake.getDirect() != 'R' && running) 
                    snake.setDirect('L'); 
                    break;
                case KeyEvent.VK_S:  if(snake.getDirect() != 'U' && running) 
                    snake.setDirect('D'); 
                    break;
                case KeyEvent.VK_D:  if(snake.getDirect() != 'L' && running) 
                    snake.setDirect('R'); 
                    break;
                case KeyEvent.VK_SPACE:  running = !running; break;
            }
            repaint();
        }
    };
    
    /**
     * Iterates through snake and draws its bodyparts
     */
    private void drawSnake(Graphics grphcs) {
        /*for(int i=0; i<SCREEN_HEIGHT/TILE_SIZE; ++i) {
            grphcs.drawLine(i*TILE_SIZE, 0, i*TILE_SIZE, SCREEN_HEIGHT);
            grphcs.drawLine(0, i*TILE_SIZE, SCREEN_WIDTH, i*TILE_SIZE);
        }*/
        
        for(int i=0; i<snake.getBodyParts(); ++i) {
            String path = "datas/snake/";
            
            path += setBodyPart(i, path);
            
            Image snakebody = new ImageIcon(path).getImage();
            grphcs.drawImage(snakebody, snake.getSnakePosX(i),
                    snake.getSnakePosY(i), TILE_SIZE, TILE_SIZE, null);
        }
    }
    
    /**
     * @param i - Index of the snake's current bodypart, starts from head
     * @param path - Path of the proper image
     * @return 
     */
    private String setBodyPart(int i, String path){
        if(i==0){
            switch(snake.getDirect()){
                case 'U':  path = "head_up.png"; break;
                case 'L':  path = "head_left.png"; break;
                case 'R':  path = "head_right.png"; break;
                case 'D':  path = "head_down.png"; break; 
            }
        }else{
            int prevX = snake.getSnakePosX(i-1);
            int currX = snake.getSnakePosX(i);
            int prevY = snake.getSnakePosY(i-1);
            int currY = snake.getSnakePosY(i);
            
            if(i==snake.getBodyParts()-1){
                if(prevX < currX)
                    path = "tail_right.png";
                else if(prevX > currX)
                    path = "tail_left.png";
                else if(prevY < currY)
                    path = "tail_down.png";
                else if(prevY > currY)
                    path = "tail_up.png";
            }else{
                int nextX = snake.getSnakePosX(i+1);
                int nextY = snake.getSnakePosY(i+1);
                
                if((prevX > currX && currY < nextY) || (prevY > currY && nextX > currX))
                    path = "body_bottomright.png";
                else if((prevX < currX && currY < nextY) || (prevY > currY && nextX < currX))
                    path = "body_bottomleft.png";
                else if((prevX < currX && currY > nextY) || (prevY < currY && nextX < currX))
                    path = "body_topleft.png";
                else if((prevX > currX && currY > nextY) || (prevY < currY && nextX > currX))
                    path = "body_topright.png";
                else if(prevY == currY && nextY == currY)
                    path = "body_horizontal.png";
                else if(prevX == currX && nextX == currX)
                    path = "body_vertical.png";
            }
        }
        return path;
    }
    
    /**
     * draws the food/gift
     */
    private void drawFood(Graphics grphcs) {
        grphcs.drawImage(foodImage, food.getFoodX(), food.getFoodY(), TILE_SIZE, TILE_SIZE, null);
    }
    
    /**
     * Iterates through rocks and draws them
     */
    private void drawRocks(Graphics grphcs) {
        for(int i=0; i<rock.getRocksNum(); i++){
            grphcs.drawImage(enemyImage, rock.getRockX()[i], rock.getRockY()[i], TILE_SIZE, TILE_SIZE, null);
        }
    }
    
    
    //--- Game logic starts here ---
    
    /**
     * Counts snake's start position, it will be in the middle of the board
     */
    private void genNewSnake(){
        snake.setStartHeadX((int)Math.ceil(rows/2)*TILE_SIZE); 
        snake.setStartHeadY((int)Math.ceil(cols/2)*TILE_SIZE);
    }
    
    /**
     * Sets the random position of new food, it can't be on any rock
     */
    private void genNewFood(){
        food.setFoodX(genRandXcoord());
        food.setFoodY(genRandYcoord());
        for(int i=0; i<rock.getRocksNum(); ++i) {
            if(samePos(food.getFoodX(), food.getFoodY(), rock.getRockX()[i], rock.getRockY()[i])){
                food.setFoodX(genRandXcoord());
                food.setFoodY(genRandYcoord());
                i=0;
            }
        }
    }
    
    /**
     * Sets the random position of rocks, they can't be on snake
     */
    private void genNewRocks(){
        for(int i=0; i<rock.getRocksNum(); ++i) {
            do{
                rock.setRockXPos(i, genRandXcoord());
                rock.setRockYPos(i, genRandYcoord());
            }while(samePos(rock.getRockXPos(i), rock.getRockYPos(i), snake.getStartHeadX(), snake.getStartHeadY()));
        }
    }
    
    /**
     * Shifts the bodyparts' coordinates in snakeX and snakeY over by one
     * U - up, D - down, L - left, R - right
     */
    private void move(){
        for(int i=snake.getBodyParts(); i>0; --i){
            snake.setSnakeIndX(i, i-1);
            snake.setSnakeIndY(i, i-1);
        }
        
        switch (snake.getDirect()){
            case 'U':  snake.setSnakeIndY(0, 0, -TILE_SIZE);
                break;
            case 'D':  snake.setSnakeIndY(0, 0, TILE_SIZE);
                break;
            case 'L':  snake.setSnakeIndX(0, 0, -TILE_SIZE);
                break;
            case 'R':  snake.setSnakeIndX(0, 0, TILE_SIZE); 
                break;
        }
    }
    
    /**
     * Checks if snake collides to top, bottom, left, right borders, or to any rocks,
     * or to itself.
     */
    private void checkCollision() {
        for(int i=snake.getBodyParts(); i>0; --i) {
            if(snake.getSnakePosX(0) == snake.getSnakePosX(i)
                    && snake.getSnakePosY(0) == snake.getSnakePosY(i))
                running = false;
        }
        
        if(snake.getSnakePosX(0) < 0 
                || snake.getSnakePosX(0) > SCREEN_WIDTH-TILE_SIZE
                || snake.getSnakePosY(0) < 0 
                || snake.getSnakePosY(0) > SCREEN_HEIGHT-TILE_SIZE)
            running = false;
        
        for(int k=0; k<rock.getRocksNum(); ++k){
            if(samePos(snake.getSnakePosX(0), snake.getSnakePosY(0), rock.getRockXPos(k), rock.getRockYPos(k)))
                running = false;
        }
        
        if(!running) {
            timer.stop();
            
            String name = JOptionPane.showInputDialog("Játék vége! Adj meg egy nevet: ");
            if (name.trim().length() == 0 || name == null) {
                name = "Játékos";
            }
            try {
                HighScores highScores = new HighScores(10);
                highScores.putHighScore(name, score);
            } catch (SQLException ex) {
                Logger.getLogger(SnakeGameGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Checks if snake finds food
     * If yes, then snake grows, points increases and places a new food
     */
    private void checkFood(){
        if(samePos(snake.getSnakePosX(0), snake.getSnakePosY(0), food.getFoodX(), food.getFoodY())){
            snake.incrementBodyParts();
            score++;
            SnakeGameGUI.refreshGameStatLabel();
            genNewFood();
        }
    }
    
    /**
     * Calculates the snake's start direction, which is random
     * @return direction
     */
    private char randomDirection(){
        int r = rand.nextInt((4 - 1) + 1) + 1;
        char d = 'R';
        switch (r){
            case 1:  d = 'L'; break;
            case 2:  d = 'U'; break;
            case 3:  d = 'D'; break;
            case 4:  d = 'R'; break;
        }
        return d;
    }
    
    /**
     * Checks if two things on the same position
     * @param x1 - x coordinate of first thing
     * @param y1 - y coordinate of first thing
     * @param x2 - x coordinate of second thing
     * @param y2 - y coordinate of second thing
     * @return true if two things on the same position
     */
    private boolean samePos(int x1, int y1, int x2, int y2) {
        return (x1==x2 && y1==y2);
    }
    
    /**
     * 
     * @return random x coordinate
     */
    private int genRandXcoord() {
        return rand.nextInt((int)(SCREEN_WIDTH/TILE_SIZE))*TILE_SIZE;
    }
    
    /**
     * 
     * @return random y coordinate
     */
    private int genRandYcoord() {
        return rand.nextInt((int)(SCREEN_HEIGHT/TILE_SIZE))*TILE_SIZE;
    }
    //--- Game logic ends here ---
    
    public int getScore(){
        return score;
    }

    public boolean getRunning() {
        return running;
    }
}
