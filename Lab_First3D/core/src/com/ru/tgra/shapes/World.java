package com.ru.tgra.shapes;

import java.util.ArrayList;

import com.flowpowered.noise.NoiseQuality;
import com.flowpowered.noise.module.source.Perlin;

public class World {
	
	public int width, depth;
	public int blockWidth = 5;
	public int blockHeight = 5;
	public int blockDepth = 5;
	
	public int baseHeight = 0;
	
	public int currentZGenerationIndex;
		
	public ArrayList<MazeWall> blockList; //This is the base terrain of the world.
	
	public ArrayList<Obstacle> obstacles;
	
	int heightMap[][];
	private Perlin terrainNoise;
	private Perlin obstacleNoise;
	
	public World(int xSize, int zSize)
	{
		terrainNoise = new Perlin();
		terrainNoise.setNoiseQuality(NoiseQuality.BEST);
		terrainNoise.setFrequency(0.2f);
		terrainNoise.setLacunarity(0.5);
		
		obstacleNoise = new Perlin();
		obstacleNoise.setNoiseQuality(NoiseQuality.BEST);
		obstacleNoise.setFrequency(0.2f);
		obstacleNoise.setLacunarity(2.0);
		
		width = xSize;
		depth = zSize;
		currentZGenerationIndex = 0;
		
		heightMap = new int[width][depth];
		blockList = new ArrayList<MazeWall>();
		obstacles = new ArrayList<Obstacle>();
		generateInitialHeightMap();
	}
	
	private void generateInitialHeightMap()
	{

		addAdditionalZ(depth);
	}
	
	private boolean obstacleCheck(int x, int z) {
		
		double margin = 1;
		
		double obstacleNoiseValue = obstacleNoise.getValue(x, z, 0.0);
		boolean higherX = obstacleNoiseValue > obstacleNoise.getValue(x-margin, z, 0.0);
		higherX = higherX && obstacleNoiseValue > obstacleNoise.getValue(x+margin, z, 0.0);
		
		boolean higherZ = obstacleNoiseValue > obstacleNoise.getValue(x,  z+margin,  0.0);
		higherZ = higherZ && obstacleNoiseValue > obstacleNoise.getValue(x, z-margin, 0.0);
		
		return higherX && higherZ;
	}
	
	public void addAdditionalZ(int zToAdd)
	{	
		for(int x = 0; x < width; x++)
		{
			for(int z = currentZGenerationIndex; z < currentZGenerationIndex + zToAdd; z++) 
			{
				int heightNoise = (int)Math.round((terrainNoise.getValue(x, z, 0.0)*50 -45));
				boolean hasObstacle = obstacleCheck(x,z);
				System.out.print(hasObstacle + ", ");
				for(int i = baseHeight; i < heightNoise; i++)
				{
					MazeWall stillBlock = new MazeWall(new Point3D(x*blockWidth, i*blockHeight, z*blockDepth), blockWidth, blockHeight, blockDepth);
					stillBlock.setColor(0.3f, 0.5f, 0.2f);
					if(i + 1 == heightNoise) {
						stillBlock.useTopTexture();
					}
					blockList.add(stillBlock);
				}
				if(hasObstacle)
				{
					char [] stack = {'p', 'p', 'p'};
					Obstacle pillar = new Obstacle(stack, new Point3D(x*blockWidth, heightNoise*blockHeight, z*blockDepth));
					obstacles.add(pillar);
				}
				System.out.println("");
			}
		}
		System.out.println("Just added!");
		currentZGenerationIndex += zToAdd;
	}
}
