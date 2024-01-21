package collision;

public class AllowedCollisions {
	public static HitboxType[][] collisions = {
		// what players (the first hitbox type) can collide with)
		{ HitboxType.BULLET, HitboxType.WATER, HitboxType.WALL },

		// what bullets can collide with
		{ HitboxType.PLAYER, HitboxType.WALL },

		// water
		{ HitboxType.PLAYER },

		// walls
		{ HitboxType.PLAYER, HitboxType.BULLET }
	};
}
