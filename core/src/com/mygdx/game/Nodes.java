/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game;

/**
 *
 * @author Nkosingiphile
 */
public class Nodes {
    
    int blockSize = 20;
    int [] xy; // an array where key:0 = x, and key:1 = y  
//    int [] startNode = {1,20};
//    int [] endNode = {28, 20};
    
    // xNode = positionX / 20
    // yNode = positionY / 20
    // this method takes in positions and returns nodes
    public int[] getNode(int positionX, int positionY){
        xy = new int[2];
        int x = positionX / blockSize;
        int y = positionY / blockSize;
        xy[0] = x;
        xy[1] = y;
        return xy;
    }
    
    public int getCost(int[] currentNode, int[] endNode){
        int currentNodeX = currentNode[0];
        int currentNodeY = currentNode[1];
        int endNodeX = endNode[0];
        int endNodeY = endNode[1];
        
        int distance = (endNodeX-currentNodeX) + (endNodeY-currentNodeY);
        distance = Math.abs(distance);
        return distance;
    }
}
