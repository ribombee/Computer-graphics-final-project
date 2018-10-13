package com.ru.tgra.shapes;

import java.util.ArrayList;

public class Maze {
	char charMaze[][];
	public ArrayList<MazeWall> wallList;
	
	public Maze(int xSize, int ySize, String maze) {
		String cleanedMaze = new String(maze);
		cleanedMaze.replaceAll("\\s", "");
		charMaze = new char[xSize][ySize];
		wallList = new ArrayList<MazeWall>(xSize*ySize);
		for(int y = 0; y < ySize; y++)
		{
			for(int x = 0; x < xSize; x++)
			{
				char value = cleanedMaze.charAt(y*xSize + x);
				charMaze[x][y] = value;
				
				if(value == 'W') {
					wallList.add(new MazeWall(new Point3D(x*5, 0, y*5), 5,5,5));
				}
				if(value == 'M') {
					wallList.add(new MazeWall(new Point3D(x*5, 0, y*5), new Point3D(x*5, -5, y*5), 5,5,5, (xSize*ySize)/10));
				}
			}
		}
	}
}
