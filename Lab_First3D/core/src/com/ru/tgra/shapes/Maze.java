package com.ru.tgra.shapes;

import java.util.ArrayList;

public class Maze {
	char charMaze[][];
	public ArrayList<MazeWall> wallList;
	
	int blockWidth = 5;
	int blockHeight = 5;
	int blockDepth = 5;
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
					wallList.add(new MazeWall(new Point3D(x*blockWidth, 0, y*blockDepth), blockWidth, blockHeight, blockDepth));
				}
				if(value == 'M') {
					wallList.add(new MazeWall(new Point3D(x*blockWidth, 0, y*blockDepth), new Point3D(x*blockWidth, -blockHeight, y*blockDepth), blockWidth, blockHeight, blockDepth, (blockHeight)/2));
				}
			}
		}
	}
}
