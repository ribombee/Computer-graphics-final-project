package com.ru.tgra.shapes;

import java.util.ArrayList;

public class Obstacle {
	//An obstacle that consists of a stack of objects currently. 
	
	public int blockWidth = 5;
	public int blockHeight = 5;
	public int blockDepth = 5;
	
	public Point3D position;
	
	public ArrayList <MazeWall> blockList;
	public ArrayList <FileModel> modelList; //TODO: use Valli's other model loading method he has hidden away somewhere
	
	public Obstacle(char []stack, Point3D bottomPos)
	{
		Point3D currentPos = new Point3D();
		currentPos.set(bottomPos.x, bottomPos.y, bottomPos.z);
		position = bottomPos;
		
		blockList = new ArrayList<MazeWall>();
		modelList = new ArrayList<FileModel>();
		
		for(char obj : stack)
		{
			if(obj == 'b') 
			{
				//TODO: put a model character at currentPos and increment currentPos.
			}
			else if(obj == 'p')
			{
				Point3D pillarPos = new Point3D(currentPos.x, currentPos.y, currentPos.z);
				MazeWall pillar = new MazeWall(pillarPos, blockWidth, blockHeight, blockDepth);
				pillar.usePillarTexture(); //TODO make use pillar texture!
				blockList.add(pillar);
			}
			currentPos.y += blockHeight;
		}
	}
	
	public void draw(Shader shader) {
		//Just draw every object hehehoho
		//For now only the blocks.
		
		for(MazeWall block : blockList)
		{
			shader.setMaterialDiffuse(1,1,1,1);	
			shader.setModelMatrix(block.getModelMatrix());
			block.draw();
		}
	}
}
