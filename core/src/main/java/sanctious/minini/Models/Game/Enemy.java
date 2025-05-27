package sanctious.minini.Models.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import sanctious.minini.View.EnemyRenderer;

public class Enemy extends MovableObject{
    private float health;
    private EnemyState state;
    private EnemyRenderer renderer = null;

    public Enemy(Vector2 position) {
        this.state = EnemyState.Idling;
        this.speed = 5f;
        this.health = 10f;
    }

    public void setRenderer(EnemyRenderer renderer) {
        this.renderer = renderer;
    }

    public void update(Player player, float delta) {
        dirVector.set(player.getPosition().cpy().sub(this.position));
        super.update(delta);
    }

    public EnemyState getState() {
        return state;
    }

    public void setState(EnemyState state) {
        this.state = state;
    }

    public EnemyRenderer getRenderer() {
        return renderer;
    }

    public void modifyHealth(float value){
        this.health += value;
    }

    public boolean isDead(){
        return health <= 0;
    }
}
