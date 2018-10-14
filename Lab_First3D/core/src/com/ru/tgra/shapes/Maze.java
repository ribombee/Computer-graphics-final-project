package com.ru.tgra.shapes;

import java.util.ArrayList;
import java.util.Arrays;

public class Maze {
	char charMaze[][];
	public ArrayList<MazeWall> wallList;
	
	public int width, height;
	public int blockWidth = 5;
	public int blockHeight = 5;
	public int blockDepth = 5;
	
	public Point3D playerStartPosition;
	
	public Maze(int xSize, int ySize, String maze) {
		String cleanedMaze = new String(maze);
		cleanedMaze.replaceAll("\\s", "");
		charMaze = new char[xSize][ySize];
		wallList = new ArrayList<MazeWall>(xSize*ySize + 1);
		wallList.add(new MazeWall(new Point3D((xSize-1)*blockWidth/2f, -blockHeight/2f, (ySize-1)*blockDepth/2f), xSize*blockWidth, 0.2f, ySize*blockDepth));
		for(int y = 0; y < ySize; y++)
		{
			for(int x = 0; x < xSize; x++)
			{
				char value = cleanedMaze.charAt(y*xSize + x);
				charMaze[x][y] = value;
				
				if(value == 'W') {
					MazeWall stillWall = new MazeWall(new Point3D(x*blockWidth, 0, y*blockDepth), blockWidth, blockHeight, blockDepth);
					stillWall.setColor(0.3f, 0.2f, 0.2f);
					wallList.add(stillWall);
				}
				if(value == 'M') {
					MazeWall movingWall = new MazeWall(new Point3D(x*blockWidth, 0, y*blockDepth), new Point3D(x*blockWidth, -blockHeight, y*blockDepth), blockWidth, blockHeight, blockDepth, (blockHeight)/2);
					movingWall.setColor(0.6f, 0.2f, 0.2f);
					wallList.add(movingWall);
				}
				if(value == 'S') {
					playerStartPosition = new Point3D(x*blockWidth, 2, y*blockDepth);
					
				}
			}
		}
		width = xSize;
		height = ySize;
	}
	public Maze(int xSize, int ySize) {
		charMaze = new char[xSize][ySize];
		wallList = new ArrayList<MazeWall>(xSize*ySize + 1);
		wallList = new ArrayList<MazeWall>(xSize*ySize + 1);
		wallList.add(new MazeWall(new Point3D((xSize-1)*blockWidth/2f, -blockHeight/2f, (ySize-1)*blockDepth/2f), xSize*blockWidth, 0.2f, ySize*blockDepth));
		
		GenerateMazeDepthFirst(xSize, ySize);
		for(int y = 0; y < ySize; y++)
		{
			for(int x = 0; x < xSize; x++) {
				
				if(charMaze[x][y] == 'W') {
					MazeWall stillWall = new MazeWall(new Point3D(x*blockWidth, 0, y*blockDepth), blockWidth, blockHeight, blockDepth);
					stillWall.setColor(0.3f, 0.2f, 0.2f);
					wallList.add(stillWall);
					//wallList.add(new MazeWall(new Point3D(x*blockWidth, 0, y*blockDepth), blockWidth, blockHeight, blockDepth));
				}
				if(charMaze[x][y] == 'M') {
					//wallList.add(new MazeWall(new Point3D(x*blockWidth, 0, y*blockDepth), new Point3D(x*blockWidth, -blockHeight, y*blockDepth), blockWidth, blockHeight, blockDepth, (blockHeight)/2));
					MazeWall movingWall = new MazeWall(new Point3D(x*blockWidth, 0, y*blockDepth), new Point3D(x*blockWidth, -blockHeight, y*blockDepth), blockWidth, blockHeight, blockDepth, (blockHeight)/2);
					movingWall.setColor(0.6f, 0.2f, 0.2f);
					wallList.add(movingWall);
				}
				if(charMaze[x][y] == 'S') {
					playerStartPosition = new Point3D(x*blockWidth, 2, y*blockDepth);
				}
			}
		}
		width = xSize;
		height = ySize;
	}
	
	private void GenerateMazeDepthFirst(int xSize, int ySize) {
		MazeGenerationNode[][] nodes = new MazeGenerationNode[xSize][ySize];
		boolean hasStart = false;
		
		//Initialization phase
		for(int y = 0; y < ySize; y++) {
			for(int x = 0; x < xSize; x++) {
				//Every empty cell must initially be entirely surrounded by walls
				MazeGenerationNode newNode = new MazeGenerationNode();
				if(x*y%2 > 0) {
					newNode.unexploredDirections = new ArrayList<Character>(Arrays.asList('r','l','d','u'));
					//Added a 10% chance that any "empty" block is a moving up and down wall
					float rand = (float) (Math.random()*10);
					
					if(!hasStart) {
						newNode.nodeValue = 'S';
						hasStart = true;
					}
					else if(rand < 1) {
						newNode.nodeValue = 'M';
					}
					else {
						newNode.nodeValue = 'E';
					}
				}
				else {
					newNode.unexploredDirections = new ArrayList<Character>();
					newNode.nodeValue = 'W';
				}
				newNode.x = x;
				newNode.y = y;
				nodes[x][y] = newNode;
			}
		}

		//Create actual maze
		MazeGenerationNode first = nodes[1][1];
		first.parent = first;
		MazeGenerationNode last = first;
		do {
			while(last.unexploredDirections.size() != 0)
			{
				int directionIndex = (int)(Math.random()*last.unexploredDirections.size());
				char direction = last.unexploredDirections.get(directionIndex);
				last.unexploredDirections.remove(directionIndex);
				MazeGenerationNode nextNode = null;
				switch (direction) {
					case'l':
						if(last.x - 2 >= 0) {
							nextNode = nodes[last.x-2][last.y];
						}
						break;
					case'r':
						if(last.x + 2 < xSize) {
							nextNode = nodes[last.x+2][last.y];
						}
						break;
					case'u':
						if(last.y - 2 >= 0) {
							nextNode = nodes[last.x][last.y-2];;
						}
						break;
					case'd':
						if(last.y + 2 < xSize) {
							nextNode = nodes[last.x][last.y+2];;
						}
						break;
				}
				if (nextNode == null) {
					continue;
				}
				else if(nextNode.nodeValue == 'E' || nextNode.nodeValue == 'M' || nextNode.nodeValue == 'S')
				{
					if(nextNode.parent != null) {
						continue;
					}
					nextNode.parent = last;
					nodes[last.x + (nextNode.x - last.x)/2][last.y + (nextNode.y - last.y)/2].nodeValue = 'E';
					last = nextNode;
				}
			}
			last = last.parent;
		}while(last != first);
		
		for(int y = 0; y < ySize; y++) {
			for(int x = 0; x < xSize; x++) {
				
				charMaze[x][y] = nodes[x][y].nodeValue;
			}
		}
	}
}
