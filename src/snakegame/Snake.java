/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

/**
 *
 * @author Ildikó
 */
class Snake {
    private char direct;
    private int startHeadX, startHeadY;
    private int[] snakeX;
    private int[] snakeY;
    private int bodyParts;

    public Snake() {
    }

    public char getDirect() {
        return direct;
    }

    public int getStartHeadX() {
        return startHeadX;
    }

    public int getStartHeadY() {
        return startHeadY;
    }

    public int[] getSnakeX() {
        return snakeX;
    }

    public int[] getSnakeY() {
        return snakeY;
    }

    public int getBodyParts() {
        return bodyParts;
    }
    
    public int getSnakePosX(int index) {
        return snakeX[index]+startHeadX;
    }
    
    public int getSnakePosY(int index) {
        return snakeY[index]+startHeadY;
    }

    public void setDirect(char direct) {
        this.direct = direct;
    }

    public void setBodyParts(int bodyParts) {
        this.bodyParts = bodyParts;
    }

    public void setSnakeIndX(int currInd, int updateInd) {
        snakeX[currInd] = snakeX[updateInd];
    }
    
    public void setSnakeIndX(int currInd, int updateInd, int val) {
        snakeX[currInd] = snakeX[updateInd]+val;
    }

    public void setSnakeIndY(int currInd, int updateInd) {
        snakeY[currInd] = snakeY[updateInd];
    }
    
    public void setSnakeIndY(int currInd, int updateInd, int val) {
        snakeY[currInd] = snakeY[updateInd]+val;
    }

    public void setStartHeadX(int startHeadX) {
        this.startHeadX = startHeadX;
    }

    public void setStartHeadY(int startHeadY) {
        this.startHeadY = startHeadY;
    }

    public void setSnakeX(int[] snakeX) {
        this.snakeX = snakeX;
    }

    public void setSnakeY(int[] snakeY) {
        this.snakeY = snakeY;
    }
    
    public int incrementBodyParts() {
        return bodyParts++;
    }
    
    
}
