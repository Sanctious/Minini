package sanctious.minini.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Player {
    public Vector2 position = new Vector2(400 / 32f, 300 / 32f);
    private PlayerState state = PlayerState.Idling;
    public boolean facing = false; // false == left
    public float speed = 2.5f;

    public Vector2 getPosition() {
        return position;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

//    public boolean isWalking(){
//        return Float.compare(speed, 0) == 0;
//    }


    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void setFacing(boolean direction){
        facing = direction;
    }

    public boolean isFacing() {
        return facing;
    }
}
