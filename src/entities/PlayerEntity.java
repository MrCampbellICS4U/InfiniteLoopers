package entities;

// the shared class that holds information about players
public class PlayerEntity extends Entity {
	public double angle;
	public int health;
	public int armor;
	public String hotBar[] = new String[3];
	public PlayerEntity(int x, int y, double angle, int health, int armor, String[] hotBar) {
		super(x, y);
		this.angle = angle;
		this.health = health;
		this.armor = armor;
		this.hotBar = hotBar;
	}
}
