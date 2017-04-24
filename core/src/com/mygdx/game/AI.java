/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

/**
 *
 * @author Nkosingiphile
 */
public class AI {
    
    static int positionX, positionY, destinationX, destinationY;
    private static Entity enemy;
    private static Player player;
    private static Nodes node = new Nodes();
    
    enum Direction { U, D, L, R };
    private Direction currentDirection; // ai starts off going right
    
    private int[] move;// = new ArrayList();
    
    private int[] startNode = {1,20}; //startNode(1,20)
    private int[] currentNode;
    private int[] playerNode;
    private int[] endNode = {28, 20}; //endNode(28,20);
    private int[] destinationNode; // where destination is either endNode or playerNode
    
    private PlayScreen playscreen;
    
    public void render(Entity enemy1, Player player1){
        enemy = enemy1;
        player = player1;
        positionX = enemy.x;
        positionY = enemy.y;
        destinationX = player.x;
        destinationY = player.y;
        //
        currentNode = node.getNode(positionX, positionY);
        playerNode = node.getNode(destinationX, destinationY);       
        
        if (playerInLineOfSight() == true){
            //Follow Straight Path to Player
            chase();
        }
        else
        {
            //Move from current position to goal
            roam();
        } 
    }
    
    //chasing state
    public void chase(){ // constituties moving from current node to molly node
        destinationNode = playerNode;
        if (atEndPoint() == false){
            nextMove();            
        }
        else{ // find a way to destroy enemy
            
            finallyMove(); // just move in current direction
            enemy.replaceEnemy();
        }
        System.out.println("CHASING");
    }
    // roaming state
    public void roam(){ // constitutes moving from start node to end node
        destinationNode = endNode;
        if (atEndPoint() == false){
            nextMove(); 
        }
        else{ // find a way to destroy enemy
            System.out.println("At end point!");
            finallyMove();
            enemy.replaceEnemy();
        }
        System.out.println("ROAMING");
    }

    
    public boolean playerInLineOfSight(){
        // if position of player is straight ahead ie. N, E, S or W
        if (currentNode[0] == playerNode[0]){   //if (positionX == destinationX)... converted to node values                 
            System.out.println("EnemyXnode:"+ currentNode[0] +", PlayerXnode:"+ playerNode[0]+" | EnemyYnode:"+ currentNode[1] +", PlayerYnode:"+ playerNode[1]);
            return true;
        }
        else if (currentNode[1] == playerNode[1]){
            System.out.println("EnemyXnode:"+ currentNode[0] +", PlayerXnode:"+ playerNode[0]+" | EnemyYnode:"+ currentNode[1] +", PlayerYnode:"+ playerNode[1]);
            return true;
        }
        else{
            return false;
        }
        
    }
    
    public void nextMove(){ // takes in two co-ordinates in the form of nodes; startPoint and endPoint
        // check for all available moves; closests nodes where map.getPosition == 0;
        checkMovesAvailable();
        
    }
    
    // checks if move is available for up, down, left & right
    public void checkMovesAvailable(){ // startPoint(x,y), endPoint(x,y)
        int[] newNode = new int[2]; // nodes left, right, up and down of current node
        int currentNodeX = currentNode[0];
        int currentNodeY = currentNode[1];
//        System.out.println("currentNodeX = "+currentNodeX);
//        System.out.println("currentNodeY = "+currentNodeY);
        
        //move = new int[4]; // where index 0 = right; 1 = left; 2 = up; 3: down 
        move = new int[] {-1,-1,-1,-1};
        
        // will be compared to . . .
        //int mapObject = enemy.playscreen.map[currentPositionX][currentPositionY]; // returns map[x][y];
        // for currentNodeX--,  ie. left
        if (enemy.playscreen.map[currentNodeX-1][currentNodeY] == 0){
            newNode[0] = currentNodeX-1;
            newNode[1] = currentNodeY;
            // store distance
            int distance = node.getCost(newNode, destinationNode);
            System.out.println("Distance left: "+ distance);
            move[0] = distance;
        }
        // for currentNodeX++, ie. right              
        if (enemy.playscreen.map[currentNodeX+1][currentNodeY] == 0){
            newNode[0] = currentNodeX+1;
            newNode[1] = currentNodeY;
            // store distance (int) and the move (Direction)
            int distance = node.getCost(newNode, destinationNode);
            System.out.println("Distance right: "+ distance);
            move[1] = distance;
        }      

        // for currentNodeY++, ie. up
        if (enemy.playscreen.map[currentNodeX][currentNodeY+1] == 0){
            newNode[0] = currentNodeX;
            newNode[1] = currentNodeY+1;
            // store distance
            int distance = node.getCost(newNode, destinationNode);
            System.out.println("Distance up: "+ distance);
            move[2] = distance;
        }
        // for currentNodeY--, ie. down   
        if (enemy.playscreen.map[currentNodeX][currentNodeY-1] == 0){
            newNode[0] = currentNodeX;
            newNode[1] = currentNodeY-1;
            // store distance
            int distance = node.getCost(newNode, destinationNode);
            System.out.println("Distance down: "+ distance);
            move[3] = distance;
            
        }
        // now compare distances to calculate the direction . . . 
        // for all 4 directions
        currentDirection = calculateDirection();
        System.out.println("Direction to move : "+ currentDirection);
        finallyMove();
        
    }
    public boolean atEndPoint(){
        if (currentNode[0] == endNode[0]){
            if (currentNode[1] == endNode[1]){
                return true;
            }
        }
        return false;
        
    }
    
    public Direction calculateDirection(){
        int lowestCost = 100;
        for (int i = 0; i < 4; i++) {
            //System.out.println("Move array at index " + i + " = " + move[i]);
            if (move[i] != -1){ // for all the values that aren't null / initailized to -1
                int cost = move[i];            
                if (cost < lowestCost){
                    lowestCost = cost;
                    // now to determine direction . . .
                    if (i == 0){ // ie. left
                        currentDirection = Direction.L;
                    }
                    else if (i == 1){ // ie. right
                        currentDirection = Direction.R;
                    }
                    else if (i == 2){ // ie. up
                        currentDirection = Direction.U;
                    }
                    else if (i == 3){ // ie. down
                        currentDirection = Direction.D;
                    }
                }
            }
//            else{
//                lowestCost = 0; // ... and current direction stays the same
//            }
        }
        System.out.println("Lowest cost = "+lowestCost);
        return currentDirection;
    }
    
    public void finallyMove(){    
        if (currentDirection == Direction.L){
            System.out.println("Moving in direction: "+ currentDirection);
            moveLeft(enemy);
        }
        else if (currentDirection == Direction.R){
            System.out.println("Moving in direction: "+ currentDirection);
            moveRight(enemy);
        }
        else if (currentDirection == Direction.U){
            System.out.println("Moving in direction: "+ currentDirection);
            moveUp(enemy);
        }
        else if (currentDirection == Direction.D){
            System.out.println("Moving in direction: "+ currentDirection );
            moveDown(enemy);
        }        
    }
    
        
    public void moveLeft(Entity entity){
        entity.dx = (int) -(entity.speed * Gdx.graphics.getDeltaTime());
    }
    public void moveRight(Entity entity){ 
        entity.dx = (int) (entity.speed * Gdx.graphics.getDeltaTime());
    }
    public void moveUp(Entity entity){
        entity.dy = (int) (entity.speed * Gdx.graphics.getDeltaTime());
    }
    public void moveDown(Entity entity){
        entity.dy = (int) -(entity.speed * Gdx.graphics.getDeltaTime());
    }
    
}
