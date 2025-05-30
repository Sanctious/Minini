package sanctious.minini.Models.Game.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import sanctious.minini.Models.Game.EnemyState;
import sanctious.minini.Models.Game.MovableObject;
import sanctious.minini.Models.Game.Player;
import sanctious.minini.View.EnemyRenderer;

public class Enemy extends MovableObject {
    protected float health;
    protected EnemyState state;
    protected EnemyRenderer renderer = null;

    public Enemy(Vector2 position) {
        this.position = position;
        this.state = EnemyState.Idling;
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
