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
	
	public ArrayList<MazeWall> blockList;
	
	int heightMap[][];
	private Perlin noise;
	
	public World(int xSize, int zSize)
	{
		noise = new Perlin();
		noise.setNoiseQuality(NoiseQuality.BEST);
		int seed = noise.getSeed();
		noise.setFrequency(0.2f);
		noise.setLacunarity(0.5);
		
		width = xSize;
		depth = zSize;
		
		heightMap = new int[width][depth];
		blockList = new ArrayList<MazeWall>(width*depth);
		generateHeightMap();
	}
	
	private void generateHeightMap()
	{
		for(int x = 0; x < width; x++)
		{
			for(int z = 0; z < depth; z++)
			{
				double heightNoise = (noise.getValue(x, z, 0.0)*50 -45);
				heightMap[x][z] =  (int) Math.round((heightNoise));
				for(int i = baseHeight; i < heightMap[x][z]; i++)
				{
					MazeWall stillBlock = new MazeWall(new Point3D(x*blockWidth, i*blockHeight, z*blockDepth), blockWidth, blockHeight, blockDepth);
					stillBlock.setColor(0.3f, 0.5f, 0.2f);
					blockList.add(stillBlock);
				}
				System.out.print(heightNoise+ ", ");
			}
			System.out.println("");
		}
	}
}
