package sanctious.minini.Models.Game.Enemies;

import com.badlogic.gdx.math.Vector2;
import sanctious.minini.Models.Game.Player;

public class BatMonster extends Enemy{
    private float shootTimer = 0;
    private final float shootInterval = 3f;

    public BatMonster(Vector2 position) {
        super(position, 2f);
        this.health = 50f;
        this.speed = 2.5f;
    }

    @Override
    public void update(Player player, float delta) {
        super.update(player, delta);

        shootTimer += delta;
    }

    public boolean readyToShoot(){
        return shootTimer >= shootInterval;
    }

    public void setShootTimer(float shootTimer) {
        this.shootTimer = shootTimer;
    }
}
