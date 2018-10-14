package com.ru.tgra.shapes;

import java.util.ArrayList;

public class MazeGenerationNode {
	int x, y;
	char nodeValue;
	public MazeGenerationNode parent;
	public ArrayList<Character> unexploredDirections;
}
