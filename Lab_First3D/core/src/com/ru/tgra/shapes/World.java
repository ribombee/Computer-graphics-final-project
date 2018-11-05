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
	
	public ArrayList<BillboardSprite> sprites;
	
	public ArrayList<LookingBob> bobs;
	
	public int phase;
	int heightMap[][];
	private Perlin terrainNoise;
	private Perlin obstacleNoise;
	
	public World(int xSize, int zSize)
	{
		terrainNoise = new Perlin();
		int seed = (int)System.nanoTime();
		terrainNoise.setSeed(seed);
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
		phase = 0;
		
		heightMap = new int[width][depth];
		blockList = new ArrayList<MazeWall>();
		obstacles = new ArrayList<Obstacle>();
		sprites = new ArrayList<BillboardSprite>();
		bobs = new ArrayList<LookingBob>();
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
	
	public void move(float dt)
	{
		for(Obstacle ob: obstacles) 
		{
			for(DeathOrb orb : ob.orbList)
			{
				orb.move(dt);
			}

		}
	}
	
	public void addAdditionalZ(int zToAdd)
	{	
		
		for(int z = 0; z < zToAdd; z++) {
			if(Math.random() <= 0.2f) {
				int xPos = (int)(Math.random()*30 + + width/2 + 10);
				int yPos = (int)(Math.random()*15) - 5;
				BillboardSprite sprite = new BillboardSprite(new Point3D(xPos*blockWidth, yPos*blockHeight, (z + currentZGenerationIndex)*blockDepth), new Vector3D(6,20,1));
				sprites.add(sprite);
			}
			if(Math.random() <= 0.2f) {
				int xPos = (int)(Math.random()*30 + width/2 + 10);
				int yPos = (int)(Math.random()*10);
				BillboardSprite sprite = new BillboardSprite(new Point3D(-xPos*blockWidth, yPos*blockHeight, (z + currentZGenerationIndex)*blockDepth), new Vector3D(6,20,1));
				sprites.add(sprite);
			}
			
			if(Math.random() <= 0.02f) {
				int yBob = 20;
				int xBob1 = 15 * blockWidth;
				int xBob2 = -15 * blockWidth;
				int zBob = (currentZGenerationIndex + z) * blockDepth;
				
				LookingBob leftBob = new LookingBob(new Point3D(xBob2, yBob, zBob), 500);
				LookingBob rightBob = new LookingBob(new Point3D(xBob1, yBob, zBob), 500);
				
				bobs.add(leftBob);
				bobs.add(rightBob);
			}
		}
		for(int x = 0; x < width; x++)
		{
			for(int z = currentZGenerationIndex; z < currentZGenerationIndex + zToAdd; z++) 
			{
				int heightNoise = (int)Math.round((terrainNoise.getValue(x, z, 0.0)*50 -45)) + baseHeight;
				boolean hasObstacle = obstacleCheck(x,z);
				if(phase <= 5) {
					for(int i = baseHeight; i < heightNoise; i++)
					{
						MazeWall stillBlock = new MazeWall(new Point3D(x*blockWidth, i*blockHeight, z*blockDepth), blockWidth, blockHeight, blockDepth);
						stillBlock.setColor(0.3f, 0.5f, 0.2f);
						if(i + 1 == heightNoise) {
							stillBlock.useTopTexture(phase);
						}
						blockList.add(stillBlock);
					}
				}
				else if(phase > 5) {
					int columnSize = (int)(Math.random()*4);
					for(int i = heightNoise - columnSize; i < heightNoise; i++)
					{
						MazeWall stillBlock = new MazeWall(new Point3D(x*blockWidth, i*blockHeight, z*blockDepth), blockWidth, blockHeight, blockDepth);
						stillBlock.setColor(0.3f, 0.5f, 0.2f);
						if(i + 1 == heightNoise) {
							stillBlock.useTopTexture(phase);
						}
						blockList.add(stillBlock);
					}
				}
				
				if(hasObstacle)
				{
					char [] threePillar = {'p', 'p', 'p'};
					char[] orbPedestal = {'p', 'p', 'o'};
					
					char[] obstacleCode;
					
					float rand = (float)Math.random();
					
					if(rand > 0.3)
					{
						obstacleCode = threePillar;
					}
					else
					{
						obstacleCode = orbPedestal;
					}
					
					Obstacle obstacle = new Obstacle(obstacleCode, new Point3D(x*blockWidth, heightNoise*blockHeight, z*blockDepth));
					obstacles.add(obstacle);

				}
			}
		}
		
		currentZGenerationIndex += zToAdd;
	}
	public void nextPhase() {
		phase++;
		baseHeight++;
		if(phase >= 5) {
			terrainNoise.setFrequency((phase-5)*0.05f + 0.2f);
		}
		else if(phase >= 1) {
			terrainNoise.setFrequency(phase*0.05f + 0.2f);
		}
	}
}
