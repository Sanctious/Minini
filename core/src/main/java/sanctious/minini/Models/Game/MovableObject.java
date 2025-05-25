package sanctious.minini.Models.Game;

import com.badlogic.gdx.math.Vector2;

public class MovableObject {
    protected Vector2 position = new Vector2(0, 0);
    protected float speed = 2.5f;
    protected Vector2 dirVector = new Vector2(0, 0);

    public void update(float delta){
        position.add(getVelocity().scl(delta));
    }


    public Vector2 getVelocity(){
        return dirVector.nor().scl(speed);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Vector2 getDirVector() {
        return dirVector;
    }

    public void setDirVector(Vector2 dirVector) {
        this.dirVector = dirVector;
    }
}
