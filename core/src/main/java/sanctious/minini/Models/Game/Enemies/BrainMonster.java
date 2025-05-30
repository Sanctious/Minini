package sanctious.minini.Models.Game.Enemies;

import com.badlogic.gdx.math.Vector2;
import sanctious.minini.View.EnemyRenderer;

public class BrainMonster extends Enemy {

    public BrainMonster(Vector2 position) {
        super(position);
        this.health = 10f;
        this.speed = 3f;
    }
}
