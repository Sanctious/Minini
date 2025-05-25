package sanctious.minini.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Player {
    public Vector2 position = new Vector2(400 / 32f, 300 / 32f);
    public float speed = 2.5f;

    public Vector2 getPosition() {
        return position;
    }

    public float getSpeed() {
        return speed;
    }
}
