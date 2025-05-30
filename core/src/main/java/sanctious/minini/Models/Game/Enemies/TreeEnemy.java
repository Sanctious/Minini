package sanctious.minini.Models.Game.Enemies;

import com.badlogic.gdx.math.Vector2;

public class TreeEnemy extends Enemy {

    public TreeEnemy(Vector2 position) {
        super(position);
        this.speed = 0f;
        this.health = 10f;
    }
}
