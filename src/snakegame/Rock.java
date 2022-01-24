/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.util.Random;

/**
 *
 * @author Ildikó
 */
class Rock {
    
    private final int rocksNum;
    private int[] rockX, rockY;
    private Random rand;

    public Rock(int minRocks, int maxRocks) {
        rand = new Random();
        rocksNum = rand.nextInt((maxRocks - minRocks) + 1) + minRocks;
        
        rockX = new int[rocksNum];
        rockY = new int[rocksNum];
    }

    public int getRocksNum() {
        return rocksNum;
    }

    public int[] getRockX() {
        return rockX;
    }

    public int[] getRockY() {
        return rockY;
    }

    public void setRockX(int[] rockX) {
        this.rockX = rockX;
    }

    public void setRockY(int[] rockY) {
        this.rockY = rockY;
    }
    
    public int getRockXPos(int ind) {
        return rockX[ind];
    }

    public void setRockXPos(int curr, int val) {
        rockX[curr] = val;
    }
    
    public int getRockYPos(int ind) {
        return rockY[ind];
    }

    public void setRockYPos(int curr, int val) {
        rockY[curr] = val;
    }
    
}
