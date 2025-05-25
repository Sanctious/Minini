package sanctious.minini.Models;

import com.badlogic.gdx.math.Vector2;

public class Bullet {
    public Vector2 position;
    public Vector2 velocity;
    public float speed = 6f;

    public Bullet(Vector2 start, Vector2 direction) {
        this.position = new Vector2(start);
        this.velocity = direction.nor().scl(speed);
    }

    public void update(float delta) {
        position.mulAdd(velocity, delta);
    }
}
