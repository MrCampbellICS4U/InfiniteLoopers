package entities;

import java.awt.Graphics;
import java.io.Serializable;
import game.world.Tiles.Tile;

public class Entity implements Serializable {
	private double x, y; // the centre of the entity
	transient private Chunker chunker;

	public double getX() { return x; }
	public double getY() { return y; }

	public double getX1() { return x - width/2; } // right x
	public double getX2() { return x + width/2; } // left x
	public double getY1() { return y - height/2; } // top y
	public double getY2() { return y + height/2; } // bottom y

	public void setPosition(double newX, double newY) {
		double oldX = x;
		double oldY = y;
		
		x = newX;
		y = newY;
		
		chunker.updateEntity(this, oldX, oldY);
	}

	private final double width, height; // width and height of the smallest rectangle that fully contains the entity
	public double getWidth() { return width; }
	public double getHeight() { return height; }

	public Entity(double x, double y, double width, double height, Chunker c) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.chunker = c;
		chunker.addEntity(this);
	}

	public void remove() {
		chunker.removeEntity(this);
	}
	
	public void update(Tile[][][] map) {};
	public void draw(Graphics g, double playerX, double playerY) {
		customDraw(g, x-playerX, y-playerY);
	}
	public void customDraw(Graphics g, double relCentreX, double relCentreY) {};
}
