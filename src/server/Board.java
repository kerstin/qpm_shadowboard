package server;

import java.util.ArrayList;

public class Board
{
	private String name;
	private int height;
	private int width;
	private int depth;
	private ArrayList<Element> elements;
	//private sessionId oder irgendwas �hnliches zur useridentifizierung oder eigene Userklasse hier bzw dr�ber
	
	public Board(int height, int width, int depth)
	{
		this.height = height;
		this.width = width;
		this.depth = depth;
	}
	
	public void generateElementfromPhoto(String fileName)
	{
		elements.add(new Element("", 0, 0));
	}
}