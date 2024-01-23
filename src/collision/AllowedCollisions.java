package collision;

public class AllowedCollisions {
	/**
	 * A 2D array representing the collision types between different entities.
	 * Each row represents a specific entity type, and each column represents the
	 * collision types it can collide with.
	 * The HitboxType enum is used to represent the different collision types.
	 *
	 * The collision matrix is as follows:
	 * - The first row represents the collision types for the entity type
	 * HitboxType.BULLET.
	 * - The second row represents the collision types for the entity type
	 * HitboxType.WATER.
	 * - The third row represents the collision types for the entity type
	 * HitboxType.WALL.
	 * - The fourth row represents the collision types for the entity type
	 * HitboxType.PLAYER.
	 *
	 * For example, collisions[0] represents the collision types for
	 */
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
