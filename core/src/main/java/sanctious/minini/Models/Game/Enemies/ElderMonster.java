package sanctious.minini.Models.Game.Enemies;

import com.badlogic.gdx.math.Vector2;
import sanctious.minini.Models.Game.Player;

public class ElderMonster extends Enemy{
    private float dashTimer = 0;
    private final float dashInterval = 3f;

    public ElderMonster(Vector2 position) {
        super(position, 10f);
        this.health = 400f;
        this.speed = 1f;
    }

    @Override
    public void update(Player player, float delta) {
        super.update(player, delta);
        if (dashTimer == 0){
            this.position.add(dirVector.cpy().scl(100));
            dashTimer = dashInterval;
        }

        if (dashTimer != 0) dashTimer -= delta;
        if (dashTimer < 0) dashTimer = 0;
    }
}
