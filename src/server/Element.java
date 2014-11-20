package server;

public class Element
{
	private String name;
	private String svgPath;
	private String depth;
	private int posX; //nicht sicher ob wirs brauchen
	private int posY;
		
	public Element(String svgPath, int posX, int posY)
	{
		this.svgPath = svgPath;
		this.posX = posX;
		this.posY = posY;
	}
}