package com.ru.tgra.shapes;

import java.util.ArrayList;

import com.flowpowered.noise.NoiseQuality;
import com.flowpowered.noise.module.source.Perlin;

public class World {
	public int width, depth;
	public int blockWidth = 5;
	public int blockHeight = 5;
	public int blockDepth = 5;
	
	public ArrayList<MazeWall> blockList;
	
	int heightMap[][];
	private Perlin noise;
	
	public World(int xSize, int zSize)
	{
		noise = new Perlin();
		noise.setNoiseQuality(NoiseQuality.BEST);
		int seed = noise.getSeed();
		noise.setFrequency(0.5f);
		
		width = xSize;
		depth = zSize;
		
		heightMap = new int[width][depth];
		blockList = new ArrayList<MazeWall>(width*depth*10);
		generateHeightMap();
	}
	
	private void generateHeightMap()
	{
		for(int x = 0; x < width; x++)
		{
			for(int z = 0; z < depth; z++)
			{
				heightMap[x][z] = (int)(100*noise.getValue(x/2, z/2, 0.0))-60;
				for(int i = 0; i < 10; i++)
				{
					MazeWall stillBlock = new MazeWall(new Point3D(x*blockWidth, heightMap[x][z]*blockHeight - i*blockHeight, z*blockDepth), blockWidth, blockHeight, blockDepth);
					stillBlock.setColor(0.3f, 0.5f, 0.2f);
					blockList.add(stillBlock);
				}
				System.out.print(heightMap[x][z] + ", ");
			}
			System.out.println("");
		}
	}
}
